package ast.practica2;

import ast.practica1.*;
import ast.protocols.tcp.TCPSegment;

/**
 * Implementación del nivel físico
 *
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public interface Channel {
    
    /**
     * Método para enviar paquetes
     * @param seg Segmento a enviar
     */

    public void send(TCPSegment seg);

    /**
     * Método para recibir paquetes
     * @return 
     */
    public TCPSegment receive();
}
