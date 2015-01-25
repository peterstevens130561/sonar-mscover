package com.stevpet.sonar.plugins.dotnet.mscover.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSplitterQueue {
    private static final Logger LOG = LoggerFactory.getLogger(XmlSplitterQueue.class);
    private List<String> queue = new ArrayList<String>();
    private int waiting=0;
    private int busy=0;
    private Object lock = new Object();
    public synchronized String take() {
           
        synchronized(lock) {
            waiting++;
            busy--;
        }
        LOG.info("waiting {}",busy);
        while(queue.size()==0) {
            try {
                wait();
            } catch (InterruptedException e) {}            
        }
        String segment;
        synchronized(lock) {
            waiting--;
            busy++;
            segment=queue.remove(0);
        }

        LOG.info("running {}",busy);
        notifyAll();
        return segment;
    }
    
    public synchronized void put(String segment) {

        while(queue.size() >= 10) {
            try {
                wait();
            } catch (InterruptedException e) {}

        }
            LOG.info("put");
            synchronized(lock) {
            queue.add(segment);
            }
            LOG.info("putted {}",queue.size());
        notifyAll();
    }

    public  void waitTillDone() {
        Random random = new Random();
        while(isBusy()) {
            LOG.info("busy {}, size {}",busy,queue.size());
            try {
               Thread.sleep(1000);
            } catch(InterruptedException e ) {

            }
        }
    }
    
    private boolean isBusy() {
        synchronized(lock) {
            return (busy!=-10 || queue.size() !=0);
        }
    }
}
