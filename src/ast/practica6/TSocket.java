
package ast.practica6;

import ast.logging.Log;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

import ast.util.Timer;
import java.util.concurrent.TimeUnit;

import ast.protocols.tcp.TCPSegment;
import java.util.Arrays;


/**
 * We assume an IP layer with errors or losses in packets.
 * @author AST's teachers
 */
public class TSocket {
    public static Log log = Protocol.log;

    protected Protocol proto;
    protected int localPort;
    protected int remotePort;

    protected Lock lk;
    protected Condition sender, receiver;

    // Sender variables:
    protected static final int SND_RTO = 500; // Retransmission timeout in milliseconds
    protected Timer timerService;
    protected Timer.Task sndRtTimer;
    protected int sndMSS;   // Send maximum segment size
    protected int sndNxt;   // Sequence number not yet transmitted
    protected TCPSegment sndUnackedSegment; // Transmitted segment not yet acknowledged

    // Receiver variables:
    protected TCPSegment rcvSegment; // Received segment not yet consumed
    protected int rcvSegUnc;         // Received segment's offset not yet consumed
    protected int rcvNxt;            // Expected sequence number to be received


    /**
     * Create an endpoint bound to the given TCP ports.
     */
    protected TSocket(Protocol p, int localPort, int remotePort) {
        proto = p;
        this.localPort = localPort;
        this.remotePort = remotePort;
        lk = new ReentrantLock();
        sender = lk.newCondition();
        receiver = lk.newCondition();
        // init sender variables
        sndMSS = proto.net.getMMS() - TCPSegment.HEADER_SIZE; // IP maximum message size - TCP header size
        sndNxt = 0;
        sndUnackedSegment = null;
        timerService = new Timer();
        // init receiver variables
        rcvSegment = null;
        rcvSegUnc = 0;
        rcvNxt = 0;
    }


    // -------------  SENDER PART  ---------------
    public void sendData(byte[] data, int offset, int count) throws InterruptedException {
        lk.lock();
        try {
            log.debug("%s->sendData(count=%d)", this, count);
            
            // for each segment to send
                // wait until the sender is not expecting an acknowledgement
                // create a data segment and send it
            while(sndUnackedSegment != null){ //waiting for an acknowledgement for that segment
                sender.await();
            }
            //Enviem els segments
            int quedenPerEnviar = count;
            while (quedenPerEnviar > 0) {
                int bytesAPosarAlSegment;
                bytesAPosarAlSegment = quedenPerEnviar;
                if (sndMSS < count) {
                    bytesAPosarAlSegment = sndMSS;
                }
                //bytesAPosarAlSegment = min(quedenPerEnviar,sndMSS)
                TCPSegment segment = this.segmentize(data, offset, bytesAPosarAlSegment);
                //guardamos el segmento a enviar en el buffer
                sndUnackedSegment = segment;
                //Enviamos
                sendSegment(segment);
                System.out.println("sendData: S'ha enviat un segment amb info: "+Arrays.toString(segment.getData()));
                quedenPerEnviar -= bytesAPosarAlSegment;
            }
        } finally {
            lk.unlock();
        }
    }

    protected TCPSegment segmentize(byte[] data, int offset, int count) {
        byte[] buf = new byte[count];
        for (int k = 0; k < count; k++) {
            buf[k] = data[offset + k];
        }
        TCPSegment segment = new TCPSegment();
        segment.setData(buf, 0, count);
        return segment;
    }

    protected void sendSegment(TCPSegment segment) {
        log.debug("%s->sendSegment(%s)", this, segment);
        //Introduim ports i numero de sequencies
        segment.setDestinationPort(this.remotePort);
        segment.setSourcePort(this.localPort);
        segment.setSeqNum(sndNxt); //importante!!
        proto.net.send(segment);
        // start timer
        startRTO();
    }

    /**
     * Timeout elapsed.
     */
    protected void timeout() {
        lk.lock();
        try{
            log.debug("%s->timeout()", this);
            if (sndUnackedSegment != null) {
                sendSegment(sndUnackedSegment);
            }
        } finally {
            lk.unlock();
        }
    }

    protected void startRTO() {
        if (sndRtTimer != null) sndRtTimer.cancel();
        sndRtTimer = timerService.startAfter(
            new Runnable() {
                @Override public void run() { timeout(); }
            },
            SND_RTO, TimeUnit.MILLISECONDS);
    }

    protected void stopRTO() {
        if (sndRtTimer != null) sndRtTimer.cancel();
        sndRtTimer = null;
    }


    // -------------  RECEIVER PART  ---------------
    /**
     * Places received data in buf
     */
    public int receiveData(byte[] buf, int offset, int maxcount) throws InterruptedException {
        lk.lock();
        int consumedData = 0;
            log.debug("%s->receiveData(maxcount=%d)", this, maxcount);
            // wait until it's a received segment
            // consume data from the received segment and decide when to send an ACK
        try{
            while(rcvSegment==null){
                receiver.await();
            }
            //CHECKSUM????
             while (consumedData < maxcount && rcvSegment != null) {
               // if (rcvSegment!=null) System.out.println("ESTADO cola recepcion: llena " );
                System.out.println("ReceiveData: ConsumeSegment before: " + Arrays.toString(rcvSegment.getData()));
                consumedData += this.consumeSegment(buf, offset, maxcount - consumedData);

                offset += consumedData;

            }
            return consumedData;
            
        }
        finally{lk.unlock();}
            
    }

