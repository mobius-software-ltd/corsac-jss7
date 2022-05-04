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

package org.restcomm.protocols.ss7.inap;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPMessage;
import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentException;
import org.restcomm.protocols.ss7.inap.api.INAPProvider;
import org.restcomm.protocols.ss7.inap.api.INAPServiceBase;
import org.restcomm.protocols.ss7.inap.api.INAPServiceListener;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

/**
 * This class must be the super class of all INAP services
 *
 * @author yulian.oifa
 *
 */
public abstract class INAPServiceBaseImpl implements INAPServiceBase {

    protected Boolean _isActivated = false;
    protected List<INAPServiceListener> serviceListeners = new CopyOnWriteArrayList<INAPServiceListener>();
    protected INAPProviderImpl inapProviderImpl = null;

    protected INAPServiceBaseImpl(INAPProviderImpl inapProviderImpl) {
        this.inapProviderImpl = inapProviderImpl;
    }

    @Override
    public INAPProvider getINAPProvider() {
        return this.inapProviderImpl;
    }

    /**
     * Creation a INAP Dialog implementation for the specific service
     *
     * @param appCntx
     * @param tcapDialog
     * @return
     */
    protected abstract INAPDialogImpl createNewDialogIncoming(INAPApplicationContext appCntx, Dialog tcapDialog);

    /**
     * Creating new outgoing TCAP Dialog. Used when creating a new outgoing INAP Dialog
     *
     * @param origAddress
     * @param destAddress
     * @return
     * @throws INAPException
     */
    protected Dialog createNewTCAPDialog(SccpAddress origAddress, SccpAddress destAddress, Long localTrId) throws INAPException {
        try {
            return this.inapProviderImpl.getTCAPProvider().getNewDialog(origAddress, destAddress, localTrId);
        } catch (TCAPException e) {
            throw new INAPException(e.getMessage(), e);
        }
    }

    public abstract void processComponent(ComponentType compType, OperationCode oc, INAPMessage parameter, INAPDialog inapDialog,
    		Integer invokeId, Integer linkedId) throws INAPParsingComponentException;

    /**
     * Returns a list of linked operations for operCode operation
     *
     * @param operCode
     * @return
     */
    public long[] getLinkedOperationList(long operCode) {
        return null;
    }

    /**
     * Adding INAP Dialog into INAPProviderImpl.dialogs Used when creating a new outgoing INAP Dialog
     *
     * @param dialog
     */
    protected void putINAPDialogIntoCollection(INAPDialogImpl dialog) {
        this.inapProviderImpl.addDialog((INAPDialogImpl) dialog);
    }

    protected void addINAPServiceListener(INAPServiceListener inapServiceListener) {
        this.serviceListeners.add(inapServiceListener);
    }

    protected void removeINAPServiceListener(INAPServiceListener inapServiceListener) {
        this.serviceListeners.remove(inapServiceListener);
    }

    /**
     * This method is invoked when INAPProviderImpl.onInvokeTimeOut() is invoked. An InvokeTimeOut may be a normal situation for
     * the component class 2, 3, or 4. In this case checkInvokeTimeOut() should return true and deliver to the INAP-user correct
     * indication
     *
     * @param dialog
     * @param invoke
     * @return
     */
    public boolean checkInvokeTimeOut(INAPDialog dialog, InvokeImpl invoke) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isActivated() {
        return this._isActivated;
    }

    /**
     * {@inheritDoc}
     */
    public void acivate() {
        this._isActivated = true;
    }

    /**
     * {@inheritDoc}
     */
    public void deactivate() {
        this._isActivated = false;

        // TODO: abort all active dialogs ?
    }

    protected void deliverErrorComponent(INAPDialog inapDialog, Integer invokeId, INAPErrorMessage inapErrorMessage) {
        for (INAPServiceListener serLis : this.serviceListeners) {
            serLis.onErrorComponent(inapDialog, invokeId, inapErrorMessage);
        }
    }

    protected void deliverRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem, boolean isLocalOriginated) {
        for (INAPServiceListener serLis : this.serviceListeners) {
            serLis.onRejectComponent(inapDialog, invokeId, problem, isLocalOriginated);
        }
    }

    protected void deliverInvokeTimeout(INAPDialog inapDialog, Invoke invoke) {
        for (INAPServiceListener serLis : this.serviceListeners) {
            serLis.onInvokeTimeout(inapDialog, invoke.getInvokeId());
        }
    }
}
