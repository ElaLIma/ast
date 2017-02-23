/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author alex
 */
public class Sender {

    private final TSocketSender tss;
    private final int N = 10; //mod a 10
    private FileInputStream fr;

    //el fitxer poema.txt ha d’estar en la carpeta del projecte.
    public Sender(Channel ch) {
        this.tss = new TSocketSender(ch);
        try {
            this.fr = new FileInputStream("poema.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    //llegeix N bytes del fitxer i els envia.
    //Retorna el número real de bytes enviats
    //-1 en cas de final de fitxer
    public int enviar() throws IOException {
        int c, i = 0;
        byte[] data = new byte[this.N];

        try {
            while ((c = this.fr.read()) != -1) {
                data[i] = (byte) c;
                i++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.tss.sendData(data, 0, i);
        return i;
    }

    //Tanca l’stream al fitxer i la connexió
    public void close() throws IOException {
        this.fr.close();
        this.tss.close();

    }
}
