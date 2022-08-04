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

package org.restcomm.protocols.ss7.map.service.oam;

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
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPServiceOam;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfiguration;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference2;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceType;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MAPDialogOamImpl extends MAPDialogImpl implements MAPDialogOam {
	private static final long serialVersionUID = 1L;

	protected MAPDialogOamImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceOam mapService, AddressString origReference, AddressString destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }


    @Override
    public Integer addActivateTraceModeRequest(IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
            MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
            TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration)
            throws MAPException {
        return this.addActivateTraceModeRequest(_Timer_Default, imsi, traceReference, traceType, omcId, extensionContainer, traceReference2, traceDepthList,
                traceNeTypeList, traceInterfaceList, traceEventList, traceCollectionEntity, mdtConfiguration);
    }

    @Override
    public Integer addActivateTraceModeRequest(int customInvokeTimeout, IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
            MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
            TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration)
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
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.activateTraceMode, req, true, false);
    }

    @Override
    public void addActivateTraceModeResponse(int invokeId, MAPExtensionContainer extensionContainer, boolean traceSupportIndicator) throws MAPException {
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
        else
            mapProviderImpl.getMAPStack().newMessageSent(MAPMessageType.activateTraceMode_Response.name());               

        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.activateTraceMode, req, false, true);
    }

    @Override
    public Integer addSendImsiRequest(ISDNAddressString msisdn) throws MAPException {
        return this.addSendImsiRequest(_Timer_Default, msisdn);
    }

    @Override
    public Integer addSendImsiRequest(int customInvokeTimeout, ISDNAddressString msisdn) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.imsiRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2))
            throw new MAPException("Bad application context name for sendImsiRequest: must be imsiRetrievalContext_V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        SendImsiRequestImpl req = new SendImsiRequestImpl(msisdn);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.sendIMSI, req, true, false);
    }

    @Override
    public void addSendImsiResponse(int invokeId, IMSI imsi) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.imsiRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2))
            throw new MAPException("Bad application context name for addSendImsiResponse: must be imsiRetrievalContext_V2");

        SendImsiResponseImpl req = new SendImsiResponseImpl(imsi);
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.sendIMSI, req, false, true);
    }

}
