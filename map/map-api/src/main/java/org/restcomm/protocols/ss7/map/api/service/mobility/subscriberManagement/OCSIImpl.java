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

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class OCSIImpl {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=0)
    private OBcsmCamelTDPDataWrapperImpl oBcsmCamelTDPDataList;
    
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNInteger camelCapabilityHandling;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull notificationToCSE;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull csiActive;

    public OCSIImpl() {
    }

    public OCSIImpl(List<OBcsmCamelTDPDataImpl> oBcsmCamelTDPDataList, MAPExtensionContainerImpl extensionContainer,
            Integer camelCapabilityHandling, boolean notificationToCSE, boolean csiActive) {
    	if(oBcsmCamelTDPDataList!=null)
    		this.oBcsmCamelTDPDataList = new OBcsmCamelTDPDataWrapperImpl(oBcsmCamelTDPDataList);
    	
        this.extensionContainer = extensionContainer;
        
        if(camelCapabilityHandling!=null) {
        	this.camelCapabilityHandling = new ASNInteger();
        	this.camelCapabilityHandling.setValue(camelCapabilityHandling.longValue());
        }
        
        if(notificationToCSE)
        	this.notificationToCSE = new ASNNull();
        
        if(csiActive)
        	this.csiActive = new ASNNull();
    }

    public List<OBcsmCamelTDPDataImpl> getOBcsmCamelTDPDataList() {
    	if(oBcsmCamelTDPDataList==null)
    		return null;
    	
        return oBcsmCamelTDPDataList.getOBcsmCamelTDPDataList();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    public Integer getCamelCapabilityHandling() {
    	if(camelCapabilityHandling==null)
    		return null;
    	
        return camelCapabilityHandling.getValue().intValue();
    }

    public boolean getNotificationToCSE() {
        return notificationToCSE!=null;
    }

    public boolean getCsiActive() {
        return csiActive!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OCSI");
        sb.append(" [");

        if (this.oBcsmCamelTDPDataList != null && this.oBcsmCamelTDPDataList.getOBcsmCamelTDPDataList()!=null) {
            sb.append("oBcsmCamelTDPDataList=[");
            for (OBcsmCamelTDPDataImpl be : this.oBcsmCamelTDPDataList.getOBcsmCamelTDPDataList()) {
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.camelCapabilityHandling != null) {
            sb.append(", camelCapabilityHandling=");
            sb.append(this.camelCapabilityHandling);
        }
        if (this.notificationToCSE!=null) {
            sb.append(", notificationToCSE");
        }
        if (this.csiActive!=null) {
            sb.append(", csiActive");
        }

        sb.append("]");

        return sb.toString();
    }

}
