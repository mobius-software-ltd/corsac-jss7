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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class DeleteSubscriberDataResponseImpl extends MobilityMessageImpl implements DeleteSubscriberDataResponse {
	private static final long serialVersionUID = 1L;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNRegionalSubscriptionResponse regionalSubscriptionResponse;
   
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public DeleteSubscriberDataResponseImpl() {
    }

    public DeleteSubscriberDataResponseImpl(RegionalSubscriptionResponse regionalSubscriptionResponse, MAPExtensionContainer extensionContainer) {
    	if(regionalSubscriptionResponse!=null)
    		this.regionalSubscriptionResponse = new ASNRegionalSubscriptionResponse(regionalSubscriptionResponse);
    		
        this.extensionContainer = extensionContainer;
    }


    @Override
    public RegionalSubscriptionResponse getRegionalSubscriptionResponse() {
    	if(this.regionalSubscriptionResponse==null)
    		return null;
    	
        return regionalSubscriptionResponse.getType();
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }


    public MAPMessageType getMessageType() {
        return MAPMessageType.deleteSubscriberData_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.deleteSubscriberData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeleteSubscriberDataResponse [");

        if (this.regionalSubscriptionResponse != null) {
            sb.append("regionalSubscriptionResponse=");
            sb.append(this.regionalSubscriptionResponse.getType());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}
