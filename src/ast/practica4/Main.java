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
import ast.practica3.MonitorChannel;
/**
 * Clase Main que crea dos nodos, que implementan dos sockets
 * con el mismo puerto destino.
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */

public class Main {

    public static void main(String[] args){
        
            //Canal de la comunicación
            Channel c = new MonitorChannel(2);

            //Receptor
            ProtocolRecv proto1 = new ProtocolRecv(c);
            new Thread(new Host1(proto1)).start();

            //Emisor
            ProtocolSend proto2 = new ProtocolSend(c);
            new Thread(new Host2(proto2)).start();
    }
}


//Cliente 1: Receptor
class Host1 implements Runnable {

    //Puerto de comunicación para establecer comunicación
    public static final int PORT = 10;
    
    //Implementación del protocolo
    protected ProtocolRecv proto;

    /**
     *  Constructor de la clase
     * @param proto Implementación del protocolo
     */
    public Host1(ProtocolRecv proto) {
        this.proto = proto;
    }

    /**
     * Código a ejecutar cuando se llame al método start()
     */
    @Override
    public void run() {
      //Creem dos sockets pels dos fils receptors:
      
      //PERQUE HI HA NOMES UN PORT? NO HAURIEN D'HAVER DOS?
      //Hay un único puerto porque este equipo establecerá todas sus comunicaciones
      //con el resto de máquinas desde dicho puerto
      TSocketRecv socket1 = proto.openForInput(PORT, PORT);
      TSocketRecv socket2 =proto.openForInput(PORT, 50);
      
      //Creem i arrenquem els dos fils receptors
      new Thread(new Receiver(socket1)).start();
      new Thread(new Receiver(socket2)).start();
    }
}


//Cliente 2: Emisor
class Host2 implements Runnable {
    
    
    //Puertos de comunicación
    public static final int PORT1 = 10;
    public static final int PORT2 = 50;

    //Implementación del protocolo
    protected ProtocolSend proto;
    
    /**
     * Contructor de la clase
     * @param proto Implementación del protocolo
     */
    public Host2(ProtocolSend proto) {
        this.proto = proto;
    }
    
    @Override
    public void run() {
      //Creem dos sockets pels dos fils emissors:
      TSocketSend socket1 = proto.openForOutput(PORT1, PORT1);
      TSocketSend socket2 = proto.openForOutput(PORT2, PORT1);
      //Creem i arrenquem els dos fils emisors
      new Thread(new Sender(socket1)).start();
      new Thread(new Sender(socket2)).start();
    }
    
}


