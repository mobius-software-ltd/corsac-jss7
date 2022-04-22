/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

import static org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil.calculateRlsdFieldsLengthWithoutData;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpConnRlsdMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.Parameter;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SccpConnRlsdMessageImpl extends SccpConnReferencedMessageImpl implements SccpConnRlsdMessage {
    protected ReleaseCause releaseCause;
    protected ByteBuf userData;
    protected Importance importance;

    public SccpConnRlsdMessageImpl(int sls, int localSsn) {
        super(130, MESSAGE_TYPE_RLSD, sls, localSsn);
    }

    protected SccpConnRlsdMessageImpl(int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(130, MESSAGE_TYPE_RLSD, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    @Override
    public ReleaseCause getReleaseCause() {
        return releaseCause;
    }

    @Override
    public void setReleaseCause(ReleaseCause releaseCause) {
        this.releaseCause = releaseCause;
    }

    @Override
    public ByteBuf getUserData() {
        return Unpooled.wrappedBuffer(userData);
    }

    @Override
    public void setUserData(ByteBuf userData) {
        this.userData = userData;
    }

    @Override
    public Importance getImportance() {
        return importance;
    }

    @Override
    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    @Override
    public void decode(ByteBuf buffer, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            LocalReferenceImpl ref = new LocalReferenceImpl();
            ref.decode(buffer, factory, sccpProtocolVersion);
            destinationLocalReferenceNumber = ref;

            ref = new LocalReferenceImpl();
            ref.decode(buffer, factory, sccpProtocolVersion);
            sourceLocalReferenceNumber = ref;

            ReleaseCauseImpl cause = new ReleaseCauseImpl();
            cause.decode(buffer, factory, sccpProtocolVersion);
            releaseCause = cause;

            int pointer = buffer.readByte() & 0xff;
            buffer.markReaderIndex();

            if (pointer == 0) {
                // we are done
                return;
            }
            
            try {
            	buffer.skipBytes(pointer - 1);
            }
            catch(IndexOutOfBoundsException ex) {
                throw new IOException("Not enough data in buffer");
            }

            int paramCode = 0;
            // EOP
            while ((paramCode = buffer.readByte() & 0xFF) != 0) {
                if (paramCode != Parameter.DATA_PARAMETER_CODE
                        && paramCode != Importance.PARAMETER_CODE) {
                    throw new ParseException(String.format("Code %d is not supported for RLSD message", paramCode));
                }
                
                int len=buffer.readByte();
                decodeOptional(paramCode, buffer.slice(buffer.readerIndex(), len), sccpProtocolVersion);
	            buffer.skipBytes(len);
            }
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public EncodingResultData encode(SccpStackImpl sccpStackImpl, LongMessageRuleType longMessageRuleType, int maxMtp3UserDataLength, Logger logger, boolean removeSPC, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	if (type == 0) {
            return new EncodingResultData(EncodingResult.MessageTypeMissing, null, null, null);
        }
        if (destinationLocalReferenceNumber == null) {
            return new EncodingResultData(EncodingResult.DestinationLocalReferenceNumberMissing, null, null, null);
        }
        if (sourceLocalReferenceNumber == null) {
            return new EncodingResultData(EncodingResult.SourceLocalReferenceNumberMissing, null, null, null);
        }
        if (releaseCause == null) {
            return new EncodingResultData(EncodingResult.ReleaseCauseMissing, null, null, null);
        }

        int fieldsLen = calculateRlsdFieldsLengthWithoutData(userData != null, importance != null);
        int availLen = maxMtp3UserDataLength - fieldsLen;

        if (availLen > 130)
            availLen = 130;

        int bfLength=0;
        if(userData!=null)
        	bfLength=userData.readableBytes();
        
        if (bfLength > availLen) { // message is too long
            if (logger.isWarnEnabled()) {
                logger.warn(String.format(
                        "Failure when sending a RLSD message: message is too long. SccpMessageSegment=%s", this));
            }
            return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_NOT_SUPPORTED);
        }

        ByteBuf out = Unpooled.buffer(fieldsLen);

        out.writeByte(type);
        ((LocalReferenceImpl) destinationLocalReferenceNumber).encode(out, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        ((LocalReferenceImpl) sourceLocalReferenceNumber).encode(out, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        ((ReleaseCauseImpl)releaseCause).encode(out, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());

        // we have 1 pointers (optionals), cdp starts after 1 octets then
        int len = 1;

        boolean optionalPresent = false;
        if (userData != null || importance != null) {
            out.writeByte(len); // optionals pointer

            optionalPresent = true;
        } else {
            // in case there is no optional
            out.writeByte(0);
        }

        if (userData != null) {
            out.writeByte(Parameter.DATA_PARAMETER_CODE);
            out.writeByte(userData.readableBytes());
            out=Unpooled.wrappedBuffer(out,userData);
        }
        if (importance != null) {
            out.writeByte(Importance.PARAMETER_CODE);
            out.writeByte(1);
            ((ImportanceImpl)importance).encode(out, removeSPC, sccpProtocolVersion);                
        }

        if (optionalPresent) {
            out.writeByte(0x00);
        }

        return new EncodingResultData(EncodingResult.Success, out, null, null);
    }

    protected void decodeOptional(int code, ByteBuf buffer, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        switch (code) {
            case Parameter.DATA_PARAMETER_CODE:
                userData = Unpooled.wrappedBuffer(buffer);
                break;

            case Importance.PARAMETER_CODE:
                ImportanceImpl imp = new ImportanceImpl();
                imp.decode(buffer, null, sccpProtocolVersion);
                importance = imp;
                break;

            default:
                throw new ParseException("Unknown optional parameter code: " + code);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Sccp Msg [Type=Rlsd");
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

        sb.append(" sourceLR=");
        if (this.sourceLocalReferenceNumber != null)
            sb.append(this.sourceLocalReferenceNumber.getValue());
        sb.append(" destLR=");
        if (this.destinationLocalReferenceNumber != null)
            sb.append(this.destinationLocalReferenceNumber.getValue());

        sb.append(" releaseCause=");
        if (this.releaseCause != null)
            sb.append(this.releaseCause.getValue());
        sb.append(" importance=");
        if (this.importance != null)
            sb.append(this.importance.getValue());

        sb.append(" isMtpOriginated=");
        sb.append(this.isMtpOriginated);

        sb.append("]");

        return sb.toString();
    }
}
