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
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNWrappedTag
public class CancelLocationRequestImplV1 extends MobilityMessageImpl implements CancelLocationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = IMSIImpl.class)
	private IMSI imsi;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation=IMSIWithLMSIImpl.class)
	private IMSIWithLMSI imsiWithLmsi;
    
	public CancelLocationRequestImplV1() {
    }

    public CancelLocationRequestImplV1(IMSI imsi, IMSIWithLMSI imsiWithLmsi) {
        super();
        if(imsi!=null)
        	this.imsi = imsi;
        else if(imsiWithLmsi!=null)
        	this.imsiWithLmsi = imsiWithLmsi;        
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
    	return null;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return null;
    }

    @Override
    public TypeOfUpdate getTypeOfUpdate() {
    	return null;
    }

    @Override
    public boolean getMtrfSupportedAndAuthorized() {
        return false;
    }

    @Override
    public boolean getMtrfSupportedAndNotAuthorized() {
        return false;
    }

    @Override
    public ISDNAddressString getNewMSCNumber() {
        return null;
    }

    @Override
    public ISDNAddressString getNewVLRNumber() {
        return null;
    }

    @Override
    public LMSI getNewLmsi() {
        return null;
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
