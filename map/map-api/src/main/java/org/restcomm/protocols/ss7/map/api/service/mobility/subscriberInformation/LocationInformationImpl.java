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

import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentityImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
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
public class LocationInformationImpl {
	private ASNInteger ageOfLocationInformation;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private GeographicalInformationImpl geographicalInformation;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ISDNAddressStringImpl vlrNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private LocationNumberMapImpl locationNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl cellGlobalIdOrServiceAreaIdOrLAIWrapped;
    
    @ASNChoise
    private CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private LSAIdentityImpl selectedLSAId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ISDNAddressStringImpl mscNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private GeodeticInformationImpl geodeticInformation;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNNull currentLocationRetrieved;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private ASNNull saiPresent;
   
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1)
    private LocationInformationEPSImpl locationInformationEPS;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)
    private UserCSGInformationImpl userCSGInformation;

    public LocationInformationImpl() {
    }

    public LocationInformationImpl(Integer ageOfLocationInformation, GeographicalInformationImpl geographicalInformation,
            ISDNAddressStringImpl vlrNumber, LocationNumberMapImpl locationNumber,
            CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI, MAPExtensionContainerImpl extensionContainer,
            LSAIdentityImpl selectedLSAId, ISDNAddressStringImpl mscNumber, GeodeticInformationImpl geodeticInformation,
            boolean currentLocationRetrieved, boolean saiPresent, LocationInformationEPSImpl locationInformationEPS,
            UserCSGInformationImpl userCSGInformation) {
        if(ageOfLocationInformation!=null) {
        	this.ageOfLocationInformation = new ASNInteger();
        	this.ageOfLocationInformation.setValue(ageOfLocationInformation.longValue());
        }
        
        this.geographicalInformation = geographicalInformation;
        this.vlrNumber = vlrNumber;
        this.locationNumber = locationNumber;
        
        if(cellGlobalIdOrServiceAreaIdOrLAI!=null)
        this.cellGlobalIdOrServiceAreaIdOrLAIWrapped = new CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl(cellGlobalIdOrServiceAreaIdOrLAI);
        this.extensionContainer = extensionContainer;
        this.selectedLSAId = selectedLSAId;
        this.mscNumber = mscNumber;
        this.geodeticInformation = geodeticInformation;
        
        if(currentLocationRetrieved)
        	this.currentLocationRetrieved = new ASNNull();
        
        if(saiPresent)
        	this.saiPresent = new ASNNull();
        
        this.locationInformationEPS = locationInformationEPS;
        this.userCSGInformation = userCSGInformation;
    }

    public Integer getAgeOfLocationInformation() {
    	if(this.ageOfLocationInformation==null)
    		return null;
    	
        return this.ageOfLocationInformation.getValue().intValue();
    }

    public GeographicalInformationImpl getGeographicalInformation() {
        return this.geographicalInformation;
    }

    public ISDNAddressStringImpl getVlrNumber() {
        return this.vlrNumber;
    }

    public LocationNumberMapImpl getLocationNumber() {
        return locationNumber;
    }

    public CellGlobalIdOrServiceAreaIdOrLAIImpl getCellGlobalIdOrServiceAreaIdOrLAI() {
    	if(cellGlobalIdOrServiceAreaIdOrLAI!=null)
    		return cellGlobalIdOrServiceAreaIdOrLAI;
    	
    	if(cellGlobalIdOrServiceAreaIdOrLAIWrapped!=null)
    		return cellGlobalIdOrServiceAreaIdOrLAIWrapped.getCellGlobalIdOrServiceAreaIdOrLAI();
    	
    	return null;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    public LSAIdentityImpl getSelectedLSAId() {
        return selectedLSAId;
    }

    public ISDNAddressStringImpl getMscNumber() {
        return mscNumber;
    }

    public GeodeticInformationImpl getGeodeticInformation() {
        return geodeticInformation;
    }

    public boolean getCurrentLocationRetrieved() {
        return currentLocationRetrieved!=null;
    }

    public boolean getSaiPresent() {
        return saiPresent!=null;
    }

    public LocationInformationEPSImpl getLocationInformationEPS() {
        return locationInformationEPS;
    }

    public UserCSGInformationImpl getUserCSGInformation() {
        return userCSGInformation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationInformation [");

        if (this.ageOfLocationInformation != null) {
            sb.append(", ageOfLocationInformation=");
            sb.append(this.ageOfLocationInformation.getValue());
        }
        if (this.geographicalInformation != null) {
            sb.append(", geographicalInformation=");
            sb.append(this.geographicalInformation);
        }
        if (this.vlrNumber != null) {
            sb.append(", vlrNumber=");
            sb.append(this.vlrNumber.toString());
        }
        if (this.locationNumber != null) {
            sb.append(", locationNumber=");
            sb.append(this.locationNumber.toString());
        }
        if (this.cellGlobalIdOrServiceAreaIdOrLAI != null) {
            sb.append(", cellGlobalIdOrServiceAreaIdOrLAI=[");
            sb.append(this.cellGlobalIdOrServiceAreaIdOrLAI.toString());
            sb.append("]");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.selectedLSAId != null) {
            sb.append(", selectedLSAId=");
            sb.append(this.selectedLSAId.toString());
        }
        if (this.mscNumber != null) {
            sb.append(", mscNumber=");
            sb.append(this.mscNumber.toString());
        }
        if (this.geodeticInformation != null) {
            sb.append(", geodeticInformation=");
            sb.append(this.geodeticInformation.toString());
        }
        if (this.currentLocationRetrieved!=null) {
            sb.append(", currentLocationRetrieved");
        }
        if (this.saiPresent!=null) {
            sb.append(", saiPresent");
        }
        if (this.locationInformationEPS != null) {
            sb.append(", locationInformationEPS=");
            sb.append(this.locationInformationEPS.toString());
        }
        if (this.userCSGInformation != null) {
            sb.append(", userCSGInformation=");
            sb.append(this.userCSGInformation.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
