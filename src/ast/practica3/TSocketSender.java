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
        int tmp = 0;

        while (tmp < length) {
            sendSegment(this.segmentize(data, offset, sndMMS));
            tmp += sndMMS;

        }
        if (tmp - length < sndMMS) {
            sendSegment(this.segmentize(data, offset, length - tmp));
        }
      

    }

    public TCPSegment segmentize(byte[] data, int offset, int length) {
        TCPSegment segment = new TCPSegment();
        segment.setData(data, offset, length);
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
