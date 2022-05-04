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

package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.primitives.LMSI;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class LMSIImpl extends ASNOctetString implements LMSI {
	public LMSIImpl() {
		super("LMSI",4,4,false);
    }

    public LMSIImpl(ByteBuf value) {
        super(value,"LMSI",4,4,false);
    }

    @Override
    public String toString() {
        return "LMSI [Data= " + printDataArr() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if(getValue()!=null)        	
        	result = prime * result + ByteBufUtil.hashCode(getValue());
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
        LMSIImpl other = (LMSIImpl) obj;
        
        ByteBuf value=getValue();
        ByteBuf otherValue=other.getValue();
        if(value==null) {
        	if(otherValue!=null)
        		return false;        	
        }
        else {
        	if(otherValue==null)
        		return false;
        	
        	if (!ByteBufUtil.equals(value, otherValue))
        		return false;
        }
        return true;
    }
}