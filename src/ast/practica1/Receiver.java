/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author alex
 */
public class Receiver {

    private final TSocketReceiver tsr;
    private final int N = 1000;
    private FileOutputStream fr;

    public Receiver(Channel ch) {
        tsr = new TSocketReceiver(ch);
        try {
            fr = new FileOutputStream("poemaRebut.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    //Rep un segment i guarda els bytes rebuts al fitxer
    //retorna el número de bytes rebuts
    //-1 si no ha rebut cap byte (final)
    public int receive() throws IOException {
        byte[] data = new byte[this.N];
        int informacioProcessada;
        informacioProcessada = this.tsr.receiveData(data, 0, this.N);
        data = this.tsr.getData();
        for (int i = 0; i < informacioProcessada; i++) {
            this.fr.write(data[i]);
        }
        if (informacioProcessada == 0) {
            this.close();
            return -1;
        }
        this.close();
        return informacioProcessada;
    }

    //Tanca l’stream al fitxer i la connexió
    public void close() throws IOException {
        this.fr.close();
        this.tsr.close();

    }

}
