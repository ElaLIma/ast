/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica3;

import java.util.concurrent.locks.*;
import ast.protocols.tcp.TCPSegment;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonitorChannel implements Channel {

    //public static final int MAX_MSG_SIZE = 1480; // Link MTU - IP header
    public static final int MAX_MSG_SIZE = 5; 
    protected Lock lk;
    protected Condition enviar, rebre;
    private static CircularQueue cua;
    private double lossRatio, nombreAleatori;
    private Random generadorAleatorio;

    public MonitorChannel(int length) {
        this.lk = new ReentrantLock();
        this.enviar = lk.newCondition();
        this.rebre = lk.newCondition();
        this.cua = new CircularQueue(length);
        this.generadorAleatorio = new Random();
    }

    public MonitorChannel(double lossRatio, int length) {
        this.lossRatio = lossRatio;
        this.lk = new ReentrantLock();
        this.enviar = lk.newCondition();
        this.rebre = lk.newCondition();
        this.cua = new CircularQueue(length);
        this.generadorAleatorio = new Random();
        

    }

    @Override

    public void send(TCPSegment seg) {
        lk.lock();

        while (cua.full()) {
            try {
                enviar.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(MonitorChannel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.nombreAleatori = generadorAleatorio.nextDouble();
       // System.out.println("El lossratio es de: "+this.lossRatio+" i el nombre: "+this.nombreAleatori);
        if (this.nombreAleatori > this.lossRatio) {
            cua.put(seg);
        }
        rebre.signal();
        lk.unlock();
    }

    @Override
    public TCPSegment receive() {
        lk.lock();

        TCPSegment tcps;
        while (cua.empty()) {
            try {
                rebre.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(MonitorChannel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tcps = (TCPSegment) this.cua.get();
        enviar.signal();

        lk.unlock();
        return tcps;
    }

    @Override
    public int getMMS() {
        return MAX_MSG_SIZE;
    }
}
