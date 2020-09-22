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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LocationInformationEPSImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private EUtranCgiImpl eUtranCellGlobalIdentity = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private TAIdImpl trackingAreaIdentity = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private GeographicalInformationImpl geographicalInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private GeodeticInformationImpl geodeticInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNNull currentLocationRetrieved = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNInteger ageOfLocationInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private DiameterIdentityImpl mmeName = null;

    /**
     *
     */
    public LocationInformationEPSImpl() {
    }

    /**
     * @param eUtranCellGlobalIdentity
     * @param trackingAreaIdentity
     * @param extensionContainer
     * @param geographicalInformation
     * @param geodeticInformation
     * @param currentLocationRetrieved
     * @param ageOfLocationInformation
     * @param mmeName
     */
    public LocationInformationEPSImpl(EUtranCgiImpl eUtranCellGlobalIdentity, TAIdImpl trackingAreaIdentity,
            MAPExtensionContainerImpl extensionContainer, GeographicalInformationImpl geographicalInformation,
            GeodeticInformationImpl geodeticInformation, boolean currentLocationRetrieved, Integer ageOfLocationInformation,
            DiameterIdentityImpl mmeName) {
        this.eUtranCellGlobalIdentity = eUtranCellGlobalIdentity;
        this.trackingAreaIdentity = trackingAreaIdentity;
        this.extensionContainer = extensionContainer;
        this.geographicalInformation = geographicalInformation;
        this.geodeticInformation = geodeticInformation;
        
        if(currentLocationRetrieved)
        	this.currentLocationRetrieved = new ASNNull();
        
        if(ageOfLocationInformation!=null) {
        	this.ageOfLocationInformation = new ASNInteger();
        	this.ageOfLocationInformation.setValue(ageOfLocationInformation.longValue());
        }
        
        this.mmeName = mmeName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationEPS#getEUtranCellGlobalIdentity()
     */
    public EUtranCgiImpl getEUtranCellGlobalIdentity() {
        return this.eUtranCellGlobalIdentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getTrackingAreaIdentity()
     */
    public TAIdImpl getTrackingAreaIdentity() {
        return this.trackingAreaIdentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getExtensionContainer()
     */
    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationEPS#getGeographicalInformation()
     */
    public GeographicalInformationImpl getGeographicalInformation() {
        return this.geographicalInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getGeodeticInformation()
     */
    public GeodeticInformationImpl getGeodeticInformation() {
        return this.geodeticInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationEPS#getCurrentLocationRetrieved()
     */
    public boolean getCurrentLocationRetrieved() {
        return this.currentLocationRetrieved!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationEPS#getAgeOfLocationInformation()
     */
    public Integer getAgeOfLocationInformation() {
    	if(this.ageOfLocationInformation==null)
    		return null;
    	
        return this.ageOfLocationInformation.getValue().intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getMmeName()
     */
    public DiameterIdentityImpl getMmeName() {
        return this.mmeName;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationInformationEPS [");

        if (this.eUtranCellGlobalIdentity != null) {
            sb.append("eUtranCellGlobalIdentity=");
            sb.append(this.eUtranCellGlobalIdentity);
        }

        if (this.trackingAreaIdentity != null) {
            sb.append(", trackingAreaIdentity=");
            sb.append(this.trackingAreaIdentity);
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        if (this.geographicalInformation != null) {
            sb.append(", geographicalInformation=");
            sb.append(this.geographicalInformation);
        }

        if (this.geodeticInformation != null) {
            sb.append(", geodeticInformation=");
            sb.append(this.geodeticInformation);
        }

        if (currentLocationRetrieved!=null) {
            sb.append(", currentLocationRetrieved");
        }

        if (this.ageOfLocationInformation != null) {
            sb.append(", ageOfLocationInformation=");
            sb.append(this.ageOfLocationInformation.getValue());
        }

        if (this.mmeName != null) {
            sb.append(", mmeName=");
            sb.append(this.mmeName);
        }

        sb.append("]");
        return sb.toString();
    }
}
