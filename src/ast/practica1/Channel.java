/**
 * Interface que emula el nivel de red del protocolo TCP/IP
 * 
 * @author Alex Llobet
 * @author Andony Ramón Elá Lima
 * 
 */
package ast.practica1;
import ast.protocols.tcp.TCPSegment;


public interface Channel {

    public void send(TCPSegment seg);

    public TCPSegment receive();
}
