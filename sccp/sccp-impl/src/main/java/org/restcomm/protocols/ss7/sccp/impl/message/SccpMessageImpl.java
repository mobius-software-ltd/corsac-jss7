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

package org.restcomm.protocols.ss7.sccp.impl.message;

import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpMessage;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author kulikov
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public abstract class SccpMessageImpl implements SccpMessage {
	public static String MESSAGE_NAME_OTHER = "OTHER";
	public static String MESSAGE_NAME_CR = "CR";
	public static String MESSAGE_NAME_CC = "CC";
	public static String MESSAGE_NAME_CREF = "CREF";
	public static String MESSAGE_NAME_RLSD = "RLSD";
	public static String MESSAGE_NAME_RLC = "RLC";
	public static String MESSAGE_NAME_DT1 = "DT1";
	public static String MESSAGE_NAME_DT2 = "DT2";
	public static String MESSAGE_NAME_AK = "AK";
	public static String MESSAGE_NAME_RSR = "RSR";
	public static String MESSAGE_NAME_RSC = "RSC";
	public static String MESSAGE_NAME_ERR = "ERR";
	public static String MESSAGE_NAME_IT = "IT";
	public static String MESSAGE_NAME_UDT = "UDT";
	public static String MESSAGE_NAME_UDTS = "UDTS";
	public static String MESSAGE_NAME_XUDT = "XUDT";
	public static String MESSAGE_NAME_XUDTS = "XUDTS";
	public static String MESSAGE_NAME_LUDT = "LUDT";
	public static String MESSAGE_NAME_LUDTS = "LUTDS";

	protected boolean isMtpOriginated;
	protected boolean isIncoming;
	protected int type;
	protected int localOriginSsn = -1;
	protected final int maxDataLen;

	// These are MTP3 signaling information set when message is received from MTP3
	protected int incomingOpc;
	protected int incomingDpc;
	protected int sls;
	// These are MTP3 signaling information that will be set into a MTP3 message
	// when sending to MTP3
	protected int outgoingDpc = -1;
	protected int networkId;

	protected SccpMessageImpl(int maxDataLen, int type, int sls, int localSsn) {
		this.isMtpOriginated = false;
		this.type = type;
		this.localOriginSsn = localSsn;
		this.incomingOpc = -1;
		this.incomingDpc = -1;
		this.sls = sls;
		this.maxDataLen = maxDataLen;
	}

	protected SccpMessageImpl(int maxDataLen, int type, int incomingOpc, int incomingDpc, int incomingSls,
			int networkId) {
		this.isMtpOriginated = true;
		this.isIncoming = true;
		this.type = type;
		this.incomingOpc = incomingOpc;
		this.incomingDpc = incomingDpc;
		this.sls = incomingSls;
		this.maxDataLen = maxDataLen;
		this.networkId = networkId;
	}

	@Override
	public int getSls() {
		return sls;
	}

	public void setSls(int sls) {
		this.sls = sls;
	}

	@Override
	public int getIncomingOpc() {
		return incomingOpc;
	}

	@Override
	public void setIncomingOpc(int opc) {
		this.incomingOpc = opc;
	}

	@Override
	public int getIncomingDpc() {
		return incomingDpc;
	}

	public int getOutgoingDpc() {
		return outgoingDpc;
	}

	public void setIncomingDpc(int dpc) {
		this.incomingDpc = dpc;
	}

	@Override
	public void setOutgoingDpc(int dpc) {
		this.outgoingDpc = dpc;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public boolean getIsMtpOriginated() {
		return isMtpOriginated;
	}

	@Override
	public boolean getIsIncoming() {
		return isIncoming;
	}

	public void setIsIncoming(boolean incoming) {
		isIncoming = incoming;
	}

	@Override
	public int getOriginLocalSsn() {
		return localOriginSsn;
	}

	@Override
	public int getNetworkId() {
		return networkId;
	}

	@Override
	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}

	public abstract void decode(ByteBuf in, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion)
			throws ParseException;

	public abstract EncodingResultData encode(SccpStackImpl sccpStackImpl, LongMessageRuleType longMessageRuleType,
			int maxMtp3UserDataLength, Logger logger, boolean removeSPC, SccpProtocolVersion sccpProtocolVersion)
			throws ParseException;

	public static String getName(int type) {
		switch (type) {
		case MESSAGE_TYPE_CR:
			return MESSAGE_NAME_CR;
		case MESSAGE_TYPE_CC:
			return MESSAGE_NAME_CC;
		case MESSAGE_TYPE_CREF:
			return MESSAGE_NAME_CREF;
		case MESSAGE_TYPE_RLSD:
			return MESSAGE_NAME_RLSD;
		case MESSAGE_TYPE_RLC:
			return MESSAGE_NAME_RLC;
		case MESSAGE_TYPE_DT1:
			return MESSAGE_NAME_DT1;
		case MESSAGE_TYPE_DT2:
			return MESSAGE_NAME_DT2;
		case MESSAGE_TYPE_AK:
			return MESSAGE_NAME_AK;
		case MESSAGE_TYPE_RSR:
			return MESSAGE_NAME_RSR;
		case MESSAGE_TYPE_RSC:
			return MESSAGE_NAME_RSC;
		case MESSAGE_TYPE_ERR:
			return MESSAGE_NAME_ERR;
		case MESSAGE_TYPE_IT:
			return MESSAGE_NAME_IT;
		case MESSAGE_TYPE_UDT:
			return MESSAGE_NAME_UDT;
		case MESSAGE_TYPE_UDTS:
			return MESSAGE_NAME_UDTS;
		case MESSAGE_TYPE_XUDT:
			return MESSAGE_NAME_XUDT;
		case MESSAGE_TYPE_XUDTS:
			return MESSAGE_NAME_XUDTS;
		case MESSAGE_TYPE_LUDT:
			return MESSAGE_NAME_LUDT;
		case MESSAGE_TYPE_LUDTS:
			return MESSAGE_NAME_LUDTS;
		}

		return MESSAGE_NAME_OTHER;
	}

	public void retain() {
	}

	public void release() {
	}
}