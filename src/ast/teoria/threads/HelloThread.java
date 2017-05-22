
package ast.teoria.threads;

/**
 * Ejemplo de creación de threads con la clase Thread
 *
 * @author Andony Ramón Elá Lima
 */
public class HelloThread extends Thread {
    public void run(){
        System.out.println("Hello from a thread!");
    }
    public static void main(String args[]){
        (new HelloThread()).start();
    }
    
}
