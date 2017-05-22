/**
 * Práctica 4: Multiplexado / Demultiplexado
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */

package ast.practica4;
import ast.logging.LogFactory;
import ast.logging.Log;

/**
 * Clase Receiver
 * Recibe un un paquete, lo compara y determina si la información es correcta.
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class Receiver implements Runnable {

    //Objeto para establecer registros
    public static Log log = LogFactory.getLog(Receiver.class);

    //Socket que guardará el puerto destino y el origen
    protected TSocketRecv input;
    //Parámetros que determinan el funcionamiento del Receiver
    protected int recvBuf, recvInterval;

    /**
     * Constructor de la clase
     * 
     * @param pcb Socket que identifica a la comunicación
     * @param recvBuf Tamaño del buffer de recepción
     * @param recvInterval Intervalo de recepción 
     */
    public Receiver(TSocketRecv pcb, int recvBuf, int recvInterval) {
        this.input = pcb;
        this.recvBuf = recvBuf;
        this.recvInterval = recvInterval;
    }

    /**
     * COnstructor de la clase
     * Sólo recibe un socket
     * @param pcb Socket de la comunicación
     */
    public Receiver(TSocketRecv pcb) {
        this(pcb, 10000,100);
    }

    /**
     * Código a ejecutar por los hilos de esta clase.
     */
    public void run() {
        
        try {
            byte n = 0;
            byte[] buf = new byte[recvBuf];
            while (true) {
                int r = input.receiveData(buf, 0, buf.length);
                // check received data stamps
                for (int j = 0; j < r; j++) {
                    if (buf[j] != n) {
                        throw new Exception("ReceiverTask: Recieved data is corrupted");
                    }
                    n = (byte) (n + 1);
                }
                log.info("Receiver: received %d bytes", r);
                Thread.sleep(recvInterval);
            }
        } catch (Exception e) {
            log.error("Excepcio a Receiver: %s", e);
            e.printStackTrace(System.err);
        }
    }

}
