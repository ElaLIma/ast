
package ast.practica5;

import ast.logging.LogFactory;
import ast.logging.Log;
import java.util.Arrays;


public class Receiver implements Runnable {
    public static Log log = LogFactory.getLog(Receiver.class);

    protected TSocket input;
    protected int recvBuf, recvInterval;

    public Receiver(TSocket pcb, int recvBuf, int recvInterval) {
        this.input = pcb;
        this.recvBuf = recvBuf;
        this.recvInterval = recvInterval;
    }

    public Receiver(TSocket pcb) {
        this(pcb, 100, 100);
    }

    public void run() {
        try {
            byte n = 0;
            byte[] buf = new byte[recvBuf];
 
            while (true) {
               // System.out.println("Receiver flag1");
                int r = input.receiveData(buf, 0, buf.length);
                System.out.println("Receiver buffer: "+Arrays.toString(buf));
                // check received data stamps
                for (int j = 0; j < r; j++) {
                    
                    if (buf[j] != n) {
                        System.err.println("elements buffer: "+buf[j]+" n: "+n);
                        throw new Exception("ReceiverTask: Recieved data is corrupted");
                    }
                    n = (byte) (n + 1);
                }
                log.info("Receiver: received %d bytes", r);
                Thread.sleep(recvInterval);
            }
        } catch (Exception e) {
            log.error("Receiver exception: %s", e);
            e.printStackTrace(System.err);
        }
    }

}


