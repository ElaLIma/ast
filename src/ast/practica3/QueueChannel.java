/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica3;

import ast.practica2.*;
import ast.practica1.*;
import ast.protocols.tcp.TCPSegment;

/**
 *
 * @author alex.llobet
 */
public class QueueChannel implements Channel {

    int sndMMS;

    private int tamanyCua;
    private CircularQueue cua;

    public QueueChannel(int tamanyCua) {
        this.cua = new CircularQueue(tamanyCua);
        this.sndMMS=4;
    }

    @Override
    public void send(TCPSegment seg) {
        this.cua.put(seg);
        this.tamanyCua++;
    }

    @Override
    public TCPSegment receive() {
        this.tamanyCua--;
        return (TCPSegment) this.cua.get();
    }

    @Override
    public int getMMS() {
        return this.sndMMS;

    }
}
