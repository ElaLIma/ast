package ast.practica4;
import ast.practica3.Channel;
import ast.practica3.MonitorChannel;

public class Main {

    public static void main(String[] args){
            Channel c = new MonitorChannel(2);

            ProtocolRecv proto1 = new ProtocolRecv(c);
            new Thread(new Host1(proto1)).start();

            ProtocolSend proto2 = new ProtocolSend(c);
            new Thread(new Host2(proto2)).start();
    }
}


class Host1 implements Runnable {

    public static final int PORT = 10;
    
    
    protected ProtocolRecv proto;

    public Host1(ProtocolRecv proto) {
        this.proto = proto;
    }

    @Override
    public void run() {
      //Creem dos sockets pels dos fils receptors:
      
      //PERQUE HI HA NOMES UN PORT? NO HAURIEN D'HAVER DOS?
      TSocketRecv socket1 = proto.openForInput(PORT, PORT);
      TSocketRecv socket2 =proto.openForInput(PORT, 50);
      
      //Creem i arrenquem els dos fils receptors
      new Thread(new Receiver(socket1)).start();
      new Thread(new Receiver(socket2)).start();
    }
}


class Host2 implements Runnable {

    public static final int PORT1 = 10;
    public static final int PORT2 = 50;

    protected ProtocolSend proto;
    
    public Host2(ProtocolSend proto) {
        this.proto = proto;
    }
    
    public void run() {
      //Creem dos sockets pels dos fils emissors:
      TSocketSend socket1 = proto.openForOutput(PORT1, PORT1);
      TSocketSend socket2 = proto.openForOutput(PORT2, PORT1);
      //Creem i arrenquem els dos fils emisors
      new Thread(new Sender(socket1)).start();
      new Thread(new Sender(socket2)).start();
    }
    
}


