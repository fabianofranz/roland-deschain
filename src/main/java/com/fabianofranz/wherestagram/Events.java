/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.fabianofranz.wherestagram;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author ffranz
 */
public class Events {
    
    private Queue<String> messages = new ConcurrentLinkedQueue<String>();
    
    static private final Events instance = new Events();
    
    private Events() {}
    
    static public Events instance() {
        return instance;
    }
    
    public Queue<String> messages() {
       return messages; 
    }
    
    public void add(final String message) {
        messages.add(message);
    }
    
    public Boolean isEmpty() {
        return messages.isEmpty();
    }
    
    public String poll() {
        return messages.poll();
    }
    
}
