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
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReceiveSequenceNumberImpl;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpConnAkMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ReceiveSequenceNumber;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class SccpConnAkMessageImpl extends SccpConnReferencedMessageImpl implements SccpConnAkMessage {

    protected ReceiveSequenceNumber receiveSequenceNumber;
    protected Credit credit;

    public SccpConnAkMessageImpl(int sls, int localSsn) {
        super(0, MESSAGE_TYPE_AK, sls, localSsn);
    }

    protected SccpConnAkMessageImpl(int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(0, MESSAGE_TYPE_AK, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    @Override
    public ReceiveSequenceNumber getReceiveSequenceNumber() {
        return receiveSequenceNumber;
    }

    @Override
    public void setReceiveSequenceNumber(ReceiveSequenceNumber receiveSequenceNumber) {
        this.receiveSequenceNumber = receiveSequenceNumber;
    }

    @Override
    public Credit getCredit() {
        return credit;
    }

    @Override
    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    @Override
    public void decode(ByteBuf buf, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	LocalReferenceImpl ref = new LocalReferenceImpl();
        ref.decode(buf, factory, sccpProtocolVersion);
        destinationLocalReferenceNumber = ref;

        ReceiveSequenceNumberImpl sequenceNumber = new ReceiveSequenceNumberImpl();
        sequenceNumber.decode(buf, factory, sccpProtocolVersion);
        receiveSequenceNumber = sequenceNumber;

        CreditImpl cred = new CreditImpl();
        cred.decode(buf, factory, sccpProtocolVersion);
        credit = cred;
    }

    @Override
    public EncodingResultData encode(SccpStackImpl sccpStackImpl, LongMessageRuleType longMessageRuleType, int maxMtp3UserDataLength, Logger logger, boolean removeSPC, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	if (type == 0) {
            return new EncodingResultData(EncodingResult.MessageTypeMissing, null, null, null);
        }
        if (destinationLocalReferenceNumber == null) {
            return new EncodingResultData(EncodingResult.DestinationLocalReferenceNumberMissing, null, null, null);
        }
        if (receiveSequenceNumber == null) {
            return new EncodingResultData(EncodingResult.ReceiveSequenceNumberMissing, null, null, null);
        }
        if (credit == null) {
            return new EncodingResultData(EncodingResult.CreditMissing, null, null, null);
        }

        // 6 is sum of 4 fixed-length field lengths
        ByteBuf buffer = Unpooled.buffer(6);
        buffer.writeByte(type);
        ((LocalReferenceImpl) destinationLocalReferenceNumber).encode(buffer, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        ((ReceiveSequenceNumberImpl) receiveSequenceNumber).encode(buffer, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        ((CreditImpl) credit).encode(buffer, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        return new EncodingResultData(EncodingResult.Success, buffer, null, null);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Sccp Msg [Type=AK");
        sb.append(" networkId=").append(this.networkId).append(" sls=").append(this.sls).append(" incomingOpc=").append(this.incomingOpc)
                .append(" incomingDpc=").append(this.incomingDpc).append(" outgoingDpc=").append(this.outgoingDpc)

                .append(" destinationLocalReferenceNumber=").append(this.destinationLocalReferenceNumber)
                .append(" pr=").append(this.receiveSequenceNumber)
                .append(" credit=").append(this.credit);

        sb.append("]");
        return sb.toString();
    }
}
