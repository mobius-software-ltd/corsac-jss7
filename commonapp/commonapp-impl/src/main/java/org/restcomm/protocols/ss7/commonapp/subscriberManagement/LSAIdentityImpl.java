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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.LSAIdentity;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
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