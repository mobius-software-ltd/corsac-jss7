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

package org.restcomm.protocols.ss7.mtp;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface Mtp3UserPart {

	void start() throws Exception;

	void stop() throws Exception;

	/**
	 * Add {@link Mtp3UserPartListener}
	 *
	 * @param listener
	 */
	void addMtp3UserPartListener(Mtp3UserPartListener listener);

	/**
	 * Remove {@link Mtp3UserPartListener}
	 *
	 * @param listener
	 */
	void removeMtp3UserPartListener(Mtp3UserPartListener listener);

	/**
	 * Get the Mtp3TransferPrimitiveFactory
	 *
	 * @return
	 */
	Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory();

	/**
	 * Return the maximum data field length of the MTP-TRANSFER message to the DPC
	 *
	 * @param dpc
	 * @return
	 */
	int getMaxUserDataLength(int dpc);

	/**
	 * If message delivering failed: MTP-PAUSE or MTR-STATUS indication will be sent
	 *
	 * @param msg
	 *
	 */
	void sendMessage(Mtp3TransferPrimitive msg, TaskCallback<Exception> callback);

	/**
	 * return PointCodeFormat
	 *
	 * @return
	 */
	RoutingLabelFormat getRoutingLabelFormat();

	/**
	 * Set PointCodeFormat
	 *
	 * @param length
	 */
	void setRoutingLabelFormat(RoutingLabelFormat routingLabelFormat) throws Exception;

	/**
	 * Returns true if lowest bit of SLS is used for loadbalancing between Linkset
	 * else returns false
	 *
	 * @return
	 */
	boolean isUseLsbForLinksetSelection();

	/**
	 * If set to true, lowest bit of SLS is used for loadbalancing between Linkset
	 * else highest bit of SLS is used.
	 *
	 * @param useLsbForLinksetSelection
	 */
	void setUseLsbForLinksetSelection(boolean useLsbForLinksetSelection) throws Exception;

	void setAffinity(boolean isEnabled, boolean isBySlsEnabled);
}
