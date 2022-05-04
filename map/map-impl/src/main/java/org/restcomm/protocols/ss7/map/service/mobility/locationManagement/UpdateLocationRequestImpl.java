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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class UpdateLocationRequestImpl extends MobilityMessageImpl implements UpdateLocationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString mscNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString roamingNumber;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString vlrNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1, defaultImplementation = LMSIImpl.class)
    private LMSI lmsi;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1, defaultImplementation = VLRCapabilityImpl.class)
    private VLRCapability vlrCapability;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1)
    private ASNNull informPreviousNetworkEntity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1)
    private ASNNull csLCSNotSupportedByUE;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress vGmlcAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1, defaultImplementation = ADDInfoImpl.class)
    private ADDInfo addInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=true,index=-1, defaultImplementation = PagingAreaImpl.class)
    private PagingArea pagingArea;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=false,index=-1)
    private ASNNull skipSubscriberDataUpdate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=false,index=-1)
    private ASNNull restorationIndicator;

    public UpdateLocationRequestImpl() {
    }
    
    public UpdateLocationRequestImpl(IMSI imsi, ISDNAddressString mscNumber,
    		ISDNAddressString roamingNumber, ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
    		VLRCapability vlrCapability, boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE,
            GSNAddress vGmlcAddress, ADDInfo addInfo, PagingArea pagingArea, boolean skipSubscriberDataUpdate,
            boolean restorationIndicator) {
        this.imsi = imsi;
        this.mscNumber = mscNumber;
        this.roamingNumber = roamingNumber;
        this.vlrNumber = vlrNumber;
        this.lmsi = lmsi;
        this.extensionContainer = extensionContainer;
        this.vlrCapability = vlrCapability;
        
        if(informPreviousNetworkEntity)
        	this.informPreviousNetworkEntity = new ASNNull();
        
        if(csLCSNotSupportedByUE)
        	this.csLCSNotSupportedByUE = new ASNNull();
        
        this.vGmlcAddress = vGmlcAddress;
        this.addInfo = addInfo;
        this.pagingArea = pagingArea;
        
        if(skipSubscriberDataUpdate)
        	this.skipSubscriberDataUpdate = new ASNNull();
        
        if(restorationIndicator)
        	this.restorationIndicator = new ASNNull();
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.updateLocation_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.updateLocation;
    }

    public IMSI getImsi() {
        return imsi;
    }

    public ISDNAddressString getMscNumber() {
        return mscNumber;
    }

    public ISDNAddressString getRoamingNumber() {
        return roamingNumber;
    }

    public ISDNAddressString getVlrNumber() {
        return vlrNumber;
    }

    public LMSI getLmsi() {
        return lmsi;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public VLRCapability getVlrCapability() {
        return vlrCapability;
    }

    public boolean getInformPreviousNetworkEntity() {
        return informPreviousNetworkEntity!=null;
    }

    public boolean getCsLCSNotSupportedByUE() {
        return csLCSNotSupportedByUE!=null;
    }

    public GSNAddress getVGmlcAddress() {
        return vGmlcAddress;
    }

    public ADDInfo getADDInfo() {
        return addInfo;
    }

    public PagingArea getPagingArea() {
        return pagingArea;
    }

    public boolean getSkipSubscriberDataUpdate() {
        return skipSubscriberDataUpdate!=null;
    }

    public boolean getRestorationIndicator() {
        return restorationIndicator!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateLocationRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi.toString());
            sb.append(", ");
        }
        if (this.mscNumber != null) {
            sb.append("mscNumber=");
            sb.append(mscNumber.toString());
            sb.append(", ");
        }
        if (this.roamingNumber != null) {
            sb.append("roamingNumber=");
            sb.append(roamingNumber.toString());
            sb.append(", ");
        }
        if (this.vlrNumber != null) {
            sb.append("vlrNumber=");
            sb.append(vlrNumber.toString());
            sb.append(", ");
        }
        if (this.lmsi != null) {
            sb.append("lmsi=");
            sb.append(lmsi.toString());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }
        if (this.vlrCapability != null) {
            sb.append("vlrCapability=");
            sb.append(vlrCapability.toString());
            sb.append(", ");
        }
        if (this.informPreviousNetworkEntity!=null) {
            sb.append("informPreviousNetworkEntity, ");
        }
        if (this.csLCSNotSupportedByUE!=null) {
            sb.append("csLCSNotSupportedByUE, ");
        }
        if (this.vGmlcAddress != null) {
            sb.append("vGmlcAddress=");
            sb.append(vGmlcAddress.toString());
            sb.append(", ");
        }
        if (this.addInfo != null) {
            sb.append("addInfo=");
            sb.append(addInfo.toString());
            sb.append(", ");
        }
        if (this.pagingArea != null) {
            sb.append("pagingArea=");
            sb.append(pagingArea.toString());
            sb.append(", ");
        }
        if (this.skipSubscriberDataUpdate!=null) {
            sb.append("skipSubscriberDataUpdate, ");
        }
        if (this.restorationIndicator!=null) {
            sb.append("restorationIndicator, ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(imsi==null)
			throw new ASNParsingComponentException("imsi should be set for update location request", ASNParsingComponentExceptionReason.MistypedRootParameter);			

		if(mscNumber==null && roamingNumber==null)
			throw new ASNParsingComponentException("either msc number or roaming number should be set for update location request", ASNParsingComponentExceptionReason.MistypedRootParameter);			

		if(vlrNumber==null)
			throw new ASNParsingComponentException("vlr number should be set for update location request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
