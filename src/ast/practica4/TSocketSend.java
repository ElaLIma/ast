package ast.practica4;

import ast.protocols.tcp.TCPSegment;

/**
 * @author AST's teachers
 */
public class TSocketSend extends TSocketBase {

  protected int sndMSS;       // Send maximum segment size

  /**
   * Create an endpoint bound to the local IP address and the given TCP port. The local IP address is determined by the
   * networking system.
   *
   * @param ch
   */
  protected TSocketSend(ProtocolSend p, int localPort, int remotePort) {
    super(p, localPort, remotePort);
    sndMSS = p.channel.getMMS() - TCPSegment.HEADER_SIZE; // IP maximum message size - TCP header size
  }

 public void sendData(byte[] data, int offset, int length) {

        /*if(length < sndMMS){
         sendSegment(this.segmentize(data, offset, length));
         }*/
        int quedenPerEnviar = length;
        
        while (quedenPerEnviar > 0) {
            int bytesAPosarAlSegment;
            bytesAPosarAlSegment = length;
            if (sndMSS < length){
                bytesAPosarAlSegment = sndMSS;
            }
            sendSegment(this.segmentize(data, offset, bytesAPosarAlSegment));
            quedenPerEnviar -= bytesAPosarAlSegment;
        }


    }

    public TCPSegment segmentize(byte[] data, int offset, int length) {
        byte[] buf = data;
        TCPSegment segment = new TCPSegment();
        segment.setData(buf, offset, length);
        //Miramos qué hay dentro del segmento:
      /*  System.out.println("Informacion dentro segmento: \n");
        for(int g=0; g<buf.length;g++)
            System.out.println(segment.getData() +"\n");*/
        
        return segment;
    }

  protected void sendSegment(TCPSegment segment) {
    proto.channel.send(segment);
  }
}
