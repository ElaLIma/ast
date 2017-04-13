
package ast.practica1;
import ast.protocols.tcp.TCPSegment;

/**
 * 
 * Implementa el nivel de transporte.
 * 
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class TSocketReceiver {

    private final Channel ch;
    private byte[] data;

    /**
     * Constructor de la clase
     * @param ch Canal de envío
     */
    public TSocketReceiver(Channel ch) {
        this.ch = ch;
    }
    
    /**
     * Obtiene un TCPSegment y toma sus datos para rellenar el buffer del receptor.
     * 
     * @param data Buffer del receptor, en el que se volcarán los datos recibidos.
     * @param offset Posición del buffer desde donde se comenzará a volcar los datos recibidos.
     * @param length Longitud de los datos a recibir. 
     * @return Número real de bytes recibidos, no siempre coincide con length.
     */

    public int receiveData(byte[] data, int offset, int length) {
        TCPSegment segment = this.ch.receive();  //Se recibe un segmento
        byte[] cpSegment = segment.getData();   //Se pasa a un array de bytes
        int tamanyRecepcio = 0;
        
        /*Llenamos nuestro arrray "data" con los bytes del array cpSegment, 
        desde la posicion offset hasta que se acaben los datos de cpSegment
         */
        for (int i = offset; i < offset + length; i++) {
            if(cpSegment[i-offset]!=-1){
                tamanyRecepcio++;
            }
            data[i] = cpSegment[i - offset];
        }
        this.data = data;
        //Si el segmento estaba vacio
        if (tamanyRecepcio == 0) {
           return -1;
        } else{
           return tamanyRecepcio;
        }

    }
    
    /**
     * Cierra la transmisión de datos.
     */

    public void close() {
        TCPSegment s = new TCPSegment();
        ch.send(s);
    }
    
    /**
     * Retorna el buffer del receptor
     * @return Buffer del receptor
     */

    public byte[] getData() {
        return this.data;
    }

}
