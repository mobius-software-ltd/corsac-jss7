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

package org.restcomm.protocols.ss7.sccp.impl.parameter;

import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SequenceNumber;
import org.restcomm.protocols.ss7.sccp.parameter.SequencingSegmenting;

import io.netty.buffer.ByteBuf;
/**
 * 
 * @author yulianoifa
 *
 */
public class SequencingSegmentingImpl extends AbstractParameter implements SequencingSegmenting {
	private static final long serialVersionUID = 1L;

	private SequenceNumber sendSequenceNumber = new SequenceNumberImpl(0);
    private SequenceNumber receiveSequenceNumber = new SequenceNumberImpl(0);
    private boolean moreData;

    public SequencingSegmentingImpl() {
    }

    public SequencingSegmentingImpl(SequenceNumber sendSequenceNumber, SequenceNumber receiveSequenceNumber, boolean moreData) {
        this.sendSequenceNumber = sendSequenceNumber;
        this.receiveSequenceNumber = receiveSequenceNumber;
        this.moreData = moreData;
    }

    @Override
    public SequenceNumber getSendSequenceNumber() {
        return sendSequenceNumber;
    }

    @Override
    public void setSendSequenceNumber(SequenceNumber sendSequenceNumber) {
        this.sendSequenceNumber = sendSequenceNumber;
    }

    @Override
    public SequenceNumber getReceiveSequenceNumber() {
        return receiveSequenceNumber;
    }

    @Override
    public void setReceiveSequenceNumber(SequenceNumber receiveSequenceNumber) {
        this.receiveSequenceNumber = receiveSequenceNumber;
    }

    @Override
    public boolean isMoreData() {
        return moreData;
    }

    @Override
    public void setMoreData(boolean moreData) {
        this.moreData = moreData;
    }
    
    @Override
    public void decode(ByteBuf buffer, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        if (buffer.readableBytes() < 2) {
            throw new ParseException();
        }
        this.sendSequenceNumber = new SequenceNumberImpl((byte)(buffer.readByte() >> 1 & 0x7F));
        
        byte b=buffer.readByte();
        this.receiveSequenceNumber = new SequenceNumberImpl((byte)(b >> 1 & 0x7F));
        this.moreData = (b & 0x01) == 1;
    }

    @Override
    public void encode(ByteBuf buffer, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	buffer.writeByte((byte) (this.sendSequenceNumber.getValue() << 1 & 0xFE));
    	buffer.writeByte((byte) (this.receiveSequenceNumber.getValue() << 1 & 0xFE | ((moreData) ? 1 : 0)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SequencingSegmentingImpl that = (SequencingSegmentingImpl) o;

        if (sendSequenceNumber != that.sendSequenceNumber) return false;
        if (receiveSequenceNumber != that.receiveSequenceNumber) return false;
        return moreData == that.moreData;

    }

    @Override
    public int hashCode() {
        int result = sendSequenceNumber.getValue();
        result = 31 * result + receiveSequenceNumber.getValue();
        result = 31 * result + (moreData ? 1 : 0);
        return result;
    }

    public String toString() {
        return new StringBuffer().append("SequencingSegmenting [").append("ps=").append(sendSequenceNumber.getValue()).append(",pr=")
                .append(receiveSequenceNumber.getValue()).append(",moreData=").append(moreData).append("]").toString();
    }
}
