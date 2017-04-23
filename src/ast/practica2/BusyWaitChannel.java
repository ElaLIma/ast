
package ast.practica2;

import ast.protocols.tcp.TCPSegment;
import java.util.Arrays;

/**
 * Clase que envia paquetes mediante espera activa con sleep()
 * 
 * @author Alex Llobet 
 * @author Andony Ramón Elá Lima
 * 
 * @see Channel
 */
public class BusyWaitChannel implements Channel {

    private CircularQueue cua;
    
    /**
     * Constructor de la clase que crea un objeto CircularQueue
     * 
     * @param tamanyCua Tamaño de la cola que se crea
     */

    public BusyWaitChannel(int tamanyCua) {
        this.cua = new CircularQueue(tamanyCua);
    }

    /**
     * Método que envia un paquete TCP
     * 
     * @param seg Paquete a enviar
     */
    @Override
    public void send(TCPSegment seg) {
        while (this.cua.full()) {

        }
        this.cua.put(seg);
    }
    
    /**
     * Método que recibe los paquetes enviados
     * 
     * @return Paquete TCP
     */

    @Override
    public TCPSegment receive() {
        TCPSegment tcps;
        while (this.cua.empty()) {
            
        }
        tcps = (TCPSegment) this.cua.get();
        System.out.println(Arrays.toString(tcps.getData()));
        return tcps;
    }

}
