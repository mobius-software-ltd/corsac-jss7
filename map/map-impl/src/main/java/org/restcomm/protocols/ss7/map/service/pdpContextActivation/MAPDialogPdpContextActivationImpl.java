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

package org.restcomm.protocols.ss7.map.service.pdpContextActivation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPDialogPdpContextActivation;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivation;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPDialogPdpContextActivationImpl extends MAPDialogImpl implements MAPDialogPdpContextActivation {
	private static final long serialVersionUID = 1L;

	protected MAPDialogPdpContextActivationImpl(MAPApplicationContext appCntx, Dialog tcapDialog,
            MAPProviderImpl mapProviderImpl, MAPServicePdpContextActivation mapService, AddressString origReference,
            AddressString destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }


    @Override
    public Integer addSendRoutingInfoForGprsRequest(IMSI imsi, GSNAddress ggsnAddress, ISDNAddressString ggsnNumber, MAPExtensionContainer extensionContainer)
            throws MAPException {
        return addSendRoutingInfoForGprsRequest(_Timer_Default, imsi, ggsnAddress, ggsnNumber, extensionContainer);
    }

    @Override
    public Integer addSendRoutingInfoForGprsRequest(int customInvokeTimeout, IMSI imsi, GSNAddress ggsnAddress, ISDNAddressString ggsnNumber,
            MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.gprsLocationInfoRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3 && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version4))
            throw new MAPException("Bad application context name for addSendRoutingInfoForGprsRequest: must be gprsLocationInfoRetrievalContext_V3 or V4");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        SendRoutingInfoForGprsRequestImpl req = new SendRoutingInfoForGprsRequestImpl(imsi, ggsnAddress, ggsnNumber, extensionContainer);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.sendRoutingInfoForGprs, req, true, false);
    }

    @Override
    public void addSendRoutingInfoForGprsResponse(int invokeId, GSNAddress sgsnAddress, GSNAddress ggsnAddress, Integer mobileNotReachableReason,
            MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.gprsLocationInfoRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3 && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version4))
            throw new MAPException("Bad application context name for addSendRoutingInfoForGprsResponse: must be gprsLocationInfoRetrievalContext_V3 or V4");

        SendRoutingInfoForGprsResponseImpl resp = new SendRoutingInfoForGprsResponseImpl(sgsnAddress, ggsnAddress, mobileNotReachableReason, extensionContainer);
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.sendRoutingInfoForGprs, resp, false, true);
    }
}