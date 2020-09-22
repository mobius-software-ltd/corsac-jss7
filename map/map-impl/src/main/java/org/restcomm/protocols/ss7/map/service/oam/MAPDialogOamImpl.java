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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPServiceOam;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfigurationImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference2Impl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReferenceImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceTypeImpl;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPDialogOamImpl extends MAPDialogImpl implements MAPDialogOam {
	private static final long serialVersionUID = 1L;

	protected MAPDialogOamImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceOam mapService, AddressStringImpl origReference, AddressStringImpl destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }


    @Override
    public Long addActivateTraceModeRequest(IMSIImpl imsi, TraceReferenceImpl traceReference, TraceTypeImpl traceType, AddressStringImpl omcId,
            MAPExtensionContainerImpl extensionContainer, TraceReference2Impl traceReference2, TraceDepthListImpl traceDepthList, TraceNETypeListImpl traceNeTypeList,
            TraceInterfaceListImpl traceInterfaceList, TraceEventListImpl traceEventList, GSNAddressImpl traceCollectionEntity, MDTConfigurationImpl mdtConfiguration)
            throws MAPException {
        return this.addActivateTraceModeRequest(_Timer_Default, imsi, traceReference, traceType, omcId, extensionContainer, traceReference2, traceDepthList,
                traceNeTypeList, traceInterfaceList, traceEventList, traceCollectionEntity, mdtConfiguration);
    }

    @Override
    public Long addActivateTraceModeRequest(int customInvokeTimeout, IMSIImpl imsi, TraceReferenceImpl traceReference, TraceTypeImpl traceType, AddressStringImpl omcId,
            MAPExtensionContainerImpl extensionContainer, TraceReference2Impl traceReference2, TraceDepthListImpl traceDepthList, TraceNETypeListImpl traceNeTypeList,
            TraceInterfaceListImpl traceInterfaceList, TraceEventListImpl traceEventList, GSNAddressImpl traceCollectionEntity, MDTConfigurationImpl mdtConfiguration)
            throws MAPException {

        boolean isTracingContext = false;
        boolean isNetworkLocUpContext = false;
        boolean isGprsLocationUpdateContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.tracingContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isTracingContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.networkLocUpContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isNetworkLocUpContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.gprsLocationUpdateContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isGprsLocationUpdateContext = true;

        if (!isTracingContext && !isNetworkLocUpContext && !isGprsLocationUpdateContext)
            throw new MAPException(
                    "Bad application context name for activateTraceModeRequest: must be tracingContext_V1, V2 or V3, networkLocUpContext_V1, V2 or V3 or gprsLocationUpdateContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        ActivateTraceModeRequestImpl req = new ActivateTraceModeRequestImpl(imsi, traceReference, traceType, omcId, extensionContainer, traceReference2,
                traceDepthList, traceNeTypeList, traceInterfaceList, traceEventList, traceCollectionEntity, mdtConfiguration);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.activateTraceMode, req, true, false);
    }

    @Override
    public void addActivateTraceModeResponse(long invokeId, MAPExtensionContainerImpl extensionContainer, boolean traceSupportIndicator) throws MAPException {
        boolean isTracingContext = false;
        boolean isNetworkLocUpContext = false;
        boolean isGprsLocationUpdateContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.tracingContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isTracingContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.networkLocUpContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isNetworkLocUpContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.gprsLocationUpdateContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isGprsLocationUpdateContext = true;

        if (!isTracingContext && !isNetworkLocUpContext && !isGprsLocationUpdateContext)
            throw new MAPException(
                    "Bad application context name for activateTraceModeResponse: must be tracingContext_V1, V2 or V3, networkLocUpContext_V1, V2 or V3 or gprsLocationUpdateContext_V3");

        ActivateTraceModeResponseImpl req=null;
        if ((traceSupportIndicator || extensionContainer != null) && this.appCntx.getApplicationContextVersion().getVersion() >= 3)
        	req = new ActivateTraceModeResponseImpl(extensionContainer, traceSupportIndicator);
       
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.activateTraceMode, req, false, true);
    }

    @Override
    public Long addSendImsiRequest(ISDNAddressStringImpl msisdn) throws MAPException {
        return this.addSendImsiRequest(_Timer_Default, msisdn);
    }

    @Override
    public Long addSendImsiRequest(int customInvokeTimeout, ISDNAddressStringImpl msisdn) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.imsiRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2))
            throw new MAPException("Bad application context name for sendImsiRequest: must be imsiRetrievalContext_V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        SendImsiRequestImpl req = new SendImsiRequestImpl(msisdn);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.sendIMSI, req, true, false);
    }

    @Override
    public void addSendImsiResponse(long invokeId, IMSIImpl imsi) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.imsiRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2))
            throw new MAPException("Bad application context name for addSendImsiResponse: must be imsiRetrievalContext_V2");

        SendImsiResponseImpl req = new SendImsiResponseImpl(imsi);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.sendIMSI, req, false, true);
    }

}
