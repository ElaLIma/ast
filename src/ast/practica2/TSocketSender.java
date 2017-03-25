/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica2;
import ast.protocols.tcp.TCPSegment;

/**
 *
 * @author alex.llobet
 */
public class TSocketSender {

    private final Channel ch;

    public TSocketSender(Channel ch) {
        this.ch = ch;
    }

    public void sendData(byte[] data, int offset, int length) {
        TCPSegment segment = new TCPSegment();
        segment.setData(data, offset, length);
        
        this.ch.send(segment);
        
    }

    public void close() {
        TCPSegment s = new TCPSegment();
        s.setFin(true);
        this.ch.send(s);
        
    }

}
