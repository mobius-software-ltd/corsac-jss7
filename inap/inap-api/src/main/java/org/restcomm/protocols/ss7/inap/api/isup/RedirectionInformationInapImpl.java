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

package org.restcomm.protocols.ss7.inap.api.isup;

import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.RedirectionInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionInformation;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x1E,constructed=false,lengthIndefinite=false)
public class RedirectionInformationInapImpl extends ASNOctetString {
	private RedirectionInformationImpl redirectionInformation;

    public RedirectionInformationInapImpl() {
    }

    public RedirectionInformationInapImpl(RedirectionInformation redirectionInformation) throws INAPException {
        setRedirectionInformation(redirectionInformation);
    }

    public void setRedirectionInformation(RedirectionInformation redirectionInformation) throws INAPException {
        if (redirectionInformation == null)
            throw new INAPException("The redirectionInformation parameter must not be null");
        
        this.redirectionInformation = (RedirectionInformationImpl) redirectionInformation;
    }

    public RedirectionInformation getRedirectionInformation() throws INAPException {
        return redirectionInformation;
    }
    
    @ASNLength
	public Integer getLength() {
		return 2;
	}
	
	@ASNEncode
	public void encode(ByteBuf buffer) {
		this.redirectionInformation.encode(buffer);
	}
	
	@ASNDecode
	public Boolean decode(ByteBuf buffer,Boolean skipErrors) {
		try {
			this.redirectionInformation=new RedirectionInformationImpl(buffer);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		
		return true;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RedirectionInformationInap [");

        try {
            RedirectionInformation ri = this.getRedirectionInformation();
            sb.append(", ");
            sb.append(ri.toString());
        } catch (INAPException e) {
        }

        sb.append("]");

        return sb.toString();
    }
}
