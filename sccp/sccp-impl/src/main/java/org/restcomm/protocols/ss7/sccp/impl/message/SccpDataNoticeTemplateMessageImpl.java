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

import static org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil.calculateLudtFieldsLengthWithoutData;
import static org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil.calculateUdtFieldsLengthWithoutData;
import static org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil.calculateXudtFieldsLengthWithoutData;
import static org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil.calculateXudtFieldsLengthWithoutData2;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpMessage;
import org.restcomm.protocols.ss7.sccp.parameter.HopCounter;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.sccp.parameter.Segmentation;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Oleg Kulikov
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public abstract class SccpDataNoticeTemplateMessageImpl extends SccpSegmentableMessageImpl {

    protected ImportanceImpl importance;
    protected SccpDataNoticeTemplateMessageImpl(int maxDataLen,int type, int outgoingSls, int localSsn,
            SccpAddress calledParty, SccpAddress callingParty, ByteBuf data, HopCounter hopCounter, Importance importance) {
        super(maxDataLen,type, outgoingSls, localSsn, calledParty, callingParty, data, hopCounter);
        this.importance = (ImportanceImpl) importance;
    }

    protected SccpDataNoticeTemplateMessageImpl(int maxDataLen, int type, int incomingOpc, int incomingDpc,
            int incomingSls, int networkId) {
        super(maxDataLen,type, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance p) {
        importance = (ImportanceImpl) p;
    }

    protected abstract boolean getIsProtocolClass1();

    protected abstract boolean getSecondParamaterPresent();

    protected abstract void getSecondParamaterData(ByteBuf data,boolean removeSPC, SccpProtocolVersion sccpProtocolVersion) throws ParseException;

    protected abstract void setSecondParamaterData(ByteBuf data, SccpProtocolVersion sccpProtocolVersion) throws ParseException;

    @Override
    public void decode(ByteBuf buffer, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            switch (this.type) {
                case SccpMessage.MESSAGE_TYPE_UDT:
                case SccpMessage.MESSAGE_TYPE_UDTS: {
                    this.setSecondParamaterData(buffer, sccpProtocolVersion);

                    int cpaPointer = buffer.readByte() & 0xff;
                    buffer.markReaderIndex();
                    buffer.skipBytes(cpaPointer - 1);
                    int len=buffer.readByte() & 0xFF;
                    super.calledParty = createAddress(buffer.slice(buffer.readerIndex(), len),factory,sccpProtocolVersion);
                    
                    buffer.resetReaderIndex();
                    cpaPointer = buffer.readByte() & 0xff;
                    buffer.markReaderIndex();
                    buffer.skipBytes(cpaPointer - 1);
                    len=buffer.readByte() & 0xFF;
                    super.callingParty = createAddress(buffer.slice(buffer.readerIndex(), len),factory,sccpProtocolVersion);
                    buffer.resetReaderIndex();
                    
                    cpaPointer = buffer.readByte() & 0xff;
                    buffer.markReaderIndex();
                    buffer.skipBytes(cpaPointer - 1);
                    len=buffer.readByte() & 0xFF;
                    this.data=buffer.slice(buffer.readerIndex(), len);
                }
                break;

                case SccpMessage.MESSAGE_TYPE_XUDT:
                case SccpMessage.MESSAGE_TYPE_XUDTS: {
                    this.setSecondParamaterData(buffer, sccpProtocolVersion);

                    this.hopCounter = new HopCounterImpl((byte) buffer.readByte());
                    if (this.hopCounter.getValue() > HopCounter.COUNT_HIGH
                            || this.hopCounter.getValue() <= HopCounter.COUNT_LOW) {
                        throw new IOException("Hop Counter must be between 1 and 15, it is: " + this.hopCounter);
                    }

                    int pointer = buffer.readByte() & 0xff;
                    buffer.markReaderIndex();
                    try {
                    	buffer.skipBytes(pointer - 1);
                    }
                    catch(IndexOutOfBoundsException ex) {
                        throw new IOException("Not enough data in buffer");
                    }
                    
                    int len = buffer.readByte() & 0xff;
                    calledParty = createAddress(buffer.slice(buffer.readerIndex(),len),factory,sccpProtocolVersion);

                    buffer.resetReaderIndex();

                    pointer = buffer.readByte() & 0xff;
                    buffer.markReaderIndex();

                    try {
                    	buffer.skipBytes(pointer - 1);
                    }
                    catch(IndexOutOfBoundsException ex) {
                        throw new IOException("Not enough data in buffer");
                    }
                    
                    len = buffer.readByte() & 0xff;
                    callingParty = createAddress(buffer.slice(buffer.readerIndex(), len),factory,sccpProtocolVersion);

                    buffer.resetReaderIndex();

                    pointer = buffer.readByte() & 0xff;
                    buffer.markReaderIndex();
                    
                    try {
                    	buffer.skipBytes(pointer - 1);
                    }
                    catch(IndexOutOfBoundsException ex) {
                        throw new IOException("Not enough data in buffer");
                    }
                    
                    len = buffer.readByte() & 0xff;
                    data=buffer.slice(buffer.readerIndex(), len);
                    
                    buffer.resetReaderIndex();

                    pointer = buffer.readByte() & 0xff;
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
                        len = buffer.readByte() & 0xff;
                        this.decodeOptional(paramCode, buffer.slice(buffer.readerIndex(), len), sccpProtocolVersion);
                        buffer.skipBytes(len);
                    }
                }
                    break;

                case SccpMessage.MESSAGE_TYPE_LUDT:
                case SccpMessage.MESSAGE_TYPE_LUDTS: {
                    this.setSecondParamaterData(buffer, sccpProtocolVersion);

                    this.hopCounter = new HopCounterImpl((byte) buffer.readByte());
                    if (this.hopCounter.getValue() > HopCounter.COUNT_HIGH
                            || this.hopCounter.getValue() <= HopCounter.COUNT_LOW) {
                        throw new IOException("Hop Counter must be between 1 and 15, it is: " + this.hopCounter);
                    }

                    int pointer = (buffer.readByte() & 0xff) + ((buffer.readByte() & 0xff) << 8);
                    buffer.markReaderIndex();
                    
                    try {
                    	buffer.skipBytes(pointer - 1);
                    }
                    catch(IndexOutOfBoundsException ex) {
                        throw new IOException("Not enough data in buffer");
                    }
                    
                    int len = buffer.readByte() & 0xff;
                    calledParty = createAddress(buffer.slice(buffer.readerIndex(), len),factory,sccpProtocolVersion);

                    buffer.resetReaderIndex();
                    pointer = (buffer.readByte() & 0xff) + ((buffer.readByte() & 0xff) << 8);
                    buffer.markReaderIndex();

                    try {
                    	buffer.skipBytes(pointer - 1);
                    }
                    catch(IndexOutOfBoundsException ex) {
                        throw new IOException("Not enough data in buffer");
                    }
                    
                    len = buffer.readByte() & 0xff;
                    callingParty = createAddress(buffer.slice(buffer.readerIndex(), len),factory,sccpProtocolVersion);

                    buffer.resetReaderIndex();
                    pointer = (buffer.readByte() & 0xff) + ((buffer.readByte() & 0xff) << 8);
                    buffer.markReaderIndex();

                    try {
                    	buffer.skipBytes(pointer - 1);
                    }
                    catch(IndexOutOfBoundsException ex) {
                        throw new IOException("Not enough data in buffer");
                    }
                    
                    len = (buffer.readByte() & 0xff) + ((buffer.readByte() & 0xff) << 8);
                    data = buffer.slice(buffer.readerIndex(), len);
                    
                    buffer.resetReaderIndex();
                    pointer = (buffer.readByte() & 0xff) + ((buffer.readByte() & 0xff) << 8);
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
                        len = buffer.readByte() & 0xff;
                        this.decodeOptional(paramCode, buffer.slice(buffer.readerIndex(), len), sccpProtocolVersion);
                        buffer.skipBytes(len);
                    }
                }
                    break;
            }
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    private void decodeOptional(int code, ByteBuf buffer, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {

        switch (code) {
            case Segmentation.PARAMETER_CODE:
                this.segmentation = new SegmentationImpl();
                this.segmentation.decode(buffer, null, sccpProtocolVersion);
                break;
            case Importance.PARAMETER_CODE:
                this.importance = new ImportanceImpl();
                this.importance.decode(buffer, null, sccpProtocolVersion);
                break;

            default:
                throw new ParseException("Uknown optional parameter code: " + code);
        }
    }

    @Override
    public EncodingResultData encode(SccpStackImpl sccpStackImpl, LongMessageRuleType longMessageRuleType, int maxMtp3UserDataLength, Logger logger,
            boolean removeSPC, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	ByteBuf data=Unpooled.wrappedBuffer(this.getData());
        if (data == null || data.readableBytes() == 0)
            return new EncodingResultData(EncodingResult.DataMissed, null, null, null);
        if (data.readableBytes() > super.maxDataLen)
            return new EncodingResultData(EncodingResult.DataMaxLengthExceeded, null, null, null);

        if (calledParty == null)
            return new EncodingResultData(EncodingResult.CalledPartyAddressMissing, null, null, null);
        if (callingParty == null)
            return new EncodingResultData(EncodingResult.CallingPartyAddressMissing, null, null, null);
        if (!this.getSecondParamaterPresent())
            return new EncodingResultData(EncodingResult.ProtocolClassMissing, null, null, null);

        ByteBuf cdp=Unpooled.buffer();
        ByteBuf cnp=Unpooled.buffer();
        ((SccpAddressImpl) super.calledParty).encode(cdp, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        ((SccpAddressImpl) super.callingParty).encode(cnp, sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());

        if (longMessageRuleType == null)
            longMessageRuleType = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;
        if (this.isMtpOriginated && this.type == SccpMessage.MESSAGE_TYPE_UDT || this.type == SccpMessage.MESSAGE_TYPE_UDTS)
            // if we have received an UDT message from MTP3, leave UDT style
            // if this is UDTS message, leave this type
            longMessageRuleType = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;

        boolean isServiceMessage = true;
        if (this instanceof SccpDataMessageImpl)
            isServiceMessage = false;

        int fieldsLen = calculateUdtFieldsLengthWithoutData(cdp.readableBytes(), cnp.readableBytes());
        int availLen = maxMtp3UserDataLength - fieldsLen;
        if (availLen > 254)
            availLen = 254;
        if (sccpProtocolVersion == SccpProtocolVersion.ANSI && availLen > 252)
            availLen = 252;

        Boolean useShortMessage=false;
        if (longMessageRuleType == LongMessageRuleType.LONG_MESSAGE_FORBBIDEN)
            useShortMessage=true;
        else if(longMessageRuleType == LongMessageRuleType.XUDT_ENABLED && data.readableBytes() <= availLen)
            useShortMessage=true;

        if (useShortMessage) {
            // use UDT / UDTS
            if (data.readableBytes() > availLen) { // message is too long to encode UDT
                if (logger.isWarnEnabled()) {
                    logger.warn(String.format(
                            "Failure when sending a UDT message: message is too long. SccpMessageSegment=%s", this));
                }
                return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_NOT_SUPPORTED);
            }

            ByteBuf out = Unpooled.buffer(fieldsLen);

            if (isServiceMessage)
                this.type = SccpMessage.MESSAGE_TYPE_UDTS;
            else
                this.type = SccpMessage.MESSAGE_TYPE_UDT;
            
            out.writeByte(this.type);
            this.getSecondParamaterData(out,removeSPC, sccpProtocolVersion);

            int len = 3;
            out.writeByte(len);

            len = (cdp.readableBytes() + 3);
            out.writeByte(len);

            len += (cnp.readableBytes());
            out.writeByte(len);

            out.writeByte((byte) cdp.readableBytes());
            out.writeBytes(cdp);

            out.writeByte((byte) cnp.readableBytes());
            out.writeBytes(cnp);

            out.writeByte(data.readableBytes());
            out=Unpooled.wrappedBuffer(out,data);

            return new EncodingResultData(EncodingResult.Success, out, null, null);
        } else if (longMessageRuleType == LongMessageRuleType.XUDT_ENABLED) {

            // use XUDT / XUDTS
            if (isServiceMessage)
                this.type = SccpMessage.MESSAGE_TYPE_XUDTS;
            else
                this.type = SccpMessage.MESSAGE_TYPE_XUDT;
            if (this.hopCounter == null)
                this.hopCounter = new HopCounterImpl(15);

            int fieldsLenX = calculateXudtFieldsLengthWithoutData(cdp.readableBytes(), cnp.readableBytes(), false,
                    this.importance != null);
            int fieldsLen2 = calculateXudtFieldsLengthWithoutData2(cdp.readableBytes(), cnp.readableBytes());
            int availLenX = maxMtp3UserDataLength - fieldsLenX;
            if (availLenX > fieldsLen2)
                availLenX = fieldsLen2;
            int fieldsLenXSegm = calculateXudtFieldsLengthWithoutData(cdp.readableBytes(), cnp.readableBytes(), true,
                    this.importance != null);
            int availLenXSegm = maxMtp3UserDataLength - fieldsLenXSegm;
            if (availLenXSegm > fieldsLen2)
                availLenXSegm = fieldsLen2;

            if (data.readableBytes() <= availLenX && data.readableBytes() <= sccpStackImpl.getZMarginXudtMessage()) {
                // one segment
                ByteBuf out = Unpooled.buffer(fieldsLenX);

                out.writeByte(this.type);

                this.getSecondParamaterData(out, removeSPC, sccpProtocolVersion);
                out.writeByte(this.hopCounter.getValue());

                // we have 4 pointers, cdp,cnp,data and optionalm, cdp starts after 4 octests than
                int len = 4;
                out.writeByte(len);

                len += cdp.readableBytes();
                out.writeByte(len);

                len += cnp.readableBytes();
                out.writeByte(len);
                boolean optionalPresent = false;
                
                if (importance != null) {
                    len += (data.readableBytes());
                    out.writeByte(len);
                    optionalPresent = true;
                } else {
                    // in case there is no optional
                    out.writeByte(0);
                }

                out.writeByte((byte) cdp.readableBytes());
                out.writeBytes(cdp);

                out.writeByte((byte) cnp.readableBytes());
                out.writeBytes(cnp);

                out.writeByte(data.readableBytes());
                out=Unpooled.wrappedBuffer(out,data);

                if (importance != null) {
                    out.writeByte(Importance.PARAMETER_CODE);
                    out.writeByte(1);
                    importance.encode(out, removeSPC, sccpProtocolVersion);                        
                }

                if (optionalPresent)
                    out.writeByte(0x00);

                return new EncodingResultData(EncodingResult.Success, out, null, null);
            } else {
                // several segments
                if (data.readableBytes() > availLenXSegm * 16) {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String.format(
                                "Failure when segmenting a message XUDT: message is too long. SccpMessageSegment=%s", this));
                    }
                    return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_FAILURE);
                }
                int segmLen;
                if (data.readableBytes() <= sccpStackImpl.getZMarginXudtMessage() * 16)
                    segmLen = sccpStackImpl.getZMarginXudtMessage();
                else
                    segmLen = availLenXSegm;
                if (segmLen > availLenXSegm)
                    segmLen = availLenXSegm;
                int segmCount = (data.readableBytes() - 1) / segmLen + 1;

                if (this.isMtpOriginated) {
                    if (this.segmentation == null) {
                        // MTP3 originated message - we may make segmentation
                        // only if incoming message has a "Segmentation" field
                        if (logger.isWarnEnabled()) {
                            logger.warn(String
                                    .format("Failure when segmenting a message: message is not locally originated but \"segmentation\" field is absent. SccpMessageSegment=%s",
                                            this));
                        }
                        return new EncodingResultData(EncodingResult.ReturnFailure, null, null,
                                ReturnCauseValue.SEG_FAILURE);
                    }

                    this.segmentation = new SegmentationImpl(true, this.segmentation.isClass1Selected(), (byte) segmCount,
                            this.segmentation.getSegmentationLocalRef());
                } else {
                    this.segmentation = new SegmentationImpl(true, this.getIsProtocolClass1(), (byte) segmCount,
                            sccpStackImpl.newSegmentationLocalRef());
                }

                ArrayList<ByteBuf> res = new ArrayList<ByteBuf>();
                int totalBytes=data.readableBytes();
                for (int num = 0; num < segmCount; num++) {
                    int fst = num * segmLen;
                    int last = fst + segmLen;
                    if (last > totalBytes)
                        last = totalBytes;
                    int mLen = last - fst;

                    ByteBuf out = Unpooled.buffer(fieldsLenXSegm + mLen);

                    out.writeByte(this.type);

                    this.getSecondParamaterData(out,removeSPC, sccpProtocolVersion);
                    out.writeByte(this.hopCounter.getValue());

                    // we have 4 pointers, cdp,cnp,data and optionalm, cdp starts after 4 octests than
                    int len = 4;
                    out.writeByte(len);

                    len += cdp.readableBytes();
                    out.writeByte(len);

                    len += cnp.readableBytes();
                    out.writeByte(len);

                    len += (mLen);
                    out.writeByte(len);

                    out.writeByte((byte) cdp.readableBytes());
                    out.writeBytes(Unpooled.wrappedBuffer(cdp));

                    out.writeByte((byte) cnp.readableBytes());
                    out.writeBytes(Unpooled.wrappedBuffer(cnp));

                    out.writeByte((byte) mLen);
                    out=Unpooled.wrappedBuffer(out,data.slice(data.readerIndex(), mLen));
                    data.skipBytes(mLen);
                    data.retain();
                    
                    out.writeByte(Segmentation.PARAMETER_CODE);
                    
                    segmentation.setRemainingSegments((byte) (segmentation.getRemainingSegments() - 1));
                    
                    out.writeByte(4);
                    segmentation.encode(out, removeSPC, sccpProtocolVersion);                        
                    segmentation.setFirstSegIndication(false);

                    if (importance != null) {
                        out.writeByte(Importance.PARAMETER_CODE);
                        out.writeByte(1);
                        importance.encode(out, removeSPC, sccpProtocolVersion);                            
                    }

                    out.writeByte(0x00);

                    res.add(out);
                    
                }

                return new EncodingResultData(EncodingResult.Success, null, res, null);
            }
        } else {

            // use LUDT / LUDTS
            if (isServiceMessage)
                this.type = SccpMessage.MESSAGE_TYPE_LUDTS;
            else
                this.type = SccpMessage.MESSAGE_TYPE_LUDT;
            if (this.hopCounter == null)
                this.hopCounter = new HopCounterImpl(15);

            if (longMessageRuleType == LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION) {
                this.segmentation = new SegmentationImpl(true, this.getIsProtocolClass1(), (byte) 0,
                        sccpStackImpl.newSegmentationLocalRef());
            }
            int fieldsLenL = calculateLudtFieldsLengthWithoutData(cdp.readableBytes(), cnp.readableBytes(),
                    this.segmentation != null, this.importance != null);
            availLen = maxMtp3UserDataLength - fieldsLenL;
            if (data.readableBytes() > availLen) { // message is too long to encode LUDT
                if (logger.isWarnEnabled()) {
                    logger.warn(String.format(
                            "Failure when sending a LUDT message: message is too long. SccpMessageSegment=%s", this));
                }
                return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_FAILURE);
            }

            ByteBuf out = Unpooled.buffer(fieldsLenL);

            out.writeByte(this.type);

            this.getSecondParamaterData(out, removeSPC, sccpProtocolVersion);
            out.writeByte(this.hopCounter.getValue());

            // we have 4 pointers, cdp,cnp,data and optionalm, cdp starts after 8 octests than
            int len = 7;
            out.writeByte(len & 0xFF);
            out.writeByte((len >> 8) & 0xFF);

            len += cdp.readableBytes() - 1;
            out.writeByte(len & 0xFF);
            out.writeByte((len >> 8) & 0xFF);

            len += cnp.readableBytes() - 1;
            out.writeByte(len & 0xFF);
            out.writeByte((len >> 8) & 0xFF);
            boolean optionalPresent = false;
            if (importance != null || segmentation != null) {
                len += data.readableBytes();
                out.writeByte(len & 0xFF);
                out.writeByte((len >> 8) & 0xFF);
                optionalPresent = true;
            } else {
                // in case there is no optional
                out.writeByte(0);
                out.writeByte(0);
            }

            out.writeByte((byte) cdp.readableBytes());
            out.writeBytes(cdp);

            out.writeByte((byte) cnp.readableBytes());
            out.writeBytes(cnp);

            out.writeByte(data.readableBytes() & 0xFF);
            out.writeByte((data.readableBytes() >> 8) & 0xFF);
            out=Unpooled.wrappedBuffer(out,data);

            if (segmentation != null) {
                out.writeByte(Segmentation.PARAMETER_CODE);
                out.writeByte(4);
                segmentation.encode(out, removeSPC, sccpProtocolVersion);                    
            }
            if (importance != null) {
                out.writeByte(Importance.PARAMETER_CODE);
                out.writeByte(1);
                importance.encode(out,removeSPC, sccpProtocolVersion);                    
            }

            if (optionalPresent)
                out.writeByte(0x00);

            return new EncodingResultData(EncodingResult.Success, out, null, null);
        }
    }

    protected SccpAddress createAddress(ByteBuf buffer, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        SccpAddressImpl addressImpl = new SccpAddressImpl();
        addressImpl.decode(buffer, factory, sccpProtocolVersion);
        return addressImpl;
    }
}
