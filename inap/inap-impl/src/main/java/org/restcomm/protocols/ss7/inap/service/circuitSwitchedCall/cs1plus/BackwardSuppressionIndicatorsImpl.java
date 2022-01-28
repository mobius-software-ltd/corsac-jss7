/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardSuppression;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardSuppressionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.InstructionIndicator;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class BackwardSuppressionIndicatorsImpl extends ASNOctetString implements BackwardSuppressionIndicators {
	public BackwardSuppressionIndicatorsImpl() {
    }

	public BackwardSuppressionIndicatorsImpl(BackwardSuppression backwardSuppression,InstructionIndicator instructionIndicator) {
		super(translate(backwardSuppression, instructionIndicator));
	}
	
    private static ByteBuf translate(BackwardSuppression backwardSuppression,InstructionIndicator instructionIndicator) {
    	if(backwardSuppression!=null || instructionIndicator!=null) {
    		ByteBuf value=Unpooled.buffer(2);
    		if(backwardSuppression!=null)
    			value.writeByte(backwardSuppression.getCode());
    		else
    			value.writeByte(0);
    		
    		if(instructionIndicator!=null)
    			value.writeByte(instructionIndicator.getCode());
    		
    		return value;	
    	}
    	else
    		return Unpooled.EMPTY_BUFFER;
    }

    public BackwardSuppression getBackwardSuppression() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 2)
            return null;

        return BackwardSuppression.getInstance(data.readByte() & 0x0F);
    }

    public InstructionIndicator getInstructionIndicator() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 2)
            return null;

        data.readByte();
        return InstructionIndicator.getInstance(data.readByte() & 0x0F);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("BackwardSuppressionIndicators [");
        
        BackwardSuppression backwardSuppression=getBackwardSuppression();
        if (backwardSuppression != null) {
            sb.append("backwardSuppression=");
            sb.append(backwardSuppression);            
        }
        
        InstructionIndicator instructionIndicator=getInstructionIndicator();
        if (instructionIndicator != null) {
            sb.append("instructionIndicator=");
            sb.append(instructionIndicator);            
        }
        
        sb.append("]");

        return sb.toString();
    }
}
