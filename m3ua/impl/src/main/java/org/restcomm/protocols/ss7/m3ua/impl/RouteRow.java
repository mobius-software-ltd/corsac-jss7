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

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.m3ua.State;
import org.restcomm.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3Primitive;
import org.restcomm.protocols.ss7.mtp.Mtp3ResumePrimitive;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class RouteRow implements AsStateListener {
	private static final Logger logger = LogManager.getLogger(RouteRow.class);

	private int mtp3Status = Mtp3PausePrimitive.PAUSE;
	private ConcurrentHashMap<String, AsImpl> servedByAsSet = null;
	private int dpc;
	private final M3UAManagementImpl m3uaManagement;
	private UUID uniqueID;

	private Semaphore pauseSemaphore = new Semaphore(1);

    protected TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		
		@Override
		public void onSuccess() {			
		}
		
		@Override
		public void onError(Exception exception) {
			logger.error("An error occured in dummy callback of route row," + exception);
		}
	};
	
	RouteRow(int dpc, M3UAManagementImpl m3uaManagement) {
		this.dpc = dpc;
		this.m3uaManagement = m3uaManagement;
		this.servedByAsSet = new ConcurrentHashMap<String, AsImpl>();
		this.uniqueID = m3uaManagement.getUuidGenerator().GenerateTimeBasedGuid(System.currentTimeMillis());
	}

	public int getDpc() {
		return dpc;
	}

	public void setDpc(int dpc) {
		this.dpc = dpc;
	}

	protected void addServedByAs(AsImpl asImpl) {
		this.servedByAsSet.put(asImpl.getName(), asImpl);
		asImpl.addAsStateListener(uniqueID, this);
	}

	protected int servedByAsSize() {
		return this.servedByAsSet.size();
	}

	protected void removeServedByAs(AsImpl asImpl) {
		boolean flag = (this.servedByAsSet.remove(asImpl.getName()) != null);
		asImpl.removeAsStateListener(uniqueID);
		if (!flag)
			logger.error(String.format("Removing route As=%s from DPC=%d failed!", asImpl, dpc));
		else if (logger.isDebugEnabled())
			logger.debug(String.format("Removed route As=%s from DPC=%d successfully!", asImpl, dpc));
	}

	@Override
	public void onAsActive(AsImpl asImpl) {
		// We only send MTP3 RESUME to MTP3 user if its not already sent for
		// this DPC
		if (this.mtp3Status != Mtp3Primitive.RESUME) {
			this.mtp3Status = Mtp3Primitive.RESUME;
			Mtp3ResumePrimitive mtp3ResumePrimitive = new Mtp3ResumePrimitive(this.dpc);
			this.m3uaManagement.sendResumeMessageToLocalUser(mtp3ResumePrimitive, dummyCallback);
		}
	}

	@Override
	public void onAsInActive(AsImpl asImpl) {
		// Send MTP3 PAUSE to MTP3 user only if its not already sent for this
		// DPC
		try {
			pauseSemaphore.acquire();
		} catch (InterruptedException e) {
		}

		try {
			if (this.mtp3Status != Mtp3Primitive.PAUSE) {
				Iterator<AsImpl> iterator = this.servedByAsSet.values().iterator();
				while (iterator.hasNext()) {
					AsImpl asImplTmp = iterator.next();
					if ((asImplTmp.getState().getName().equals(State.STATE_ACTIVE))
							|| (asImplTmp.getState().getName().equals(State.STATE_PENDING)))
						// If there are more AS in ACTIVE || PENDING state, no need
						// to call PAUSE for this DPC
						return;
				}

				this.mtp3Status = Mtp3Primitive.PAUSE;

				Mtp3PausePrimitive mtp3PausePrimitive = new Mtp3PausePrimitive(this.dpc);
				this.m3uaManagement.sendPauseMessageToLocalUser(mtp3PausePrimitive, dummyCallback);
			}
		} finally {
			pauseSemaphore.release();
		}
	}

	@Override
	public String toString() {
		return "RouteRow [dpc=" + dpc + ", mtp3Status=" + mtp3Status + ", asSet=" + servedByAsSet + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dpc;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RouteRow other = (RouteRow) obj;
		if (dpc != other.dpc)
			return false;
		return true;
	}

}
