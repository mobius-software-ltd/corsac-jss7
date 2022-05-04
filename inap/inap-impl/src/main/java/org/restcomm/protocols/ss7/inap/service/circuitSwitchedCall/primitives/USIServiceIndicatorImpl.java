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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import java.util.List;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.USIServiceIndicator;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class USIServiceIndicatorImpl implements USIServiceIndicator {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNObjectIdentifier global;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNOctetString local;

    public USIServiceIndicatorImpl() {
    }

    public USIServiceIndicatorImpl(List<Long> global) {
    	if(global!=null)
    		this.global=new ASNObjectIdentifier(global,"Global",true,false);    	
    }

    public USIServiceIndicatorImpl(ByteBuf local) {
    	if(local!=null)
    		this.local=new ASNOctetString(local,"Local",null,null,false);    	
    }

    public List<Long> getGlobal() {
    	if(global==null || global.getValue()==null)
    		return null;
    	
        return global.getValue();
    }

    public ByteBuf getLocal() {
    	if(local==null)
    		return null;
    	
    	return local.getValue();    	
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("USIServiceIndicator [");

        if (this.global != null && this.global.getValue()!=null) {
            sb.append(", global=");
            sb.append(ASNObjectIdentifier.printDataArrLong(getGlobal()));
        }
        
        if (this.local != null && this.local.getValue()!=null) {
            sb.append(", local=");
            sb.append(local.printDataArr());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(global==null && local==null)
			throw new ASNParsingComponentException("either local or global should be set for usi service indicators", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
