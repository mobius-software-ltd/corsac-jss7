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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriterium;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class DPAnalysedInfoCriteriumImpl implements DPAnalysedInfoCriterium {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString dialledNumber;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=1)
	private ASNInteger serviceKey;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2, defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString gsmSCFAddress;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=3)
	private ASNDefaultCallHandling defaultCallHandling;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;

    public DPAnalysedInfoCriteriumImpl() {
    }

    public DPAnalysedInfoCriteriumImpl(ISDNAddressString dialledNumber, int serviceKey, ISDNAddressString gsmSCFAddress,
            DefaultCallHandling defaultCallHandling, MAPExtensionContainer extensionContainer) {
        this.dialledNumber = dialledNumber;
        this.serviceKey = new ASNInteger(serviceKey,"ServiceKey",0,Integer.MAX_VALUE,false);
        this.gsmSCFAddress = gsmSCFAddress;
        
        if(defaultCallHandling!=null)
        	this.defaultCallHandling = new ASNDefaultCallHandling(defaultCallHandling);
        	
        this.extensionContainer = extensionContainer;
    }

    public ISDNAddressString getDialledNumber() {
        return this.dialledNumber;
    }

    public int getServiceKey() {
    	if(this.serviceKey==null)
    		return 0;
    	
        return this.serviceKey.getIntValue();
    }

    public ISDNAddressString getGsmSCFAddress() {
        return this.gsmSCFAddress;
    }

    public DefaultCallHandling getDefaultCallHandling() {
    	if(this.defaultCallHandling==null)
    		return null;
    	
        return this.defaultCallHandling.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DPAnalysedInfoCriterium [");

        if (this.dialledNumber != null) {
            sb.append("dialledNumber=");
            sb.append(this.dialledNumber.toString());
            sb.append(", ");
        }

        sb.append("serviceKey=");
        sb.append(this.serviceKey.getValue());
        sb.append(", ");

        if (this.gsmSCFAddress != null) {
            sb.append("gsmSCFAddress=");
            sb.append(this.gsmSCFAddress.toString());
            sb.append(", ");
        }

        if (this.defaultCallHandling != null && this.defaultCallHandling.getType() != null) {
            sb.append("defaultCallHandling=");
            sb.append(this.defaultCallHandling.getType().toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(dialledNumber==null)
			throw new ASNParsingComponentException("dialed number should be set for dp analysed info", ASNParsingComponentExceptionReason.MistypedParameter);

		if(serviceKey==null)
			throw new ASNParsingComponentException("service key should be set for dp analysed info", ASNParsingComponentExceptionReason.MistypedParameter);

		if(gsmSCFAddress==null)
			throw new ASNParsingComponentException("gsm scf address should be set for dp analysed info", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
