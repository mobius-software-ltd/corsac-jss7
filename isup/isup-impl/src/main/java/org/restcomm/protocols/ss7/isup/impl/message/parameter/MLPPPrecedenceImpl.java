/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

/**
 * Start time:08:42:25 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.MLPPPrecedence;

/**
 * Start time:08:42:25 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MLPPPrecedenceImpl extends AbstractISUPParameter implements MLPPPrecedence {
	private int lfb;
    private int precedenceLevel;
    private int mllpServiceDomain;
    // FIXME: ensure zero in first digit.?
    private ByteBuf niDigits;

    public MLPPPrecedenceImpl() {
        super();

    }

    public MLPPPrecedenceImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public MLPPPrecedenceImpl(byte lfb, byte precedenceLevel, int mllpServiceDomain, ByteBuf niDigits) {
        super();
        this.lfb = lfb;
        this.precedenceLevel = precedenceLevel;
        this.mllpServiceDomain = mllpServiceDomain;
        setNiDigits(niDigits);
    }

    public void decode(ByteBuf buffer) throws ParameterException {
        if (buffer == null || buffer.readableBytes() != 6) {
            throw new ParameterException("buffer must  not be null and length must  be 6");
        }

        byte b=buffer.readByte();
        this.precedenceLevel = (byte) (b & 0x0F);
        this.lfb = (byte) ((b >> 5) & 0x03);
        byte v = 0;
        
        this.niDigits = Unpooled.buffer(4);
        for (int i = 0; i < 2; i++) {
            v = 0;
            v = buffer.readByte();
            this.niDigits.writeByte((byte) (v & 0x0F));
            this.niDigits.writeByte((byte) ((v >> 4) & 0x0F));
        }

        this.mllpServiceDomain = buffer.readByte() << 16;
        this.mllpServiceDomain |= buffer.readByte() << 8;
        this.mllpServiceDomain |= buffer.readByte();        
    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	byte b= (byte) ((this.lfb & 0x03) << 5);
        b |= this.precedenceLevel & 0x0F;
        buffer.writeByte(b);
        
        byte v = 0;
        ByteBuf curr=getNiDigits();
        for (int i = 0; i < 2; i++) {
            v = 0;

            v |= (curr.readByte() & 0x0F) << 4;
            v |= (curr.readByte() & 0x0F);

            buffer.writeByte(v);
        }

        buffer.writeByte((byte) (this.mllpServiceDomain >> 16));
        buffer.writeByte((byte) (this.mllpServiceDomain >> 8));
        buffer.writeByte((byte) this.mllpServiceDomain);        
    }

    public byte getLfb() {
        return (byte) lfb;
    }

    public void setLfb(byte lfb) {
        this.lfb = lfb;
    }

    public byte getPrecedenceLevel() {
        return (byte) precedenceLevel;
    }

    public void setPrecedenceLevel(byte precedenceLevel) {
        this.precedenceLevel = precedenceLevel;
    }

    public int getMllpServiceDomain() {
        return mllpServiceDomain;
    }

    public void setMllpServiceDomain(int mllpServiceDomain) {
        this.mllpServiceDomain = mllpServiceDomain;
    }

    public ByteBuf getNiDigits() {
    	if(niDigits==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(niDigits);
    }

    public void setNiDigits(ByteBuf niDigits) {
        if (niDigits == null || niDigits.readableBytes() != 4) {
            throw new IllegalArgumentException();
        }
        this.niDigits = niDigits;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("MLPPPrecedence [");

        sb.append("lfb=");
        sb.append(lfb);
        sb.append(", ");
        sb.append("precedenceLevel=");
        sb.append(precedenceLevel);
        sb.append(", ");
        sb.append("mllpServiceDomain=");
        sb.append(mllpServiceDomain);

        if (niDigits != null) {
            sb.append(", ");
            sb.append("niDigits=");
            ByteBuf curr=getNiDigits();
            while(curr.readableBytes()>0) {
                sb.append((int) curr.readByte());
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }
}
