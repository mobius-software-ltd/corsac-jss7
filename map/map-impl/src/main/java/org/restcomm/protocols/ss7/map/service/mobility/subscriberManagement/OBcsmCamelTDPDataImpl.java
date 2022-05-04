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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;

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
public class OBcsmCamelTDPDataImpl implements OBcsmCamelTDPData {
	private ASNOBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint;
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gsmSCFAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNDefaultCallHandling defaultCallHandling;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public OBcsmCamelTDPDataImpl() {
    }

    public OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint, int serviceKey,
    		ISDNAddressString gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainer extensionContainer) {
        if(oBcsmTriggerDetectionPoint!=null)
        	this.oBcsmTriggerDetectionPoint = new ASNOBcsmTriggerDetectionPoint(oBcsmTriggerDetectionPoint);
        			
        this.serviceKey = new ASNInteger(serviceKey,"ServiceKey",0,Integer.MAX_VALUE,false);
    	this.gsmSCFAddress = gsmSCFAddress;
        
        if(defaultCallHandling!=null)
        	this.defaultCallHandling = new ASNDefaultCallHandling(defaultCallHandling);
        	
        this.extensionContainer = extensionContainer;
    }

    public OBcsmTriggerDetectionPoint getOBcsmTriggerDetectionPoint() {
    	if(oBcsmTriggerDetectionPoint==null)
    		return null;
    	
        return oBcsmTriggerDetectionPoint.getType();
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
        sb.append("OBcsmCamelTDPData");
        sb.append(" [");

        if (this.oBcsmTriggerDetectionPoint != null) {
            sb.append("oBcsmTriggerDetectionPoint=");
            sb.append(this.oBcsmTriggerDetectionPoint.toString());
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
		if(oBcsmTriggerDetectionPoint==null)
			throw new ASNParsingComponentException("obcsm trigger detection point should be set for obcsm camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(serviceKey!=null)
			throw new ASNParsingComponentException("service key should be set for obcsm camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(gsmSCFAddress!=null)
			throw new ASNParsingComponentException("gsm scf address should be set for obcsm camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(defaultCallHandling!=null)
			throw new ASNParsingComponentException("default call handling should be set for obcsm camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
