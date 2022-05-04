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

package org.restcomm.protocols.ss7.map.datacoding;

import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ASNCBSDataCodingSchemeImpl extends ASNSingleByte {
	
	public ASNCBSDataCodingSchemeImpl() {
        super("CBSDataCodingScheme",0,255,false);
    }

    public ASNCBSDataCodingSchemeImpl(Integer data) {
    	super(data,"CBSDataCodingScheme",0,255,false);
    }

    public ASNCBSDataCodingSchemeImpl(CBSDataCodingScheme scheme) {
    	super(scheme.getCode(),"CBSDataCodingScheme",0,255,false);
    }

    public CBSDataCodingScheme getDataCoding() {
    	Integer data=getValue();
        if (data == null)
            return null;
        
        return new CBSDataCodingSchemeImpl(data);
    }

    @Override
    public String toString() {

        CBSDataCodingScheme dataCoding=getDataCoding();
        StringBuilder sb = new StringBuilder();
        sb.append("CBSDataCoding");
        sb.append(" [");
        if (dataCoding!=null)
            sb.append(dataCoding);
        
        sb.append("]");

        return sb.toString();
    }
}
