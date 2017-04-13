
package ast.practica1;

import ast.protocols.tcp.TCPSegment;

/**
 * 
 * Implementa el nivel de transporte.
 * 
 * @author Alex Llobet
 * @author Andony Ramón Elá Lima
 */
public class TSocketSender {

    private final Channel ch;

    /**
     * Contructor de la clase
     * @param ch Canal de envío
     */
    public TSocketSender(Channel ch) {
        this.ch = ch;
    }

    /**
     * Envía los datos por elcanal
     * 
     * @param data Array de bytes a enviar
     * @param offset Posición del array, desde donde se tomarán los datos a enviar   
     * @param length Número de posiciones a enviar.
     */
    public void sendData(byte[] data, int offset, int length) {
        TCPSegment segment = new TCPSegment();
        segment.setData(data, offset, length);
        this.ch.send(segment);
        
    }
    
    /**
     * Cierra la comunicación con el receptor.
     */

    public void close() {
        TCPSegment s = new TCPSegment();
        this.ch.send(s);
    }

}
