/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.m3ua.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.m3ua.impl.message.aspsm.HeartbeatImpl;
import org.restcomm.protocols.ss7.m3ua.impl.scheduler.M3UATask;
import org.restcomm.protocols.ss7.m3ua.message.aspsm.Heartbeat;

import com.mobius.software.common.dal.timers.PeriodicQueuedTasks;
import com.mobius.software.common.dal.timers.Timer;

/**
 * @author Amit Bhayani
 * @author yulianoifa
 *
 */
public class HeartBeatTimer extends M3UATask {

	private static final Logger logger = LogManager.getLogger(HeartBeatTimer.class);

	private static final int HEART_BEAT_ACK_MISSED_ALLOWED = 2;

	private static final Heartbeat HEART_BEAT = new HeartbeatImpl();

	private volatile long lastM3UAMessageTime = 0L;
	private volatile int heartBeatAckMissed = 0;

	private AspFactoryImpl aspFactoryImpl = null;

	/**
	 *
	 */
	public HeartBeatTimer(AspFactoryImpl aspFactoryImpl) {
		super(aspFactoryImpl.m3UAManagementImpl != null ? aspFactoryImpl.m3UAManagementImpl.workerPool.getPeriodicQueue() : null);

		this.aspFactoryImpl = aspFactoryImpl;
	}

	public void setQueuedTasks(PeriodicQueuedTasks<Timer> queuedTasks) {
		super.queuedTasks = queuedTasks;
	}

	protected void reset() {
		this.lastM3UAMessageTime = System.currentTimeMillis();
		this.heartBeatAckMissed = 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.m3ua.impl.scheduler.M3UATask#tick(long)
	 */
	@Override
	public void tick(long now) {
		if (now - this.lastM3UAMessageTime >= this.aspFactoryImpl.m3UAManagementImpl.getHeartbeatTime()) {
			this.lastM3UAMessageTime = now;
			this.heartBeatAckMissed++;

			this.aspFactoryImpl.write(HEART_BEAT);
		}

		if (this.heartBeatAckMissed > HEART_BEAT_ACK_MISSED_ALLOWED) {
			logger.warn(String.format(
					"HEART_BEAT ACK missed %d is greater than configured %d for AspFactory %s. Underlying Association will be stopped and started again",
					this.heartBeatAckMissed, HEART_BEAT_ACK_MISSED_ALLOWED, this.aspFactoryImpl.getName()));
			try {
				this.aspFactoryImpl.transportManagement.stopAssociation(this.aspFactoryImpl.associationName);
			} catch (Exception e) {
				logger.warn(String.format("Error while trying to stop underlying Association for AspFactpry=%s",
						this.aspFactoryImpl.getName()), e);
			}

			try {
				this.aspFactoryImpl.transportManagement.startAssociation(this.aspFactoryImpl.associationName);
			} catch (Exception e) {
				logger.error(String.format("Error while trying to start underlying Association for AspFactpry=%s",
						this.aspFactoryImpl.getName()), e);
			}

			// finally cancel
			this.stop();
		}
	}

	@Override
	public String printTaskDetails() {
		return "Task name: M3UAHeartBeatTimer";
	}
}
