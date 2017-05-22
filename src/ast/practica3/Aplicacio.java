/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica3;

/**
 *
 * @author alex
 */


public class Aplicacio {
    public static void main(String[] args){
            Channel c = new MonitorChannel(2000);
            new Thread(new Sender(c)).start();
            new Thread(new Receiver(c)).start();
    }
}