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

import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.FCIBCCCAMELSequence1Impl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public class FurnishChargingInformationRequestImpl extends CircuitSwitchedCallMessageImpl implements
        FurnishChargingInformationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1,defaultImplementation = FCIBCCCAMELSequence1Impl.class)
    private ASNOctetString fciBillingChargingCharacteristics;

    public FurnishChargingInformationRequestImpl() {
    }

    public FurnishChargingInformationRequestImpl(byte[] fciBillingChargingCharacteristics) {
    	if(fciBillingChargingCharacteristics!=null) {
    		this.fciBillingChargingCharacteristics = new ASNOctetString();
    		this.fciBillingChargingCharacteristics.setValue(Unpooled.wrappedBuffer(fciBillingChargingCharacteristics));
    	}
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.furnishChargingInformation_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.furnishChargingInformation;
    }

    @Override
    public byte[] getFCIBillingChargingCharacteristics() {
    	if(fciBillingChargingCharacteristics==null || fciBillingChargingCharacteristics.getValue()==null)
    		return null;
    	
    	ByteBuf value=fciBillingChargingCharacteristics.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("FurnishChargingInformationIndication [");
        this.addInvokeIdInfo(sb);

        if (this.fciBillingChargingCharacteristics != null && fciBillingChargingCharacteristics.getValue()!=null) {
            sb.append(", FCIBillingChargingCharacteristics=");
            sb.append(ASNOctetString.printDataArr(getFCIBillingChargingCharacteristics()));
        }

        sb.append("]");

        return sb.toString();
    }
}
