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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PDNGWUpdate;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWIdentity;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDNGWIdentityImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class PDNGWUpdateImpl implements PDNGWUpdate {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1,defaultImplementation = APNImpl.class)
    private APN apn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = PDNGWIdentityImpl.class)
    private PDNGWIdentity pdnGwIdentity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNInteger contextId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public PDNGWUpdateImpl() {
    }

    public PDNGWUpdateImpl(APN apn, PDNGWIdentity pdnGwIdentity, Integer contextId, MAPExtensionContainer extensionContainer) {
        this.apn = apn;
        this.pdnGwIdentity = pdnGwIdentity;
        
        if(contextId!=null)
        	this.contextId = new ASNInteger(contextId);
        	
        this.extensionContainer = extensionContainer;
    }

    public APN getAPN() {
        return this.apn;
    }

    public PDNGWIdentity getPdnGwIdentity() {
        return this.pdnGwIdentity;
    }

    public Integer getContextId() {
    	if(this.contextId==null)
    		return null;
    	
        return this.contextId.getIntValue();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PDNGWUpdate [");

        if (this.apn != null) {
            sb.append("apn=");
            sb.append(this.apn.toString());
            sb.append(", ");
        }

        if (this.pdnGwIdentity != null) {
            sb.append("pdnGwIdentity=");
            sb.append(this.pdnGwIdentity.toString());
            sb.append(", ");
        }

        if (this.contextId != null) {
            sb.append("contextId=");
            sb.append(this.contextId.getValue());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
