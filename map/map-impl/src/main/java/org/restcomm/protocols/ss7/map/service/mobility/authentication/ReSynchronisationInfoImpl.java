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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ReSynchronisationInfoImpl implements ReSynchronisationInfo {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ASNOctetString rand;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1)
	private ASNOctetString auts;
    
    public ReSynchronisationInfoImpl() {
    }

    public ReSynchronisationInfoImpl(ByteBuf rand, ByteBuf auts) {
    	if(rand!=null)
    		this.rand = new ASNOctetString(rand,"RAND",16,16,false);
    	
    	if(auts!=null)
    		this.auts = new ASNOctetString(auts,"AUTS",14,14,false);    	
    }

    public ByteBuf getRand() {
    	if(this.rand==null)
    		return null;
    	
    	return this.rand.getValue();    	
    }

    public ByteBuf getAuts() {
    	if(this.auts==null)
    		return null;
    	
    	return this.auts.getValue();    	
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReSynchronisationInfo [");

        if (this.rand != null) {
            sb.append("rand=[");
            sb.append(rand.printDataArr());
            sb.append("], ");
        }
        if (this.auts != null) {
            sb.append("auts=[");
            sb.append(auts.printDataArr());
            sb.append("], ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(rand==null)
			throw new ASNParsingComponentException("rand should be set for resynchronization info", ASNParsingComponentExceptionReason.MistypedParameter);

		if(auts==null)
			throw new ASNParsingComponentException("auts should be set for resynchronization info", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
