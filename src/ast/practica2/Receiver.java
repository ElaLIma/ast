/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author alex
 */
public class Receiver implements Runnable {

    private final TSocketReceiver tsr;
    private final int N = 10;
    private FileOutputStream fr;
    private Mutex m;

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
    public int receive() throws IOException {
        byte[] data = new byte[this.N];
        int informacioProcessada; //nombre bytes llegits
        informacioProcessada = this.tsr.receiveData(data, 0, this.N);
        this.fr.write(data, 0, informacioProcessada);
        return informacioProcessada;
    }

    //Tanca l’stream al fitxer i la connexió
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
