
package ast.practica2;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Clase que implementa la exclusión mutua.
 *
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 *
 */
public class Mutex{
    
    AtomicBoolean ab;
    
    /**
     * Constructor de la clase.
     */

    public Mutex() {
        ab = new AtomicBoolean(false);
    }

    /**
     * Método que bloquea la la cola, la convierte en una zona de exclusión, 
     * para que el resto de threads no puedan entrar a la cola.
     */
    public void entraZC() {
        while(ab.getAndSet(true)){;}
    }

    /**
     * Método que libera la cola.
     */
    public void surtZC() {
        ab.set(false);
    }
    
}
