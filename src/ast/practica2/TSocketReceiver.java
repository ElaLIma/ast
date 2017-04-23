
package ast.practica2;

import ast.practica1.*;
import ast.protocols.tcp.TCPSegment;

/**
 * Clase que implementa el extremo receptor del socket
 *
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 */
public class TSocketReceiver {

    private final Channel ch;
    private byte[] data;

    /**
     * Constructor de la clase,recibe el canal
     *
     * @param ch Canal de comunicación
     */
    public TSocketReceiver(Channel ch) {
        this.ch = ch;
    }

    /**
     * Método que implementa la recepción de paquetes
     * 
     * @param data Array en el que se introducirán los datos recibidos
     * @param offset Posicion desde la que se introducirán los datos en el array de datos
     * @param length Tamaño de datos a recibir
     * @return Número de datos reales recibidos, información útil.
     */
    public int receiveData(byte[] data, int offset, int length) {
        TCPSegment segment = this.ch.receive();  //Se recibe un segmento
        if (segment.isFin()) {
            this.close();
        }
        byte[] cpSegment = segment.getData();   //Se pasa a un array de bytes
        int tamanyRecepcio = 0;

        /*Llenamos nuestro arrray "data" con los bytes del array cpSegment, 
        desde la posicion offset hasta que se acaben los datos de cpSegment
         */
        for (int i = offset; i < offset + length; i++) {
            if (cpSegment[i - offset] != 0) { //aumenta el tamanyo de datos útiles recibidos mientras no se lea -1 (mirar Sender) 
                tamanyRecepcio++;
            }
            data[i] = cpSegment[i - offset];
        }
        //Si el segmento estaba vacio
        if (tamanyRecepcio == 0) {
            return -1;
        } else {
            return tamanyRecepcio;
        }

    }
    
    /**
     * Método que cierra la conexión
     */

    public void close() {
        System.out.println("Fi de transmissió");
    }

    public byte[] getData() {
        return this.data;
    }

}
