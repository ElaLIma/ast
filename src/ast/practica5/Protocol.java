
package ast.practica5;
import ast.util.FDuplexChannel;
// define imports

import ast.protocols.tcp.TCPSegment;

import ast.logging.Log;
import ast.logging.LogFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author upcnet
 */
public class Protocol {
    public static Log log = LogFactory.getLog(Protocol.class);

    protected ArrayList<TSocket> sockets;
    protected Thread task;
    protected Lock lk;
    protected FDuplexChannel.Peer net;

    public Protocol(FDuplexChannel.Peer ch) {
        sockets = new ArrayList<TSocket>();
        task = new Thread(new ReceiverTask());
        task.start();
        lk = new ReentrantLock();
        net = ch;
    }

    public TSocket openWith(int localPort, int remotePort) {
        lk.lock();
        try {

            TSocket newSocket = new TSocket(this, localPort, remotePort);
            sockets.add(newSocket);
            return newSocket;

        } finally {
            lk.unlock();
        }
    }

    protected void ipInput(TCPSegment segment) {
        try{
          TSocket properSocket = this.getMatchingTSocket(segment.getDestinationPort(), segment.getSourcePort());
            try {
                properSocket.processReceivedSegment(segment);
            } catch (InterruptedException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            }
      }catch(NullPointerException cuidado){
          System.err.println("El socket no existe");
      };
    }

    protected TSocket getMatchingTSocket(int localPort, int remotePort) {
       lk.lock();
    try {
      TSocket foundSocket;
      Iterator itr = sockets.iterator();
      while(itr.hasNext()){
          foundSocket = (TSocket) itr.next();
          System.err.println("Buscamos: R:"+remotePort+" :local: "+localPort);
          System.out.println("Puertos de socket. Remote: "+foundSocket.remotePort+"local: "+foundSocket.localPort);
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
                TCPSegment rseg = net.receive();
                ipInput(rseg);
            }
        }
    }


}
