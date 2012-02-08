package com.fabianofranz.deschain;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Encapsulates the event messages from Instagram.
 *
 * @author Fabiano Franz
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
