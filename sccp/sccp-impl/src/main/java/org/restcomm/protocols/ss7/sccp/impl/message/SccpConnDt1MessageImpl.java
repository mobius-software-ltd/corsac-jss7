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
import org.restcomm.protocols.ss7.sccp.impl.parameter.SegmentingReassemblingImpl;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpConnDt1Message;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SegmentingReassembling;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class SccpConnDt1MessageImpl extends SccpConnSegmentableMessageImpl implements SccpConnDt1Message {
    protected SegmentingReassembling segmentingReassembling;

    public SccpConnDt1MessageImpl(int maxDataLen, int sls, int localSsn) {
        super(maxDataLen, MESSAGE_TYPE_DT1, sls, localSsn);
    }

    protected SccpConnDt1MessageImpl(int maxDataLen, int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(maxDataLen, MESSAGE_TYPE_DT1, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    @Override
    public SegmentingReassembling getSegmentingReassembling() {
        return segmentingReassembling;
    }

    @Override
    public void setSegmentingReassembling(SegmentingReassembling segmentingReassembling) {
        this.segmentingReassembling = segmentingReassembling;
    }

    @Override
    public void decode(ByteBuf buffer, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	LocalReferenceImpl ref = new LocalReferenceImpl();
        ref.decode(buffer, factory, sccpProtocolVersion);
        destinationLocalReferenceNumber = ref;

        SegmentingReassemblingImpl segmenting = new SegmentingReassemblingImpl();
        segmenting.decode(buffer, factory, sccpProtocolVersion);
        segmentingReassembling = segmenting;

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
        if (segmentingReassembling == null) {
            return new EncodingResultData(EncodingResult.SegmentingReassemblingMissing, null, null, null);
        }
        if (userData == null) {
            return new EncodingResultData(EncodingResult.DataMissed, null, null, null);
        }

        ByteBuf bf = userData;
        if(userData!=null)
        	bf=Unpooled.wrappedBuffer(bf);
        
        // 7 = 5 (fixed fields length) + 1 (variable fields pointers) + 1
        // (variable fields lengths)
        int fieldsLen = 7;
        int availLen = maxMtp3UserDataLength - fieldsLen;

        if (availLen > 256)
            availLen = 256;

        int bfBytes=0;
        if(bf!=null)
        	bfBytes=bf.readableBytes();
        
        if (bfBytes > availLen) { // message is too long
            if (logger.isWarnEnabled()) {
                logger.warn(String.format(
                        "Failure when sending a DT1 message: message is too long. SccpMessageSegment=%s", this));
            }
            return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_NOT_SUPPORTED);
        }

        ByteBuf output = Unpooled.buffer(fieldsLen+2);
        output.writeByte(type);
        ((LocalReferenceImpl) destinationLocalReferenceNumber).encode(output,sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        ((SegmentingReassemblingImpl) segmentingReassembling).encode(output,sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        
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
        return segmentingReassembling.isMoreData();
    }

    @Override
    public void setMoreData(boolean moreData) {
        segmentingReassembling = new SegmentingReassemblingImpl(moreData);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Sccp Msg [Type=DT1");
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
        sb.append(" moreData=");
        if (this.segmentingReassembling != null)
            sb.append(this.segmentingReassembling.isMoreData());
        sb.append(" isMtpOriginated=");
        sb.append(this.isMtpOriginated);

        sb.append("]");

        return sb.toString();
    }
}
