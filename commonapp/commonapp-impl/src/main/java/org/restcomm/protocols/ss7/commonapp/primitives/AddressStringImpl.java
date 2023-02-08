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

package org.restcomm.protocols.ss7.commonapp.primitives;

import java.nio.charset.CharacterCodingException;
import java.util.concurrent.ConcurrentHashMap;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharset;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetDecoder;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetDecodingData;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetEncoder;
import org.restcomm.protocols.ss7.commonapp.datacoding.Gsm7EncodingStyle;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

import io.netty.buffer.ByteBuf;

/**	
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public class AddressStringImpl implements AddressString  {
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
    	return getLength();
    }
    
    public Integer getLength() {
    	if (this.getNumberingPlan() == NumberingPlan.spare_5) {
    		int bits = address.length() * 7;
    		if(bits%8 == 0)
    			return bits/8 + 1;
    		
    		return bits/8 + 2;
    	}
    	else
    		return TbcdStringImpl.getLength(false, null, address) + 1;
	}
    
    @ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) throws ASNParsingException {
    	encode(buffer);
    }
    
    public void encode(ByteBuf buffer) throws ASNParsingException {
    	if (this.address.length() > maxLength*2)
            throw new ASNParsingException("Error when encoding AddressString: address length must not exceed 38 digits");
    	
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
                throw new ASNParsingException(e);
            }
        } else
        	TbcdStringImpl.encodeString(buffer, address);		
	}
    
    @ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors) throws ASNParsingComponentException {
    	decode(buffer);
    	return false;
    }
    
    public void decode(ByteBuf buffer) throws ASNParsingComponentException {
    	if (buffer.readableBytes() > maxLength+1)
            throw new ASNParsingComponentException("Error when decoding AddressString: mesage length must not exceed 20",
                    ASNParsingComponentExceptionReason.MistypedParameter);
    	
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
	            	throw new ASNParsingComponentException(ex,ASNParsingComponentExceptionReason.MistypedParameter);
	            }
            }
        } else
        	this.address=TbcdStringImpl.decodeString(buffer);		
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(addressNature==null)
			throw new ASNParsingComponentException("address nature is required for Address nature", ASNParsingComponentExceptionReason.MistypedParameter);
		
		if(numberingPlan==null)
			throw new ASNParsingComponentException("numering plan is required for Address nature", ASNParsingComponentExceptionReason.MistypedParameter);
		
		if(address==null)
			throw new ASNParsingComponentException("address required for Address nature", ASNParsingComponentExceptionReason.MistypedParameter);
		
		if (this.address.length() > maxLength*2)
            throw new ASNParsingComponentException("Error when encoding AddressString: address length must not exceed " + (maxLength*2) + " digits", ASNParsingComponentExceptionReason.MistypedParameter);    	
	}
}
