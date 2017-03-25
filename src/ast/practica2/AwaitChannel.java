/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica2;

import ast.protocols.tcp.TCPSegment;
import java.util.Arrays;

/**
 *
 * @author lastusr11
 */
public class AwaitChannel implements Channel {

    private static CircularQueue cua;
    boolean permiso;
    Mutex m;

    public AwaitChannel(int length) {
        this.m = new Mutex();
        this.cua = new CircularQueue(length);
    }

    @Override

    public void send(TCPSegment seg) {
        m.entraZC();
        while (cua.full()) {
            m.surtZC();
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
        System.out.println(Arrays.toString(tcps.getData()));
        m.surtZC();
        return tcps;
    }

}
