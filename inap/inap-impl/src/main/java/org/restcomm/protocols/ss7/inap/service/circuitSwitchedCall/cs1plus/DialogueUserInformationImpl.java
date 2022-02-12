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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DialogueUserInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ReceivingFunctionsRequested;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SendingFunctionsActive;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class DialogueUserInformationImpl implements DialogueUserInformation {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNSendingFunctionsActive sendingFunctionsActive;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNReceivingFunctionsRequested receivingFunctionsRequested;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1)
    private ASNInteger trafficSimulationSessionID;

    public DialogueUserInformationImpl() {
    }

    public DialogueUserInformationImpl(SendingFunctionsActive sendingFunctionsActive,
    		ReceivingFunctionsRequested receivingFunctionsRequested, Integer trafficSimulationSessionID) {
    	if(sendingFunctionsActive!=null)
    		this.sendingFunctionsActive=new ASNSendingFunctionsActive(sendingFunctionsActive);
    		
    	if(receivingFunctionsRequested!=null)
    		this.receivingFunctionsRequested=new ASNReceivingFunctionsRequested(receivingFunctionsRequested);
    		
    	if(trafficSimulationSessionID!=null)
    		this.trafficSimulationSessionID=new ASNInteger(trafficSimulationSessionID,"TrafficSimulationSessionID",0,65535,false);    		
    }

    public SendingFunctionsActive getSendingFunctionsActive() {
    	if(sendingFunctionsActive==null)
    		return null;
    	
    	return sendingFunctionsActive.getType();
    }

    public ReceivingFunctionsRequested getReceivingFunctionsRequested() {
    	if(receivingFunctionsRequested==null)
    		return null;
    	
    	return receivingFunctionsRequested.getType();
    }

    public Integer getTrafficSimulationSessionID() {
    	if(trafficSimulationSessionID==null || trafficSimulationSessionID.getValue()==null)
    		return null;
    	
    	return trafficSimulationSessionID.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DialogueUserInformation [");

        if (this.sendingFunctionsActive != null && this.sendingFunctionsActive.getType()!=null) {
            sb.append(", sendingFunctionsActive=");
            sb.append(sendingFunctionsActive.getType());
        }

        if (this.receivingFunctionsRequested != null && this.receivingFunctionsRequested.getType()!=null) {
            sb.append(", receivingFunctionsRequested=");
            sb.append(receivingFunctionsRequested.getType());
        }

        if (this.trafficSimulationSessionID != null && this.trafficSimulationSessionID.getValue()!=null) {
            sb.append(", trafficSimulationSessionID=");
            sb.append(trafficSimulationSessionID.getValue());
        }
        
        sb.append("]");

        return sb.toString();
    }
}