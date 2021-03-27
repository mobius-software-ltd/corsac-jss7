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

package org.restcomm.protocols.ss7.map.service.mobility.faultRecovery;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.ASNNetworkResourceImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ResetRequestImpl extends MobilityMessageImpl implements ResetRequest {
	private static final long serialVersionUID = 1L;

	private ASNNetworkResourceImpl networkResource;
    private ISDNAddressStringImpl hlrNumber;
    private IMSIListWrapperImpl hlrList;

    private long mapProtocolVersion;

    public ResetRequestImpl() {
    	this.mapProtocolVersion = 3;
    }
    
    public ResetRequestImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public ResetRequestImpl(NetworkResource networkResource, ISDNAddressStringImpl hlrNumber, List<IMSIImpl> hlrList, long mapProtocolVersion) {
        if(networkResource!=null) {
        	this.networkResource = new ASNNetworkResourceImpl();
        	this.networkResource.setType(networkResource);
        }
        
        this.hlrNumber = hlrNumber;
        
        if(hlrList!=null)
        	this.hlrList = new IMSIListWrapperImpl(hlrList);

        this.mapProtocolVersion = mapProtocolVersion;
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
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
    public ISDNAddressStringImpl getHlrNumber() {
        return hlrNumber;
    }

    @Override
    public List<IMSIImpl> getHlrList() {
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
            for (IMSIImpl imsi : this.hlrList.getIMSIs()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(imsi.toString());
            }
            sb.append("], ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(this.mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}

