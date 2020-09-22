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
package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SSCamelDataImpl {	
	private SSCodeListWrapperImpl ssEventList;
    private ISDNAddressStringImpl gsmSCFAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;

    public SSCamelDataImpl() {
    }

    public SSCamelDataImpl(ArrayList<SSCodeImpl> ssEventList, ISDNAddressStringImpl gsmSCFAddress,
            MAPExtensionContainerImpl extensionContainer) {
        if(ssEventList!=null)
        	this.ssEventList = new SSCodeListWrapperImpl(ssEventList);
        
        this.gsmSCFAddress = gsmSCFAddress;
        this.extensionContainer = extensionContainer;
    }

    public ArrayList<SSCodeImpl> getSsEventList() {
    	if(this.ssEventList==null)
    		return null;
    	
        return this.ssEventList.getSSCode();
    }

    public ISDNAddressStringImpl getGsmSCFAddress() {
        return this.gsmSCFAddress;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SSCamelData [");

        if (this.ssEventList != null && this.ssEventList.getSSCode()!=null) {
            sb.append("ssEventList=[");
            boolean firstItem = true;
            for (SSCodeImpl be : this.ssEventList.getSSCode()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.gsmSCFAddress != null) {
            sb.append("gsmSCFAddress=");
            sb.append(this.gsmSCFAddress.toString());
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

}
