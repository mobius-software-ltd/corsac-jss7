/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargingAnalysisInputData;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

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

    public ChargingAnalysisInputDataImpl(ByteBuf chargingOrigin, ByteBuf tariffActivityCode, Integer chargingCode) {
    	if(chargingOrigin!=null)
    		this.chargingOrigin=new ASNOctetString(chargingOrigin,"ChargingOrigin",1,1,false);    	
    	
    	if(tariffActivityCode!=null)
    		this.tariffActivityCode=new ASNOctetString(tariffActivityCode,"ChargingOrigin",4,4,false);
    	
    	if(chargingCode!=null)
    		this.chargingCode=new ASNInteger(chargingCode,"ChargingCode",0,4095,false);    		
    }

    public ByteBuf getChargingOrigin() {
    	if(chargingOrigin==null)
    		return null;
    	
    	return chargingOrigin.getValue();
    }

    public ByteBuf getTariffActivityCode() {
    	if(tariffActivityCode==null)
    		return null;
    	
    	return tariffActivityCode.getValue();
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
            sb.append(chargingOrigin.printDataArr());
        }
        
        if (this.tariffActivityCode != null && this.tariffActivityCode.getValue()!=null) {
            sb.append(", tariffActivityCode=");
            sb.append(chargingOrigin.printDataArr());
        }
        
        if (this.chargingCode != null && this.chargingCode.getValue()!=null) {
            sb.append(", chargingCode=");
            sb.append(chargingCode.getValue());
        }
        
        sb.append("]");

        return sb.toString();
    }
}