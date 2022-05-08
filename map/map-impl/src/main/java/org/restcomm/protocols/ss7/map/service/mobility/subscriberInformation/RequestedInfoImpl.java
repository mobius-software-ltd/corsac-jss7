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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class RequestedInfoImpl implements RequestedInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull locationInformation;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull subscriberState;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNNull currentLocation;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNDomainTypeImpl requestedDomain;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNNull imei;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNNull msClassmark;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNNull mnpRequestedInfo;

    /**
     *
     */
    public RequestedInfoImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param locationInformation
     * @param subscriberState
     * @param extensionContainer
     * @param currentLocation
     * @param requestedDomain
     * @param imei
     * @param msClassmark
     * @param mnpRequestedInfo
     */
    public RequestedInfoImpl(boolean locationInformation, boolean subscriberState, MAPExtensionContainer extensionContainer,
            boolean currentLocation, DomainType requestedDomain, boolean imei, boolean msClassmark, boolean mnpRequestedInfo) {
        super();
        if(locationInformation)
        	this.locationInformation = new ASNNull();
        
        if(subscriberState)
        	this.subscriberState = new ASNNull();
        
        this.extensionContainer = extensionContainer;
        
        if(currentLocation)
        	this.currentLocation = new ASNNull();
        
        if(requestedDomain!=null)
        	this.requestedDomain = new ASNDomainTypeImpl(requestedDomain);
        	
        if(imei)
        	this.imei = new ASNNull();
        
        if(msClassmark)
        	this.msClassmark = new ASNNull();
        
        if(mnpRequestedInfo)
        	this.mnpRequestedInfo = new ASNNull();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getLocationInformation()
     */
    public boolean getLocationInformation() {
        return this.locationInformation!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getSubscriberState()
     */
    public boolean getSubscriberState() {
        return this.subscriberState!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getCurrentLocation()
     */
    public boolean getCurrentLocation() {
        return this.currentLocation!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getRequestedDomain()
     */
    public DomainType getRequestedDomain() {
    	if(this.requestedDomain==null)
    		return null;
    	
        return this.requestedDomain.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getImei()
     */
    public boolean getImei() {
        return this.imei!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getMsClassmark()
     */
    public boolean getMsClassmark() {
        return this.msClassmark!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getMnpRequestedInfo()
     */
    public boolean getMnpRequestedInfo() {
        return this.mnpRequestedInfo!=null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestedInfo [");

        if (locationInformation!=null) {
            sb.append(", locationInformation");
        }

        if (subscriberState!=null) {
            sb.append(", subscriberState");
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        if (currentLocation!=null) {
            sb.append(", currentLocation");
        }

        if (this.requestedDomain != null) {
            sb.append(", requestedDomain=");
            sb.append(this.requestedDomain.getType());
        }

        if (imei!=null) {
            sb.append(", imei");
        }

        if (msClassmark!=null) {
            sb.append(", msClassmark");
        }

        if (mnpRequestedInfo!=null) {
            sb.append(", mnpRequestedInfo");
        }

        sb.append("]");
        return sb.toString();
    }
}