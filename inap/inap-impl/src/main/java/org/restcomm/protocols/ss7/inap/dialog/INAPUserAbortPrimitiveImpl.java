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
    }

    public INAPUserAbortPrimitiveImpl(INAPUserAbortReason reason) {
        setValue(Long.valueOf(reason.getCode()));
    }

    public INAPUserAbortReason getINAPUserAbortReason() {
    	if(getValue()==null)
    		return null;
    	
        return INAPUserAbortReason.getInstance(getValue().intValue());
    }
}