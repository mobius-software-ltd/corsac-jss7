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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGIndex;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
*
*/
public class CUGIndexImpl extends ASNInteger implements CUGIndex {
	public CUGIndexImpl() {
		super("CUGIndex",0L,32767L,false);
    }

    public CUGIndexImpl(int data) {
        super(data,"CUGIndex",0,32767,false);
    }

    public int getData() {
    	Integer value=getIntValue();
    	if(value==null)
    		return 0;
    	
        return value;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CUGIndex [");

        sb.append(this.getData());

        sb.append("]");

        return sb.toString();
    }

}
