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

package org.restcomm.protocols.ss7.tcapAnsi.tc.dialog.events;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.EventType;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class TCPAbortIndicationImpl extends DialogIndicationImpl implements TCPAbortIndication {
	private static final long serialVersionUID = 1L;

	// This indication is used to inform user of abnormal cases.
    private PAbortCause cause;

    // private boolean localProviderOriginated = false;

    TCPAbortIndicationImpl() {
        super(EventType.Abort);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication#getPAbortCause()
     */
    public PAbortCause getPAbortCause() {
        return this.cause;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication#setPAbortCause(org.restcomm.protocols.ss7.tcap
     * .asn.comp.PAbortCauseType)
     */
    public void setPAbortCause(PAbortCause t) {
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
