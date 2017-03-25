/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica3;
import ast.protocols.tcp.TCPSegment;

/**
 *
 * @author alex.llobet
 */
public interface Channel {
    public int getMMS();

    public void send(TCPSegment seg);

    public TCPSegment receive();
}
