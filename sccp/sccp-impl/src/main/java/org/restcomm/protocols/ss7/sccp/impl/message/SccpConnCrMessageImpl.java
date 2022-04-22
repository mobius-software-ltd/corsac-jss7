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

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.HopCounter;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.Parameter;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SccpConnCrMessageImpl extends SccpAddressedMessageImpl implements SccpConnCrMessage {
    protected LocalReference sourceLocalReferenceNumber;
    protected ProtocolClass protocolClass;
    protected Credit credit;
    protected ByteBuf userData;
    protected Importance importance;

    public SccpConnCrMessageImpl(int sls, int localSsn, SccpAddress calledParty, SccpAddress callingParty, HopCounter hopCounter) {
        super(130, MESSAGE_TYPE_CR, sls, localSsn, calledParty, callingParty, hopCounter);
    }

    public SccpConnCrMessageImpl(int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(130, MESSAGE_TYPE_CR, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    @Override
    public LocalReference getSourceLocalReferenceNumber() {
        return sourceLocalReferenceNumber;
    }

    @Override
    public void setSourceLocalReferenceNumber(LocalReference sourceLocalReferenceNumber) {
        this.sourceLocalReferenceNumber = sourceLocalReferenceNumber;
    }

    @Override
    public ProtocolClass getProtocolClass() {
        return protocolClass;
    }

    @Override
    public void setProtocolClass(ProtocolClass protocolClass) {
        this.protocolClass = protocolClass;
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
            sourceLocalReferenceNumber = ref;

            ProtocolClassImpl protocol = new ProtocolClassImpl();
            protocol.decode(buffer, factory, sccpProtocolVersion);
            protocolClass = protocol;

            int cpaPointer = buffer.readByte() & 0xFF;
            buffer.markReaderIndex();

            buffer.skipBytes(cpaPointer - 1);
            int len=buffer.readByte();
            calledParty = createAddress(buffer.slice(buffer.readerIndex(), len), factory, sccpProtocolVersion);

            buffer.resetReaderIndex();

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
                if (paramCode != Credit.PARAMETER_CODE
                        && paramCode != SccpAddress.CGA_PARAMETER_CODE
                        && paramCode != Parameter.DATA_PARAMETER_CODE
                        && paramCode != HopCounter.PARAMETER_CODE
                        && paramCode != Importance.PARAMETER_CODE) {
                    throw new ParseException(String.format("Code %d is not supported for CR message", paramCode));
                }
                
                len = buffer.readByte() & 0xff;
                decodeOptional(paramCode, buffer.slice(buffer.readerIndex(), len), sccpProtocolVersion, factory);
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
        if (sourceLocalReferenceNumber == null) {
            return new EncodingResultData(EncodingResult.SourceLocalReferenceNumberMissing, null, null, null);
        }
        if (protocolClass == null) {
            return new EncodingResultData(EncodingResult.ProtocolClassMissing, null, null, null);
        }
        if (calledParty == null) {
            return new EncodingResultData(EncodingResult.CalledPartyAddressMissing, null, null, null);
        }

        ByteBuf cdp=Unpooled.buffer();
        ((SccpAddressImpl) calledParty).encode(cdp, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        
        ByteBuf cnp = null;
        if (callingParty != null) {
        	cnp=Unpooled.buffer();
            ((SccpAddressImpl) callingParty).encode(cnp, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        }

        int cnpLength=0;
        if(cnp!=null)
        	cnpLength=cnp.readableBytes();
        
        int fieldsLen = MessageUtil.calculateCrFieldsLengthWithoutData(cdp.readableBytes(), credit != null, cnpLength,
                hopCounter != null, importance != null);
        int availLen = maxMtp3UserDataLength - fieldsLen;

        if (availLen > 130)
            availLen = 130;

        int bfLength=0;
        if(userData!=null)
        	bfLength=userData.readableBytes();
        
        if (bfLength > availLen) {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format(
                        "Failure when sending a CR message: message is too long. SccpMessageSegment=%s", this));
            }
            return new EncodingResultData(EncodingResult.SegmentationNotSupported, null, null, null);
        }

        ByteBuf out = Unpooled.buffer(fieldsLen);

        out.writeByte(type);
        ((LocalReferenceImpl) sourceLocalReferenceNumber).encode(out, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        ((ProtocolClassImpl) protocolClass).encode(out, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        
        // we have 2 pointers (cdp, optionals), cdp starts after 2 octets then
        int len = 2;
        out.writeByte(len); //cdp pointer

        boolean optionalPresent = false;
        if (credit != null || callingParty != null || userData != null || hopCounter != null || importance != null) {
            len += cdp.readableBytes();
            out.writeByte(len); // optionals pointer

            optionalPresent = true;
        } else {
            // in case there is no optional
            out.writeByte(0);
        }

        out.writeByte(cdp.readableBytes());
        out.writeBytes(cdp);

        if (credit != null) {
            out.writeByte(Credit.PARAMETER_CODE);
            out.writeByte(1);
            ((CreditImpl)credit).encode(out,removeSPC, sccpProtocolVersion);                
        }
        if (callingParty != null) {
            out.writeByte(SccpAddress.CGA_PARAMETER_CODE);
            out.writeByte(cnp.readableBytes());
            out.writeBytes(cnp);
        }
        if (userData != null) {
            out.writeByte(Parameter.DATA_PARAMETER_CODE);
            out.writeByte(userData.readableBytes());
            out=Unpooled.wrappedBuffer(out,userData);
        }
        
        if (hopCounter != null) {
            out.writeByte(HopCounter.PARAMETER_CODE);
            out.writeByte(1);
            ((HopCounterImpl)hopCounter).encode(out,removeSPC, sccpProtocolVersion);                
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

    protected void decodeOptional(int code, ByteBuf buffer, final SccpProtocolVersion sccpProtocolVersion, ParameterFactory factory) throws ParseException {
        switch (code) {
            case Credit.PARAMETER_CODE:
                CreditImpl cred = new CreditImpl();
                cred.decode(buffer, factory, sccpProtocolVersion);
                credit = cred;
                break;

            case SccpAddress.CGA_PARAMETER_CODE:
                SccpAddressImpl address = new SccpAddressImpl();
                address.decode(buffer, factory, sccpProtocolVersion);
                callingParty = address;
                break;

            case SccpAddress.CDA_PARAMETER_CODE:
                SccpAddressImpl address2 = new SccpAddressImpl();
                address2.decode(buffer, factory, sccpProtocolVersion);
                calledParty = address2;
                break;

            case Parameter.DATA_PARAMETER_CODE:
                userData = buffer;
                break;

            case HopCounter.PARAMETER_CODE:
                HopCounterImpl counter = new HopCounterImpl(buffer.readByte());
                hopCounter = counter;
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

    protected SccpAddress createAddress(ByteBuf buffer, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        SccpAddressImpl addressImpl = new SccpAddressImpl();
        addressImpl.decode(buffer, factory, sccpProtocolVersion);
        return addressImpl;
    }

    public boolean reduceHopCounter() {
        if (this.hopCounter != null) {
            int val = this.hopCounter.getValue();
            if (--val <= 0) {
                val = 0;
            }
            ((HopCounterImpl)this.hopCounter).setValue(val);
            if (val == 0)
                return false;
        }
        return true;
    }

    public boolean getSccpCreatesSls() {
        return false;
    }

    @Override
    public boolean getReturnMessageOnError() {
        throw new IllegalStateException();
    }

    @Override
    public void clearReturnMessageOnError() {
        throw new IllegalStateException();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Sccp Msg [Type=CR");
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
        sb.append(" CallingAddress(");
        sb.append(this.callingParty);
        sb.append(") CalledParty(");
        sb.append(this.calledParty);
        sb.append(")");
        sb.append(" DataLen=");
        if (this.userData != null)
            sb.append(this.userData.readableBytes());

        sb.append(" sourceLR=");
        if (this.sourceLocalReferenceNumber != null)
            sb.append(this.sourceLocalReferenceNumber.getValue());
        sb.append(" protocolClass=");
        if (this.protocolClass != null)
            sb.append(this.protocolClass.getProtocolClass());
        sb.append(" credit=");
        if (this.credit != null)
            sb.append(this.credit.getValue());
        sb.append(" importance=");
        if (this.importance != null)
            sb.append(this.importance.getValue());
        sb.append(" hopCounter=");
        if (this.hopCounter != null)
            sb.append(this.hopCounter.getValue());
        sb.append(" isMtpOriginated=");
        sb.append(this.isMtpOriginated);

        sb.append("]");

        return sb.toString();
    }
}
