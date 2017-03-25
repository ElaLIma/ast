/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica2;

import ast.util.Queue;
import java.util.Iterator;

public class CircularQueue<E> implements Queue<E> {

    private final E[] cua;
    private final int N;
    private volatile int nombreElements;
    private volatile int inici;
    private volatile int fi;

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
        } else {
            return N - size() >= n || n == 0;
        }
    }

    @Override
    public boolean empty() {
        return this.nombreElements == 0;
    }

    @Override
    public boolean full() {
        return this.nombreElements == N;
    }

    @Override
    public E peekFirst() {
        if (cua[this.inici] != null) {
            return cua[this.inici];
        } else {
            return null;
        }
    }

    @Override
    public E peekLast() {
        int finalito = (this.inici + this.nombreElements - 1) % N;
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
            this.nombreElements--;
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
