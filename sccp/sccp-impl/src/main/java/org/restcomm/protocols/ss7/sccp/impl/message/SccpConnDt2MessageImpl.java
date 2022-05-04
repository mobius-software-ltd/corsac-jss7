/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.restcomm.protocols.ss7.sccp.impl.message;

import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SequencingSegmentingImpl;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpConnDt2Message;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SequenceNumber;
import org.restcomm.protocols.ss7.sccp.parameter.SequencingSegmenting;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class SccpConnDt2MessageImpl extends SccpConnSegmentableMessageImpl implements SccpConnDt2Message {
    protected SequencingSegmenting sequencingSegmenting;

    public SccpConnDt2MessageImpl(int maxDataLen, int sls, int localSsn) {
        super(maxDataLen, MESSAGE_TYPE_DT2, sls, localSsn);
    }

    protected SccpConnDt2MessageImpl(int maxDataLen, int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(maxDataLen, MESSAGE_TYPE_DT2, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    @Override
    public SequencingSegmenting getSequencingSegmenting() {
        return sequencingSegmenting;
    }

    @Override
    public void setSequencingSegmenting(SequencingSegmenting sequencingSegmenting) {
        this.sequencingSegmenting = sequencingSegmenting;
    }

    @Override
    public void decode(ByteBuf buffer, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	LocalReferenceImpl ref = new LocalReferenceImpl();
        ref.decode(buffer, factory, sccpProtocolVersion);
        destinationLocalReferenceNumber = ref;

        SequencingSegmentingImpl sequencing = new SequencingSegmentingImpl();
        sequencing.decode(buffer, factory, sccpProtocolVersion);
        sequencingSegmenting = sequencing;

        int dataPointer = buffer.readByte() & 0xFF;
        buffer.markReaderIndex();
        buffer.skipBytes(dataPointer - 1);
        int len = buffer.readByte() & 0xff;
        userData = buffer.slice(buffer.readerIndex(), len);
    }

    @Override
    public EncodingResultData encode(SccpStackImpl sccpStackImpl, LongMessageRuleType longMessageRuleType, int maxMtp3UserDataLength, Logger logger, boolean removeSPC, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	if (type == 0) {
            return new EncodingResultData(EncodingResult.MessageTypeMissing, null, null, null);
        }
        if (destinationLocalReferenceNumber == null) {
            return new EncodingResultData(EncodingResult.DestinationLocalReferenceNumberMissing, null, null, null);
        }
        if (sequencingSegmenting == null) {
            return new EncodingResultData(EncodingResult.SequencingSegmentingMissing, null, null, null);
        }
        if (userData == null) {
            return new EncodingResultData(EncodingResult.DataMissed, null, null, null);
        }

        ByteBuf bf = userData;
        if(userData!=null)
        	bf=Unpooled.wrappedBuffer(bf);
        
        // 8 = 6 (fixed fields length) + 1 (variable fields pointers) + 1
        // (variable fields lengths)
        int fieldsLen = 8;
        int availLen = maxMtp3UserDataLength - fieldsLen;

        if (availLen > 256)
            availLen = 256;

        int bfBytes=0;
        if(bf!=null)
        	bfBytes=bf.readableBytes();
        
        if (bfBytes > availLen) { // message is too long
            if (logger.isWarnEnabled()) {
                logger.warn(String.format(
                        "Failure when sending a DT2 message: message is too long. SccpMessageSegment=%s", this));
            }
            return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_NOT_SUPPORTED);
        }

        ByteBuf output = Unpooled.buffer(fieldsLen+2);
        output.writeByte(type);
        ((LocalReferenceImpl) destinationLocalReferenceNumber).encode(output,sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        ((SequencingSegmentingImpl) sequencingSegmenting).encode(output,sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());

        // we have 1 pointers (data), cdp starts after 1 octets then
        int len = 1;
        output.writeByte(len);
        output.writeByte((byte) bfBytes);
        if(bf!=null)
        	output=Unpooled.wrappedBuffer(output,bf);

        return new EncodingResultData(EncodingResult.Success, output, null, null);
    }

    @Override
    public boolean isMoreData() {
        return sequencingSegmenting.isMoreData();
    }

    @Override
    public void setMoreData(boolean moreData) {
        if (sequencingSegmenting == null) {
            // sendSequenceNumber and receiveSequenceNumber are later re-initialized in MessageSender
            sequencingSegmenting = new SequencingSegmentingImpl();
        }
        sequencingSegmenting.setMoreData(moreData);
    }

    public void setSequencing(SequenceNumber sendSequenceNumber, SequenceNumber receiveSequenceNumber) {
        if (sequencingSegmenting == null) {
            // sendSequenceNumber and receiveSequenceNumber are later re-initialized in MessageSender
            sequencingSegmenting = new SequencingSegmentingImpl();
        }
        sequencingSegmenting.setSendSequenceNumber(sendSequenceNumber);
        sequencingSegmenting.setReceiveSequenceNumber(receiveSequenceNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Sccp Msg [Type=DT2");
        sb.append(" networkId=");
        sb.append(this.networkId);
        sb.append(" sls=");
        sb.append(this.sls);
        sb.append(" incomingOpc=");
        sb.append(this.incomingOpc);
        sb.append(" incomingDpc=");
        sb.append(this.incomingDpc);
        sb.append(" outgoingDpc=");
        sb.append(this.outgoingDpc);
        sb.append(" DataLen=");
        if (this.userData != null)
            sb.append(this.userData.readableBytes());

        sb.append(" destLR=");
        if (this.destinationLocalReferenceNumber != null)
            sb.append(this.destinationLocalReferenceNumber.getValue());
        sb.append(" sequencingSegmenting=");
        if (this.sequencingSegmenting != null)
            sb.append(this.sequencingSegmenting);
        sb.append(" isMtpOriginated=");
        sb.append(this.isMtpOriginated);

        sb.append("]");

        return sb.toString();
    }
}
