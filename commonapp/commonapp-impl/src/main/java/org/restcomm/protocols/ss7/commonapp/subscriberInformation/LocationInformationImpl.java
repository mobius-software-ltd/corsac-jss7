/*
 * Mobius Software LTD
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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationEPS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationNumberMap;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.UserCSGInformation;
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
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LocationInformationImpl implements LocationInformation {
	private ASNInteger ageOfLocationInformation;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = GeographicalInformationImpl.class)
    private GeographicalInformation geographicalInformation;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString vlrNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = LocationNumberMapImpl.class)
    private LocationNumberMap locationNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private CellGlobalIdOrServiceAreaIdOrLAIWrapperImpl cellGlobalIdOrServiceAreaIdOrLAIWrapped;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
  	private CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl cellGlobalIdOrServiceAreaIdFixedLength = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = LSAIdentityImpl.class)
    private LSAIdentity selectedLSAId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString mscNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1, defaultImplementation = GeodeticInformationImpl.class)
    private GeodeticInformation geodeticInformation;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNNull currentLocationRetrieved;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private ASNNull saiPresent;
   
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1, defaultImplementation = LocationInformationEPSImpl.class)
    private LocationInformationEPS locationInformationEPS;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1, defaultImplementation = UserCSGInformationImpl.class)
    private UserCSGInformation userCSGInformation;

    public LocationInformationImpl() {
    }

    public LocationInformationImpl(Integer ageOfLocationInformation, GeographicalInformation geographicalInformation,
            ISDNAddressString vlrNumber, LocationNumberMap locationNumber,
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, MAPExtensionContainer extensionContainer,
            LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation,
            boolean currentLocationRetrieved, boolean saiPresent, LocationInformationEPS locationInformationEPS,
            UserCSGInformation userCSGInformation) {
        if(ageOfLocationInformation!=null)
        	this.ageOfLocationInformation = new ASNInteger(ageOfLocationInformation,"AgeOfLocationInformation",0,Integer.MAX_VALUE,false);
        	
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
    	
        return this.ageOfLocationInformation.getIntValue();
    }

    public GeographicalInformation getGeographicalInformation() {
        return this.geographicalInformation;
    }

    public ISDNAddressString getVlrNumber() {
        return this.vlrNumber;
    }

    public LocationNumberMap getLocationNumber() {
        return locationNumber;
    }

    public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
    	if(this.cellGlobalIdOrServiceAreaIdFixedLength!=null)
    		return cellGlobalIdOrServiceAreaIdFixedLength.getCellGlobalIdOrServiceAreaIdOrLAI();    		
    	else if(cellGlobalIdOrServiceAreaIdOrLAIWrapped!=null)
    		return cellGlobalIdOrServiceAreaIdOrLAIWrapped.getCellGlobalIdOrServiceAreaIdOrLAI();
    	
    	return null;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public LSAIdentity getSelectedLSAId() {
        return selectedLSAId;
    }

    public ISDNAddressString getMscNumber() {
        return mscNumber;
    }

    public GeodeticInformation getGeodeticInformation() {
        return geodeticInformation;
    }

    public boolean getCurrentLocationRetrieved() {
        return currentLocationRetrieved!=null;
    }

    public boolean getSaiPresent() {
        return saiPresent!=null;
    }

    public LocationInformationEPS getLocationInformationEPS() {
        return locationInformationEPS;
    }

    public UserCSGInformation getUserCSGInformation() {
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

        CellGlobalIdOrServiceAreaIdOrLAI result=getCellGlobalIdOrServiceAreaIdOrLAI();
        if (result!=null) {
        	sb.append("cellGlobalIdOrServiceAreaIdOrLAI=");
            sb.append(result);
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
