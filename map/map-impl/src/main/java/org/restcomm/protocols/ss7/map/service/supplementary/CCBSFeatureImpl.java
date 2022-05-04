/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CCBSFeature;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CCBSFeatureImpl implements CCBSFeature {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNInteger ccbsIndex;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString bSubscriberNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString bSubscriberSubaddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private BasicServiceCodeWrapperImpl basicServiceCode;

    public CCBSFeatureImpl() {
    }

    public CCBSFeatureImpl(Integer ccbsIndex, ISDNAddressString bSubscriberNumber, ISDNAddressString bSubscriberSubaddress, BasicServiceCode basicServiceCode) {
        if(ccbsIndex!=null)
        	this.ccbsIndex = new ASNInteger(ccbsIndex,"CCBSIndex",1,5,false);
        	
        this.bSubscriberNumber = bSubscriberNumber;
        this.bSubscriberSubaddress = bSubscriberSubaddress;
        
        if(basicServiceCode!=null)
        	this.basicServiceCode = new BasicServiceCodeWrapperImpl(basicServiceCode);
    }

    public Integer getCcbsIndex() {
    	if(ccbsIndex==null)
    		return null;
    	
        return ccbsIndex.getIntValue();
    }

    public ISDNAddressString getBSubscriberNumber() {
        return bSubscriberNumber;
    }

    public ISDNAddressString getBSubscriberSubaddress() {
        return bSubscriberSubaddress;
    }

    public BasicServiceCode getBasicServiceCode() {
    	if(basicServiceCode==null)
    		return null;
    	
        return basicServiceCode.getBasicServiceCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CCBSFeature [");

        if (this.ccbsIndex != null) {
            sb.append("ccbsIndex=");
            sb.append(this.ccbsIndex.getValue());
            sb.append(", ");
        }
        if (this.bSubscriberNumber != null) {
            sb.append("bSubscriberNumber=");
            sb.append(this.bSubscriberNumber);
            sb.append(", ");
        }
        if (this.bSubscriberSubaddress != null) {
            sb.append("bSubscriberSubaddress=");
            sb.append(this.bSubscriberSubaddress);
            sb.append(", ");
        }
        if (this.basicServiceCode != null && this.basicServiceCode.getBasicServiceCode()!=null) {
            sb.append("basicServiceCode=");
            sb.append(this.basicServiceCode.getBasicServiceCode());
            sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }

}
