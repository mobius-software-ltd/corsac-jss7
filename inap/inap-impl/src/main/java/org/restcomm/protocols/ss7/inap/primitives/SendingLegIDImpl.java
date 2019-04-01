package org.restcomm.protocols.ss7.inap.primitives;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.inap.api.primitives.SendingLegID;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;


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

/**
*
* @author yulian oifa
*
*/
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=false,lengthIndefinite=false)
public class SendingLegIDImpl implements SendingLegID {
	private LegType sendingSideID;
    
    public SendingLegIDImpl() {
    }

    public SendingLegIDImpl(LegType legID) {
        this.sendingSideID = legID;        
    }

    @Override
    public LegType getSendingSideID() {
        return sendingSideID;
    }
    
    @ASNLength
	public Integer getLength() {
		return 1;
	}
	
	@ASNEncode
	public void encode(ByteBuf buffer) {
		buffer.writeByte(this.sendingSideID.getCode());
	}
	
	@ASNDecode
	public Boolean decode(ByteBuf buffer,Boolean skipErrors) {
		this.sendingSideID=LegType.getInstance(buffer.readByte());
		return true;
	}

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("LegID [");
        if (this.sendingSideID != null) {
            sb.append("sendingSideID=");
            sb.append(sendingSideID);
        }
        sb.append("]");

        return sb.toString();
    }
}
