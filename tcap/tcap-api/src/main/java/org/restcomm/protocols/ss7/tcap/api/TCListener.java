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

package org.restcomm.protocols.ss7.tcap.api;

import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface TCListener {

    // dialog handlers
    /**
     * Invoked for TC_UNI. See Q.771 3.1.2.2.2.1
     */
    void onTCUni(TCUniIndication ind);

    /**
     * Invoked for TC_BEGIN. See Q.771 3.1.2.2.2.1
     */
    void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback);

    /**
     * Invoked for TC_CONTINUE dialog primitive. See Q.771 3.1.2.2.2.2/3.1.2.2.2.3
     *
     * @param ind
     */
    void onTCContinue(TCContinueIndication ind, TaskCallback<Exception> callback);

    /**
     * Invoked for TC_END dialog primitive. See Q.771 3.1.2.2.2.4
     *
     * @param ind
     */
    void onTCEnd(TCEndIndication ind, TaskCallback<Exception> callback);

    /**
     * Invoked for TC-U-Abort primitive(P-Abort-Cause is present.). See Q.771 3.1.2.2.2.4
     *
     * @param ind
     */
    void onTCUserAbort(TCUserAbortIndication ind);

    /**
     * Invoked TC-P-Abort (when dialog has been terminated by some unpredicatable environment cause). See Q.771 3.1.4.2
     *
     * @param ind
     */
    void onTCPAbort(TCPAbortIndication ind);

    /**
     * Invoked when TC-Notice primitive has been received. A TC-NOTICE indication primitive is only passed to the TC-user if the
     * requested service (i.e. transfer of components) cannot be provided (the network layer cannot deliver the embedded message
     * to the remote node) and the TC-user requested the return option in the Quality of Service parameter of the dialogue
     * handling request primitive.
     *
     * @param ind
     */
    void onTCNotice(TCNoticeIndication ind);

    /**
     * Called once dialog is released. It is invoked once primitives are delivered. Indicates that stack has no reference, and
     * dialog object is considered invalid.
     *
     * @param dialog
     */
    void onDialogReleased(Dialog dialog);

    /**
     *
     * @param dialog
     * @param invokeId
     */
    void onInvokeTimeout(Dialog dialog,int invokeId, InvokeClass invokeClass);

    /**
     * Called once dialog times out. Once this method is called, dialog cant be used anymore.
     *
     * @param dialog
     */
    void onDialogTimeout(Dialog dialog);
}