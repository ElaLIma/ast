/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica3;

import ast.protocols.tcp.TCPSegment;

/**
 *
 * @author alex.llobet
 */
public class TSocketSender extends TSocketBase {

    protected int sndMMS;

    public TSocketSender(Channel ch) {
        super(ch);
        sndMMS = ch.getMMS();
    }

    public void sendData(byte[] data, int offset, int length) {

        /*if(length < sndMMS){
         sendSegment(this.segmentize(data, offset, length));
         }*/
        int quedenPerEnviar = length;

        while (quedenPerEnviar > 0) {
            int bytesAPosarAlSegment;
            bytesAPosarAlSegment = length;
            if (sndMMS < length) {
                bytesAPosarAlSegment = sndMMS;
            }
            sendSegment(this.segmentize(data, offset, bytesAPosarAlSegment));
            quedenPerEnviar -= bytesAPosarAlSegment;
            offset += bytesAPosarAlSegment;
        }

    }

    public TCPSegment segmentize(byte[] data, int offset, int length) {
        byte[] buf = new byte[length];
        for (int k = offset; k < length; k++) {
            buf[k - offset] = data[k];
        }
        TCPSegment segment = new TCPSegment();
        segment.setData(buf, offset, length);
        //Miramos qué hay dentro del segmento:
      /*  System.out.println("Informacion dentro segmento: \n");
         for(int g=0; g<buf.length;g++)
         System.out.println(segment.getData() +"\n");*/

        return segment;
    }

    protected void sendSegment(TCPSegment segment) {
        channel.send(segment);

    }

    public void close() {
        TCPSegment s = new TCPSegment();
        s.setFin(true);
        this.sendSegment(s);

    }

}
