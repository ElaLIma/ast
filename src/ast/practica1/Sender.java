
package ast.practica1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 * Implementación del nivel de aplicación
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class Sender {

    private final TSocketSender tss;
    private final int N = 10;
    private FileInputStream fr;
    private int offset;
    

    /**
     * Constructor de la clase 
     * @param ch Canal de envío
     */
    public Sender(Channel ch) {
        tss = new TSocketSender(ch);
        this.offset = 0;
        try {
            fr = new FileInputStream("poema.txt");
        } catch (FileNotFoundException ex) {
        ex.printStackTrace();
        }
        
    }

    
    /**
     * Lee N bytes del fichero y los envia. Retorna el número real de bytes
     * enviados. -1 en caso de final de fichero
     * 
     * @return Número de bytes leídos, -1 en casode final del fichero.
     */
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
    
    /**
     * Cierra los streams del fichero y la conexión.
     * @throws IOException 
     */
    public void close() throws IOException {
        this.fr.close();
        this.tss.close();
    }
    
 }
