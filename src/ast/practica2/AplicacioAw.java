/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica2;

import java.io.IOException;

/**
 *
 * @author alex
 */
public class AplicacioAw {

    public static void main(String[] args) throws IOException {
        Channel ch = new AwaitChannel(2);
        Sender s = new Sender(ch);
        Thread sThread = new Thread(s);
        Receiver r = new Receiver(ch);
        Thread rThread = new Thread(r);
        sThread.start();
        rThread.start();
    }
}
