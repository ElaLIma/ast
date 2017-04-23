package ast.practica2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Clase que implementa el emisor
 *
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class Sender implements Runnable {

    private final TSocketSender tss;
    private final int N = 10;
    private FileInputStream fr;
    Mutex m;

    /**
     * Constructor de la clase
     *
     * @param ch Canal por el que se enviarán los paquetes
     */
    public Sender(Channel ch) {
        tss = new TSocketSender(ch);
        Mutex m = new Mutex();
        try {
            fr = new FileInputStream("poema.txt");
        } catch (FileNotFoundException ex) {
        }

    }

    //llegeix N bytes del fitxer i els envia.
    //Retorna el número real de bytes enviats
    //-1 en cas de final de fitxer
    /**
     * Método que envia paquetes, introduciéndolos en la cola
     * @return
     * @throws IOException 
     */
    public int enviar() throws IOException {
        byte[] data = new byte[this.N]; //Creamos un array de bytes que llenaremos
        int datosLeidos = this.fr.read(data, 0, N);
        if (datosLeidos > 0) {
            this.tss.sendData(data, 0, N);
        }
        return datosLeidos;
    }

    //Tanca l’stream al fitxer i la connexió
    /**
     * Cierra el stream que abre el fichero y la conexión.
     * @throws IOException 
     */
    public void close() throws IOException {
        this.fr.close();
        this.tss.close();

    }
    

    @Override
    public void run() {
        int datosLeidos; //en teoria, lee y envia hasta el final de fichero.

        try {
            do {

                datosLeidos = this.enviar();
            } while (datosLeidos > 0);
            this.close(); //llegados al EOF, cerramos la emision.
        } catch (IOException ex) {
            ex.getMessage();
        }
    }

}
