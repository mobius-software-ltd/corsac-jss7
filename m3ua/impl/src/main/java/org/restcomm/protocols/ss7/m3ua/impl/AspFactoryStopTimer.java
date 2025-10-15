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
import org.restcomm.protocols.ss7.m3ua.impl.scheduler.M3UATask;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class AspFactoryStopTimer extends M3UATask {

	private static Logger logger = LogManager.getLogger(AspFactoryStopTimer.class);

	private int STOP_TIMER_TIMEOUT = 3000;

	private AspFactoryImpl aspFactoryImpl = null;

	private long initiatedTime = 0L;

	/**
	 * @param aspFactoryImpl
	 * @param queuedTasks
	 */
	public AspFactoryStopTimer(AspFactoryImpl aspFactoryImpl) {
		super(aspFactoryImpl.m3UAManagementImpl.workerPool.getPeriodicQueue());
		this.aspFactoryImpl = aspFactoryImpl;
		this.initiatedTime = System.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.m3ua.impl.scheduler.M3UATask#tick(long)
	 */
	@Override
	public void tick(long now) {
		if (now - this.initiatedTime >= STOP_TIMER_TIMEOUT) {

			if (this.aspFactoryImpl.association.isConnected()) {
				logger.warn(String.format(
						"Asp=%s was stopped but underlying Association=%s was not stopped after TIMEOUT=%d ms. Forcing stop now",
						this.aspFactoryImpl.getName(), this.aspFactoryImpl.association.getName(), STOP_TIMER_TIMEOUT));
				try {
					this.aspFactoryImpl.transportManagement.stopAssociation(this.aspFactoryImpl.association.getName());
				} catch (Exception e) {
					logger.error(String.format("Exception while trying to stop Association=%s",
							this.aspFactoryImpl.association.getName()));
				}
			}

			// Finally cancel
			this.stop();
		} // if(now-this.initiatedTime >= STOP_TIMER_TIMEOUT)
	}

	@Override
	public String printTaskDetails() {
		return "Task name: M3UAAspFactoryStopTimer";
	}
}
