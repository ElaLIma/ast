/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica1;

import java.io.IOException;

/**
 *
 * @author alex
 */
public class Aplicacio {

    public static void main(String[] args) throws IOException {
        Channel ch = new QueueChannel(2);
        try {
            Sender s = new Sender(ch);
            Receiver r = new Receiver(ch);

            int ls = s.enviar();
            while (ls > 0) {
                int lr = r.receive();
                if (lr < 0) {
                    System.out.println("Error en recepció!!");
                }
                ls = s.enviar();
            }
            s.close();
            r.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
