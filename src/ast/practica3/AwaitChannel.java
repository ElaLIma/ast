/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica3;

import ast.practica2.*;
import ast.protocols.tcp.TCPSegment;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lastusr11
 */
public class AwaitChannel implements Channel {

    private static CircularQueue cua;
    boolean permiso;
    int sndMMS;
    Mutex m;

    public AwaitChannel(int length) {
        this.m = new Mutex();
        this.cua = new CircularQueue(length);
        this.sndMMS=4;
    }

    @Override

    public void send(TCPSegment seg) {
        m.entraZC();
        while (cua.full()) {
            m.surtZC();
            m.entraZC();
        }
        this.cua.put(seg);
        m.surtZC();
    }

    @Override
    public TCPSegment receive() {
        TCPSegment tcps;
        m.entraZC();
        while (cua.empty()) {
            m.surtZC();
            m.entraZC();
        }
        tcps = (TCPSegment) this.cua.get();
        m.surtZC();
        return tcps;
    }
    @Override
    public int getMMS(){
        return this.sndMMS;
                
    }
            

}
