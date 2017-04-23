
package ast.practica1;

import ast.protocols.tcp.TCPSegment;

/**
 * 
 * Clase que representa el canal, mediante una cola circular.
 * 
 * @author Alex Llobet
 * @author Andony Ramón Elá Lima
 * 
 * @see Channel
 */
public class QueueChannel implements Channel {

    private int tamanyCua;
    private CircularQueue cua;

    
    /**
     * Constructor de la clase.
     * @param tamanyCua Tamaño de la cola que emula el canal.
     */
    public QueueChannel(int tamanyCua) {
        this.cua = new CircularQueue(tamanyCua);
    }

    /**
     * Envia los paquetes al receptor, colocándolos en la cola.
     * @param seg Paquete a enviar.
     */
    @Override
    public void send(TCPSegment seg) {
        this.cua.put(seg);
        this.tamanyCua++;
    }
    
    /**
     * Recibe un paquete enviado por el emisor, sacándo el primer paquete de la cola.
     * 
     * @return TCPSegment Paquete recibido
     */

    @Override
    public TCPSegment receive() {
        this.tamanyCua--;
        return (TCPSegment) this.cua.get();
    }

}
