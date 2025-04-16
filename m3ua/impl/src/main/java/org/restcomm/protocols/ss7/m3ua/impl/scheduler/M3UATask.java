/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.m3ua.impl.scheduler;

import com.mobius.software.common.dal.timers.PeriodicQueuedTasks;
import com.mobius.software.common.dal.timers.Timer;

public abstract class M3UATask implements Timer {
	private volatile long startTime = System.currentTimeMillis();
	protected PeriodicQueuedTasks<Timer> queuedTasks;

	public M3UATask(PeriodicQueuedTasks<Timer> queuedTasks) {
		this.queuedTasks = queuedTasks;
	}

	@Override
	public final void execute() {
		if (this.startTime != Long.MAX_VALUE) {
			long now = System.currentTimeMillis();
			this.tick(now);

			// check if its canceled after run;
			if (this.startTime != Long.MAX_VALUE && this.queuedTasks != null) {
				this.startTime = System.currentTimeMillis();
				this.queuedTasks.store(this.getRealTimestamp(), this);
			}
		}
	}

	public abstract void tick(long now);

	public void start() {
		this.startTime = System.currentTimeMillis();
	}

	@Override
	public void stop() {
		this.startTime = Long.MAX_VALUE;
	}

	@Override
	public long getStartTime() {
		return this.startTime;
	}

	@Override
	public Long getRealTimestamp() {
		return this.startTime + 500L;
	}
}
