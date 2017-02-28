/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica1;

import ast.util.Queue;
import java.util.Iterator;

public class CircularQueue<E> implements Queue<E> {

    private final E[] cua;
    private final int N;
    private int nombreElements;
    private int inici;
    private int fi;

    public CircularQueue(int N) {
        this.N = N;
        cua = (E[]) (new Object[N]);
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Unsupported method iterator()");
    }

    @Override
    public int size() {
        return this.nombreElements;
    }

    @Override
    public boolean hasFree(int n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException();
        } else return N - size() >= n || n == 0;
    }

    @Override
    public boolean empty() {
        return nombreElements == 0;
    }

    @Override
    public boolean full() {
        return nombreElements == N;
    }

    @Override
    public E peekFirst() {
        if (cua[inici] != null) {
            return cua[inici];
        } else {
            return null;
        }
    }

    @Override
    public E peekLast() {
        int finalito = (inici + nombreElements-1) % N;
        if (cua[finalito] != null) {
            return cua[finalito];
        } else {
            return null;
        }
    }

    @Override
    public E get() throws IllegalStateException {
        if (empty()) {
            throw new IllegalStateException();
        } else {
            E objecte = cua[inici];
            cua[inici] = null;
            inici = (inici + 1) % N; //ahora el inicio se ha desplazado. El modulo es por si se da toda la vuelta
            nombreElements--;
            return objecte; //pot contenir nullPointer
        }
    }

    @Override
    public void put(E e) throws IllegalStateException {
        if (full()) {
            throw new IllegalStateException();
        } else {
            cua[fi] = e;
            fi = (fi + 1) % N;
            this.nombreElements++;
        }

    }

}
