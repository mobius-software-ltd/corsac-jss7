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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.SccpStack;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.restcomm.protocols.ss7.sccp.message.MessageFactory;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.HopCounter;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import io.netty.buffer.ByteBuf;


/**
 *
 * @author kulikov
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MessageFactoryImpl implements MessageFactory {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(MessageFactoryImpl.class);

    private transient SccpStack sccpStack;

    public MessageFactoryImpl(SccpStackImpl sccpStack) {
        this.sccpStack = sccpStack;
    }

    public SccpDataMessage createDataMessageClass0(SccpAddress calledParty, SccpAddress callingParty, ByteBuf data,
            int localSsn, boolean returnMessageOnError, HopCounter hopCounter, Importance importance) {
        return new SccpDataMessageImpl( this.sccpStack.getMaxDataMessage(),new ProtocolClassImpl(0, returnMessageOnError),
        		sccpStack.newSls(), localSsn, calledParty, callingParty, data, hopCounter, importance);
    }

    public SccpDataMessage createDataMessageClass1(SccpAddress calledParty, SccpAddress callingParty, ByteBuf data, int sls,
            int localSsn, boolean returnMessageOnError, HopCounter hopCounter, Importance importance) {
        return new SccpDataMessageImpl( this.sccpStack.getMaxDataMessage(),new ProtocolClassImpl(1, returnMessageOnError), sls, localSsn,
                calledParty, callingParty, data, hopCounter, importance);
    }

    public SccpNoticeMessage createNoticeMessage(int origMsgType, ReturnCause returnCause, SccpAddress calledParty,
            SccpAddress callingParty, ByteBuf data, HopCounter hopCounter, Importance importance) {
        int type = SccpMessage.MESSAGE_TYPE_UNDEFINED;
        switch (origMsgType) {
            case SccpMessage.MESSAGE_TYPE_UDT:
                type = SccpMessage.MESSAGE_TYPE_UDTS;
                break;
            case SccpMessage.MESSAGE_TYPE_XUDT:
                type = SccpMessage.MESSAGE_TYPE_XUDTS;
                break;
            case SccpMessage.MESSAGE_TYPE_LUDT:
                type = SccpMessage.MESSAGE_TYPE_LUDTS;
                break;
        }

        return new SccpNoticeMessageImpl(this.sccpStack.getMaxDataMessage(), type, returnCause, calledParty, callingParty, data, hopCounter,
                importance);
    }

    public SccpConnCrMessage createConnectMessageClass2(int localSsn, SccpAddress calledAddress, SccpAddress callingAddress, ByteBuf data, Importance importance) {
        SccpConnCrMessageImpl message = new SccpConnCrMessageImpl(sccpStack.newSls(), localSsn, calledAddress, callingAddress, null);
        message.setCalledPartyAddress(calledAddress);
        message.setCallingPartyAddress(callingAddress);
        message.setProtocolClass(new ProtocolClassImpl(2));
        message.setUserData(data);
        message.setImportance(importance);
        return message;
    }

    public SccpConnCrMessage createConnectMessageClass3(int localSsn, SccpAddress calledAddress, SccpAddress callingAddress, Credit credit, ByteBuf data, Importance importance) {
        SccpConnCrMessageImpl message = new SccpConnCrMessageImpl(sccpStack.newSls(), localSsn, calledAddress, callingAddress, null);
        message.setCalledPartyAddress(calledAddress);
        message.setCallingPartyAddress(callingAddress);
        message.setProtocolClass(new ProtocolClassImpl(3));
        message.setCredit(credit);
        message.setUserData(data);
        message.setImportance(importance);
        return message;
    }

    public SccpMessageImpl createMessage(int type, int opc, int dpc, int sls, ByteBuf buffer, final SccpProtocolVersion sccpProtocolVersion, int networkId)
            throws ParseException {
        SccpMessageImpl msg = null;
        switch (type) {
            case SccpMessage.MESSAGE_TYPE_UDT:
            case SccpMessage.MESSAGE_TYPE_XUDT:
            case SccpMessage.MESSAGE_TYPE_LUDT:
                msg = new SccpDataMessageImpl(this.sccpStack.getMaxDataMessage(), type, opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_UDTS:
            case SccpMessage.MESSAGE_TYPE_XUDTS:
            case SccpMessage.MESSAGE_TYPE_LUDTS:
                msg = new SccpNoticeMessageImpl(this.sccpStack.getMaxDataMessage(), type, opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_CR:
                msg = new SccpConnCrMessageImpl(opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_CC:
                msg = new SccpConnCcMessageImpl(opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_CREF:
                msg = new SccpConnCrefMessageImpl(opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_RLSD:
                msg = new SccpConnRlsdMessageImpl(opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_RLC:
                msg = new SccpConnRlcMessageImpl(opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_DT1:
                msg = new SccpConnDt1MessageImpl(this.sccpStack.getMaxDataMessage(), opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_DT2:
                msg = new SccpConnDt2MessageImpl(this.sccpStack.getMaxDataMessage(), opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_AK:
                msg = new SccpConnAkMessageImpl(opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_RSR:
                msg = new SccpConnRsrMessageImpl(opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_RSC:
                msg = new SccpConnRscMessageImpl(opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_ERR:
                msg = new SccpConnErrMessageImpl(opc, dpc, sls, networkId);
                break;

            case SccpMessage.MESSAGE_TYPE_IT:
                msg = new SccpConnItMessageImpl(opc, dpc, sls, networkId);
                break;
        }

        if (msg != null) {
            msg.decode(buffer, sccpStack.getSccpProvider().getParameterFactory(), sccpProtocolVersion);
        } else if (logger.isWarnEnabled()) {
            logger.warn("No message implementation for MT: " + type);
        }
        return msg;
    }
}
