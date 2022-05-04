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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumber;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class PDPTypeNumberImpl extends ASNSingleByte implements PDPTypeNumber {
	public PDPTypeNumberImpl() {
		super("PDPTypeNumber",0,0x57,false);
    }

    public PDPTypeNumberImpl(int data) {
    	super(data,"PDPTypeNumber",0,0x57,false);
    }

    public PDPTypeNumberImpl(PDPTypeNumberValue value) {
    	super(value==null?0:value.getCode(),"PDPTypeNumber",0,0x57,false);    	
    }

    public PDPTypeNumberValue getPDPTypeNumberValue() {
    	if(getValue()==null)
    		return null;
    	
        return PDPTypeNumberValue.getInstance(getValue());
    }

    public int getData() {
    	Integer result=getValue();
    	if(result==null)
    		return 0;
    	
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PDPTypeNumber [");

        if (this.getPDPTypeNumberValue() != null) {
            sb.append("PDPTypeNumberValue=");
            sb.append(this.getPDPTypeNumberValue());
        }

        sb.append("]");

        return sb.toString();
    }

}
