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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.LSAIdentity;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class LSAIdentityImpl extends ASNOctetString implements LSAIdentity {
	public LSAIdentityImpl() {
		super("LSAIdentity",3,3,false);
    }

    public LSAIdentityImpl(ByteBuf value) {
    	super(value,"LSAIdentity",3,3,false);
    }

    public boolean isPlmnSignificantLSA() {
    	ByteBuf buf=getValue();
    	if(buf==null || buf.readableBytes()<3)
    		return false;
    	
    	buf.skipBytes(2);
        return ((buf.readByte() & 0x01) == 0x01);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LSAIdentity [");

        sb.append("PLMN Significant LSA=");
        sb.append(this.isPlmnSignificantLSA());

        if(getValue()!=null) {
        	sb.append(", data=");
        	sb.append(printDataArr());
        }
        
        sb.append("]");

        return sb.toString();
    }
}