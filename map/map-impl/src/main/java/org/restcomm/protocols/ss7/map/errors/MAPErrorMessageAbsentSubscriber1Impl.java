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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberReason;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 */
public class MAPErrorMessageAbsentSubscriber1Impl extends BooleanMAPErrorMessage1Impl implements
MAPErrorMessageAbsentSubscriber {
	/**
     * For MAP V1
     *
     * @param mwdSet
     */
    public MAPErrorMessageAbsentSubscriber1Impl(Boolean mwdSet) {
        super(MAPErrorCode.absentSubscriber);
        setValue(mwdSet);
    }

    public MAPErrorMessageAbsentSubscriber1Impl() {
        super(MAPErrorCode.absentSubscriber);
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

        sb.append("MAPErrorMessageAbsentSubscriber [");

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