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
    private final int N = 10;
    private FileInputStream fr;
    private int offset;
    

    //el fitxer poema.txt ha d’estar en la carpeta del projecte.
    public Sender(Channel ch) {
        tss = new TSocketSender(ch);
        this.offset = 0;
        try {
            fr = new FileInputStream("poema.txt");
        } catch (FileNotFoundException ex) {
        ex.printStackTrace();
        }
        
    }

    
    //llegeix N bytes del fitxer i els envia.
    //Retorna el número real de bytes enviats
    //-1 en cas de final de fitxer
    public int enviar() {
    int c, datosLeidos=0,i = 0,posicion = 0;
        byte[] data = new byte[this.N]; //Creamos un array de bytes que llenaremos

        try {
            
            if (this.fr.available() == 0) { //si no queda nada para leer o final de fichero
                return -1; //devuelve -1
            } else {
                for(i=offset;i<offset+this.N;i++){ //desde el offset (último dato leído) hasta la longitud que hemos de enviar
                    if(((c = this.fr.read()) == -1)){ //si cuando leemos (ejemplo: "hola" y llegamos hasta despues de la "a")
                        data[posicion]=-1; //llemamos de -1 lo que queda y en recepción ya miraremos
                    }
                    else{
                        data[posicion] = (byte) c; //no estamos en final de fichero, luego leemos el siguiente byte
                        datosLeidos++;
                    }
                    posicion++;
                }
                
                offset = offset + this.N; //actualizamos offset
                
                }
            } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.tss.sendData(data, 0, i);
        return datosLeidos; //no hemos devuelto -1, ni hemos encontrado final de fichero, enotnces hemos leido N bytes
    }
    
    //Tanca l’stream al fitxer i la connexió
    public void close() {

    }
    
 }
