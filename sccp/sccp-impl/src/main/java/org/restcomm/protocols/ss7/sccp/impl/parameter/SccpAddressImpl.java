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

package org.restcomm.protocols.ss7.sccp.impl.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.indicator.AddressIndicator;
import org.restcomm.protocols.ss7.indicator.GlobalTitleIndicator;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public class SccpAddressImpl extends AbstractParameter implements SccpAddress {
	private static final long serialVersionUID = 1L;

	private static final byte ROUTE_ON_PC_FLAG = 0x40;
    private static final short REMOVE_PC_FLAG = 0xFE;
    private static final short REMOVE_PC_FLAG_ANSI = 0xFD;
    private static final byte PC_PRESENT_FLAG = 0x01;
    private static final byte PC_PRESENT_FLAG_ANSI = 0x02;

    private GlobalTitle gt;
    private int pc = 0;
    private int ssn = -1;

    private AddressIndicator ai;

    // If this SccpAddress is translated address
    private boolean translated;

    public SccpAddressImpl() {
    }

    public SccpAddressImpl(final RoutingIndicator ri, final GlobalTitle gt, final int dpc, final int ssn) {
        this.gt = gt;
        this.pc = dpc;
        this.ssn = ssn;
        this.ai = new AddressIndicator(dpc > 0, ssn > 0, ri, gt == null ? GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED
                : gt.getGlobalTitleIndicator());
    }

    public boolean isTranslated() {
        return translated;
    }

    public void setTranslated(boolean translated) {
        this.translated = translated;
    }

    public AddressIndicator getAddressIndicator() {
        return this.ai;
    }

    public int getSignalingPointCode() {
        return pc;
    }

    public int getSubsystemNumber() {
        return ssn;
    }

    public GlobalTitle getGlobalTitle() {
        return gt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SccpAddressImpl other = (SccpAddressImpl) obj;
        //NOTE: AI is rewritten during routing, this is a hack :/
//        if (ai == null) {
//            if (other.ai != null)
//                return false;
//        } else if (!ai.equals(other.ai))
//            return false;
        if (gt == null) {
            if (other.gt != null)
                return false;
        } else if (!gt.equals(other.gt))
            return false;
        if (pc != other.pc)
            return false;
        if (ssn != other.ssn)
            return false;
        //if (translated != other.translated)
        //    return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ai == null) ? 0 : ai.hashCode());
        result = prime * result + ((gt == null) ? 0 : gt.hashCode());
        result = prime * result + pc;
        result = prime * result + ssn;
        //result = prime * result + (translated ? 1231 : 1237);
        return result;
    }

    public String toString() {
        return ((new StringBuffer()).append("pc=").append(pc).append(",ssn=").append(ssn).append(",AI=").append(ai.getValue(SccpProtocolVersion.ITU))
                .append(",gt=").append(gt)).toString();
    }

    @Override
    public void decode(ByteBuf buffer, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	int b = buffer.readByte() & 0xff;
        this.ai = new AddressIndicator((byte) b, sccpProtocolVersion);

        if (sccpProtocolVersion == SccpProtocolVersion.ANSI) {
            if (this.ai.isSSNPresent()) {
                this.ssn = buffer.readByte() & 0xff;
            }

            if (this.ai.isPCPresent()) {
                int b1 = buffer.readByte() & 0xff;
                int b2 = buffer.readByte() & 0xff;
                int b3 = buffer.readByte() & 0xff;

                this.pc = (b3 << 16) | (b2 << 8) | b1;
            }
        } else {
            if (this.ai.isPCPresent()) {
                int b1 = buffer.readByte() & 0xff;
                int b2 = buffer.readByte() & 0xff;

                this.pc = ((b2 & 0x3f) << 8) | b1;
            }

            if (this.ai.isSSNPresent()) {
                this.ssn = buffer.readByte() & 0xff;
            }
        }

        if(this.ai.getGlobalTitleIndicator()!=GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED){
            this.gt = factory.createGlobalTitle(this.ai.getGlobalTitleIndicator());
            ((AbstractGlobalTitle) this.gt).decode(buffer, factory, sccpProtocolVersion);
        }
    }

    @Override
    public void encode(ByteBuf buffer, final boolean removeSpc, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	byte aiValue = ai.getValue(sccpProtocolVersion);

        if (sccpProtocolVersion == SccpProtocolVersion.ANSI) {
            if (removeSpc && ((aiValue & ROUTE_ON_PC_FLAG) == 0x00)) {
                // Routing on GT so lets remove PC flag

                aiValue = (byte) (aiValue & REMOVE_PC_FLAG_ANSI);
            }

            buffer.writeByte(aiValue);

            if (ai.isSSNPresent()) {
            	buffer.writeByte((byte) this.ssn);
            }

            if ((aiValue & PC_PRESENT_FLAG_ANSI) == PC_PRESENT_FLAG_ANSI) {
                // If Point Code included in SCCP Address
            	buffer.writeByte((byte) this.pc);
            	buffer.writeByte((byte) (this.pc >> 8));
            	buffer.writeByte((byte) (this.pc >> 16));
            }
        } else {
            if (removeSpc && ((aiValue & ROUTE_ON_PC_FLAG) == 0x00)) {
                // Routing on GT so lets remove PC flag

                aiValue = (byte) (aiValue & REMOVE_PC_FLAG);
            }

            buffer.writeByte(aiValue);

            if ((aiValue & PC_PRESENT_FLAG) == PC_PRESENT_FLAG) {
                // If Point Code included in SCCP Address
            	buffer.writeByte((byte) this.pc);
            	buffer.writeByte(((this.pc >> 8) & 0x3f));
            }

            if (ai.isSSNPresent()) {
            	buffer.writeByte((byte) this.ssn);
            }
        }

        if (ai.getGlobalTitleIndicator() != GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED) {
            ((AbstractGlobalTitle) this.gt).encode(buffer, removeSpc, sccpProtocolVersion);
        }
    }
}
