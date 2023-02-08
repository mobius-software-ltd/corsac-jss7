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

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.TMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.TMSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendIdentificationRequestImplV3 extends MobilityMessageImpl implements SendIdentificationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = TMSIImpl.class)
	private TMSI tmsi;
    
	private ASNInteger numberOfRequestedVectors;
    private ASNNull segmentationProhibited;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString mscNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = LAIFixedLengthImpl.class)
    private LAIFixedLength previousLAI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNInteger hopCounter;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull mtRoamingForwardingSupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString newVLRNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = LMSIImpl.class)
    private LMSI newLmsi;
    
    public SendIdentificationRequestImplV3() {
    }
    
    public SendIdentificationRequestImplV3(TMSI tmsi, Integer numberOfRequestedVectors, boolean segmentationProhibited,
    		MAPExtensionContainer extensionContainer, ISDNAddressString mscNumber, LAIFixedLength previousLAI,
            Integer hopCounter, boolean mtRoamingForwardingSupported, ISDNAddressString newVLRNumber, LMSI newLmsi) {
        super();
        this.tmsi = tmsi;
        
        if(numberOfRequestedVectors!=null)
        	this.numberOfRequestedVectors = new ASNInteger(numberOfRequestedVectors,"NumberOfRequestedVectors",1,5,false);
        	
        if(segmentationProhibited)
        	this.segmentationProhibited = new ASNNull();
        
        this.extensionContainer = extensionContainer;
        this.mscNumber = mscNumber;
        this.previousLAI = previousLAI;
        
        if(hopCounter!=null)
        	this.hopCounter = new ASNInteger(hopCounter,"HopCounter",0,3,false);
        	
        if(mtRoamingForwardingSupported)
        	this.mtRoamingForwardingSupported = new ASNNull();
        
        this.newVLRNumber = newVLRNumber;
        this.newLmsi = newLmsi;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.sendIdentification_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.sendIdentification;
    }

    @Override
    public TMSI getTmsi() {
        return this.tmsi;
    }

    @Override
    public Integer getNumberOfRequestedVectors() {
    	if(this.numberOfRequestedVectors==null)
    		return null;
    	
        return this.numberOfRequestedVectors.getValue().intValue();
    }

    @Override
    public boolean getSegmentationProhibited() {
        return this.segmentationProhibited!=null;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public ISDNAddressString getMscNumber() {
        return this.mscNumber;
    }

    @Override
    public LAIFixedLength getPreviousLAI() {
        return this.previousLAI;
    }

    @Override
    public Integer getHopCounter() {
    	if(this.hopCounter==null)
    		return null;
    	
        return this.hopCounter.getIntValue();
    }

    @Override
    public boolean getMtRoamingForwardingSupported() {
        return this.mtRoamingForwardingSupported!=null;
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
        sb.append("SendIdentificationRequest [");

        if (this.tmsi != null) {
            sb.append("tmsi=");
            sb.append(tmsi.toString());
            sb.append(", ");
        }

        if (this.numberOfRequestedVectors != null) {
            sb.append("numberOfRequestedVectors=");
            sb.append(numberOfRequestedVectors.getValue());
            sb.append(", ");
        }

        if (this.segmentationProhibited!=null) {
            sb.append("segmentationProhibited, ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }
        if (this.mscNumber != null) {
            sb.append("mscNumber=");
            sb.append(mscNumber.toString());
            sb.append(", ");
        }
        if (this.previousLAI != null) {
            sb.append("previousLAI=");
            sb.append(previousLAI.toString());
            sb.append(", ");
        }
        if (this.hopCounter != null) {
            sb.append("hopCounter=");
            sb.append(hopCounter.getValue());
            sb.append(", ");
        }

        if (this.mtRoamingForwardingSupported!=null) {
            sb.append("mtRoamingForwardingSupported, ");
        }

        if (this.newVLRNumber != null) {
            sb.append("newVLRNumber=");
            sb.append(newVLRNumber.toString());
            sb.append(", ");
        }

        if (this.newLmsi != null) {
            sb.append("lmsi=");
            sb.append(newLmsi.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(tmsi==null)
			throw new ASNParsingComponentException("tmsi should be set for send indetification request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}