package ast.practica5;

import ast.logging.Log;
import ast.practica3.CircularQueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ast.protocols.tcp.TCPSegment;
import java.util.Arrays;

/**
 * @author AST's teachers
 */
public class TSocket {

    public static Log log = Protocol.log;

    protected Protocol proto;
    protected Lock lk;
    protected Condition receiver, sender;

    protected int localPort;
    protected int remotePort;

    // Sender variables:
    
    protected int sndMSS;       // Send maximum segment size
    protected boolean sndIsUna; // segment not yet acknowledged ?

    // Receiver variables:
    //protected CircularQueue<TCPSegment> rcvQueue; per què es fa servir?
    protected TCPSegment rcvSegment;
    protected int rcvSegConsumedBytes;

    /**
     * Create an endpoint bound to the given TCP ports.
     */
    protected TSocket(Protocol p, int localPort, int remotePort) {
        lk = new ReentrantLock();
        receiver = lk.newCondition();
        sender = lk.newCondition();
        proto = p;
        this.localPort = localPort;
        this.remotePort = remotePort;
        // init sender variables
        sndMSS = p.net.getMMS() - TCPSegment.HEADER_SIZE; // IP maximum message size - TCP header size
        sndIsUna = false;
        // init receiver variables
        rcvSegment = null;
        rcvSegConsumedBytes = 0;
        
        
    }

    // -------------  SENDER PART  ---------------
    public void sendData(byte[] data, int offset, int length) throws InterruptedException {
        lk.lock();
       // System.out.println("sendData: flag1");
        try {

            log.debug("%s->sendData(length=%d)", this, length);
            int quedenPerEnviar = length;

            while (quedenPerEnviar > 0) {
              //  System.out.println("sendData: flag2");
                int bytesAPosarAlSegment;
                bytesAPosarAlSegment = quedenPerEnviar;
                if (sndMSS < length) {
                    bytesAPosarAlSegment = sndMSS;
                }
                
               
               TCPSegment aux = this.segmentize(data, offset, bytesAPosarAlSegment);
                sendSegment(aux);
                System.out.println("sendData: S'ha enviat un segment amb info: "+Arrays.toString(aux.getData()));
                quedenPerEnviar -= bytesAPosarAlSegment;
            }
            // for each segment to send
            // wait until the sender is not expecting an acknowledgement
            // create a data segment and send it
        } finally {
            lk.unlock();
        }
    }

    protected TCPSegment segmentize(byte[] data, int offset, int length) {

        byte[] buf = new byte[length];

        for (int k = 0; k < length; k++) {

            buf[k] = data[offset + k];

        }

        TCPSegment segment = new TCPSegment();

        segment.setData(buf, 0, length);
        return segment;
    }

    protected void sendSegment(TCPSegment segment) throws InterruptedException{
        segment.setDestinationPort(this.remotePort);
        segment.setSourcePort(this.localPort);
        log.debug("%s->sendSegment(%s)", this, segment);
        while (sndIsUna == true) {
               
                    sender.await(); //Si no tenemos confirmacion de ACK, esperamos
                }
        proto.net.send(segment);
        sndIsUna = true;
        
    }

    // -------------  RECEIVER PART  ---------------
    /**
     * Places received data in buf
     */
    public int receiveData(byte[] buf, int offset, int maxlen) throws InterruptedException {
        lk.lock();
        int consumedData = 0;

        try {

            // wait until receive unit queue is not empty
            while (rcvSegment == null) { //Se extraeran los datos de aqui
                this.receiver.await();
            }

            while (consumedData < maxlen && rcvSegment != null) {
                System.out.println("ReceiveData: ConsumeSegment before: " + Arrays.toString(rcvSegment.getData()));
                consumedData += this.consumeSegment(buf, offset, maxlen - consumedData);

                offset += consumedData;

            }
            return consumedData;

        } finally {
            lk.unlock();
        }
    }

    protected int consumeSegment(byte[] buf, int offset, int maxlen) {
        // assertion: rcvSegment != null && rcvSegment.getDataLength() > rcvSegConsumedBytes
        // get data from rcvSegment and copy to receiveData's buffer
        int n = rcvSegment.getDataLength() - rcvSegConsumedBytes;
        if (n > maxlen) {
            // receiveData's buffer is small. Consume a fragment of the received segment
            n = maxlen;
        }
        // n == min(length, rcvSegment.getDataLength() - rcvSegConsumedBytes)
        System.out.println("ConsumeSegment: toConsume data: " + Arrays.toString(rcvSegment.getData()));
        System.arraycopy(rcvSegment.getData(), rcvSegment.getDataOffset() + rcvSegConsumedBytes, buf, offset, n);
        rcvSegConsumedBytes += n;
        System.out.println("Consume Segment: buffer with data: " + Arrays.toString(buf));
        if (rcvSegConsumedBytes == rcvSegment.getDataLength()) {
            // rcvSegment is totally consumed. Remove it

            rcvSegment = null;
            sendAck();
            rcvSegConsumedBytes = 0;
            
        }
        return n;
    }

    protected void sendAck() {
        TCPSegment ack = new TCPSegment();
        ack.setSourcePort(localPort);
        ack.setDestinationPort(remotePort);
        ack.setFlags(TCPSegment.ACK);
        log.debug("%s->sendAck(%s)", this, ack);
        proto.net.send(ack);
    }

    // -------------  SEGMENT ARRIVAL  -------------
    /**
     * Segment arrival.
     *
     * @param rseg segment of received packet
     */
    protected void processReceivedSegment(TCPSegment rseg) throws InterruptedException {
        lk.lock();
        try {
            // Check ACK
            if (rseg.isAck()) {
                System.out.println("Es ack");
                //Si es ACK, podemos despertar a la hebra de aplicacion SENDER
                this.sndIsUna = false; //ya no estamos a la espera de un ACK
                sender.signal();
                logDebugState();

            } else if (rseg.getDataLength() > 0) {

                // Process segment data
                if (rcvSegment != null) {
                    log.warn("%s->processReceivedSegment: no free space: %d lost bytes",
                            this, rseg.getDataLength());
                    return;
                }
                //si Se salta el IF entonces es que la cola de recepcion esta vacía:
                rcvSegment = rseg; //lenamos la cola
                sndIsUna=true; //cola llena, no envíes más info Sender.
                receiver.signal(); //Despertamos para que recepcion procese datos
                
                System.out.println("processReceivedSegment: recibido segmento de info: " + Arrays.toString(rcvSegment.getData()));
        
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
        buf.append("{sndIsUna=").append(sndIsUna);
        if (rcvSegment == null) {
            buf.append(",rcvSegment=null");
        } else {
            buf.append(",rcvSegment.dataLength=").append(rcvSegment.getDataLength());
            buf.append(",rcvSegConsumedBytes=").append(rcvSegConsumedBytes);
        }
        return buf.append("}").toString();
    }

}
