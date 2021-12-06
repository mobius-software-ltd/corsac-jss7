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

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SMSCSIImpl implements SMSCSI {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private SMSCamelTDPDataListWrapperImpl smsCamelTdpDataList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNInteger camelCapabilityHandling;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNNull notificationToCSE;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNNull csiActive;

    public SMSCSIImpl() {
    }

    public SMSCSIImpl(List<SMSCAMELTDPData> smsCamelTdpDataList, Integer camelCapabilityHandling,
            MAPExtensionContainer extensionContainer, boolean notificationToCSE, boolean csiActive) {
    	
    	if(smsCamelTdpDataList!=null)
    		this.smsCamelTdpDataList =new SMSCamelTDPDataListWrapperImpl(smsCamelTdpDataList);    	
    	
        if(camelCapabilityHandling!=null) {
        	this.camelCapabilityHandling = new ASNInteger();
        	this.camelCapabilityHandling.setValue(camelCapabilityHandling.longValue());
        }
        
        this.extensionContainer = extensionContainer;
        
        if(notificationToCSE)
        	this.notificationToCSE = new ASNNull();
        
        if(csiActive)
        	this.csiActive = new ASNNull();
    }

    public List<SMSCAMELTDPData> getSmsCamelTdpDataList() {
    	if(this.smsCamelTdpDataList==null)
    		return null;
    	
        return this.smsCamelTdpDataList.getSMSCamelTDPData();
    }

    public Integer getCamelCapabilityHandling() {
    	if(this.camelCapabilityHandling==null)
    		return null;
    	
        return this.camelCapabilityHandling.getValue().intValue();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public boolean getNotificationToCSE() {
        return this.notificationToCSE!=null;
    }

    public boolean getCsiActive() {
        return this.csiActive!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SMSCSI [");

        if (this.smsCamelTdpDataList != null && this.smsCamelTdpDataList.getSMSCamelTDPData()!=null) {
            sb.append("smsCamelTdpDataList=[");
            boolean firstItem = true;
            for (SMSCAMELTDPData be : this.smsCamelTdpDataList.getSMSCamelTDPData()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.camelCapabilityHandling != null) {
            sb.append("camelCapabilityHandling=");
            sb.append(this.camelCapabilityHandling.getValue());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.notificationToCSE!=null) {
            sb.append("notificationToCSE ");
            sb.append(", ");
        }

        if (this.csiActive!=null) {
            sb.append("csiActive ");
        }

        sb.append("]");

        return sb.toString();
    }

}
