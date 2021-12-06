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
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

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
public class TCSIImpl implements TCSI {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=0)
    public TBcsmCamelTDPDataWrapperImpl tBcsmCamelTDPDataList;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	public MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    public ASNInteger camelCapabilityHandling;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    public ASNNull notificationToCSE;
        
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    public ASNNull csiActive;

    public TCSIImpl() {       
    }

    public TCSIImpl(List<TBcsmCamelTDPData> tBcsmCamelTDPDataList, MAPExtensionContainer extensionContainer,
            Integer camelCapabilityHandling, boolean notificationToCSE, boolean csiActive) {        
        this.tBcsmCamelTDPDataList = new TBcsmCamelTDPDataWrapperImpl(tBcsmCamelTDPDataList);        
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

    public List<TBcsmCamelTDPData> getTBcsmCamelTDPDataList() {
    	if(tBcsmCamelTDPDataList==null)
    		return null;
    	
        return tBcsmCamelTDPDataList.getTBcsmCamelTDPDataList();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public Integer getCamelCapabilityHandling() {
    	if(this.camelCapabilityHandling==null)
    		return null;
    	
        return camelCapabilityHandling.getValue().intValue();
    }

    public boolean getNotificationToCSE() {
    	if(notificationToCSE!=null)
    		return true;
    	
        return false;
    }

    public boolean getCsiActive() {
    	if(csiActive!=null)
    		return true;
    	
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TCSI");
        sb.append(" [");

        if (this.tBcsmCamelTDPDataList != null && this.tBcsmCamelTDPDataList.getTBcsmCamelTDPDataList()!=null) {
            sb.append("tBcsmCamelTDPDataList=[");
            for (TBcsmCamelTDPData be : this.tBcsmCamelTDPDataList.getTBcsmCamelTDPDataList()) {
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
