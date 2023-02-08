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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
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
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,lengthIndefinite=false)
public class CancelLocationRequestImplV3 extends MobilityMessageImpl implements CancelLocationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = IMSIImpl.class)
	private IMSI imsi;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=0, defaultImplementation = IMSIWithLMSIImpl.class)
	private IMSIWithLMSI imsiWithLmsi;
	
    private ASNCancellationTypeImpl cancellationType;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNTypeOfUpdateImpl typeOfUpdate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull mtrfSupportedAndAuthorized;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull mtrfSupportedAndNotAuthorized;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString newMSCNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString newVLRNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = LMSIImpl.class)
    private LMSI newLmsi;
    
    public CancelLocationRequestImplV3() {    
    }

    public CancelLocationRequestImplV3(IMSI imsi, IMSIWithLMSI imsiWithLmsi, CancellationType cancellationType,
    		MAPExtensionContainer extensionContainer, TypeOfUpdate typeOfUpdate, boolean mtrfSupportedAndAuthorized,
            boolean mtrfSupportedAndNotAuthorized, ISDNAddressString newMSCNumber, ISDNAddressString newVLRNumber,
            LMSI newLmsi) {
        super();
        if(imsi!=null)
        	this.imsi = imsi;
        else if(imsiWithLmsi!=null)
        	this.imsiWithLmsi = imsiWithLmsi;
        
        if(cancellationType!=null)
        	this.cancellationType = new ASNCancellationTypeImpl(cancellationType);
        	
        this.extensionContainer = extensionContainer;
        
        if(typeOfUpdate!=null)
        	this.typeOfUpdate = new ASNTypeOfUpdateImpl(typeOfUpdate);
        	
        if(mtrfSupportedAndAuthorized)
        	this.mtrfSupportedAndAuthorized = new ASNNull();
        
        if(mtrfSupportedAndNotAuthorized)
        	this.mtrfSupportedAndNotAuthorized = new ASNNull();
        
        this.newMSCNumber = newMSCNumber;
        this.newVLRNumber = newVLRNumber;
        this.newLmsi = newLmsi;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.cancelLocation_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.cancelLocation;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public IMSIWithLMSI getImsiWithLmsi() {
        return this.imsiWithLmsi;
    }

    @Override
    public CancellationType getCancellationType() {
    	if(this.cancellationType==null)
    		return null;
    	
        return this.cancellationType.getType();
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public TypeOfUpdate getTypeOfUpdate() {
    	if(this.typeOfUpdate==null)
    		return null;
    	
        return this.typeOfUpdate.getType();
    }

    @Override
    public boolean getMtrfSupportedAndAuthorized() {
        return this.mtrfSupportedAndAuthorized!=null;
    }

    @Override
    public boolean getMtrfSupportedAndNotAuthorized() {
        return this.mtrfSupportedAndNotAuthorized!=null;
    }

    @Override
    public ISDNAddressString getNewMSCNumber() {
        return this.newMSCNumber;
    }

    @Override
    public ISDNAddressString getNewVLRNumber() {
        return this.newVLRNumber;
    }

    @Override
    public LMSI getNewLmsi() {
        return this.newLmsi;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CancelLocationRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi.toString());
            sb.append(", ");
        }
        if (this.imsiWithLmsi != null) {
            sb.append("imsiWithLmsi=");
            sb.append(imsiWithLmsi.toString());
            sb.append(", ");
        }
        if (this.cancellationType != null) {
            sb.append("cancellationType=");
            sb.append(cancellationType.getType());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }
        if (this.typeOfUpdate != null) {
            sb.append("typeOfUpdate=");
            sb.append(typeOfUpdate.getType());
            sb.append(", ");
        }
        if (this.mtrfSupportedAndAuthorized!=null) {
            sb.append("mtrfSupportedAndAuthorized, ");
        }
        if (this.mtrfSupportedAndNotAuthorized!=null) {
            sb.append("mtrfSupportedAndNotAuthorized, ");
        }
        if (this.newMSCNumber != null) {
            sb.append("newMSCNumber=");
            sb.append(newMSCNumber.toString());
            sb.append(", ");
        }
        if (this.newVLRNumber != null) {
            sb.append("newVLRNumber=");
            sb.append(newVLRNumber.toString());
            sb.append(", ");
        }
        if (this.newLmsi != null) {
            sb.append("newLmsi=");
            sb.append(newLmsi.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(imsi==null && imsiWithLmsi==null)
			throw new ASNParsingComponentException("either imsi or imsi with lmsi should be set for cancel location request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(imsi!=null && imsiWithLmsi!=null)
			throw new ASNParsingComponentException("either imsi or imsi with lmsi should be set for cancel location request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