    protected int consumeSegment(byte[] buf, int offset, int maxcount) {
        // assertion: rcvSegment != null && rcvSegment.getDataLength() > rcvSegUnc
        // get data from rcvSegment and copy to buf
        int n = rcvSegment.getDataLength() - rcvSegUnc;
        if (n > maxcount) {
            // receiveData's buffer is small. Consume a fragment of the received segment
            n = maxcount;
        }
        // n == min(maxcount, rcvSegment.getDataLength() - rcvSegUnc)
        System.arraycopy(rcvSegment.getData(), rcvSegment.getDataOffset() + rcvSegUnc, buf, offset, n);
        rcvSegUnc += n;
       // System.err.println("rcvSegUnc"+ rcvSegUnc+"data Length: "+rcvSegment.getDataLength());
        if (rcvSegUnc == rcvSegment.getDataLength()) {
            // rcvSegment is totally consumed. Remove it
            rcvSegment = null;
            rcvSegUnc = 0;
            
            //System.err.println("SE CAMBIA EL VALOR DE RCVnEXT");
            sendAck(); //totally consumed!! Podemos recibir otro
            
        }
        return n;
    }

    protected void sendAck() {
        TCPSegment ack = new TCPSegment();
        ack.setSourcePort(localPort);
        ack.setDestinationPort(remotePort);
        ack.setFlags(TCPSegment.ACK);
        
        ack.setAckNum(rcvNxt); //??
       System.out.println("ACK NUM: "+ack.getAckNum()+"RCVNXT: "+rcvNxt);
        log.debug("%s->sendAck(%s)", this, ack);
	proto.net.send(ack);
    }


    // -------------  SEGMENT ARRIVAL  -------------
    /**
     * Segment arrival.
     * @param rseg segment of received packet
     */
    protected void processReceivedSegment(TCPSegment rseg) {
        lk.lock();
        try {
            
            // Check ACK
            if (rseg.isAck()) {
                
                // if ACK number is not the expected one
                //     Retransmit unacked segment
                System.out.println("ACK rebut numero: "+rseg.getAckNum()+" Se esperaba "+sndNxt);
                System.out.println("rcvNxt: "+rcvNxt+" sndNxt: "+sndNxt);
                if(rseg.getAckNum()!=(1-sndNxt)){ //el numero prox a enviar  = el numero enviado desactualizado
                     //el segmento estaba guardado en un buffer
                    sendSegment(sndUnackedSegment);
                    System.out.println("Se reenvia el segmento de datos: "+ Arrays.toString(sndUnackedSegment.getData()));
                }
                // else
                //     Clear unacked segment variable and stop the timer
                //     Wake up the sendData's thread
                else{
                    
                    sndUnackedSegment = null;
                    sndNxt= (sndNxt+1)%2; //actualizamos el numero 
               //     System.err.println("SE CAMBIA EL VALOR DE SNDNXT");
                    stopRTO();
                    sender.signal();
                }
                
                logDebugState();
                return;
            }
            // Process segment data
            if (rseg.getDataLength() > 0) {
                if (rseg.getSeqNum() != rcvNxt) {// si el num de seq del paquet diferent al que
                                                //voliem rebre no era el paquet que voliem
                                                //i hem dinformar al emisor que reenvii un altre
                                                //perque aquest esta fora d'ordre.
                    System.out.println("ERROR: FUERA DE SECUENCIA");
                    sendAck(); // Why ?
                    return;
                }//Si passsa el if, es que el segmento tiene info y es el que toca
                if (rcvSegment != null) {
                    log.warn("%s->processReceivedSegment: no free space: %d lost bytes",
                                this, rseg.getDataLength());
                    System.out.println("ERROR: COLA RECEPCION LLENA");
                    return;
                }//Tenim el paquet correcte i el podem processar. (cua unitat buida)
                
                // Set the segment not yet consumed
                // Wake up the receiveData's thread
                rcvNxt = (rcvNxt+1)%2;
                rcvSegment = rseg;
                receiver.signal();
                logDebugState();
            }
        } finally {
            lk.unlock();
        }
    }


    // -------------  LOG SUPPORT  ---------------
    protected void logDebugState() {
        if (log.debugEnabled()) {
            log.debug("%s=> state: %s", this, stateToString());
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(proto.net.getAddr()).append("/{local=").append(localPort);
        buf.append(",remote=").append(remotePort).append("}");
        return buf.toString(); 
    }

    public String stateToString() {
        StringBuilder buf = new StringBuilder();
        if (sndUnackedSegment == null) {
            buf.append("{sndUnackedSegment=null");
        } else {
            buf.append("{sndUnackedSegment.seqNum=").append(sndUnackedSegment.getSeqNum());
        }
        buf.append(",sndNxt=").append(sndNxt);
        buf.append(",rcvNxt=").append(rcvNxt);
        if (rcvSegment == null) {
            buf.append(",rcvSegment=null");
        } else {
            buf.append(",rcvSegment.seqNum=").append(rcvSegment.getSeqNum());
            buf.append(",rcvSegment.dataLength=").append(rcvSegment.getDataLength());
            buf.append(",rcvSegUnc=").append(rcvSegUnc);
        }
        return buf.append("}").toString();
    }

}


