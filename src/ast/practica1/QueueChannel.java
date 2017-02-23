/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica1;

import ast.protocols.tcp.TCPSegment;

/**
 *
 * @author alex.llobet
 */
public class QueueChannel implements Channel {

    private int tamanyCua;
    private CircularQueue cua;//= new CircularQueue(tamanyCua);

    public QueueChannel(int tamanyCua) {
        this.cua = new CircularQueue(tamanyCua);
    }

    @Override
    public void send(TCPSegment seg) {
        this.cua.put(seg);
        this.tamanyCua++;
    }

    @Override
    public TCPSegment receive() {
        return (TCPSegment) this.cua.get();
    }

    /* public void setCua(int tamanyCua){
        this.tamanyCua = tamanyCua;
    }*/
}
