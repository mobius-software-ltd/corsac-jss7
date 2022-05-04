/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.restcomm.protocols.ss7.m3ua.impl.scheduler;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class M3UAScheduler implements Runnable {
    private static final Logger logger = LogManager.getLogger(M3UAScheduler.class);

    protected ConcurrentLinkedQueue<M3UATask> firstPool = new ConcurrentLinkedQueue<M3UATask>();
    protected ConcurrentLinkedQueue<M3UATask> secondPool = new ConcurrentLinkedQueue<M3UATask>();

    private AtomicInteger currPool=new AtomicInteger(1);
    
    public void execute(M3UATask task) {
        if (task == null) {
            return;
        }
        
        if(currPool.get()%2==1)
        	this.firstPool.offer(task);
        else
        	this.secondPool.offer(task);        
    }

    public void run() {
        long now = System.currentTimeMillis();
        
        ConcurrentLinkedQueue<M3UATask> activeTasks;
        ConcurrentLinkedQueue<M3UATask> nextTasks;
        if(currPool.getAndIncrement()%2==1) {
        	activeTasks=this.firstPool;
        	nextTasks=this.secondPool;
        } else {
        	activeTasks=this.secondPool;
        	nextTasks=this.firstPool;
        }
        
        M3UATask task=activeTasks.poll();
        while(task!=null) {
            // check if has been canceled from different thread.
            if (!task.isCanceled()) {                
                try {
                    task.run(now);
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Failuer on task run.", e);
                    }
                }
                // check if its canceled after run;
                if (!task.isCanceled()) {
                	nextTasks.offer(task);
                }
            }
            
            task=activeTasks.poll();
        }
    }
}