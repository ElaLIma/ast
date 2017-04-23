
package ast.practica2;
import ast.protocols.tcp.TCPSegment;

/**
 * Clase que implementa el extremo emisor del socket
 *
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class TSocketSender {

    private final Channel ch;

    /**
     * Constructor de la clase
     * @param ch Canal por donde enviar los paquetes
     */
    public TSocketSender(Channel ch) {
        this.ch = ch;
    }

    /**
     * Método que envia los paquetes TCP
     * 
     * @param data Array de datos
     * @param offset Posición del array de datos, desde donde coger los datos a enviar
     * @param length Longitud de los datos a enviar
     */
    public void sendData(byte[] data, int offset, int length) {
        TCPSegment segment = new TCPSegment();
        segment.setData(data, offset, length);
        
        this.ch.send(segment);
        
    }
    
    /**
     * Cierra la conexión.
     */

    public void close() {
        TCPSegment s = new TCPSegment();
        s.setFin(true);
        this.ch.send(s);
        
    }

}
