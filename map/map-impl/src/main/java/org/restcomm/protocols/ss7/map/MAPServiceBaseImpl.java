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

package org.restcomm.protocols.ss7.map;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.MAPServiceBase;
import org.restcomm.protocols.ss7.map.api.MAPServiceListener;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.service.callhandling.MAPServiceCallHandlingImpl;
import org.restcomm.protocols.ss7.map.service.lsm.MAPServiceLsmImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MAPServiceMobilityImpl;
import org.restcomm.protocols.ss7.map.service.oam.MAPServiceOamImpl;
import org.restcomm.protocols.ss7.map.service.pdpContextActivation.MAPServicePdpContextActivationImpl;
import org.restcomm.protocols.ss7.map.service.sms.MAPServiceSmsImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.MAPServiceSupplementaryImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

/**
 * This class must be the super class of all MAP services
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public abstract class MAPServiceBaseImpl implements MAPServiceBase {
    protected Boolean _isActivated = false;
    // protected Set<MAPServiceListener> serviceListeners = new HashSet<MAPServiceListener>();
    protected List<MAPServiceListener> serviceListeners = new CopyOnWriteArrayList<MAPServiceListener>();
    protected MAPProviderImpl mapProviderImpl = null;

    protected MAPServiceBaseImpl(MAPProviderImpl mapProviderImpl) {
        this.mapProviderImpl = mapProviderImpl;
    }

    public MAPProvider getMAPProvider() {
        return this.mapProviderImpl;
    }

    /**
     * Creation a MAP Dialog implementation for the specific service
     *
     * @param appCntx
     * @param tcapDialog
     * @return
     */
    protected abstract MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog);

    /**
     * Creating new outgoing TCAP Dialog. Used when creating a new outgoing MAP Dialog
     *
     * @param origAddress
     * @param destAddress
     * @return
     * @throws MAPException
     */
    protected Dialog createNewTCAPDialog(SccpAddress origAddress, SccpAddress destAddress, Long localTrId, int networkId) throws MAPException {
        try {
            return this.mapProviderImpl.getTCAPProvider().getNewDialog(origAddress, destAddress, localTrId, networkId);
        } catch (TCAPException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    public abstract void processComponent(ComponentType compType, OperationCode oc, MAPMessage parameter, MAPDialog mapDialog,
    		Integer invokeId, Integer linkedId) throws MAPParsingComponentException;

    /**
     * Adding MAP Dialog into MAPProviderImpl.dialogs Used when creating a new outgoing MAP Dialog
     *
     * @param dialog
     */
    protected void putMAPDialogIntoCollection(MAPDialogImpl dialog) {
        this.mapProviderImpl.addDialog((MAPDialogImpl) dialog);
    }

    protected void addMAPServiceListener(MAPServiceListener mapServiceListener) {
        this.serviceListeners.add(mapServiceListener);
    }

    protected void removeMAPServiceListener(MAPServiceListener mapServiceListener) {
        this.serviceListeners.remove(mapServiceListener);
    }

    /**
     * {@inheritDoc}
     */
    public MAPApplicationContext getMAPv1ApplicationContext(int operationCode) {
        return null;
    }

    /**
     *
     * Returns a list of linked operations for operCode operation
     *
     * @param operCode
     * @return
     */
    public long[] getLinkedOperationList(long operCode) {
        return null;
    }

    /**
     * This method is invoked when MAPProviderImpl.onInvokeTimeOut() is invoked. An InvokeTimeOut may be a normal situation for
     * the component class 2, 3, or 4. In this case checkInvokeTimeOut() should return true and deliver to the MAP-user correct
     * indication
     *
     * @param dialog
     * @param invoke
     * @return
     */
    public boolean checkInvokeTimeOut(MAPDialog dialog, InvokeImpl invoke) {
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

    protected void deliverErrorComponent(MAPDialog mapDialog, Integer invokeId, MAPErrorMessage mapErrorMessage) {
        for (MAPServiceListener serLis : this.serviceListeners) {
            serLis.onErrorComponent(mapDialog, invokeId, mapErrorMessage);
        }
    }

    protected void deliverRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem, boolean isLocalOriginated) {
        for (MAPServiceListener serLis : this.serviceListeners) {
            serLis.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
        }
    }

    // protected void deliverProviderErrorComponent(MAPDialog mapDialog, Long invokeId, MAPProviderError providerError) {
    // for (MAPServiceListener serLis : this.serviceListeners) {
    // serLis.onProviderErrorComponent(mapDialog, invokeId, providerError);
    // }
    // }

    protected void deliverInvokeTimeout(MAPDialog mapDialog, Invoke invoke) {
        for (MAPServiceListener serLis : this.serviceListeners) {
            serLis.onInvokeTimeout(mapDialog, invoke.getInvokeId());
        }
    }
    
    public static List<String> getNames() {
    	return Arrays.asList(new String[] { MAPServiceCallHandlingImpl.NAME, MAPServiceLsmImpl.NAME, MAPServiceMobilityImpl.NAME, MAPServiceOamImpl.NAME, MAPServicePdpContextActivationImpl.NAME, MAPServiceSmsImpl.NAME, MAPServiceSupplementaryImpl.NAME});
    }
}