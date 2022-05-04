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

package org.restcomm.protocols.ss7.inap.dialog;

import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.inap.api.dialog.INAPUserAbortReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPUserAbortPrimitiveImpl extends ASNEnumerated {
	public static final List<Long> INAP_AbortReason_OId = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 2L, 2L });

    public INAPUserAbortPrimitiveImpl() {
    	super("INAPUserAbortPrimitive",1,8,false);
    }

    public INAPUserAbortPrimitiveImpl(INAPUserAbortReason reason) {
        super(reason.getCode(),"INAPUserAbortPrimitive",1,8,false);
    }

    public INAPUserAbortReason getINAPUserAbortReason() {
    	Integer value=getIntValue();
    	if(value==null)
    		return null;
    	
        return INAPUserAbortReason.getInstance(value);
    }
}