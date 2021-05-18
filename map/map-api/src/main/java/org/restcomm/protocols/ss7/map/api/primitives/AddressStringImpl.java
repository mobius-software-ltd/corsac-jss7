/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import java.nio.charset.CharacterCodingException;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharset;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetDecoder;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetDecodingData;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetEncoder;
import org.restcomm.protocols.ss7.map.api.datacoding.Gsm7EncodingStyle;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public class AddressStringImpl  {
	protected int NO_EXTENSION_MASK = 0x80;
    protected int NATURE_OF_ADD_IND_MASK = 0x70;
    protected int NUMBERING_PLAN_IND_MASK = 0x0F;

    protected AddressNature addressNature;
    protected NumberingPlan numberingPlan;
    protected String address;

    private boolean isExtension;
    private Integer maxLength;
    
    public AddressStringImpl() {
    	this.maxLength=19;
    }
    
    public AddressStringImpl(Integer maxLength) {
    	this.maxLength=maxLength;
    }
    
    public AddressStringImpl(AddressNature addressNature, NumberingPlan numberingPlan, String address) {
    	this.maxLength=19;
        this.addressNature = addressNature;
        this.numberingPlan = numberingPlan;
        this.address = address;
    }

    public AddressStringImpl(Integer maxLength,AddressNature addressNature, NumberingPlan numberingPlan, String address) {
        this.maxLength=maxLength;
        this.addressNature = addressNature;
        this.numberingPlan = numberingPlan;
        this.address = address;
    }

    public AddressStringImpl(boolean isExtension, AddressNature addressNature, NumberingPlan numberingPlan, String address) {
    	this.maxLength=19;
        this.isExtension = isExtension;
        this.addressNature = addressNature;
        this.numberingPlan = numberingPlan;
        this.address = address;
    }
    
    public AddressStringImpl(Integer maxLength,boolean isExtension, AddressNature addressNature, NumberingPlan numberingPlan, String address) {
    	this.maxLength=maxLength;
        this.isExtension = isExtension;
        this.addressNature = addressNature;
        this.numberingPlan = numberingPlan;
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public AddressNature getAddressNature() {
        return this.addressNature;
    }

    public NumberingPlan getNumberingPlan() {
        return this.numberingPlan;
    }

    public boolean isExtension() {
        return isExtension;
    }

    @ASNLength
	public Integer getLength(ASNParser parser) {
    	return TbcdString.getLength(false, null, address) + 1;
	}
    
    @ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) throws MAPException {
    	if (this.address.length() > maxLength*2)
            throw new MAPException("Error when encoding AddressString: address length must not exceed 38 digits");
    	
    	int nature = 0x080;

        if (this.isExtension) {
            nature = 0;
        }

        nature = nature | (this.addressNature.getIndicator() << 4);
        nature = nature | (this.numberingPlan.getIndicator());
        buffer.writeByte(nature);
        
        if (numberingPlan == NumberingPlan.spare_5) {
            // -- In the context of the DestinationSubscriberNumber field in ConnectSMSArg or
            // -- InitialDPSMSArg, a CalledPartyBCDNumber may also contain an alphanumeric
            // -- character string. In this case, type-of-number '101'B is used, in accordance
            // -- with 3GPP TS 23.040 [6]. The address is coded in accordance with the
            // -- GSM 7-bit default alphabet definition and the SMS packing rules
            // -- as specified in 3GPP TS 23.038 [15] in this case.

            GSMCharset cs = new GSMCharset();
            GSMCharsetEncoder encoder = (GSMCharsetEncoder) cs.newEncoder();
            try {
                encoder.encode(address,buffer);                
            } catch (CharacterCodingException e) {
                throw new MAPException(e);
            }
        } else
        	TbcdString.encodeString(buffer, address);		
	}
    
    @ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,Boolean skipErrors) throws MAPParsingComponentException {
    	if (buffer.readableBytes() > maxLength+1)
            throw new MAPParsingComponentException("Error when decoding AddressString: mesage length must not exceed 20",
                    MAPParsingComponentExceptionReason.MistypedParameter);
    	
    	int nature = buffer.readByte();

        if ((nature & NO_EXTENSION_MASK) == 0x80) {
            this.isExtension = false;
        } else {
            this.isExtension = true;
        }

        int natureOfAddInd = ((nature & NATURE_OF_ADD_IND_MASK) >> 4);
        this.addressNature = AddressNature.getInstance(natureOfAddInd);
        
        int numbPlanInd = (nature & NUMBERING_PLAN_IND_MASK);
        this.numberingPlan = NumberingPlan.getInstance(numbPlanInd);
        
        if (this.getNumberingPlan() == NumberingPlan.spare_5) {
            // -- In the context of the DestinationSubscriberNumber field in ConnectSMSArg or
            // -- InitialDPSMSArg, a CalledPartyBCDNumber may also contain an alphanumeric
            // -- character string. In this case, type-of-number '101'B is used, in accordance
            // -- with 3GPP TS 23.040 [6]. The address is coded in accordance with the
            // -- GSM 7-bit default alphabet definition and the SMS packing rules
            // -- as specified in 3GPP TS 23.038 [15] in this case.

            if (buffer.readableBytes()==0)
            	this.address="";
            else {
	            GSMCharset cs = new GSMCharset();
	            GSMCharsetDecoder decoder = (GSMCharsetDecoder) cs.newDecoder();
	            int totalSeptetCount = buffer.readableBytes() + (buffer.readableBytes() / 8);
	            GSMCharsetDecodingData encodingData = new GSMCharsetDecodingData(Gsm7EncodingStyle.bit7_sms_style,totalSeptetCount, 0);
	            decoder.setGSMCharsetDecodingData(encodingData);	
	            try {
	            	this.address=decoder.decode(buffer);
	            }
	            catch(CharacterCodingException ex) {
	            	throw new MAPParsingComponentException(ex,MAPParsingComponentExceptionReason.MistypedParameter);
	            }
            }
        } else
        	this.address=TbcdString.decodeString(buffer);
		
		return false;
	}

    @Override
    public String toString() {
        return "AddressString[AddressNature=" + this.addressNature.toString() + ", NumberingPlan="
                + this.numberingPlan.toString() + ", Address=" + this.address + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((addressNature == null) ? 0 : addressNature.hashCode());
        result = prime * result + ((numberingPlan == null) ? 0 : numberingPlan.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AddressStringImpl other = (AddressStringImpl) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (addressNature != other.addressNature)
            return false;
        if (numberingPlan != other.numberingPlan)
            return false;
        return true;
    }
}
