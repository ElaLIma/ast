/**
 * Práctica 4: Multiplexado / Demultiplexado
 * 
 * 
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
package ast.practica4;

// declareu imports
import ast.practica3.Channel;
import java.util.ArrayList;


/**
 * Clase que registra los sockets en el nodo emisor
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class ProtocolSend extends ProtocolBase {

  protected ArrayList<TSocketSend> sockets;

  /**
   * Constructor de la clase
   * @param ch Canal de comunicación
   */
  public ProtocolSend(Channel ch) {
    super(ch);
    sockets = new ArrayList<TSocketSend>();
  }

  /**
   * Método que crea un socket con los puertos recibidos como parámetros
   * @param localPort Puerto de emisión
   * @param remotePort Puerto de recepción
   * @return TSocketSend
   */
  
  public TSocketSend openForOutput(int localPort, int remotePort) {
        lk.lock();
        try {

            TSocketSend newSocket = new TSocketSend(this, localPort, remotePort);
            sockets.add(newSocket);
            return newSocket;

        } finally {
            lk.unlock();
        }
    }
}
