package ast.practica1;

import ast.util.Queue;
import java.util.Iterator;

/**
 * Clase CircularQueue que implementa una cola circular mediante una lista
 * enlazada, con la inteface Queue.
 *
 * @author Andony Ramón Elá Lima
 * @author Alex Llobet
 *
 * @see Queue
 */
public class CircularQueue<E> implements Queue<E> {

    private final E[] cua;
    private final int N;
    private int nombreElements;
    private int inici;
    private int fi;

    /**
     * Constructor de la clase que recibe el tamaño de la cola
     *
     * @param N Tamaño de la cola
     */
    public CircularQueue(int N) {
        this.N = N;
        cua = (E[]) (new Object[N]);
    }

    /**
     * Lanza una excepción porque no se usa en esta práctica.
     *
     * @return Un iterrador sobre la cola circular
     */
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Unsupported method iterator()");
    }

    /**
     * Retorna el número de elementos que contiene la cola
     *
     * @return Número de elementos de la cola.
     */
    @Override
    public int size() {
        return this.nombreElements;
    }

    /**
     * Consulta si en la cola hay n o más espacios disponibles.
     *
     * @param n Número de espacios a consultar.
     * @return {@code true} si el número de espacios libres es mayor o igual que n.
     * @throws IllegalArgumentException
     */
    @Override
    public boolean hasFree(int n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException();
        } else {
            return N - size() >= n || n == 0;
        }
    }

    /**
     * Consulta si la cola está vacía.
     *
     * @return {@code true} si la cola está vacía.
     */
    @Override
    public boolean empty() {
        return nombreElements == 0;
    }

    /**
     * Consulta si la cola está completa, esdecir, no tiene espacios vacíos.
     *
     * @return {@code true} si la cola ya no tiene espacios vacíos.
     */
    @Override
    public boolean full() {
        return nombreElements == N;
    }

    /**
     * Selecciona y devuelve el primer elemento de la cola, sin borrarlo.
     *
     * @return El primer elemento de la cola.
     */
    @Override
    public E peekFirst() {
        if (cua[inici] != null) {
            return cua[inici];
        } else {
            return null;
        }
    }

    /**
     * Selecciona y devuelve el último elemento de la cola, sin borrarlo.
     *
     * @return El último elemento de la cola.
     */
    @Override
    public E peekLast() {
        int finalito = (inici + nombreElements - 1) % N;
        if (cua[finalito] != null) {
            return cua[finalito];
        } else {
            return null;
        }
    }

    /**
     * Selecciona y devuelve el primer elemento de la cola, y despúes lo borra.
     *
     * @return El primer elemento de la cola.
     * @throws IllegalStateException
     */
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

    /**
     * Inserta el elemento pasado en la última posición de la cola. Si la cola
     * está vacía, lanza una exception.
     *
     * @param e Elemento a insertar en la cola.
     * @throws IllegalStateException
     */
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
