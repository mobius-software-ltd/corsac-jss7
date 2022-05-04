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

package org.restcomm.protocols.ss7.tcap.tc.dialog.events;

import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.EventType;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;

import io.netty.buffer.ByteBuf;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class TCPAbortIndicationImpl extends DialogIndicationImpl implements TCPAbortIndication {
	// This indication is used to inform user of abnormal cases.
    private PAbortCauseType cause;

    // private boolean localProviderOriginated = false;

    TCPAbortIndicationImpl(ByteBuf originalBuffer) {
        super(EventType.PAbort, originalBuffer);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication#getPAbortCause()
     */
    public PAbortCauseType getPAbortCause() {
        return this.cause;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication#setPAbortCause(org.restcomm.protocols.ss7.tcap
     * .asn.comp.PAbortCauseType)
     */
    public void setPAbortCause(PAbortCauseType t) {
        this.cause = t;
    }

    // public boolean isLocalProviderOriginated() {
    // return localProviderOriginated;
    // }
    //
    // public void setLocalProviderOriginated(boolean val) {
    // localProviderOriginated = val;
    // }
}
