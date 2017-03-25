/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
package ast.practica3;

import ast.logging.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TSocketBase {

    public static Log log = LogFactory.getLog(TSocketBase.class);
    protected Lock lk;
    protected Condition appCV;
    protected Channel channel;

    protected TSocketBase(Channel ch) {
        lk = new ReentrantLock();
        appCV = lk.newCondition();
        channel = ch;
    }

}
