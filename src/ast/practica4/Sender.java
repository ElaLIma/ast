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
 * Clase Sender
 * Envía paquetes según los parámetros pasados al constructor.
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class Sender implements Runnable {
    //Objeto para establecer registros
    public static Log log = LogFactory.getLog(Sender.class);
    
    //Socket que guardará el puerto destino y el origen
    protected TSocketSend output;
    protected int sendNum, sendSize, sendInterval;

    /**
     * Constructor de la clase
     * @param pcb Socket que identifica a la comunicación
     * @param sendNum Número de paquetes a enviar
     * @param sendSize Tamaño de los paquetes a enviar
     * @param sendInterval Intervalo de envío
     */
    public Sender(TSocketSend pcb, int sendNum, int sendSize, int sendInterval) {
        this.output = pcb;
        this.sendNum = sendNum;
        this.sendSize = sendSize;
        this.sendInterval = sendInterval;
    }
    
    /**
     * Constructor
     * Sólo recibe un socket
     * @param pcb Socket que identifica a la comunicación
     */
    public Sender(TSocketSend pcb) {
        this(pcb, 60, 900, 50);
    }
    
    /**
     * Código a ejecutar por los hilos de esta clase.
     */
    public void run() {
         
        try {
            byte n = 0;
            byte[] buf = new byte[sendSize];
            for (int i = 0; i < sendNum; i++) {
                Thread.sleep(sendInterval);
                // stamp data to send
                for (int j = 0; j < sendSize; j++) {
                    buf[j] = n;
                    n = (byte) (n + 1);
                }
                output.sendData(buf, 0, buf.length);
            }
            log.info("Sender: transmission finished");
        } catch (Exception e) {
            log.error("Excepcio a Sender: %s", e);
            e.printStackTrace(System.err);
        }
    }

}


