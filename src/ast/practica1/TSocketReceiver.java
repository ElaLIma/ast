/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica1;

import ast.protocols.tcp.TCPSegment;

/**
 *
 * @author alex.llobet
 */
public class TSocketReceiver {

    private final Channel ch;
    private byte[] data;

    public TSocketReceiver(Channel ch) {
        this.ch = ch;
    }

    public int receiveData(byte[] data, int offset, int length) {
        TCPSegment segment = this.ch.receive();  //Se recibe un segmento
        byte[] cpSegment = segment.getData();   //Se pasa a un array de bytes
        
        //ERROR: Aixo ens diu la quantitat de cel·les, no d'elements.
        int tamanyRecepcio = cpSegment.length;  //Obtenemos el tamaño del array

        /*Llenamos nuestro arrray "data" con los bytes del array cpSegment, 
        desde la posicion offset hasta que se acaben los datos de cpSegment
         */
        for (int i = offset; i < offset + tamanyRecepcio; i++) {
            data[i] = cpSegment[i - offset];
        }
        //Si el segmento estaba vacio
        if (tamanyRecepcio == 0) {
            this.data = data;
            return -1;
        } else if (tamanyRecepcio == length) {
            this.data = data;
            return length;
        } /*Llenamos de -1 (flag) indicando que el segmento contenia menos 
        datos que nuestro parametro length (la falta de datos se llena con -1)*/ else {
            for (int i = offset + tamanyRecepcio; i < length; i++) {
                data[i] = -1;
            }
            this.data = data;
            return tamanyRecepcio;
        }

    }

    public void close() {
        TCPSegment s = new TCPSegment();
        ch.send(s);
    }

    public byte[] getData() {
        return this.data;
    }

}
