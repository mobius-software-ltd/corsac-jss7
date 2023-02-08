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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class TBcsmCamelTDPDataImpl implements TBcsmCamelTDPData {
	private ASNTBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint;
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gsmSCFAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNDefaultCallHandling defaultCallHandling;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public TBcsmCamelTDPDataImpl() {
    }

    public TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint, int serviceKey,
    		ISDNAddressString gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainer extensionContainer) {
        if(tBcsmTriggerDetectionPoint!=null)
        	this.tBcsmTriggerDetectionPoint = new ASNTBcsmTriggerDetectionPoint(tBcsmTriggerDetectionPoint);
        	       
        this.serviceKey = new ASNInteger(serviceKey,"ServiceKey",0,Integer.MAX_VALUE,false);
        this.gsmSCFAddress = gsmSCFAddress;
        
        if(defaultCallHandling!=null)
        	this.defaultCallHandling = new ASNDefaultCallHandling(defaultCallHandling);
        	
        this.extensionContainer = extensionContainer;
    }

    public TBcsmTriggerDetectionPoint getTBcsmTriggerDetectionPoint() {
    	if(tBcsmTriggerDetectionPoint==null)
    		return null;
    	
        return tBcsmTriggerDetectionPoint.getType();
    }

    public int getServiceKey() {
    	return serviceKey.getIntValue();
    }

    public ISDNAddressString getGsmSCFAddress() {
        return gsmSCFAddress;
    }

    public DefaultCallHandling getDefaultCallHandling() {
    	if(defaultCallHandling==null)
    		return null;
    	
        return defaultCallHandling.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TBcsmCamelTDPData");
        sb.append(" [");

        if (this.tBcsmTriggerDetectionPoint != null) {
            sb.append("tBcsmTriggerDetectionPoint=");
            sb.append(this.tBcsmTriggerDetectionPoint.toString());
        }
        sb.append(", serviceKey=");
        sb.append(this.serviceKey);
        if (this.gsmSCFAddress != null) {
            sb.append(", gsmSCFAddress=");
            sb.append(this.gsmSCFAddress.toString());
        }
        if (this.defaultCallHandling != null) {
            sb.append(", defaultCallHandling=");
            sb.append(this.defaultCallHandling.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(tBcsmTriggerDetectionPoint==null)
			throw new ASNParsingComponentException("tbcsm trigger detection point should be set for tbcsm camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(serviceKey!=null)
			throw new ASNParsingComponentException("service key should be set for tbcsm camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(gsmSCFAddress!=null)
			throw new ASNParsingComponentException("gsm scf address should be set for tbcsm camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(defaultCallHandling!=null)
			throw new ASNParsingComponentException("default call handling should be set for tbcsm camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
