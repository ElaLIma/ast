package ast.practica4;

import ast.practica3.Channel;
import ast.protocols.tcp.TCPSegment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author upcnet
 */
public class ProtocolRecv extends ProtocolBase {

  protected Thread task;
  protected ArrayList<TSocketRecv> sockets;

  public ProtocolRecv(Channel ch) {
    super(ch);
    sockets = new ArrayList<TSocketRecv>();
    task = new Thread(new ReceiverTask());
    task.start();
  }

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

  protected void ipInput(TCPSegment segment) throws InterruptedException {
      try{
          TSocketRecv properSocket = this.getMatchingTSocket(segment.getSourcePort(), segment.getDestinationPort());
           properSocket.processReceivedSegment(segment);
      }catch(NullPointerException cuidado){
          System.err.println("El socket no existe");
      };
      
     
    
	
  }

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
