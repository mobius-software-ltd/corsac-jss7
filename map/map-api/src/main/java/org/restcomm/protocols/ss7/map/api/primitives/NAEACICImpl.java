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
package org.restcomm.protocols.ss7.map.api.primitives;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 *
 *
 ___________________________________________________________ | | 8 | 7 | 6 | 5 | 4 | 3 | 2 | 1 |
 * |___________|_____|_____|_____|_____|_____|_____|_____|_____| | Octet 1: |Spare| Type of net idn | network idn plan |
 * |___________|_____|_________________|_______________________| | Octet 2: | DIGIT 2 | DIGIT 1 | |
 * __________|_______________________|_______________________| | Octet 3: | DIGIT 4(or 0000) | DIGIT 3 |
 * |___________|_______________________|_______________________|
 *
 *
 * @author Lasith Waruna Perera
 *
 */
public class NAEACICImpl extends ASNOctetString {
	protected static final int NETWORK_IND_PLAN_MASK = 0x0F;
    protected static final int NETWORK_IND_TYPE_MASK = 0x70;
    protected static final int THREE_OCTET_CARRIER_CODE_MASK = 0x0F;

    public NAEACICImpl() {
    }

    public NAEACICImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public NAEACICImpl(String carrierCode, NetworkIdentificationPlanValue networkIdentificationPlanValue,
            NetworkIdentificationTypeValue networkIdentificationTypeValue) throws MAPException {
        setParameters(carrierCode, networkIdentificationPlanValue, networkIdentificationTypeValue);
    }

    public byte[] getData() {
    	ByteBuf buf=getValue();
    	byte[] data=new byte[buf.readableBytes()];
    	buf.readBytes(data);
        return data;
    }

    public String getCarrierCode() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() == 0)
            return null;

        try {
        	value.readByte();
            String address = TbcdString.decodeString(value);
            if (address.length() == 4
                    && this.getNetworkIdentificationPlanValue().equals(
                            NetworkIdentificationPlanValue.threeDigitCarrierIdentification)) {
                return address.substring(0, 3);
            }
            return address;
        } catch (MAPParsingComponentException e) {
            return null;
        }
    }

    public NetworkIdentificationPlanValue getNetworkIdentificationPlanValue() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() == 0)
            return null;
        
        int planValue = value.readByte() & 0x0FF;
        return NetworkIdentificationPlanValue.getInstance(planValue & NETWORK_IND_PLAN_MASK);
    }

    public NetworkIdentificationTypeValue getNetworkIdentificationTypeValue() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() == 0)
            return null;

        int typeValue = value.readByte() & 0x0FF;
        typeValue = ((typeValue & NETWORK_IND_TYPE_MASK) >> 4);
        return NetworkIdentificationTypeValue.getInstance(typeValue);
    }

    private void setParameters(String carrierCode, NetworkIdentificationPlanValue networkIdentificationPlanValue,
            NetworkIdentificationTypeValue networkIdentificationTypeValue) throws MAPException {

        if (carrierCode == null || networkIdentificationPlanValue == null || networkIdentificationTypeValue == null)
            throw new MAPException("Error when encoding NAEACIC: carrierCode, networkIdentificationPlanValue or networkIdentificationTypeValue is empty");

        if (!(carrierCode.length() == 3 || carrierCode.length() == 4))
            throw new MAPException("Error when encoding NAEACIC: carrierCode lenght should be 3 or 4");

        ByteBuf value=Unpooled.buffer();

        int octOne = 0;
        octOne = octOne | (networkIdentificationTypeValue.getCode() << 4);
        octOne = octOne | networkIdentificationPlanValue.getCode();

        value.writeByte(octOne);

        try {
            TbcdString.encodeString(value, carrierCode);
        } catch (MAPException e) {
            throw new MAPException(e);
        }

        if (carrierCode.length() == 3) {
        	value.setByte(2, value.getByte(2)& THREE_OCTET_CARRIER_CODE_MASK);            
        }
        
        setValue(value);
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("NAEACIC");
        sb.append(" [");
        if (this.getNetworkIdentificationPlanValue() != null) {
            sb.append("NetworkIdentificationPlanValue=");
            sb.append(this.getNetworkIdentificationPlanValue());
        }
        if (this.getNetworkIdentificationTypeValue() != null) {
            sb.append(", NetworkIdentificationTypeValue=");
            sb.append(this.getNetworkIdentificationTypeValue());
        }
        if (this.getCarrierCode() != null) {
            sb.append(", CarrierCode=");
            sb.append(this.getCarrierCode());
        }
        sb.append("]");

        return sb.toString();
    }

}
