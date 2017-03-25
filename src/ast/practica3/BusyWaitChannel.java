/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica3;

import ast.practica2.*;
import ast.protocols.tcp.TCPSegment;
import java.util.Arrays;

/**
 *
 * @author lastusr11
 */
public class BusyWaitChannel implements Channel {

    private CircularQueue cua;
    int sndMMS;

    public BusyWaitChannel(int tamanyCua) {
        this.cua = new CircularQueue(tamanyCua);
    }

    @Override
    public void send(TCPSegment seg) {
        while (this.cua.full()) {

            /* System.out.println("Soy el snder, el tamaño de la cola es: " + this.cua.size());
             System.out.println("Soy el snder, llena: " + this.cua.full());*/
        }
        this.cua.put(seg);
    }

    @Override
    public TCPSegment receive() {
        TCPSegment tcps;
        while (this.cua.empty()) {
            /*System.out.println("Soy el rceiver, el tamaño de la cola es: " + this.cua.size());
             System.out.println("Soy el snder, vacia: " + this.cua.full());*/
        }
        tcps = (TCPSegment) this.cua.get();
        System.out.println(Arrays.toString(tcps.getData()));
        return tcps;
    }
    @Override
    public int getMMS(){
        return this.sndMMS;
                
    }

}
