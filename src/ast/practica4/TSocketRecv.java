
package ast.practica4;



import ast.practica3.CircularQueue;
import ast.protocols.tcp.TCPSegment;
import java.util.Arrays;

/**
 * Socket for receiving endpoint.
 *
 * @author upcnet
 */
public class TSocketRecv extends TSocketBase {

    protected CircularQueue<TCPSegment> rcvQueue;
    protected int rcvSegUnc;
    protected int rcvSegConsumedBytes;

    /**
     * Create an endpoint bound to the local IP address and the given TCP port.
     * The local IP address is determined by the networking system.
     * @param ch
     */
    protected TSocketRecv(ProtocolRecv p, int localPort, int remotePort) {
        super(p, localPort, remotePort);
        rcvQueue = new CircularQueue<TCPSegment>(20);
        rcvSegUnc = 0;
        rcvSegConsumedBytes = 0;
    }

    public int receiveData(byte[] buf, int offset, int length) throws InterruptedException {
        lk.lock();
        int consumedData = 0;

        try {

            // wait until receive queue is not empty
            while (rcvQueue.empty()) {
                this.appCV.await();
            }//mientras el buffer no esté lleno
            while (consumedData < length && !rcvQueue.empty()) {
                // fill buf with bytes from segments in rcvQueue
                // Hint: use consumeSegment!
                System.out.println("Tamaño buffer: "+buf.length +" offset: "+offset);
                consumedData += this.consumeSegment(buf, offset, length - consumedData);
                offset += consumedData;
                

            }
            
             return consumedData;

        } finally {
            lk.unlock();
        }
       
    }

    protected int consumeSegment(byte[] buf, int offset, int length) {
        TCPSegment seg = rcvQueue.peekFirst();
        System.err.println("Tamany dades: "+seg.getDataLength());
        System.out.println(" dades: "+Arrays.toString(seg.getData()));
        
        // get data from seg and copy to receiveData's buffer
        int n = seg.getData().length - rcvSegConsumedBytes;
        System.out.println("DFJKLASHJASHLDJHSKLA"+seg.getData().length);
        if (n > length) {
            // receiveData's buffer is small. Consume a fragment of the received segment
            n = length;
        }
        // n == min(length, seg.getDataLength() - rcvSegConsumedBytes)
        System.arraycopy(seg.getData(), seg.getData().length+rcvSegConsumedBytes, buf, offset, n);
        rcvSegConsumedBytes += n;
        if (rcvSegConsumedBytes == seg.getDataLength()) {
            // seg is totally consumed. Remove from rcvQueue
            rcvQueue.get();
            rcvSegConsumedBytes = 0;
        }
       /* System.out.println("Segmento consumido: ");
        for(int k=0; k<n ;k++)
            System.out.println(buf[k]+" ");*/
        return n;
    }

    /**
     * TCPSegment arrival.
     *
     * @param rseg segment of received packet
     */
    protected void processReceivedSegment(TCPSegment rseg) throws InterruptedException {
        lk.lock();
        try {
                    
            if (!rcvQueue.full()) {
                rcvQueue.put(rseg);
                this.appCV.signal();
            }

        } finally {
            lk.unlock();
        }
    }
}




