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
package org.restcomm.protocols.ss7.cap.service.gprs;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ApplyChargingGPRSRequestImpl extends GprsMessageImpl implements ApplyChargingGPRSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1,defaultImplementation = ChargingCharacteristicsImpl.class)
    private ChargingCharacteristics chargingCharacteristics;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger tariffSwitchInterval;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = PDPIDImpl.class)
    private PDPID pdpID;

    public ApplyChargingGPRSRequestImpl() {
    }

    public ApplyChargingGPRSRequestImpl(ChargingCharacteristics chargingCharacteristics, Integer tariffSwitchInterval,
            PDPID pdpID) {
        super();
        this.chargingCharacteristics = chargingCharacteristics;
        
        if(tariffSwitchInterval!=null) {
        	this.tariffSwitchInterval = new ASNInteger();
        	this.tariffSwitchInterval.setValue(tariffSwitchInterval.longValue());
        }
        
        this.pdpID = pdpID;
    }

    @Override
    public ChargingCharacteristics getChargingCharacteristics() {
        return this.chargingCharacteristics;
    }

    @Override
    public Integer getTariffSwitchInterval() {
    	if(this.tariffSwitchInterval==null || this.tariffSwitchInterval.getValue()==null)
    		return null;
    	
        return this.tariffSwitchInterval.getValue().intValue();
    }

    @Override
    public PDPID getPDPID() {
        return this.pdpID;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.applyChargingGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.applyChargingGPRS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ApplyChargingGPRSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.chargingCharacteristics != null) {
            sb.append(", chargingCharacteristics=");
            sb.append(this.chargingCharacteristics.toString());
            sb.append(", ");
        }

        if (this.tariffSwitchInterval != null) {
            sb.append(", tariffSwitchInterval=");
            sb.append(this.tariffSwitchInterval.toString());
            sb.append(", ");
        }

        if (this.pdpID != null) {
            sb.append(", pdpID=");
            sb.append(this.pdpID.toString());
        }
        sb.append("]");

        return sb.toString();
    }

}
