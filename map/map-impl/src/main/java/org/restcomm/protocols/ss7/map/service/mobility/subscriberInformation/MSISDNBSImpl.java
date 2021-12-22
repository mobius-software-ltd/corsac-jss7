/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBS;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * Created by vsubbotin on 26/05/16.
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MSISDNBSImpl implements MSISDNBS {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString msisdn;
        
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private ExtBasicServiceCodeListWrapperImpl basicServiceList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public MSISDNBSImpl() {
    }

    public MSISDNBSImpl(ISDNAddressString msisdn, List<ExtBasicServiceCode> basicServiceList,
            MAPExtensionContainer extensionContainer) {
        this.msisdn = msisdn;
        
        if(basicServiceList!=null)
        	this.basicServiceList = new ExtBasicServiceCodeListWrapperImpl(basicServiceList);
        
        this.extensionContainer = extensionContainer;
    }

    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    public List<ExtBasicServiceCode> getBasicServiceList() {
    	if(this.basicServiceList==null)
    		return null;
    	
        return this.basicServiceList.getExtBasicServiceCode();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MSISDNBS [");

        if (this.msisdn != null) {
            sb.append("msisdn=");
            sb.append(this.msisdn);
            sb.append(", ");
        }
        if (this.basicServiceList != null && this.basicServiceList.getExtBasicServiceCode()!=null) {
            sb.append("basicServiceList=[");
            boolean firstItem = true;
            for (ExtBasicServiceCode extCwFeature: basicServiceList.getExtBasicServiceCode()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(extCwFeature);
            }
            sb.append("], ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer);
            sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }
}
