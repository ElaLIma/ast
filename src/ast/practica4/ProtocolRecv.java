/**
 * Práctica 4: Multiplexado / Demultiplexado
 * 
 * 
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
package ast.practica4;

import ast.practica3.Channel;
import ast.protocols.tcp.TCPSegment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que registra los sockets en el nodo receptor
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class ProtocolRecv extends ProtocolBase {

  protected Thread task;
  protected ArrayList<TSocketRecv> sockets;

  /**
   * Constructor de la clase
   * @param ch canal de comunicación
   */
  public ProtocolRecv(Channel ch) {
    super(ch);
    sockets = new ArrayList<TSocketRecv>();
    task = new Thread(new ReceiverTask());
    task.start();
  }

  /**
   * Método que crea un socket y lo registra
   * @param localPort Puerto de emisión
   * @param remotePort Puero de recepción
   * @return TSocketRecv
   */
  public TSocketRecv openForInput(int localPort, int remotePort) {
    lk.lock();
    try {
	
      TSocketRecv newSocket = new TSocketRecv(this, localPort, remotePort);
      sockets.add(newSocket);
      return newSocket;
	  
    } finally {
      lk.unlock();
    }
  }

  /**
   * Método que devuelve el socket asociado a un paquete, y lo procesa
   * @param segment Paquete recibido por parámetro.
   * @throws InterruptedException 
   */
  protected void ipInput(TCPSegment segment) throws InterruptedException {
      try{         
          TSocketRecv properSocket = this.getMatchingTSocket(segment.getDestinationPort(), segment.getSourcePort());
           properSocket.processReceivedSegment(segment);
      }catch(NullPointerException cuidado){
          System.err.println("El socket no existe");
      };
 	
  }

  /**
   * Método que devuelve el socket asociado a los puertos recibidos como parámetros.
   * @param localPort Puerto de emisión
   * @param remotePort Puerto de recepción
   * @return TSocketRecv
   */
  protected TSocketRecv getMatchingTSocket(int localPort, int remotePort) {
    lk.lock();
    try {
      TSocketRecv foundSocket;
      Iterator itr = sockets.iterator();
      while(itr.hasNext()){
          foundSocket = (TSocketRecv) itr.next();
          if(foundSocket.localPort == localPort && foundSocket.remotePort == remotePort){
              return foundSocket;
          }
          
      }
      return null;
    } finally {
      lk.unlock();
    }
  }

  class ReceiverTask implements Runnable {

    public void run() {
      
      while (true) {
        TCPSegment rseg = channel.receive();
        
          try {
              ipInput(rseg);
          } catch (InterruptedException ex) {
              Logger.getLogger(ProtocolRecv.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
    }
  }

}
