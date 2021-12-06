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

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GMLCRestriction;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NotificationToMSUser;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ServiceType;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

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
public class ServiceTypeImpl implements ServiceType {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=0)
    private ASNInteger serviceTypeIdentity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNGMLCRestriction gmlcRestriction;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNotificationToMSUser notificationToMSUser;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public ServiceTypeImpl() {
    }

    public ServiceTypeImpl(int serviceTypeIdentity, GMLCRestriction gmlcRestriction, NotificationToMSUser notificationToMSUser,
            MAPExtensionContainer extensionContainer) {
        this.serviceTypeIdentity = new ASNInteger();
        this.serviceTypeIdentity.setValue((long)serviceTypeIdentity & 0x0FFFFFFFFL);
        
        if(gmlcRestriction!=null) {
        	this.gmlcRestriction = new ASNGMLCRestriction();
        	this.gmlcRestriction.setType(gmlcRestriction);
        }
        
        if(notificationToMSUser!=null) {
        	this.notificationToMSUser = new ASNNotificationToMSUser();
        	this.notificationToMSUser.setType(notificationToMSUser);
        }
        
        this.extensionContainer = extensionContainer;
    }

    public int getServiceTypeIdentity() {
    	if(serviceTypeIdentity==null)
    		return 0;
    	
        return this.serviceTypeIdentity.getValue().intValue();
    }

    public GMLCRestriction getGMLCRestriction() {
    	if(this.gmlcRestriction==null)
    		return null;
    	
        return this.gmlcRestriction.getType();
    }

    public NotificationToMSUser getNotificationToMSUser() {
    	if(this.notificationToMSUser==null)
    		return null;
    	
        return this.notificationToMSUser.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServiceType [");

        if(serviceTypeIdentity!=null) {
	        sb.append("serviceTypeIdentity=");
	        sb.append(this.serviceTypeIdentity.getValue());
	        sb.append(", ");
        }
        
        if (this.gmlcRestriction != null) {
            sb.append("gmlcRestriction=");
            sb.append(this.gmlcRestriction.getType());
            sb.append(", ");
        }

        if (this.notificationToMSUser != null) {
            sb.append("notificationToMSUser=");
            sb.append(this.notificationToMSUser.getType());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}
