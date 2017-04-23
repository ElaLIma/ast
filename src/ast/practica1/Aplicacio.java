/**
 * Práctica 1: CircularQueue
 * 
 * Clase Aplicacio que contiene la función main() en la que se envía y se recibe
 * de forma secuencial, el fichero poema.txt
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
package ast.practica1;
import java.io.IOException;


public class Aplicacio {
    
    /**
     * Se envían bytes mientras el buffer contega.
     * @param args
     * @throws IOException 
     */

    public static void main(String[] args) throws IOException {
        Channel ch = new QueueChannel(2);
        try {
            Sender s = new Sender(ch);
            Receiver r = new Receiver(ch);

            int ls = s.enviar();
            while (ls > 0) {
                int lr = r.receive();
                if (lr < 0) {
                    System.out.println("Error en recepció!!");
                }
                ls = s.enviar();
            }
            s.close();
            r.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
