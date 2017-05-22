
package ast.practica6;

import ast.util.FDuplexChannel;
import ast.protocols.tcp.TCPSegment;

import ast.logging.Log;
import ast.logging.LogFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * @author AST's teachers
 */
public class Protocol {
    public static Log log = LogFactory.getLog(Protocol.class);

    protected Lock lk;
    protected FDuplexChannel.Peer net;
    protected Thread task;
    protected ArrayList<TSocket> sockets;

    public Protocol(FDuplexChannel.Peer ch) {
        lk = new ReentrantLock();
        net = ch;
        sockets = new ArrayList<TSocket>();
        task = new Thread(new ReceiverTask());
        task.start();
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
          properSocket.processReceivedSegment(segment);
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
