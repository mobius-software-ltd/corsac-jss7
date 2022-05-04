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

package org.restcomm.protocols.ss7.commonapp.isup;

import java.util.concurrent.ConcurrentHashMap;

import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CallingPartyCategoryImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyCategory;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x05,constructed=false,lengthIndefinite=false)
public class CallingPartysCategoryIsupImpl implements CallingPartysCategoryIsup {
	private CallingPartyCategoryImpl category;

    public CallingPartysCategoryIsupImpl() {
    }

    public CallingPartysCategoryIsupImpl(CallingPartyCategoryImpl category) {
        this.category = category;
    }

    public CallingPartysCategoryIsupImpl(CallingPartyCategory callingPartyCategory) throws ASNParsingException {
        setCallingPartysCategory(callingPartyCategory);
    }

    public void setCallingPartysCategory(CallingPartyCategory callingPartyCategory) throws ASNParsingException {
        if (callingPartyCategory == null)
            throw new ASNParsingException("The callingPartyCategory parameter must not be null");
        
        this.category = (CallingPartyCategoryImpl) callingPartyCategory;
    }

    public CallingPartyCategory getCallingPartyCategory() {
    	return category;
    }

    @ASNLength
    public Integer getLength(ASNParser parser) {
		return 1;
	}
	
	@ASNEncode
	public void encode(ASNParser parser, ByteBuf buffer) {
		this.category.encode(buffer);
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors) {
		try {
			this.category=new CallingPartyCategoryImpl(buffer);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		
		return false;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallingPartysCategoryInap [");

        if (this.getCallingPartyCategory() != null) {
        	CallingPartyCategory cpc = this.getCallingPartyCategory();
            sb.append(", ");
            sb.append(cpc.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(category==null)
			throw new ASNParsingComponentException("category should be set for calling partys category isup", ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}
