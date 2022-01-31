/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.isup;

import java.util.concurrent.ConcurrentHashMap;

import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserTeleserviceInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

import io.netty.buffer.ByteBuf;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x17,constructed=false,lengthIndefinite=false)
public class HighLayerCompatibilityIsupImpl implements HighLayerCompatibilityIsup {
	private UserTeleserviceInformationImpl teleserviceInformation;

    public HighLayerCompatibilityIsupImpl() {
    }

    public HighLayerCompatibilityIsupImpl(UserTeleserviceInformation highLayerCompatibility) throws ASNParsingException {
        setHighLayerCompatibility(highLayerCompatibility);
    }

    public void setHighLayerCompatibility(UserTeleserviceInformation highLayerCompatibility) throws ASNParsingException {
        if (highLayerCompatibility == null)
            throw new ASNParsingException("The callingPartyCategory parameter must not be null");
        
        this.teleserviceInformation = (UserTeleserviceInformationImpl) highLayerCompatibility;
    }

    public UserTeleserviceInformation getHighLayerCompatibility() throws ASNParsingException {
        return teleserviceInformation;
    }
    
    @ASNLength
    public Integer getLength(ASNParser parser) {
		return 2;
	}
	
	@ASNEncode
	public void encode(ASNParser parser, ByteBuf buffer) {
		this.teleserviceInformation.encode(buffer);
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors) {
		try {
			this.teleserviceInformation=new UserTeleserviceInformationImpl(buffer);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		
		return false;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HighLayerCompatibilityInap [");

        try {
            UserTeleserviceInformation cpc = this.getHighLayerCompatibility();
            sb.append(", ");
            sb.append(cpc.toString());
        } catch (ASNParsingException e) {
        }

        sb.append("]");

        return sb.toString();
    }
}
