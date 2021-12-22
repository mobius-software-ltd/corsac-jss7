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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberReason;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
public class MAPErrorMessageAbsentSubscriber1Impl extends BooleanMAPErrorMessage1Impl implements
MAPErrorMessageAbsentSubscriber {
	protected String _PrimitiveName = "MAPErrorMessageAbsentSubscriber";

    /**
     * For MAP V1
     *
     * @param mwdSet
     */
    public MAPErrorMessageAbsentSubscriber1Impl(Boolean mwdSet) {
        super((long) MAPErrorCode.absentSubscriber);
        setValue(mwdSet);
    }

    public MAPErrorMessageAbsentSubscriber1Impl() {
        super((long) MAPErrorCode.absentSubscriber);
    }

    public boolean isEmAbsentSubscriber() {
        return true;
    }

    public MAPErrorMessageAbsentSubscriber getEmAbsentSubscriber() {
        return this;
    }

    @Override
    public Boolean getMwdSet() {
        return getValue();
    }

    @Override
    public void setMwdSet(Boolean val) {
        setValue(val);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getValue() != null)
            sb.append("mwdSet=" + this.getValue().toString());
        sb.append("]");

        return sb.toString();
    }

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return null;
	}

	@Override
	public AbsentSubscriberReason getAbsentSubscriberReason() {
		return null;
	}

	@Override
	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {		
	}

	@Override
	public void setAbsentSubscriberReason(AbsentSubscriberReason absentSubscriberReason) {
	}
}