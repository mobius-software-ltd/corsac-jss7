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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NACarrierInformation;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NACarrierSelectionInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NAEACIC;
import org.restcomm.protocols.ss7.commonapp.primitives.NAEACICImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author  yulian.oifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class NACarrierInformationImpl implements NACarrierInformation {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = NAEACICImpl.class)
    private NAEACIC naEACIC;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNNACarrierSelectionInfoImpl carrierSelectionInfo;

    public NACarrierInformationImpl() {
    }

    public NACarrierInformationImpl(NAEACIC naEACIC,
            NACarrierSelectionInfo naCarrierSelectionInfo) {
    	this.naEACIC = naEACIC;
    	
    	if(naCarrierSelectionInfo!=null)
    		this.carrierSelectionInfo = new ASNNACarrierSelectionInfoImpl(naCarrierSelectionInfo);    		
    }

    public NAEACIC getNAEACIC() {
    	return naEACIC;
    }

    public NACarrierSelectionInfo getNACarrierSelectionInfo() {
    	if(carrierSelectionInfo==null)
    		return null;
    	
        return carrierSelectionInfo.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("NACarrierInformation [");

        if (this.naEACIC != null) {
            sb.append("naEACIC=");
            sb.append(naEACIC);
            sb.append(", ");
        }
        if (this.carrierSelectionInfo != null && this.carrierSelectionInfo.getType() != null) {
            sb.append("carrierSelectionInfo=");
            sb.append(carrierSelectionInfo.getType().toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}
