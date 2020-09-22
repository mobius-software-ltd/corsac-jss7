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

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.TimeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CSGSubscriptionDataImpl {
	private CSGIdImpl csgId;
    private TimeImpl expirationDate;
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private APNListWrapperImpl lipaAllowedAPNList;

    public CSGSubscriptionDataImpl() {
    }

    public CSGSubscriptionDataImpl(CSGIdImpl csgId, TimeImpl expirationDate, MAPExtensionContainerImpl extensionContainer,
            ArrayList<APNImpl> lipaAllowedAPNList) {
        this.csgId = csgId;
        this.expirationDate = expirationDate;
        this.extensionContainer = extensionContainer;
        
        if(lipaAllowedAPNList!=null)
        	this.lipaAllowedAPNList = new APNListWrapperImpl(lipaAllowedAPNList);
    }

    public CSGIdImpl getCsgId() {
        return this.csgId;
    }

    public TimeImpl getExpirationDate() {
        return this.expirationDate;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public ArrayList<APNImpl> getLipaAllowedAPNList() {
    	if(this.lipaAllowedAPNList==null)
    		return null;
    	
        return this.lipaAllowedAPNList.getAPNs();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CSGSubscriptionData [");

        if (this.csgId != null) {
            sb.append("csgId=");
            sb.append(this.csgId.toString());
            sb.append(", ");
        }

        if (this.expirationDate != null) {
            sb.append("expirationDate=");
            sb.append(this.expirationDate.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.lipaAllowedAPNList != null && this.lipaAllowedAPNList.getAPNs()!=null) {
            sb.append("lipaAllowedAPNList=[");
            boolean firstItem = true;
            for (APNImpl be : this.lipaAllowedAPNList.getAPNs()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("] ");
        }

        sb.append("]");

        return sb.toString();
    }

}
