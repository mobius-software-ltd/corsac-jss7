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

package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.primitives.ASNEMLPPPriorityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class RegisterSSRequestImpl extends SupplementaryMessageImpl implements RegisterSSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = SSCodeImpl.class)
    private SSCode ssCode;
    
    @ASNChoise(defaultImplementation = BasicServiceCodeImpl.class)
    private BasicServiceCode basicServiceCode;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = AddressStringImpl.class)
    private AddressString forwardedToNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString forwardedToSubaddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNInteger noReplyConditionTime;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNEMLPPPriorityImpl defaultPriority;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNInteger nbrUser;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString longFTNSupported;

    public RegisterSSRequestImpl() {
    }

    public RegisterSSRequestImpl(SSCode ssCode, BasicServiceCode basicServiceCode, AddressString forwardedToNumber, ISDNAddressString forwardedToSubaddress,
            Integer noReplyConditionTime, EMLPPPriority defaultPriority, Integer nbrUser, ISDNAddressString longFTNSupported) {
        this.ssCode = ssCode;
        this.basicServiceCode=basicServiceCode;
        this.forwardedToNumber = forwardedToNumber;
        this.forwardedToSubaddress = forwardedToSubaddress;
        
        if(noReplyConditionTime!=null) {
        	this.noReplyConditionTime = new ASNInteger();
        	this.noReplyConditionTime.setValue(noReplyConditionTime.longValue());
        }
        
        if(defaultPriority!=null) {
        	this.defaultPriority = new ASNEMLPPPriorityImpl();
        	this.defaultPriority.setType(defaultPriority);
        }
        
        if(nbrUser!=null) {
        	this.nbrUser = new ASNInteger();
        	this.nbrUser.setValue(nbrUser.longValue());
        }
        
        this.longFTNSupported = longFTNSupported;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.registerSS_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.registerSS;
    }

    @Override
    public SSCode getSsCode() {
        return ssCode;
    }

    @Override
    public BasicServiceCode getBasicService() {
        return basicServiceCode;
    }

    @Override
    public AddressString getForwardedToNumber() {
        return forwardedToNumber;
    }

    @Override
    public ISDNAddressString getForwardedToSubaddress() {
        return forwardedToSubaddress;
    }

    @Override
    public Integer getNoReplyConditionTime() {
    	if(noReplyConditionTime==null)
    		return null;
    	
        return noReplyConditionTime.getValue().intValue();
    }

    @Override
    public EMLPPPriority getDefaultPriority() {
    	if(defaultPriority==null)
    		return null;
    	
        return defaultPriority.getType();
    }

    @Override
    public Integer getNbrUser() {
    	if(nbrUser==null)
    		return null;
    	
        return nbrUser.getValue().intValue();
    }

    @Override
    public ISDNAddressString getLongFTNSupported() {
        return longFTNSupported;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RegisterSSRequest [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(ssCode);
            sb.append(", ");
        }
        if (this.basicServiceCode != null) {
            sb.append("basicService=");
            sb.append(basicServiceCode);
            sb.append(", ");
        }
        if (this.forwardedToNumber != null) {
            sb.append("forwardedToNumber=");
            sb.append(forwardedToNumber);
            sb.append(", ");
        }
        if (this.forwardedToSubaddress != null) {
            sb.append("forwardedToSubaddress=");
            sb.append(forwardedToSubaddress);
            sb.append(", ");
        }
        if (this.noReplyConditionTime != null) {
            sb.append("noReplyConditionTime=");
            sb.append(noReplyConditionTime.getValue());
            sb.append(", ");
        }
        if (this.defaultPriority != null) {
            sb.append("defaultPriority=");
            sb.append(defaultPriority.getType());
            sb.append(", ");
        }
        if (this.nbrUser != null) {
            sb.append("nbrUser=");
            sb.append(nbrUser.getValue());
            sb.append(", ");
        }
        if (this.longFTNSupported != null) {
            sb.append("longFTNSupported=");
            sb.append(longFTNSupported);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
