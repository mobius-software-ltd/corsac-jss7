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
 * Start time:14:44:16 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.RangeAndStatus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Start time:14:44:16 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RangeAndStatusImpl extends AbstractISUPParameter implements RangeAndStatus {
	private byte range;
    private ByteBuf status;

    // FIXME:
    // private Status[] status = null;

    public RangeAndStatusImpl(ByteBuf b) throws ParameterException {
        super();
        if (b.readableBytes() < 1) {
            throw new ParameterException("RangeAndStatus requires atleast 1 byte.");
        }
        decode(b);

    }

    public RangeAndStatusImpl() {
        super();

    }

    public RangeAndStatusImpl(byte range, ByteBuf status) {
        super();
        this.range = range;
        setStatus(status);
    }

    public void decode(ByteBuf b) throws ParameterException {

        this.range = b.readByte();
        if (b.readableBytes()==0)
            return;
        
        this.status = b.readSlice(b.readableBytes());        
    }

    public void encode(ByteBuf b) throws ParameterException {
        checkData(range, status);

        b.writeByte(this.range);
        if(this.status!=null)
        	b.writeBytes(this.status);
    }

    public byte getRange() {
        return range;
    }

    public void setRange(byte range) {
        this.setRange(range, false);
    }

    public void setRange(byte range, boolean addStatus) {
        this.range = range;
        // range tells how much cics are affected, or potentially affected.
        // statys field contains bits(1|0) to indicate
        if (addStatus) {
            // check len of byte, +1, for cic in message.
            int len = (range + 1) / 8;
            if ((range + 1) % 8 != 0) {
                len++;
            }
            this.status = Unpooled.buffer(len);
            for(int i=0;i<len;i++)
            	this.status.writeByte(0);
        }
    }

    public ByteBuf getStatus() {
        return status;
    }

    public void setStatus(ByteBuf status) {
        this.status = status;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.message.parameter.RangeAndStatus#isAffected (byte)
     */
    public boolean isAffected(byte b) throws IllegalArgumentException {
        if (this.status.readableBytes() < (b / 8)) {
            throw new IllegalArgumentException("Argument exceeds status!");
        }
        int index_l = (b / 8);

        int index = b % 8; // number of bit to lit... ech
        int n2Pattern = (int) Math.pow(2, index); // hmm no int pows... sucks
        return (this.status.getByte(index_l) & n2Pattern) > 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.message.parameter.RangeAndStatus#setAffected (byte, boolean)
     */
    public void setAffected(byte subrange, boolean v) throws IllegalArgumentException {
        if (this.status == null) {
            throw new IllegalArgumentException("Can not set affected if no status present!");
        }
        // ceck
        if (this.status.readableBytes() < (subrange / 8)) {
            throw new IllegalArgumentException("Argument exceeds status!");
        }
        int index_l = (subrange / 8);
        int index = subrange % 8; // number of bit to lit... ech
        int n2Pattern = (int) Math.pow(2, index); // hmm no int pows... sucks

        if (v) {
            this.status.setByte(index_l,this.status.getByte(index_l) | n2Pattern);
        } else {
            // not, we have to inverse pattern...
            n2Pattern = 0xFF ^ n2Pattern; // this will create bits with zeros in place of n2Pattern ones!
            this.status.setByte(index_l,this.status.getByte(index_l) & n2Pattern); // do logical and, this will kill proper bit and leave rest unchanged
        }
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    private static void checkData(byte range, ByteBuf status) throws ParameterException {
        // FIXME: add checks specific to messages~!
        if (status != null) {

            int len = (range + 1) / 8;
            if ((range + 1) % 8 != 0) {
                len++;
            }
            if (status.readableBytes() != len) {
                throw new ParameterException("Wrong length of status part: " + status.readableBytes() + ", range: " + range);
            }
        } else {
            // there are cases when this can be null;
        }
    }

}
