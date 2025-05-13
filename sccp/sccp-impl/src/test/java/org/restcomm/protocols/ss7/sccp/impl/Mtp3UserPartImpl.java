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

package org.restcomm.protocols.ss7.sccp.impl;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusCause;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartBaseImpl;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.common.dal.timers.WorkerPool;

import io.netty.buffer.ByteBuf;

/**
 * @author abhayani
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public class Mtp3UserPartImpl extends Mtp3UserPartBaseImpl {

	// protected ConcurrentLinkedQueue<byte[]> readFrom;
	// protected ConcurrentLinkedQueue<byte[]> writeTo;

	private List<Mtp3UserPartImpl> otherParts = new ArrayList<Mtp3UserPartImpl>();
	private ArrayList<Mtp3TransferPrimitive> messages = new ArrayList<Mtp3TransferPrimitive>();
	private List<Integer> dpcs = new ArrayList<Integer>();

	SccpHarness sccpHarness;

	public Mtp3UserPartImpl(SccpHarness sccpHarness, WorkerPool workerPool) {
		super(null, workerPool);
		this.sccpHarness = sccpHarness;
		try {
			this.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setOtherPart(Mtp3UserPartImpl otherPart) {
		this.otherParts.add(otherPart);
	}

	@Override
	public void sendMessage(Mtp3TransferPrimitive msg, TaskCallback<Exception> callback) {
		// we need to work with copy otherwise the buffer would be released
		Mtp3TransferPrimitive copy = new Mtp3TransferPrimitive(msg.getSi(), msg.getNi(), msg.getMp(), msg.getOpc(),
				msg.getDpc(), msg.getSls(), msg.getData().copy(), msg.getPointCodeFormat());
		if (!this.otherParts.isEmpty()) {			
			if (otherParts.size() == 1)
				this.otherParts.iterator().next().sendTransferMessageToLocalUser(copy, copy.getSls(), callback);
			else
				for (Mtp3UserPartImpl part : otherParts)
					if (part.dpcs.contains(msg.getDpc()))
						part.sendTransferMessageToLocalUser(copy, copy.getSls(), callback);
		} else {
			this.messages.add(copy); 
			callback.onSuccess();
		}
	}

	public void sendTransferMessageToLocalUser(int opc, int dpc, ByteBuf data, TaskCallback<Exception> callback) {
		int si = Mtp3UserPartBaseImpl._SI_SERVICE_SCCP;
		int ni = 2;
		int mp = 0;
		int sls = 0;
		Mtp3TransferPrimitiveFactory factory = this.getMtp3TransferPrimitiveFactory();
		Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(si, ni, mp, opc, dpc, sls, data);
		int seqControl = 0;
		this.sendTransferMessageToLocalUser(msg, seqControl, callback);
	}

	public void sendPauseMessageToLocalUser(int affectedDpc, TaskCallback<Exception> callback) {
		Mtp3PausePrimitive msg = new Mtp3PausePrimitive(affectedDpc);
		this.sendPauseMessageToLocalUser(msg, callback);
	}

	public void sendResumeMessageToLocalUser(int affectedDpc, TaskCallback<Exception> callback) {
		Mtp3ResumePrimitive msg = new Mtp3ResumePrimitive(affectedDpc);
		this.sendResumeMessageToLocalUser(msg, callback);
	}

	public void sendStatusMessageToLocalUser(int affectedDpc, Mtp3StatusCause cause, int congestionLevel,
			int userPartIdentity, TaskCallback<Exception> callback) {
		Mtp3StatusPrimitive msg = new Mtp3StatusPrimitive(affectedDpc, cause, congestionLevel, userPartIdentity);
		this.sendStatusMessageToLocalUser(msg, callback);
	}

	public void sendEndCongestionMessageToLocalUser(int affectedDpc, TaskCallback<Exception> callback) {
		Mtp3EndCongestionPrimitive msg = new Mtp3EndCongestionPrimitive(affectedDpc);
		this.sendEndCongestionMessageToLocalUser(msg, callback);
	}

	public List<Mtp3TransferPrimitive> getMessages() {
		return messages;
	}

	@Override
	public int getMaxUserDataLength(int dpc) {
		return 1000;
	}

	public void addDpc(int dpc) {
		if (!dpcs.contains(dpc))
			dpcs.add(dpc);
	}
}
