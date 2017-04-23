/**
 * Práctica 2: Introducción a los Threads
 * 
 * Clase AplicacioBW que contiene la función main() en la que se envía y se recibe
 * recibe implementación de espera activa con await
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
package ast.practica2;

import java.io.IOException;

/**
 *
 * @author alex
 */
public class AplicacioBW {

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
