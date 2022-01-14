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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargingAnalysisInputData;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ChargingAnalysisInputDataImpl implements ChargingAnalysisInputData {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
	private ASNOctetString chargingOrigin;
	    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1)
    private ASNOctetString tariffActivityCode;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1)
    private ASNInteger chargingCode;
    
	public ChargingAnalysisInputDataImpl() {
    }

    public ChargingAnalysisInputDataImpl(byte[] chargingOrigin, byte[] tariffActivityCode, Integer chargingCode) {
    	if(chargingOrigin!=null) {
    		this.chargingOrigin=new ASNOctetString();
    		this.chargingOrigin.setValue(Unpooled.wrappedBuffer(chargingOrigin));
    	}
    	
    	if(tariffActivityCode!=null) {
    		this.tariffActivityCode=new ASNOctetString();
    		this.tariffActivityCode.setValue(Unpooled.wrappedBuffer(tariffActivityCode));
    	}
    	
    	if(chargingCode!=null) {
    		this.chargingCode=new ASNInteger();
    		this.chargingCode.setValue(chargingCode.longValue());
    	}
    }

    public byte[] getChargingOrigin() {
    	if(chargingOrigin==null || chargingOrigin.getValue()==null)
    		return null;
    	
    	ByteBuf value=chargingOrigin.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public byte[] getTariffActivityCode() {
    	if(tariffActivityCode==null || tariffActivityCode.getValue()==null)
    		return null;
    	
    	ByteBuf value=tariffActivityCode.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
    }

    public Integer getChargingCode() {
    	if(chargingCode==null || chargingCode.getValue()==null)
    		return null;
    	
    	return chargingCode.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ChargingAnalysisInputData [");

        if (this.chargingOrigin != null && this.chargingOrigin.getValue()!=null) {
            sb.append(", chargingOrigin=");
            sb.append(ASNOctetString.printDataArr(getChargingOrigin()));
        }
        
        if (this.tariffActivityCode != null && this.tariffActivityCode.getValue()!=null) {
            sb.append(", tariffActivityCode=");
            sb.append(ASNOctetString.printDataArr(getTariffActivityCode()));
        }
        
        if (this.chargingCode != null && this.chargingCode.getValue()!=null) {
            sb.append(", chargingCode=");
            sb.append(chargingCode.getValue());
        }
        
        sb.append("]");

        return sb.toString();
    }
}