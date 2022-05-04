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

package org.restcomm.protocols.ss7.cap.dialog;

import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.dialog.CAPUserAbortReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPUserAbortPrimitiveImpl extends ASNEnumerated {
	public static final List<Long> CAP_AbortReason_OId = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 2L, 2L });

    public CAPUserAbortPrimitiveImpl() {
    	super("CAPUserAbortPrimitive",1,8,false);
    }

    public CAPUserAbortPrimitiveImpl(CAPUserAbortReason reason) {
        super(reason.getCode(),"CAPUserAbortPrimitive",1,8,false);
    }

    public CAPUserAbortReason getCAPUserAbortReason() {
    	Integer value=getIntValue();
    	if(value==null)
    		return null;
    	
        return CAPUserAbortReason.getInstance(value);
    }
}