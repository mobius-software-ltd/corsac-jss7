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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationEPS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TAId;
import org.restcomm.protocols.ss7.commonapp.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LocationInformationEPSImpl implements LocationInformationEPS {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = EUtranCgiImpl.class)
    private EUtranCgi eUtranCellGlobalIdentity = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = TAIdImpl.class)
    private TAId trackingAreaIdentity = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = GeographicalInformationImpl.class)
    private GeographicalInformation geographicalInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = GeodeticInformationImpl.class)
    private GeodeticInformation geodeticInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNNull currentLocationRetrieved = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNInteger ageOfLocationInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1, defaultImplementation = DiameterIdentityImpl.class)
    private DiameterIdentity mmeName = null;

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
    public LocationInformationEPSImpl(EUtranCgi eUtranCellGlobalIdentity, TAId trackingAreaIdentity,
            MAPExtensionContainer extensionContainer, GeographicalInformation geographicalInformation,
            GeodeticInformation geodeticInformation, boolean currentLocationRetrieved, Integer ageOfLocationInformation,
            DiameterIdentity mmeName) {
        this.eUtranCellGlobalIdentity = eUtranCellGlobalIdentity;
        this.trackingAreaIdentity = trackingAreaIdentity;
        this.extensionContainer = extensionContainer;
        this.geographicalInformation = geographicalInformation;
        this.geodeticInformation = geodeticInformation;
        
        if(currentLocationRetrieved)
        	this.currentLocationRetrieved = new ASNNull();
        
        if(ageOfLocationInformation!=null)
        	this.ageOfLocationInformation = new ASNInteger(ageOfLocationInformation,"AgeOfLocationInformation",0,Integer.MAX_VALUE,false);
        	
        this.mmeName = mmeName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationEPS#getEUtranCellGlobalIdentity()
     */
    public EUtranCgi getEUtranCellGlobalIdentity() {
        return this.eUtranCellGlobalIdentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getTrackingAreaIdentity()
     */
    public TAId getTrackingAreaIdentity() {
        return this.trackingAreaIdentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationEPS#getGeographicalInformation()
     */
    public GeographicalInformation getGeographicalInformation() {
        return this.geographicalInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getGeodeticInformation()
     */
    public GeodeticInformation getGeodeticInformation() {
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
    	
        return this.ageOfLocationInformation.getIntValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getMmeName()
     */
    public DiameterIdentity getMmeName() {
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
