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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReleaseCallPartyConnectionRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ReleaseCallPartyConnectionRequestImpl extends CircuitSwitchedCallMessageImpl implements ReleaseCallPartyConnectionRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private SendingLegIDWrapperImpl legToBeReleased;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = CauseIsupImpl.class)
    private CauseIsup releaseCause;
    
    public ReleaseCallPartyConnectionRequestImpl() {
    }

    public ReleaseCallPartyConnectionRequestImpl(LegType legToBeReleased,CauseIsup releaseCause) {
        if(legToBeReleased!=null)
        	this.legToBeReleased=new SendingLegIDWrapperImpl(new SendingLegIDImpl(legToBeReleased));
        
        this.releaseCause=releaseCause;
    }

    public INAPMessageType getMessageType() {
        return INAPMessageType.releaseCallPartyConnection_Request;
    }

    public int getOperationCode() {
        return INAPOperationCode.releaseCallPartyConnection;
    }

    @Override
    public LegType getLegToBeReleased() 
    {
    	if(legToBeReleased==null || legToBeReleased.getSendingLegID()==null)
    		return null;
    	
		return legToBeReleased.getSendingLegID().getSendingSideID();
	}

    @Override
    public CauseIsup getReleaseCause() 
	{
		return releaseCause;
	}

	public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ReleaseCallPartyConnectionRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (legToBeReleased != null && legToBeReleased.getSendingLegID()!=null && legToBeReleased.getSendingLegID().getSendingSideID()!=null) {
            sb.append(", legToBeReleased=");
            sb.append(legToBeReleased.getSendingLegID().getSendingSideID());
        }
        if (releaseCause != null) {
            sb.append(", releaseCause=");
            sb.append(releaseCause);
        }

        sb.append("]");

        return sb.toString();
    }
}
