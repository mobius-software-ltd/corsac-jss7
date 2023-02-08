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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;

/**
 * Start time:15:14:32 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public class CauseIndicatorsImpl extends AbstractISUPParameter implements CauseIndicators {
	private int location = 0;
    private int causeValue = 0;
    private int codingStandard = 0;
    private int recommendation = 0;
    private ByteBuf diagnostics = null;

    public CauseIndicatorsImpl() {
        super();

    }

    public CauseIndicatorsImpl(int codingStandard, int location, int recommendation, int causeValue, ByteBuf diagnostics) {
        super();
        this.setCodingStandard(codingStandard);
        this.setLocation(location);
        this.setRecommendation(recommendation);
        this.setCauseValue(causeValue);
        this.diagnostics = diagnostics;
    }

    public void decode(ByteBuf b) throws ParameterException {

        // NOTE: there are ext bits but we do not care about them
        // FIXME: "Recommendation" optional field must be encoded/decoded when codingStandard!=_CODING_STANDARD_ITUT

        if (b == null || b.readableBytes() < 2) {
            throw new ParameterException("buffer must not be null or has size less than 2");
        }
        // Used because of Q.850 - we must ignore recomendation
        // first two bytes are mandatory
        int v = 0;
        // remove ext
        v = b.readByte() & 0x7F;
        this.location = v & 0x0F;
        this.codingStandard = v >> 5;        
        
        v = 0;
        v = b.readByte() & 0x7F;
        this.causeValue = v;
        if (b.readableBytes()>0) {            
            if (b.readableBytes() % 3 != 0) {
                throw new ParameterException("Diagnostics part  must have 3xN bytes, it has: " + (b.readableBytes()));
            }

            this.diagnostics=b.slice(b.readerIndex(), b.readableBytes());
        }
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        int v = this.location & 0x0F;
        v |= (byte) ((this.codingStandard & 0x03) << 5) | (0x01 << 7);
        buffer.writeByte(v);
        buffer.writeByte(this.causeValue | (0x01 << 7));
        if (this.diagnostics != null) {
        	buffer.writeBytes(getDiagnostics());
        }
    }

    public int getCodingStandard() {
        return codingStandard;
    }

    public void setCodingStandard(int codingStandard) {
        this.codingStandard = codingStandard & 0x03;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location & 0x0F;
    }

    public int getCauseValue() {
        return causeValue & 0x7F;
    }

    public int getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(int recommendation) {
        this.recommendation = recommendation & 0x7F;
    }

    public void setCauseValue(int causeValue) {
        this.causeValue = causeValue;
    }

    public ByteBuf getDiagnostics() {
    	if(diagnostics==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(diagnostics);
    }

    public void setDiagnostics(ByteBuf diagnostics) {
        this.diagnostics = diagnostics;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("CauseIndicators [");

        sb.append("codingStandard=");
        sb.append(codingStandard);
        sb.append(", location=");
        sb.append(location);
        sb.append(", recommendation=");
        sb.append(recommendation);
        sb.append(", causeValue=");
        sb.append(causeValue);

        if (this.diagnostics != null) {
            sb.append(", diagnostics=[");
            sb.append(printDataArr(getDiagnostics()));
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

    protected String printDataArr(ByteBuf data) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if (data != null) {
            while(data.readableBytes()>0) {
                if (first)
                    first = false;
                else
                    sb.append(", ");
                
                sb.append(data.readByte());
            }
        }

        return sb.toString();
    }
}
