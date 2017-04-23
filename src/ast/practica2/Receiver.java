package ast.practica2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Clase que implementa el receptor
 *
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class Receiver implements Runnable {

    private final TSocketReceiver tsr;
    private final int N = 10;
    private FileOutputStream fr;
    private Mutex m;

    /**
     * Constructor de la clase que recibe un canal por donde enviar los paquetes
     *
     * @param ch Canal por donde enviar los paquetes
     */
    public Receiver(Channel ch) {
        tsr = new TSocketReceiver(ch);
        this.m = new Mutex();
        try {
            fr = new FileOutputStream("poemaRebut.txt");
        } catch (FileNotFoundException ex) {
        }
    }

    //Rep un segment i guarda els bytes rebuts al fitxer
    //retorna el número de bytes rebuts
    //-1 si no ha rebut cap byte (final)
    /**
     * Método que recibe los paquetes enviados
     *
     * @return El número real bytes recibidos, información útil
     * @throws IOException
     */
    public int receive() throws IOException {
        byte[] data = new byte[this.N];
        int informacioProcessada; //nombre bytes llegits
        informacioProcessada = this.tsr.receiveData(data, 0, this.N);
        this.fr.write(data, 0, informacioProcessada);
        return informacioProcessada;
    }

    //Tanca l’stream al fitxer i la connexió
    /**
     * Cierra el stream que lee el fichero y la conexión
     *
     * @throws IOException
     */
    public void close() throws IOException {
        this.tsr.close();
        this.fr.close();

    }
    //ElReceiver llegeix del Channel fins EOF i escriu per pantalla

    @Override
    public void run() {
        int infoProcessada = 0;
        try {
            do {
                infoProcessada = this.receive();
            } while (infoProcessada > 0);
            this.close();
        } catch (IOException ex) {
            ex.getMessage();
        }

    }

}
