/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.ejb;

/**
 *
 * @author Dejan
 */
public class EJBUtil {

    /**
     * Use this method to obtain an IP address (as a String object) of the client that is making a remote
     * EJB call.
     * 
     * It relies on JBoss giving names of worker-threads in the following fashion:
     * 
     * WorkerThread#0[192.168.0.108:55208]
     * 
     * @return 
     */
    public static String getCurrentClientIPAddress() {  
        String currentThreadName = Thread.currentThread().getName();
        int begin = currentThreadName.indexOf('[') + 1;  
        int end = currentThreadName.indexOf(']') - 1;  
        String remoteClient = currentThreadName.substring(begin, end);  
        
        return remoteClient;  
    }   // getCurrentClientIPAddress() method
    
}
