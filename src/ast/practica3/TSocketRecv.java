/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica3;

import ast.protocols.tcp.TCPSegment;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TSocketRecv extends TSocketBase {

    protected Thread thread;
    protected CircularQueue<TCPSegment> rcvQueue;
    protected int rcvSegConsumedBytes;
    // invariant: rcvQueue.empty() || rcvQueue.peekFirst().getDataLength() > rcvSegConsumedBytes

    public TSocketRecv(Channel ch) {
        super(ch);
        rcvQueue = new CircularQueue<TCPSegment>(2000);
        rcvSegConsumedBytes = 0;
        thread = new Thread(new ReceiverTask());
        thread.start();
    }

    /**
     * Places received data in buf Veure descripció detallada en Exercici 3!!
     */
    public int receiveData(byte[] buf, int offset, int length) throws InterruptedException {
        lk.lock();
        int consumedData = 0;

        try {
            System.out.println("Hilo : " + this);

            // wait until receive queue is not empty
            while (rcvQueue.empty()) {
                this.appCV.await();
            }//mientras el buffer no esté lleno
            while (consumedData < length && !rcvQueue.empty()) {
                // fill buf with bytes from segments in rcvQueue
                // Hint: use consumeSegment!
                consumedData += this.consumeSegment(buf, offset, length - consumedData);
                System.err.println("[receiveData()]Cantidad de datos consumidos : " + consumedData);
                offset += consumedData;

            }
            return consumedData;

        } finally {
            lk.unlock();
        }
    }

    protected int consumeSegment(byte[] buf, int offset, int length) {
        TCPSegment seg = rcvQueue.peekFirst();

        // get data from seg and copy to receiveData's buffer
        int n = seg.getDataLength() - rcvSegConsumedBytes;
        if (n > length) {
            // receiveData's buffer is small. Consume a fragment of the received segment
            n = length;
        }
        System.out.println("---------------------------");
        System.out.println("Cantidad de datos a copiar : " + n);
        System.out.println("Offset del buffer de copia : " + offset);
        System.out.println("Tamaño del buffer de copia : " + buf.length);
        System.out.println("Tamaño del segmento a copiar : " + seg.getDataLength());
        int suma = seg.getDataOffset() + rcvSegConsumedBytes;
        System.out.println("Offset desde donde copiar : " + suma);
        System.out.println("Offset deL segmento recibido : " + seg.getDataOffset());
        System.out.println("Offset desde donde copiar : " + rcvSegConsumedBytes);
        
        System.arraycopy(seg.getData(), rcvSegConsumedBytes, buf, offset, n);
        
        rcvSegConsumedBytes += n;
        
        System.err.println("[consumeSegment] : " + rcvSegConsumedBytes);

        if (rcvSegConsumedBytes == seg.getDataLength()) {
            System.out.println("entro en if");
            // seg is totally consumed. Remove from rcvQueue
            rcvQueue.get();
            rcvSegConsumedBytes = 0;
        }

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

    public void close() {
        System.out.println("Fi de transmissió");
    }

    class ReceiverTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                TCPSegment rseg = channel.receive();
                try {
                    processReceivedSegment(rseg);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TSocketRecv.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
