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

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganization;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationValue;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class PDPTypeOrganizationImpl extends ASNSingleByte implements PDPTypeOrganization {
	public PDPTypeOrganizationImpl() {  
		super("PDPTypeOrganization",1,2,false);
    }

    public PDPTypeOrganizationImpl(int data) {
    	super(data,"PDPTypeOrganization",1,2,false);
    }

    public PDPTypeOrganizationImpl(PDPTypeOrganizationValue value) {
    	super(value==null?0:(value.getCode() | 0xF0),"PDPTypeOrganization",1,2,false);    	       
    }

    public PDPTypeOrganizationValue getPDPTypeOrganizationValue() {
        return PDPTypeOrganizationValue.getInstance(getData() & 0x0F);
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
        sb.append("PDPTypeOrganization [");

        if (this.getPDPTypeOrganizationValue() != null) {
            sb.append("PDPTypeOrganizationValue=");
            sb.append(this.getPDPTypeOrganizationValue());
        }

        sb.append("]");

        return sb.toString();
    }
}
