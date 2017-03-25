/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast.practica3;

/**
 *
 * @author lastusr11
 */

import ast.practica2.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Mutex{
    
    AtomicBoolean ab;

    public Mutex() {
        ab = new AtomicBoolean(false);
    }

    
    public void entraZC() {
        while(ab.getAndSet(true)){;}
    }

    
    public void surtZC() {
        ab.set(false);
    }
    
}
