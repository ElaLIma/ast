/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica1;

/**
 *
 * @author lastusr11
 */
public class mainCircular {

    public static void main(String[] args) {
        int el = 5;
        CircularQueue cua = new CircularQueue(4);
        for (int i = 0; i < 4; i++) {
            cua.put(i);
        }
        Object ultimo = cua.peekLast();
        System.out.println("Tamaño: " + cua.size());
        System.out.println("Primer elemento: " + cua.peekFirst());
        System.out.println("Ultimo elemento: " + ultimo);
        System.out.println("Extracción: " + cua.get());
        System.out.println("Primer elemento: " + cua.peekFirst());
        ultimo = cua.peekLast();
        System.out.println("Ultimo elemento: " + ultimo);
        System.out.println("Extracción: " + cua.get());
        System.out.println("Primer elemento: " + cua.peekFirst());
        System.out.println("Ultimo elemento: " + cua.peekLast());
        System.out.println("Extracción: " + cua.get());
        System.out.println("Primer elemento: " + cua.peekFirst());
        System.out.println("Ultimo elemento: " + cua.peekLast());
        System.out.println("Extracción: " + cua.get());
        System.out.println("Primer elemento: " + cua.peekFirst());
        System.out.println("Ultimo elemento: " + cua.peekLast());
        System.out.println("Tamaño después de extraer: " + cua.size());

        System.out.println("Se ha introducido un elemento.");
        System.out.println("Tamaño después de introducir: " + cua.size());
        System.out.println("Primer elemento: " + cua.peekFirst());
        System.out.println("Ultimo elemento: " + cua.peekLast());
        System.out.println("¿3 espacios libre?: " + cua.hasFree(3));
        for (int i = 0; i < 4; i++) {
            cua.put(i);
        }

    }

}
