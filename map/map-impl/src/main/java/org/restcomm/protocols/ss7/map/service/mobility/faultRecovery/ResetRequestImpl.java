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

package org.restcomm.protocols.ss7.map.service.mobility.faultRecovery;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIListWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.restcomm.protocols.ss7.map.primitives.ASNNetworkResourceImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ResetRequestImpl extends MobilityMessageImpl implements ResetRequest {
	private static final long serialVersionUID = 1L;

	private ASNNetworkResourceImpl networkResource;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString hlrNumber;
    
	private IMSIListWrapperImpl hlrList;

    public ResetRequestImpl() {
    }

    public ResetRequestImpl(NetworkResource networkResource, ISDNAddressString hlrNumber, List<IMSI> hlrList) {
        if(networkResource!=null)
        	this.networkResource = new ASNNetworkResourceImpl(networkResource);
        	
        this.hlrNumber = hlrNumber;
        
        if(hlrList!=null)
        	this.hlrList = new IMSIListWrapperImpl(hlrList);
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.reset_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.reset;
    }

    @Override
    public NetworkResource getNetworkResource() {
    	if(networkResource==null)
    		return null;
    	
        return networkResource.getType();
    }

    @Override
    public ISDNAddressString getHlrNumber() {
        return hlrNumber;
    }

    @Override
    public List<IMSI> getHlrList() {
    	if(hlrList==null)
    		return null;
    	
        return hlrList.getIMSIs();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResetRequest [");

        if (this.networkResource != null) {
            sb.append("networkResource=");
            sb.append(this.networkResource.getType());
            sb.append(", ");
        }

        if (this.hlrNumber != null) {
            sb.append("hlrNumber=");
            sb.append(this.hlrNumber.toString());
            sb.append(", ");
        }

        if (this.hlrList != null && this.hlrList.getIMSIs()!=null) {
            sb.append("hlrList=[");
            boolean firstItem = true;
            for (IMSI imsi : this.hlrList.getIMSIs()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(imsi.toString());
            }
            sb.append("], ");
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(hlrNumber==null)
			throw new ASNParsingComponentException("hlr number should be set for reset request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
