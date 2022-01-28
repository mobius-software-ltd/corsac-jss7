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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;

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
public class LocationInformationGPRSImpl implements LocationInformationGPRS {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl cellGlobalIdOrServiceAreaIdOrLAIWrapped = null;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
	private CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl cellGlobalIdOrServiceAreaIdFixedLength = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = RAIdentityImpl.class)
    private RAIdentity routeingAreaIdentity = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = GeographicalInformationImpl.class)
    private GeographicalInformation geographicalInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString sgsnNumber = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = LSAIdentityImpl.class)
    private LSAIdentity selectedLSAIdentity = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNNull saiPresent = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1, defaultImplementation = GeodeticInformationImpl.class)
    private GeodeticInformation geodeticInformation = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNNull currentLocationRetrieved = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private ASNInteger ageOfLocationInformation = null;

    /**
     *
     */
    public LocationInformationGPRSImpl() {        
    }

    /**
     * @param cellGlobalIdOrServiceAreaIdOrLAI
     * @param routeingAreaIdentity
     * @param geographicalInformation
     * @param sgsnNumber
     * @param selectedLSAIdentity
     * @param extensionContainer
     * @param saiPresent
     * @param geodeticInformation
     * @param currentLocationRetrieved
     * @param ageOfLocationInformation
     */
    public LocationInformationGPRSImpl(CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
    		RAIdentity routeingAreaIdentity, GeographicalInformation geographicalInformation, ISDNAddressString sgsnNumber,
    		LSAIdentity selectedLSAIdentity, MAPExtensionContainer extensionContainer, boolean saiPresent,
    		GeodeticInformation geodeticInformation, boolean currentLocationRetrieved, Integer ageOfLocationInformation) {
        
    	if(cellGlobalIdOrServiceAreaIdOrLAI!=null)
        	this.cellGlobalIdOrServiceAreaIdOrLAIWrapped = new CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl(cellGlobalIdOrServiceAreaIdOrLAI);
        
        this.routeingAreaIdentity = routeingAreaIdentity;
        this.geographicalInformation = geographicalInformation;
        this.sgsnNumber = sgsnNumber;
        this.selectedLSAIdentity = selectedLSAIdentity;
        this.extensionContainer = extensionContainer;
        
        if(saiPresent)
        	this.saiPresent = new ASNNull();
        
        this.geodeticInformation = geodeticInformation;
        
        if(currentLocationRetrieved)
        	this.currentLocationRetrieved = new ASNNull();
        
        if(ageOfLocationInformation!=null)
        	this.ageOfLocationInformation = new ASNInteger(ageOfLocationInformation);        	
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationGPRS#getCellGlobalIdOrServiceAreaIdOrLAI()
     */
    public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
    	if(this.cellGlobalIdOrServiceAreaIdFixedLength!=null)
    		return cellGlobalIdOrServiceAreaIdFixedLength.getCellGlobalIdOrServiceAreaIdOrLAI();    		
    	else if(this.cellGlobalIdOrServiceAreaIdOrLAIWrapped!=null)
    		return this.cellGlobalIdOrServiceAreaIdOrLAIWrapped.getCellGlobalIdOrServiceAreaIdOrLAI();
    	
    	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#getRouteingAreaIdentity()
     */
    public RAIdentity getRouteingAreaIdentity() {
        return this.routeingAreaIdentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationGPRS#getGeographicalInformation()
     */
    public GeographicalInformation getGeographicalInformation() {
        return this.geographicalInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#getSGSNNumber()
     */
    public ISDNAddressString getSGSNNumber() {
        return this.sgsnNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#getLSAIdentity()
     */
    public LSAIdentity getLSAIdentity() {
        return this.selectedLSAIdentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#isSaiPresent()
     */
    public boolean isSaiPresent() {
        return this.saiPresent!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#getGeodeticInformation()
     */
    public GeodeticInformation getGeodeticInformation() {
        return this.geodeticInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationGPRS#isCurrentLocationRetrieved()
     */
    public boolean isCurrentLocationRetrieved() {
        return this.currentLocationRetrieved!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationGPRS#getAgeOfLocationInformation()
     */
    public Integer getAgeOfLocationInformation() {
    	if(this.ageOfLocationInformation==null)
    		return null;
    	
        return this.ageOfLocationInformation.getIntValue();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationInformationGPRS [");

        CellGlobalIdOrServiceAreaIdOrLAI result=getCellGlobalIdOrServiceAreaIdOrLAI();
        if (result!=null) {
        	sb.append("cellGlobalIdOrServiceAreaIdOrLAI=");
            sb.append(result);
        }

        if (this.routeingAreaIdentity != null) {
            sb.append(", routeingAreaIdentity=");
            sb.append(this.routeingAreaIdentity);
        }

        if (this.geographicalInformation != null) {
            sb.append(", geographicalInformation=");
            sb.append(this.geographicalInformation);
        }

        if (this.sgsnNumber != null) {
            sb.append(", sgsnNumber=");
            sb.append(this.sgsnNumber);
        }

        if (this.selectedLSAIdentity != null) {
            sb.append(", selectedLSAIdentity=");
            sb.append(this.selectedLSAIdentity);
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        if (saiPresent!=null) {
            sb.append(", saiPresent");
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

        sb.append("]");
        return sb.toString();
    }
}
