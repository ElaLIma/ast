
package ast.practica2;

import ast.protocols.tcp.TCPSegment;
import java.util.Arrays;

/**
 * 
 * Clase AwaitChannel que implementa la espera activa con await para implementar
 * exclusión mutua y basada en condiciones.
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 * 
 * @see Channel
 * 
 */
public class AwaitChannel implements Channel {

    private static CircularQueue cua;
    boolean permiso;
    Mutex m;
    
    /**
     * Constructor de la clase, que recibe el tamaño de la cola y crea un objeto 
     * Mutex y otro objeto CircularQueue
     * 
     * @param length Tamaño de la cola 
     */

    public AwaitChannel(int length) {
        this.m = new Mutex();
        this.cua = new CircularQueue(length);
    }

    /**
     * 
     * Método que envia paquetes TCP implementando exclusión mutua en la cola 
     * del canal.
     * 
     * @param seg Segmento a enviar
     */
    @Override
    public void send(TCPSegment seg) {
        m.entraZC();
        while (cua.full()) {
            m.surtZC();
        }
        this.cua.put(seg);
        m.surtZC();
    }

    /**
     * Método que recibe un paquete. Saca los paquetes de la cola, 
     * siempre que no esté vacía.
     * 
     * @return El número de bytes recibidos. 
     */
    @Override
    public TCPSegment receive() {
        TCPSegment tcps;
        m.entraZC();
        while (cua.empty()) {
            m.surtZC();
            m.entraZC();
        }

        tcps = (TCPSegment) this.cua.get();
        System.out.println(Arrays.toString(tcps.getData()));
        m.surtZC();
        return tcps;
    }

}
