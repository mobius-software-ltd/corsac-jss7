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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.LegInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.LegStatus;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class LegInformationImpl implements LegInformation {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private SendingLegIDWrapperImpl legID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNLegStatus legStatus;

    public LegInformationImpl() {
    }

    public LegInformationImpl(LegType legID,LegStatus legStatus) {
    	if(legID!=null)
    		this.legID=new SendingLegIDWrapperImpl(new SendingLegIDImpl(legID)); 
    	
    	if(legStatus!=null)
    		this.legStatus=new ASNLegStatus(legStatus);
    }

    public LegType getLegID() {
    	if(legID==null || legID.getSendingLegID()==null)
    		return null;
    	
        return legID.getSendingLegID().getSendingSideID();
    }

    public LegStatus getLegStatus() {
    	if(legStatus==null)
    		return null;
    	
    	return legStatus.getType();    	
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("LegInformationImpl [");

        if (this.legID != null && this.legID.getSendingLegID()!=null && this.legID.getSendingLegID().getSendingSideID()!=null) {
            sb.append(", legID=");
            sb.append(legID.getSendingLegID().getSendingSideID());
        }
        
        if (this.legStatus != null && this.legStatus.getType()!=null) {
            sb.append(", legStatus=");
            sb.append(legStatus.getType());
        }

        sb.append("]");

        return sb.toString();
    }
}
