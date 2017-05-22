/**
 * Práctica 5: Control de flujo (Stop & wait)
 *
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
package ast.practica5;
import ast.util.FDuplexChannel;
// define imports
/**
 * 
 * Crea un canal FullDuplex entre dos equipos
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class Main {

    public static void main(String[] args){
        FDuplexChannel c = new FDuplexChannel();

        new Thread(new Host1(c.getLeft())).start();
        new Thread(new Host2(c.getRight())).start();
    }    

}

class Host1 implements Runnable {

    public static final int PORT = 10;

    protected Protocol proto;
    
    public Host1(FDuplexChannel.Peer peer) {
        this.proto = new Protocol(peer);
    }

    public void run() {
        TSocket pcb = proto.openWith(Host1.PORT, Host2.PORT);
        new Sender(pcb).run();
    }
    
}

class Host2 implements Runnable {

    public static final int PORT = 20;

    protected Protocol proto;

    public Host2(FDuplexChannel.Peer peer) {
        this.proto = new Protocol(peer);
    }

    public void run() {
        TSocket pcb = proto.openWith(Host2.PORT, Host1.PORT);
        new Receiver(pcb).run();
    }
}


