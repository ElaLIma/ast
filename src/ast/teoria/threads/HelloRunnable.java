package ast.teoria.threads;

/**
 *
 * Ejemplo de creación de threads implementando Runnable
 *
 * @author Andony Ramón Elá Lima
 */
public class HelloRunnable implements Runnable {

    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String args[]) {
        (new Thread(new HelloRunnable())).start();

    }

}
