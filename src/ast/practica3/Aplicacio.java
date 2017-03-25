/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica3;

import ast.practica2.*;
import java.io.IOException;

/**
 *
 * @author alex
 */
public class Aplicacio {

    public static void main(String[] args) throws IOException {
        Channel ch = new BusyWaitChannel(2);
        Sender s = new Sender(ch);
        Thread sThread = new Thread(s);
        Receiver r = new Receiver(ch);
        Thread rThread = new Thread(r);
        sThread.start();
        rThread.start();
    }
}
