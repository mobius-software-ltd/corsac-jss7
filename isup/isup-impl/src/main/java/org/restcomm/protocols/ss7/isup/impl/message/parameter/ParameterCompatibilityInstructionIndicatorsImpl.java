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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.ParameterCompatibilityInstructionIndicators;

/**
 * Start time:12:42:55 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class ParameterCompatibilityInstructionIndicatorsImpl implements ParameterCompatibilityInstructionIndicators, Encodable {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private byte parameterCode;
    // FIXME: decide how to use this.
    private boolean transitAtIntermediateExchangeIndicator;
    private boolean releaseCallindicator;
    private boolean sendNotificationIndicator;
    private boolean discardMessageIndicator;
    private boolean discardParameterIndicator;
    private int passOnNotPossibleIndicator;
    private int bandInterworkingIndicator;

    private boolean secondOctetPresenet;

    private ByteBuf raw;
    private boolean useAsRaw;

    public ParameterCompatibilityInstructionIndicatorsImpl(final byte code, final ByteBuf b) throws ParameterException {
        super();
        this.parameterCode = code;
        decode(b);
    }

    public ParameterCompatibilityInstructionIndicatorsImpl() {
        super();

    }

    /**
     * This constructor shall be used in cases more octets are defined, User needs to manipulate and encode them properly.
     *
     * @param b
     * @param userAsRaw
     * @throws ParameterException
     */
    public ParameterCompatibilityInstructionIndicatorsImpl(final byte code, final ByteBuf b, final boolean userAsRaw) throws ParameterException {
        super();
        this.parameterCode = code;
        this.raw = b;
        this.useAsRaw = userAsRaw;
        if (!userAsRaw)
            decode(b);
    }

    public ParameterCompatibilityInstructionIndicatorsImpl(boolean transitAtIntermediateExchangeIndicator, boolean releaseCallindicator,
            boolean sendNotificationIndicator, boolean discardMessageIndicator, boolean discardParameterIndicator,
            int passOnNotPossibleIndicator, boolean secondOctetPresenet) {
        super();
        this.transitAtIntermediateExchangeIndicator = transitAtIntermediateExchangeIndicator;
        this.releaseCallindicator = releaseCallindicator;
        this.sendNotificationIndicator = sendNotificationIndicator;
        this.discardMessageIndicator = discardMessageIndicator;
        this.discardParameterIndicator = discardParameterIndicator;
        this.setPassOnNotPossibleIndicator(passOnNotPossibleIndicator);
        this.secondOctetPresenet = secondOctetPresenet;

    }

    public ParameterCompatibilityInstructionIndicatorsImpl(boolean transitAtIntermediateExchangeIndicator, boolean releaseCallindicator,
            boolean sendNotificationIndicator, boolean discardMessageIndicator, boolean discardParameterIndicator,
            int passOnNotPossibleIndicator, int bandInterworkingIndicator) {
        super();
        this.transitAtIntermediateExchangeIndicator = transitAtIntermediateExchangeIndicator;
        this.releaseCallindicator = releaseCallindicator;
        this.sendNotificationIndicator = sendNotificationIndicator;
        this.discardMessageIndicator = discardMessageIndicator;
        this.discardParameterIndicator = discardParameterIndicator;
        this.setPassOnNotPossibleIndicator(passOnNotPossibleIndicator);
        this.setBandInterworkingIndicator(bandInterworkingIndicator);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() < 1) {
            throw new ParameterException("buffer must  not be null and length must  be greater than  0");
        }

        // XXX: Cheat, we read only defined in Q763 2 octets, rest we ignore...
        int v = b.readByte();
        this.transitAtIntermediateExchangeIndicator = (v & 0x01) == _TURN_ON;
        this.releaseCallindicator = ((v >> 1) & 0x01) == _TURN_ON;
        this.sendNotificationIndicator = ((v >> 2) & 0x01) == _TURN_ON;
        this.discardMessageIndicator = ((v >> 3) & 0x01) == _TURN_ON;
        this.discardParameterIndicator = ((v >> 4) & 0x01) == _TURN_ON;
        this.passOnNotPossibleIndicator = ((v >> 5) & 0x03);
        if((((v >> 7) & 0x01) == 0))
        	return;
        
        v = b.readByte();
        this.setBandInterworkingIndicator((v & 0x03));
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        if (this.useAsRaw) {
            // FIXME: make sure we properly encode ext bit?
        	buffer.writeBytes(getRaw());
        	return;
        }
        
        byte b=0;
        b |= (this.transitAtIntermediateExchangeIndicator ? _TURN_ON : _TURN_OFF);
        b |= (this.releaseCallindicator ? _TURN_ON : _TURN_OFF) << 1;
        b |= (this.sendNotificationIndicator ? _TURN_ON : _TURN_OFF) << 2;
        b |= (this.discardMessageIndicator ? _TURN_ON : _TURN_OFF) << 3;
        b |= (this.discardParameterIndicator ? _TURN_ON : _TURN_OFF) << 4;
        b |= this.passOnNotPossibleIndicator << 5;
        if (this.secondOctetPresenet) 
        	b|=0x80;
        
        buffer.writeByte(b);
        
        if (this.secondOctetPresenet) {
        	buffer.writeByte((byte) ((this.bandInterworkingIndicator & 0x03)));            
        }
    }

    @Override
    public void setParamerterCode(byte code) {
        this.parameterCode = code;
    }

    @Override
    public byte getParameterCode() {
        return this.parameterCode;
    }

    public boolean isTransitAtIntermediateExchangeIndicator() {
        return transitAtIntermediateExchangeIndicator;
    }

    public void setTransitAtIntermediateExchangeIndicator(boolean transitAtIntermediateExchangeIndicator) {
        this.transitAtIntermediateExchangeIndicator = transitAtIntermediateExchangeIndicator;
    }

    public boolean isReleaseCallIndicator() {
        return releaseCallindicator;
    }

    public void setReleaseCallIndicator(boolean releaseCallindicator) {
        this.releaseCallindicator = releaseCallindicator;
    }

    public boolean isSendNotificationIndicator() {
        return sendNotificationIndicator;
    }

    public void setSendNotificationIndicator(boolean sendNotificationIndicator) {
        this.sendNotificationIndicator = sendNotificationIndicator;
    }

    public boolean isDiscardMessageIndicator() {
        return discardMessageIndicator;
    }

    public void setDiscardMessageIndicator(boolean discardMessageIndicator) {
        this.discardMessageIndicator = discardMessageIndicator;
    }

    public boolean isDiscardParameterIndicator() {
        return discardParameterIndicator;
    }

    public void setDiscardParameterIndicator(boolean discardParameterIndicator) {
        this.discardParameterIndicator = discardParameterIndicator;
    }

    public int getPassOnNotPossibleIndicator() {
        return passOnNotPossibleIndicator;
    }

    public void setPassOnNotPossibleIndicator(int passOnNotPossibleIndicator2) {
        this.passOnNotPossibleIndicator = passOnNotPossibleIndicator2;
    }

    public int getBandInterworkingIndicator() {
        return bandInterworkingIndicator;
    }

    public void setBandInterworkingIndicator(int bandInterworkingIndicator) {
        this.bandInterworkingIndicator = bandInterworkingIndicator;
        this.secondOctetPresenet = true;
    }

    public boolean isSecondOctetPresent() {
        return secondOctetPresenet;
    }

    public void setSecondOctetPresent(boolean secondOctetPresenet) {
        this.secondOctetPresenet = secondOctetPresenet;
    }

    public ByteBuf getRaw() {
    	if(raw==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(raw);
    }

    public void setRaw(ByteBuf raw) {
        this.raw = raw;
    }

    public boolean isUseAsRaw() {
        return useAsRaw;
    }

    public void setUseAsRaw(boolean useAsRaw) {
        this.useAsRaw = useAsRaw;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ParameterCompatibilityInstructionIndicators [");

        sb.append("parameterCode=");
        sb.append(parameterCode);
        sb.append(", ");
        sb.append("transitAtIntermediateExchangeIndicator=");
        sb.append(transitAtIntermediateExchangeIndicator);
        sb.append(", ");
        sb.append("releaseCallindicator=");
        sb.append(releaseCallindicator);
        sb.append(", ");
        sb.append("sendNotificationIndicator=");
        sb.append(sendNotificationIndicator);
        sb.append(", ");
        sb.append("discardMessageIndicator=");
        sb.append(discardMessageIndicator);
        sb.append(", ");
        sb.append("discardParameterIndicator=");
        sb.append(discardParameterIndicator);
        sb.append(", ");
        sb.append("passOnNotPossibleIndicator=");
        sb.append(passOnNotPossibleIndicator);
        sb.append(", ");
        sb.append("bandInterworkingIndicator=");
        sb.append(bandInterworkingIndicator);
        sb.append(", ");
        sb.append("secondOctetPresenet=");
        sb.append(secondOctetPresenet);
        sb.append(", ");
        sb.append("useAsRaw=");
        sb.append(useAsRaw);
        if (raw != null) {
            sb.append(", ");
            sb.append("raw=");
            ByteBuf curr=getRaw();
            while(curr.readableBytes()>0) {
                sb.append((int) curr.readByte());
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }
}
