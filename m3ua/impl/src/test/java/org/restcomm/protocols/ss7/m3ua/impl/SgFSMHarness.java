package org.restcomm.protocols.ss7.m3ua.impl;

import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSM;
import org.restcomm.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.common.dal.timers.WorkerPool;

public class SgFSMHarness {
	protected static final Logger logger = LogManager.getLogger(SgFSMHarness.class);

	protected ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();
	protected MessageFactoryImpl messageFactory = new MessageFactoryImpl();
	
	protected UUID listenerUUID;
		
	protected WorkerPool workerPool;
	
	protected final Semaphore sendSemaphore = new Semaphore(0);
	protected final AtomicInteger sentMessages = new AtomicInteger(0);
	protected final Semaphore receiveSemaphore = new Semaphore(0);

	public void setUp() throws Exception {
		this.workerPool = new WorkerPool();
		this.workerPool.start(16);
	}
	
	public AspState getAspState(FSM fsm) {
		return AspState.getState(fsm.getState().getName());
	}

	public AsState getAsState(FSM fsm) {
		return AsState.getState(fsm.getState().getName());
	}
	
	public TaskCallback<Exception> getSendMessageCallback(int messages) {
		return new TaskCallback<Exception>() {
			@Override
			public void onSuccess() {				
				SgFSMHarness.this.sentMessages.incrementAndGet();
				if (SgFSMHarness.this.sentMessages.get() == messages)
					SgFSMHarness.this.sendSemaphore.release();
			}

			@Override
			public void onError(Exception exception) {
				logger.error(exception);
			}
		};
	}
	
	public static void acquire(Semaphore semaphore, Long timeout) throws InterruptedException {
		if (timeout == null)
			semaphore.acquire();
		else if (!semaphore.tryAcquire(timeout, TimeUnit.MILLISECONDS))
			throw new RuntimeException("Semaphore is not required in " + timeout + " milliseconds");
	}
}
