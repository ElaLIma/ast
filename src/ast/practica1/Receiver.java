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
 * Implementación del nivel de aplicación
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class Receiver {

    private final TSocketReceiver tsr;
    private final int N = 10;
    private FileOutputStream fr;

    public Receiver(Channel ch) {
        tsr = new TSocketReceiver(ch);
        try {
            fr = new FileOutputStream("poemaRebut.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Recibe un segmento y guarda los bytes recibidos en un fichero.  
     * 
     * @return Número de bytes recibidos, -1 si no recibe nada.
     * @throws IOException 
     */

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
        //this.close();
        return informacioProcessada;
    }

    /**
     * Cierra los streams del fichero y la conexión.
     * @throws IOException 
     */
    public void close() throws IOException {
        this.fr.close();
        this.tsr.close();

    }

}
