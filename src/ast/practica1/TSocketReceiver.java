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

    public void close() {
        TCPSegment s = new TCPSegment();
        ch.send(s);
    }

    public byte[] getData() {
        return this.data;
    }

}
