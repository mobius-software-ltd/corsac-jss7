/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.inap.charging;

import org.restcomm.protocols.ss7.inap.api.charging.TariffDuration;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class TariffDurationImpl extends ASNInteger implements TariffDuration {
	public TariffDurationImpl() {
		super("TariffDuration",0L,36000L,false);
    }

    public TariffDurationImpl(Integer value) {
    	super(value,"TariffDuration",0,36000,false);   	
    }

    public Integer getData() {
    	return getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TarifDuration [");
        Integer data=getData();
        if (data != null) {
            sb.append("data=");
            sb.append(data);            
        }
        sb.append("]");

        return sb.toString();
    }
}
