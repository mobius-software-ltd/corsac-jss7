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

package org.restcomm.protocols.ss7.map.service.mobility;

import java.util.List;

import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.TMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AccessType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContext;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.FailureCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EctData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfiguration;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference2;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceType;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationFailureReportRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationFailureReportResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.ResetRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.RestoreDataRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.RestoreDataResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiResponseImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.CancelLocationRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.CancelLocationRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.CancelLocationResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PurgeMSRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PurgeMSRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PurgeMSResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationResponseImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateGprsLocationRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateGprsLocationResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateLocationRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateLocationResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateLocationResponseImplV2;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.ProvideSubscriberInfoRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.ProvideSubscriberInfoResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DeleteSubscriberDataRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DeleteSubscriberDataResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataResponseImpl;
import org.restcomm.protocols.ss7.map.service.oam.ActivateTraceModeRequestImpl;
import org.restcomm.protocols.ss7.map.service.oam.ActivateTraceModeResponseImpl;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPDialogMobilityImpl extends MAPDialogImpl implements MAPDialogMobility {
	private static final long serialVersionUID = 1L;

	protected MAPDialogMobilityImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceMobility mapService, AddressString origReference, AddressString destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }

	public Long addSendAuthenticationInfoRequest(IMSI imsi) throws MAPException {
        return this.addSendAuthenticationInfoRequest(_Timer_Default, imsi);
    }

    public Long addSendAuthenticationInfoRequest(int customInvokeTimeout, IMSI imsi) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.infoRetrievalContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException(
                    "Bad application context name for sendAuthenticationInfoRequest: must be infoRetrievalContext_V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        SendAuthenticationInfoRequestImplV1 req=null;
        if (imsi != null)
            req = new SendAuthenticationInfoRequestImplV1(this.appCntx.getApplicationContextVersion().getVersion(), imsi);
            
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.sendAuthenticationInfo, req, true, false);
    }
    
    public Long addSendAuthenticationInfoRequest(IMSI imsi, int numberOfRequestedVectors, boolean segmentationProhibited,
            boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
            MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException {
        return this.addSendAuthenticationInfoRequest(_Timer_Default, imsi, numberOfRequestedVectors, segmentationProhibited,
                immediateResponsePreferred, reSynchronisationInfo, extensionContainer, requestingNodeType, requestingPlmnId,
                numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS);
    }

    public Long addSendAuthenticationInfoRequest(int customInvokeTimeout, IMSI imsi, int numberOfRequestedVectors,
            boolean segmentationProhibited, boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
            MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.infoRetrievalContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for sendAuthenticationInfoRequest: must be infoRetrievalContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        SendAuthenticationInfoRequestImplV3 req=null;
        if (imsi != null)
            req = new SendAuthenticationInfoRequestImplV3(this.appCntx
                    .getApplicationContextVersion().getVersion(), imsi, numberOfRequestedVectors, segmentationProhibited,
                    immediateResponsePreferred, reSynchronisationInfo, extensionContainer, requestingNodeType,
                    requestingPlmnId, numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS);
            
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.sendAuthenticationInfo, req, true, false);
    }

    public void addSendAuthenticationInfoResponse(long invokeId, AuthenticationSetList authenticationSetList) throws MAPException {
        doAddSendAuthenticationInfoResponse(false, invokeId, authenticationSetList);
    }

    public void addSendAuthenticationInfoResponse_NonLast(long invokeId, AuthenticationSetList authenticationSetList) throws MAPException {
        doAddSendAuthenticationInfoResponse(true, invokeId, authenticationSetList);
    }

    protected void doAddSendAuthenticationInfoResponse(boolean nonLast, long invokeId,
            AuthenticationSetList authenticationSetList) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.infoRetrievalContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException(
                    "Bad application context name for addSendAuthenticationInfoResponse: must be infoRetrievalContext_V2");
        
        SendAuthenticationInfoResponseImplV1 req = null;
        if (authenticationSetList != null)
            req = new SendAuthenticationInfoResponseImplV1(this.appCntx.getApplicationContextVersion().getVersion(), 
            		authenticationSetList);
        
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.sendAuthenticationInfo, req, false, !nonLast);
    }
    
    public void addSendAuthenticationInfoResponse(long invokeId, AuthenticationSetList authenticationSetList,
            MAPExtensionContainer extensionContainer, EpsAuthenticationSetList epsAuthenticationSetList) throws MAPException {
        doAddSendAuthenticationInfoResponse(invokeId, authenticationSetList, extensionContainer,
                epsAuthenticationSetList);
    }

    protected void doAddSendAuthenticationInfoResponse(long invokeId,
            AuthenticationSetList authenticationSetList, MAPExtensionContainer extensionContainer,
            EpsAuthenticationSetList epsAuthenticationSetList) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.infoRetrievalContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addSendAuthenticationInfoResponse: must be infoRetrievalContext_V3");

        SendAuthenticationInfoResponseImplV3 req = null;
        if (authenticationSetList != null || extensionContainer != null || epsAuthenticationSetList != null)
            req = new SendAuthenticationInfoResponseImplV3(this.appCntx.getApplicationContextVersion().getVersion(), 
            		authenticationSetList, extensionContainer,epsAuthenticationSetList);
        
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.sendAuthenticationInfo, req, false, true);
    }

    @Override
    public Long addAuthenticationFailureReportRequest(IMSI imsi, FailureCause failureCause, MAPExtensionContainer extensionContainer, Boolean reAttempt,
            AccessType accessType, byte[] rand, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber) throws MAPException {
        return this.addAuthenticationFailureReportRequest(_Timer_Default, imsi, failureCause, extensionContainer, reAttempt, accessType, rand, vlrNumber,
                sgsnNumber);
    }

    @Override
    public Long addAuthenticationFailureReportRequest(int customInvokeTimeout, IMSI imsi, FailureCause failureCause, MAPExtensionContainer extensionContainer,
            Boolean reAttempt, AccessType accessType, byte[] rand, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.authenticationFailureReportContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException("Bad application context name for authenticationFailureReportRequest: must be authenticationFailureReportContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        AuthenticationFailureReportRequestImpl req = new AuthenticationFailureReportRequestImpl(imsi, failureCause, extensionContainer, reAttempt, accessType,
                rand, vlrNumber, sgsnNumber);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.authenticationFailureReport, req, true, false);
    }

    @Override
    public void addAuthenticationFailureReportResponse(long invokeId, MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.authenticationFailureReportContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException("Bad application context name for authenticationFailureReportResponse: must be authenticationFailureReportContext_V3");

        AuthenticationFailureReportResponseImpl req=null;
        if (extensionContainer != null)
            req = new AuthenticationFailureReportResponseImpl(extensionContainer);
            
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.authenticationFailureReport, req, false, true);
    }

    public Long addUpdateLocationRequest(IMSI imsi, ISDNAddressString mscNumber, ISDNAddressString roamingNumber,
    		ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, VLRCapability vlrCapability,
            boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo,
            PagingArea pagingArea, boolean skipSubscriberDataUpdate, boolean restorationIndicator) throws MAPException {
        return addUpdateLocationRequest(_Timer_Default, imsi, mscNumber, roamingNumber, vlrNumber, lmsi, extensionContainer,
                vlrCapability, informPreviousNetworkEntity, csLCSNotSupportedByUE, vGmlcAddress, addInfo, pagingArea,
                skipSubscriberDataUpdate, restorationIndicator);
    }

    public Long addUpdateLocationRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString mscNumber,
    		ISDNAddressString roamingNumber, ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
            VLRCapability vlrCapability, boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE,
            GSNAddress vGmlcAddress, ADDInfo addInfo, PagingArea pagingArea, boolean skipSubscriberDataUpdate,
            boolean restorationIndicator) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for UpdateLocationRequest: must be networkLocUpContext_V1, V2 or V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        UpdateLocationRequestImpl req = new UpdateLocationRequestImpl(this.appCntx.getApplicationContextVersion().getVersion(),
                imsi, mscNumber, roamingNumber, vlrNumber, lmsi, extensionContainer, vlrCapability,
                informPreviousNetworkEntity, csLCSNotSupportedByUE, vGmlcAddress, addInfo, pagingArea,
                skipSubscriberDataUpdate, restorationIndicator);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.updateLocation, req, true, false);
    }

    public void addUpdateLocationResponse(long invokeId, ISDNAddressString hlrNumber) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1)
            throw new MAPException(
                    "Bad application context name for UpdateLocationResponse: must be networkLocUpContext_V1");

        UpdateLocationResponseImplV1 req = new UpdateLocationResponseImplV1(this.appCntx.getApplicationContextVersion().getVersion(), hlrNumber);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.updateLocation, req, false, true);
    }
    
    public void addUpdateLocationResponse(long invokeId, ISDNAddressString hlrNumber, MAPExtensionContainer extensionContainer,
            boolean addCapability, boolean pagingAreaCapability) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for UpdateLocationResponse: must be networkLocUpContext_V2 or V3");

        UpdateLocationResponseImplV2 req = new UpdateLocationResponseImplV2(this.appCntx.getApplicationContextVersion()
                .getVersion(), hlrNumber, extensionContainer, addCapability, pagingAreaCapability);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.updateLocation, req, false, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. MAPDialogSubscriberInformation
     * #addAnyTimeInterrogationRequest(org.restcomm .protocols.ss7.map.api.primitives.SubscriberIdentity,
     * org.restcomm.protocols .ss7.map.api.service.subscriberInformation.RequestedInfo,
     * org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    public long addAnyTimeInterrogationRequest(SubscriberIdentity subscriberIdentity, RequestedInfo requestedInfo,
    		ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer) throws MAPException {

        return this.addAnyTimeInterrogationRequest(_Timer_Default, subscriberIdentity, requestedInfo, gsmSCFAddress,
                extensionContainer);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * MAPDialogSubscriberInformation#addAnyTimeInterrogationRequest(long,
     * org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity, org.restcomm
     * .protocols.ss7.map.api.service.subscriberInformation.RequestedInfo,
     * org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    public long addAnyTimeInterrogationRequest(long customInvokeTimeout, SubscriberIdentity subscriberIdentity,
            RequestedInfo requestedInfo, ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer)
            throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.anyTimeEnquiryContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for AnyTimeInterrogationRequest: must be networkLocUpContext_V3");

        Long customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=(long)getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        AnyTimeInterrogationRequestImpl req = new AnyTimeInterrogationRequestImpl(subscriberIdentity, requestedInfo,
                gsmSCFAddress, extensionContainer);
        return this.sendDataComponent(null, null, null, customTimeout, (long) MAPOperationCode.anyTimeInterrogation, req, true, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * MAPDialogSubscriberInformation#addAnyTimeInterrogationResponse(long)
     */
    public void addAnyTimeInterrogationResponse(long invokeId, SubscriberInfo subscriberInfo,
            MAPExtensionContainer extensionContainer) throws MAPException {
        doAddAnyTimeInterrogationResponse(false, invokeId, subscriberInfo, extensionContainer);
    }

    public void addAnyTimeInterrogationResponse_NonLast(long invokeId, SubscriberInfo subscriberInfo,
            MAPExtensionContainer extensionContainer) throws MAPException {
        doAddAnyTimeInterrogationResponse(true, invokeId, subscriberInfo, extensionContainer);
    }

    protected void doAddAnyTimeInterrogationResponse(boolean nonLast, long invokeId, SubscriberInfo subscriberInfo,
            MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.anyTimeEnquiryContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for AnyTimeInterrogationRequest: must be networkLocUpContext_V3");

        AnyTimeInterrogationResponseImpl req = new AnyTimeInterrogationResponseImpl(subscriberInfo, extensionContainer);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.anyTimeInterrogation, req, false, !nonLast);
    }

    public long addAnyTimeSubscriptionInterrogationRequest(SubscriberIdentity subscriberIdentity,
            RequestedSubscriptionInfo requestedSubscriptionInfo, ISDNAddressString gsmSCFAddress,
            MAPExtensionContainer extensionContainer, boolean isLongFTNSupported) throws MAPException {
        return this.addAnyTimeSubscriptionInterrogationRequest(_Timer_Default, subscriberIdentity, requestedSubscriptionInfo,
                gsmSCFAddress, extensionContainer, isLongFTNSupported);
    }

    public long addAnyTimeSubscriptionInterrogationRequest(int customInvokeTimeout, SubscriberIdentity subscriberIdentity,
            RequestedSubscriptionInfo requestedSubscriptionInfo, ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer,
            boolean isLongFTNSupported) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.anyTimeInfoHandlingContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for AnyTimeSubscriptionInterrogationRequest: must be anyTimeInfoHandlingContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        AnyTimeSubscriptionInterrogationRequestImpl req = new AnyTimeSubscriptionInterrogationRequestImpl(subscriberIdentity, requestedSubscriptionInfo, gsmSCFAddress, extensionContainer, isLongFTNSupported);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.anyTimeSubscriptionInterrogation, req, true, false);
    }

    public void addAnyTimeSubscriptionInterrogationResponse(long invokeId, CallForwardingData callForwardingData,
            CallBarringData callBarringData, ODBInfo odbInfo, CAMELSubscriptionInfo camelSubscriptionInfo,
            SupportedCamelPhases supportedVlrCamelPhases, SupportedCamelPhases supportedSgsnCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIsInVlr,
            OfferedCamel4CSIs offeredCamel4CSIsInSgsn, List<MSISDNBS> msisdnBsList,
            List<CSGSubscriptionData> csgSubscriptionDataList, CallWaitingData callWaitingData, CallHoldData callHoldData,
            ClipData clipData, ClirData clirData, EctData ectData) throws MAPException {
        doAddAnyTimeSubscriptionInterrogationResponse(false, invokeId, callForwardingData, callBarringData, odbInfo,
                camelSubscriptionInfo, supportedVlrCamelPhases, supportedSgsnCamelPhases, extensionContainer,
                offeredCamel4CSIsInVlr, offeredCamel4CSIsInSgsn, msisdnBsList, csgSubscriptionDataList, callWaitingData,
                callHoldData, clipData, clirData, ectData);
    }

    public void addAnyTimeSubscriptionInterrogationResponse_NonLast(long invokeId, CallForwardingData callForwardingData,
            CallBarringData callBarringData, ODBInfo odbInfo, CAMELSubscriptionInfo camelSubscriptionInfo,
            SupportedCamelPhases supportedVlrCamelPhases, SupportedCamelPhases supportedSgsnCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIsInVlr,
            OfferedCamel4CSIs offeredCamel4CSIsInSgsn, List<MSISDNBS> msisdnBsList,
            List<CSGSubscriptionData> csgSubscriptionDataList, CallWaitingData callWaitingData, CallHoldData callHoldData,
            ClipData clipData, ClirData clirData, EctData ectData) throws MAPException {
        doAddAnyTimeSubscriptionInterrogationResponse(true, invokeId, callForwardingData, callBarringData, odbInfo,
                camelSubscriptionInfo, supportedVlrCamelPhases, supportedSgsnCamelPhases, extensionContainer,
                offeredCamel4CSIsInVlr, offeredCamel4CSIsInSgsn, msisdnBsList, csgSubscriptionDataList, callWaitingData,
                callHoldData, clipData, clirData, ectData);
    }

    protected void doAddAnyTimeSubscriptionInterrogationResponse(boolean nonLast, long invokeId,
            CallForwardingData callForwardingData, CallBarringData callBarringData, ODBInfo odbInfo,
            CAMELSubscriptionInfo camelSubscriptionInfo, SupportedCamelPhases supportedVlrCamelPhases,
            SupportedCamelPhases supportedSgsnCamelPhases, MAPExtensionContainer extensionContainer,
            OfferedCamel4CSIs offeredCamel4CSIsInVlr, OfferedCamel4CSIs offeredCamel4CSIsInSgsn,
            List<MSISDNBS> msisdnBsList, List<CSGSubscriptionData> csgSubscriptionDataList,
            CallWaitingData callWaitingData, CallHoldData callHoldData, ClipData clipData, ClirData clirData, EctData ectData)
            throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.anyTimeInfoHandlingContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for AnyTimeSubscriptionInterrogationRequest: must be anyTimeInfoHandlingContext_V3");

        AnyTimeSubscriptionInterrogationResponseImpl req = new AnyTimeSubscriptionInterrogationResponseImpl(callForwardingData, callBarringData, odbInfo,
                camelSubscriptionInfo, supportedVlrCamelPhases, supportedSgsnCamelPhases, extensionContainer, offeredCamel4CSIsInVlr, offeredCamel4CSIsInSgsn,
                msisdnBsList, csgSubscriptionDataList, callWaitingData, callHoldData, clipData, clirData, ectData);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.anyTimeSubscriptionInterrogation, req, false, !nonLast);
    }

    @Override
    public long addProvideSubscriberInfoRequest(IMSI imsi, LMSI lmsi, RequestedInfo requestedInfo, MAPExtensionContainer extensionContainer,
            EMLPPPriority callPriority) throws MAPException {
        return this.addProvideSubscriberInfoRequest(_Timer_Default, imsi, lmsi, requestedInfo, extensionContainer, callPriority);
    }

    @Override
    public long addProvideSubscriberInfoRequest(long customInvokeTimeout, IMSI imsi, LMSI lmsi, RequestedInfo requestedInfo,
            MAPExtensionContainer extensionContainer, EMLPPPriority callPriority) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.subscriberInfoEnquiryContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException("Bad application context name for ProvideSubscriberInfoRequest: must be subscriberInfoEnquiryContext_V3");

        Long customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=(long)getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        ProvideSubscriberInfoRequestImpl req = new ProvideSubscriberInfoRequestImpl(imsi, lmsi, requestedInfo, extensionContainer, callPriority);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.provideSubscriberInfo, req, true, false);
    }

    @Override
    public void addProvideSubscriberInfoResponse(long invokeId, SubscriberInfo subscriberInfo,
            MAPExtensionContainer extensionContainer) throws MAPException {
        doAddProvideSubscriberInfoResponse(false, invokeId, subscriberInfo, extensionContainer);
    }

    @Override
    public void addProvideSubscriberInfoResponse_NonLast(long invokeId, SubscriberInfo subscriberInfo,
            MAPExtensionContainer extensionContainer) throws MAPException {
        doAddProvideSubscriberInfoResponse(true, invokeId, subscriberInfo, extensionContainer);
    }


    protected void doAddProvideSubscriberInfoResponse(boolean nonLast, long invokeId, SubscriberInfo subscriberInfo,
            MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.subscriberInfoEnquiryContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for ProvideSubscriberInfoResponse: must be subscriberInfoEnquiryContext_V3");

        ProvideSubscriberInfoResponseImpl req = new ProvideSubscriberInfoResponseImpl(subscriberInfo, extensionContainer);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.provideSubscriberInfo, req, false, !nonLast);
    }

    @Override
    public Long addCheckImeiRequest(IMEI imei) throws MAPException {

        return this.addCheckImeiRequest(_Timer_Default, imei);
    }

    @Override
    public Long addCheckImeiRequest(long customInvokeTimeout, IMEI imei) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.equipmentMngtContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)) {
            throw new MAPException(
                    "Bad application context name for CheckImeiRequest: must be equipmentMngtContext_V1, V2");
        }

        Long customTimeout;
        if (customInvokeTimeout == _Timer_Default) {
            customTimeout=(long)getMediumTimer();
        } else {
            customTimeout=customInvokeTimeout;
        }

        CheckImeiRequestImplV1 req = new CheckImeiRequestImplV1(this.appCntx.getApplicationContextVersion().getVersion(), imei, null);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.checkIMEI, req, true, false);
    }
    
    @Override
    public Long addCheckImeiRequest(IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer) throws MAPException {

        return this.addCheckImeiRequest(_Timer_Default, imei, requestedEquipmentInfo, extensionContainer);
    }

    @Override
    public Long addCheckImeiRequest(long customInvokeTimeout, IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.equipmentMngtContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3) {
            throw new MAPException(
                    "Bad application context name for CheckImeiRequest: must be equipmentMngtContext_V3");
        }

        Long customTimeout;
        if (customInvokeTimeout == _Timer_Default) {
            customTimeout=(long)getMediumTimer();
        } else {
            customTimeout=customInvokeTimeout;
        }

        CheckImeiRequestImplV3 req = new CheckImeiRequestImplV3(this.appCntx.getApplicationContextVersion().getVersion(), imei,
                requestedEquipmentInfo, extensionContainer);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.checkIMEI, req, true, false);
    }

    @Override
    public void addCheckImeiResponse(long invokeId, EquipmentStatus equipmentStatus) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.equipmentMngtContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)) {
            throw new MAPException(
                    "Bad application context name for CheckImeiResponse: must be equipmentMngtContext_V1, V2");
        }

        CheckImeiResponseImplV1 resp = new CheckImeiResponseImplV1(this.appCntx.getApplicationContextVersion().getVersion(),equipmentStatus);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.checkIMEI, resp, false, true);
    }
    
    @Override
    public void addCheckImeiResponse(long invokeId, EquipmentStatus equipmentStatus, UESBIIu bmuef,
            MAPExtensionContainer extensionContainer) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.equipmentMngtContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3) {
            throw new MAPException(
                    "Bad application context name for CheckImeiResponse: must be equipmentMngtContext_V3");
        }

        CheckImeiResponseImplV3 resp = new CheckImeiResponseImplV3(this.appCntx.getApplicationContextVersion().getVersion(),
                equipmentStatus, bmuef, extensionContainer);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.checkIMEI, resp, false, true);
    }

    @Override
    public Long addCheckImeiRequest_Huawei(IMEI imei, IMSI imsi) throws MAPException {

        return this.addCheckImeiRequest_Huawei(_Timer_Default, imei, imsi);
    }

    @Override
    public Long addCheckImeiRequest_Huawei(long customInvokeTimeout, IMEI imei, IMSI imsi) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.equipmentMngtContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)) {
            throw new MAPException(
                    "Bad application context name for CheckImeiRequest: must be equipmentMngtContext_V1, V2");
        }

        Long customTimeout;
        if (customInvokeTimeout == _Timer_Default) {
            customTimeout=(long)getMediumTimer();
        } else {
            customTimeout=customInvokeTimeout;
        }

        CheckImeiRequestImplV1 req = new CheckImeiRequestImplV1(this.appCntx.getApplicationContextVersion().getVersion(), imei, imsi);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.checkIMEI, req, true, false);
    }

    @Override
    public Long addInsertSubscriberDataRequest(IMSI imsi, ISDNAddressString msisdn, Category category,
            SubscriberStatus subscriberStatus, List<ExtBearerServiceCode> bearerServiceList,
            List<ExtTeleserviceCode> teleserviceList, List<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCode> regionalSubscriptionData,
            List<VoiceBroadcastData> vbsSubscriptionData, List<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) throws MAPException {

        return this.addInsertSubscriberDataRequest(_Timer_Default, imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData,
                vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo);
    }

    @Override
    public Long addInsertSubscriberDataRequest(long customInvokeTimeout, IMSI imsi, ISDNAddressString msisdn,
            Category category, SubscriberStatus subscriberStatus, List<ExtBearerServiceCode> bearerServiceList,
            List<ExtTeleserviceCode> teleserviceList, List<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCode> regionalSubscriptionData,
            List<VoiceBroadcastData> vbsSubscriptionData, List<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) throws MAPException {

        return this.addInsertSubscriberDataRequest(customInvokeTimeout, imsi, msisdn, category, subscriberStatus,
                bearerServiceList, teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature,
                regionalSubscriptionData, vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo, null, null,
                null, false, null, null, false, null, null, null, null, null, null, null, null, false, null, null, false, null,
                null, null, false, false, null);
    }

    @Override
    public Long addInsertSubscriberDataRequest(IMSI imsi, ISDNAddressString msisdn, Category category,
            SubscriberStatus subscriberStatus, List<ExtBearerServiceCode> bearerServiceList,
            List<ExtTeleserviceCode> teleserviceList, List<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCode> regionalSubscriptionData,
            List<VoiceBroadcastData> vbsSubscriptionData, List<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo, MAPExtensionContainer extensionContainer,
            NAEAPreferredCI naeaPreferredCI, GPRSSubscriptionData gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformation lsaInformation, boolean lmuIndicator, LCSInformation lcsInformation, Integer istAlertTimer,
            AgeIndicator superChargerSupportedInHLR, MCSSInfo mcSsInfo,
            CSAllocationRetentionPriority csAllocationRetentionPriority, SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo,
            ChargingCharacteristics chargingCharacteristics, AccessRestrictionData accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionData epsSubscriptionData, List<CSGSubscriptionData> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressString sgsnNumber, DiameterIdentity mmeName,
            Long subscribedPeriodicRAUTAUtimer, boolean vplmnLIPAAllowed, Boolean mdtUserConsent,
            Long subscribedPeriodicLAUtimer) throws MAPException {

        return this.addInsertSubscriberDataRequest(_Timer_Default, imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData,
                vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo, extensionContainer, naeaPreferredCI,
                gprsSubscriptionData, roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode, lsaInformation,
                lmuIndicator, lcsInformation, istAlertTimer, superChargerSupportedInHLR, mcSsInfo,
                csAllocationRetentionPriority, sgsnCamelSubscriptionInfo, chargingCharacteristics, accessRestrictionData,
                icsIndicator, epsSubscriptionData, csgSubscriptionDataList, ueReachabilityRequestIndicator, sgsnNumber,
                mmeName, subscribedPeriodicRAUTAUtimer, vplmnLIPAAllowed, mdtUserConsent, subscribedPeriodicLAUtimer);
    }

    @Override
    public Long addInsertSubscriberDataRequest(long customInvokeTimeout, IMSI imsi, ISDNAddressString msisdn,
            Category category, SubscriberStatus subscriberStatus, List<ExtBearerServiceCode> bearerServiceList,
            List<ExtTeleserviceCode> teleserviceList, List<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCode> regionalSubscriptionData,
            List<VoiceBroadcastData> vbsSubscriptionData, List<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo, MAPExtensionContainer extensionContainer,
            NAEAPreferredCI naeaPreferredCI, GPRSSubscriptionData gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformation lsaInformation, boolean lmuIndicator, LCSInformation lcsInformation, Integer istAlertTimer,
            AgeIndicator superChargerSupportedInHLR, MCSSInfo mcSsInfo,
            CSAllocationRetentionPriority csAllocationRetentionPriority, SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo,
            ChargingCharacteristics chargingCharacteristics, AccessRestrictionData accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionData epsSubscriptionData, List<CSGSubscriptionData> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressString sgsnNumber, DiameterIdentity mmeName,
            Long subscribedPeriodicRAUTAUtimer, boolean vplmnLIPAAllowed, Boolean mdtUserConsent,
            Long subscribedPeriodicLAUtimer) throws MAPException {

        boolean isSubscriberDataMngtContext = false;
        boolean isNetworkLocUpContext = false;
        boolean isGprsLocationUpdateContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.subscriberDataMngtContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx
                        .getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isSubscriberDataMngtContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.networkLocUpContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx
                        .getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isNetworkLocUpContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.gprsLocationUpdateContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isGprsLocationUpdateContext = true;
        if (isSubscriberDataMngtContext == false && isNetworkLocUpContext == false && isGprsLocationUpdateContext == false)
            throw new MAPException(
                    "Bad application context name for InsertSubscriberDataRequest: must be networkLocUpContext_V1, V2 or V3 or "
                            + "subscriberDataMngtContext_V1, V2 or V3 or gprsLocationUpdateContext_V3");

        Long customTimeout;
        if (customInvokeTimeout == _Timer_Default) {
            customTimeout=(long)getMediumTimer();
        } else {
            customTimeout=customInvokeTimeout;
        }

        InsertSubscriberDataRequestImpl req = new InsertSubscriberDataRequestImpl(this.appCntx.getApplicationContextVersion()
                .getVersion(), imsi, msisdn, category, subscriberStatus, bearerServiceList, teleserviceList, provisionedSS,
                odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData, vbsSubscriptionData,
                vgcsSubscriptionData, vlrCamelSubscriptionInfo, extensionContainer, naeaPreferredCI, gprsSubscriptionData,
                roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode, lsaInformation, lmuIndicator,
                lcsInformation, istAlertTimer, superChargerSupportedInHLR, mcSsInfo, csAllocationRetentionPriority,
                sgsnCamelSubscriptionInfo, chargingCharacteristics, accessRestrictionData, icsIndicator, epsSubscriptionData,
                csgSubscriptionDataList, ueReachabilityRequestIndicator, sgsnNumber, mmeName, subscribedPeriodicRAUTAUtimer,
                vplmnLIPAAllowed, mdtUserConsent, subscribedPeriodicLAUtimer);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.insertSubscriberData, req, true, false);
    }

    @Override
    public void addInsertSubscriberDataResponse(long invokeId, List<ExtTeleserviceCode> teleserviceList,
            List<ExtBearerServiceCode> bearerServiceList, List<SSCode> ssList, ODBGeneralData odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse) throws MAPException {

        this.addInsertSubscriberDataResponse(invokeId, teleserviceList, bearerServiceList, ssList, odbGeneralData,
                regionalSubscriptionResponse, null, null, null, null);
    }

    @Override
    public void addInsertSubscriberDataResponse(long invokeId, List<ExtTeleserviceCode> teleserviceList,
            List<ExtBearerServiceCode> bearerServiceList, List<SSCode> ssList, ODBGeneralData odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse, SupportedCamelPhases supportedCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIs, SupportedFeatures supportedFeatures)
            throws MAPException {

        boolean isSubscriberDataMngtContext = false;
        boolean isNetworkLocUpContext = false;
        boolean isGprsLocationUpdateContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.subscriberDataMngtContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx
                        .getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isSubscriberDataMngtContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.networkLocUpContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx
                        .getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isNetworkLocUpContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.gprsLocationUpdateContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isGprsLocationUpdateContext = true;
        if (isSubscriberDataMngtContext == false && isNetworkLocUpContext == false && isGprsLocationUpdateContext == false)
            throw new MAPException(
                    "Bad application context name for InsertSubscriberDataResponse: must be networkLocUpContext_V1, V2 or V3 or "
                            + "subscriberDataMngtContext_V1, V2 or V3 or gprsLocationUpdateContext_V3");

        InsertSubscriberDataResponseImpl resp = null;
        if ((teleserviceList != null || bearerServiceList != null || ssList != null || odbGeneralData != null || regionalSubscriptionResponse != null
                || supportedCamelPhases != null || extensionContainer != null || offeredCamel4CSIs != null || supportedFeatures != null)
                && this.appCntx.getApplicationContextVersion().getVersion() != 1)
            resp = new InsertSubscriberDataResponseImpl(this.appCntx.getApplicationContextVersion().getVersion(),
                    teleserviceList, bearerServiceList, ssList, odbGeneralData, regionalSubscriptionResponse, supportedCamelPhases, extensionContainer,
                    offeredCamel4CSIs, supportedFeatures);
        
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.insertSubscriberData, resp, false, true);
    }

    @Override
    public Long addDeleteSubscriberDataRequest(IMSI imsi, List<ExtBasicServiceCode> basicServiceList, List<SSCode> ssList,
            boolean roamingRestrictionDueToUnsupportedFeature, ZoneCode regionalSubscriptionIdentifier, boolean vbsGroupIndication,
            boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainer extensionContainer,
            GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
            LSAInformationWithdraw lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdraw specificCSIWithdraw,
            boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw,
            boolean apnOiReplacementWithdraw, boolean csgSubscriptionDeleted) throws MAPException {

        return this.addDeleteSubscriberDataRequest(_Timer_Default, imsi, basicServiceList, ssList, roamingRestrictionDueToUnsupportedFeature,
                regionalSubscriptionIdentifier, vbsGroupIndication, vgcsGroupIndication, camelSubscriptionInfoWithdraw, extensionContainer,
                gprsSubscriptionDataWithdraw, roamingRestrictedInSgsnDueToUnsuppportedFeature, lsaInformationWithdraw, gmlcListWithdraw,
                istInformationWithdraw, specificCSIWithdraw, chargingCharacteristicsWithdraw, stnSrWithdraw, epsSubscriptionDataWithdraw,
                apnOiReplacementWithdraw, csgSubscriptionDeleted);
    }

    @Override
    public Long addDeleteSubscriberDataRequest(long customInvokeTimeout, IMSI imsi, List<ExtBasicServiceCode> basicServiceList, List<SSCode> ssList,
            boolean roamingRestrictionDueToUnsupportedFeature, ZoneCode regionalSubscriptionIdentifier, boolean vbsGroupIndication,
            boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainer extensionContainer,
            GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
            LSAInformationWithdraw lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdraw specificCSIWithdraw,
            boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw,
            boolean apnOiReplacementWithdraw, boolean csgSubscriptionDeleted) throws MAPException {

        boolean isSubscriberDataMngtContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.subscriberDataMngtContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isSubscriberDataMngtContext = true;
        if (isSubscriberDataMngtContext == false)
            throw new MAPException("Bad application context name for DeleteSubscriberDataRequest: must be subscriberDataMngtContext_V1, V2 or V3");

        Long customTimeout;
        if (customInvokeTimeout == _Timer_Default) {
            customTimeout=(long)getMediumTimer();
        } else {
            customTimeout=customInvokeTimeout;
        }

        DeleteSubscriberDataRequestImpl req = new DeleteSubscriberDataRequestImpl(imsi, basicServiceList, ssList, roamingRestrictionDueToUnsupportedFeature,
                regionalSubscriptionIdentifier, vbsGroupIndication, vgcsGroupIndication, camelSubscriptionInfoWithdraw, extensionContainer,
                gprsSubscriptionDataWithdraw, roamingRestrictedInSgsnDueToUnsuppportedFeature, lsaInformationWithdraw, gmlcListWithdraw,
                istInformationWithdraw, specificCSIWithdraw, chargingCharacteristicsWithdraw, stnSrWithdraw, epsSubscriptionDataWithdraw,
                apnOiReplacementWithdraw, csgSubscriptionDeleted);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.deleteSubscriberData, req, true, false);
    }

    @Override
    public void addDeleteSubscriberDataResponse(long invokeId, RegionalSubscriptionResponse regionalSubscriptionResponse,
            MAPExtensionContainer extensionContainer) throws MAPException {

        boolean isSubscriberDataMngtContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.subscriberDataMngtContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isSubscriberDataMngtContext = true;
        if (isSubscriberDataMngtContext == false)
            throw new MAPException("Bad application context name for DeleteSubscriberDataResponse: must be subscriberDataMngtContext_V1, V2 or V3");

        DeleteSubscriberDataResponseImpl resp = null;
        if ((regionalSubscriptionResponse != null || extensionContainer != null) && this.appCntx.getApplicationContextVersion().getVersion() != 1)
            resp = new DeleteSubscriberDataResponseImpl(regionalSubscriptionResponse, extensionContainer);

        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.deleteSubscriberData, resp, false, true);
    }

    @Override
    public Long addCancelLocationRequest(IMSI imsi, IMSIWithLMSI imsiWithLmsi)
            throws MAPException {

        return this.addCancelLocationRequest(_Timer_Default, imsi, imsiWithLmsi);
    }

    @Override
    public Long addCancelLocationRequest(int customInvokeTimeout, IMSI imsi, IMSIWithLMSI imsiWithLmsi) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationCancellationContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2))
            throw new MAPException(
                    "Bad application context name for CancelLocationRequest: must be locationCancellationContext_V1, or V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        CancelLocationRequestImplV1 req = new CancelLocationRequestImplV1(imsi, imsiWithLmsi,this.appCntx.getApplicationContextVersion().getVersion());
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.cancelLocation, req, true, false);
    }
    
    @Override
    public Long addCancelLocationRequest(IMSI imsi, IMSIWithLMSI imsiWithLmsi, CancellationType cancellationType,
            MAPExtensionContainer extensionContainer, TypeOfUpdate typeOfUpdate, boolean mtrfSupportedAndAuthorized,
            boolean mtrfSupportedAndNotAuthorized, ISDNAddressString newMSCNumber, ISDNAddressString newVLRNumber, LMSI newLmsi)
            throws MAPException {

        return this.addCancelLocationRequest(_Timer_Default, imsi, imsiWithLmsi, cancellationType, extensionContainer,
                typeOfUpdate, mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi);
    }

    @Override
    public Long addCancelLocationRequest(int customInvokeTimeout, IMSI imsi, IMSIWithLMSI imsiWithLmsi,
            CancellationType cancellationType, MAPExtensionContainer extensionContainer, TypeOfUpdate typeOfUpdate,
            boolean mtrfSupportedAndAuthorized, boolean mtrfSupportedAndNotAuthorized, ISDNAddressString newMSCNumber,
            ISDNAddressString newVLRNumber, LMSI newLmsi) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationCancellationContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for CancelLocationRequest: must be locationCancellationContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        CancelLocationRequestImplV3 req = new CancelLocationRequestImplV3(imsi, imsiWithLmsi, cancellationType, extensionContainer,
                typeOfUpdate, mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi,
                this.appCntx.getApplicationContextVersion().getVersion());
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.cancelLocation, req, true, false);
    }

    @Override
    public void addCancelLocationResponse(long invokeId, MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationCancellationContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for CancelLocationResponse: must be locationCancellationContext_V1, V2 or V3");

        CancelLocationResponseImpl req = null;
        if (extensionContainer != null)
            req = new CancelLocationResponseImpl(extensionContainer);
            
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.cancelLocation, req, false, true);

    }

    @Override
    public Long addSendIdentificationRequest(TMSI tmsi, Integer numberOfRequestedVectors, boolean segmentationProhibited,
            MAPExtensionContainer extensionContainer, ISDNAddressString mscNumber, LAIFixedLength previousLAI,
            Integer hopCounter, boolean mtRoamingForwardingSupported, ISDNAddressString newVLRNumber, LMSI lmsi)
            throws MAPException {
        return this.addSendIdentificationRequest(_Timer_Default, tmsi, numberOfRequestedVectors, segmentationProhibited,
                extensionContainer, mscNumber, previousLAI, hopCounter, mtRoamingForwardingSupported, newVLRNumber, lmsi);
    }

    @Override
    public Long addSendIdentificationRequest(int customInvokeTimeout, TMSI tmsi) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.interVlrInfoRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2))
            throw new MAPException(
                    "Bad application context name for SendIdentificationRequest: must be interVlrInfoRetrievalContext_V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getShortTimer();
        else
            customTimeout=customInvokeTimeout;

        SendIdentificationRequestImplV1 req = new SendIdentificationRequestImplV1(tmsi, this.appCntx.getApplicationContextVersion().getVersion());
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.sendIdentification, req, true, false);
    }
    
    @Override
    public Long addSendIdentificationRequest(TMSI tmsi) throws MAPException {
        return this.addSendIdentificationRequest(_Timer_Default, tmsi);
    }

    @Override
    public Long addSendIdentificationRequest(int customInvokeTimeout, TMSI tmsi, Integer numberOfRequestedVectors,
            boolean segmentationProhibited, MAPExtensionContainer extensionContainer, ISDNAddressString mscNumber,
            LAIFixedLength previousLAI, Integer hopCounter, boolean mtRoamingForwardingSupported,
            ISDNAddressString newVLRNumber, LMSI lmsi) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.interVlrInfoRetrievalContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for SendIdentificationRequest: must be interVlrInfoRetrievalContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getShortTimer();
        else
            customTimeout=customInvokeTimeout;

        SendIdentificationRequestImplV3 req = new SendIdentificationRequestImplV3(tmsi, numberOfRequestedVectors,
                segmentationProhibited, extensionContainer, mscNumber, previousLAI, hopCounter, mtRoamingForwardingSupported,
                newVLRNumber, lmsi, this.appCntx.getApplicationContextVersion().getVersion());
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.sendIdentification, req, true, false);
    }

    @Override
    public void addSendIdentificationResponse(long invokeId, IMSI imsi, AuthenticationSetList authenticationSetList) throws MAPException {
        doAddSendIdentificationResponse(false, invokeId, imsi, authenticationSetList);
    }

    @Override
    public void addSendIdentificationResponse_NonLast(long invokeId, IMSI imsi, AuthenticationSetList authenticationSetList) throws MAPException {
        doAddSendIdentificationResponse(true, invokeId, imsi, authenticationSetList);
    }

    protected void doAddSendIdentificationResponse(boolean nonLast, long invokeId, IMSI imsi,AuthenticationSetList authenticationSetList) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.interVlrInfoRetrievalContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException(
                    "Bad application context name for AddSendIdentificationResponse: must be interVlrInfoRetrievalContext_V2");
        SendIdentificationResponseImplV1 req = new SendIdentificationResponseImplV1(imsi, authenticationSetList, this.appCntx.getApplicationContextVersion().getVersion());
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.sendIdentification, req, false, !nonLast);
    }
    
    @Override
    public void addSendIdentificationResponse(long invokeId, IMSI imsi, AuthenticationSetList authenticationSetList,
            CurrentSecurityContext currentSecurityContext, MAPExtensionContainer extensionContainer) throws MAPException {
        doAddSendIdentificationResponse(invokeId, imsi, authenticationSetList, currentSecurityContext,
                extensionContainer);
    }

    protected void doAddSendIdentificationResponse(long invokeId, IMSI imsi,
            AuthenticationSetList authenticationSetList, CurrentSecurityContext currentSecurityContext,
            MAPExtensionContainer extensionContainer) throws MAPException {
        if (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for AddSendIdentificationResponse: must be interVlrInfoRetrievalContext_V2 or V3");
        
        SendIdentificationResponseImplV3 req = new SendIdentificationResponseImplV3(imsi, authenticationSetList,
                currentSecurityContext, extensionContainer, this.appCntx.getApplicationContextVersion().getVersion());
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.sendIdentification, req, false, true);
    }

    @Override
    public Long addUpdateGprsLocationRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString sgsnNumber,
            GSNAddress sgsnAddress, MAPExtensionContainer extensionContainer, SGSNCapability sgsnCapability,
            boolean informPreviousNetworkEntity, boolean psLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo,
            EPSInfo epsInfo, boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability)
            throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.gprsLocationUpdateContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for UpdateGprsLocationRequest: must be gprsLocationUpdateContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        UpdateGprsLocationRequestImpl req = new UpdateGprsLocationRequestImpl(imsi, sgsnNumber, sgsnAddress,
                extensionContainer, sgsnCapability, informPreviousNetworkEntity, psLCSNotSupportedByUE, vGmlcAddress, addInfo,
                epsInfo, servingNodeTypeIndicator, skipSubscriberDataUpdate, usedRATType, gprsSubscriptionDataNotNeeded,
                nodeTypeIndicator, areaRestricted, ueReachableIndicator, epsSubscriptionDataNotNeeded, uesrvccCapability,
                this.appCntx.getApplicationContextVersion().getVersion());
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.updateGprsLocation, req, true, false);
    }

    @Override
    public Long addUpdateGprsLocationRequest(IMSI imsi, ISDNAddressString sgsnNumber, GSNAddress sgsnAddress,
            MAPExtensionContainer extensionContainer, SGSNCapability sgsnCapability, boolean informPreviousNetworkEntity,
            boolean psLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo, EPSInfo epsInfo,
            boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability)
            throws MAPException {
        return addUpdateGprsLocationRequest(_Timer_Default, imsi, sgsnNumber, sgsnAddress, extensionContainer, sgsnCapability,
                informPreviousNetworkEntity, psLCSNotSupportedByUE, vGmlcAddress, addInfo, epsInfo, servingNodeTypeIndicator,
                skipSubscriberDataUpdate, usedRATType, gprsSubscriptionDataNotNeeded, nodeTypeIndicator, areaRestricted,
                ueReachableIndicator, epsSubscriptionDataNotNeeded, uesrvccCapability);
    }

    @Override
    public void addUpdateGprsLocationResponse(long invokeId, ISDNAddressString hlrNumber,
            MAPExtensionContainer extensionContainer, boolean addCapability, boolean sgsnMmeSeparationSupported)
            throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.gprsLocationUpdateContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for UpdateGprsLocationResponse: must be gprsLocationUpdateContext_V3");

        UpdateGprsLocationResponseImpl req = new UpdateGprsLocationResponseImpl(hlrNumber, extensionContainer, addCapability,
                sgsnMmeSeparationSupported);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.updateGprsLocation, req, false, true);

    }

    @Override
    public Long addPurgeMSRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString vlrNumber) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.msPurgingContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException(
                    "Bad application context name for PurgeMSRequest: must be msPurgingContext_V2");
        
        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        PurgeMSRequestImplV1 req = new PurgeMSRequestImplV1(imsi, vlrNumber, this.appCntx.getApplicationContextVersion().getVersion());
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.purgeMS, req, true, false);
    }

    @Override
    public Long addPurgeMSRequest(IMSI imsi, ISDNAddressString vlrNumber) throws MAPException {
        return addPurgeMSRequest(_Timer_Default, imsi, vlrNumber);
    }
    
    @Override
    public Long addPurgeMSRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString vlrNumber,
    		ISDNAddressString sgsnNumber, MAPExtensionContainer extensionContainer) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.msPurgingContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for PurgeMSRequest: must be msPurgingContext_V3");
        
        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        PurgeMSRequestImplV3 req = new PurgeMSRequestImplV3(imsi, vlrNumber, sgsnNumber, extensionContainer, this.appCntx
                .getApplicationContextVersion().getVersion());
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.purgeMS, req, true, false);
    }

    @Override
    public Long addPurgeMSRequest(IMSI imsi, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber,
            MAPExtensionContainer extensionContainer) throws MAPException {
        return addPurgeMSRequest(_Timer_Default, imsi, vlrNumber, sgsnNumber, extensionContainer);
    }

    @Override
    public void addPurgeMSResponse(long invokeId, boolean freezeTMSI, boolean freezePTMSI,
            MAPExtensionContainer extensionContainer, boolean freezeMTMSI) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.msPurgingContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3) && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)))
            throw new MAPException("Bad application context name for PurgeMSResponse: must be msPurgingContext_V3 OR  msPurgingContext_V2");

        PurgeMSResponseImpl resp = null;        
        if (this.appCntx.getApplicationContextVersion().getVersion() >= 3 && (freezeTMSI || freezePTMSI || extensionContainer != null || freezeMTMSI))
            resp =new PurgeMSResponseImpl(freezeTMSI, freezePTMSI, extensionContainer, freezeMTMSI);

        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.purgeMS, resp, false, true);
    }

    @Override
    public Long addResetRequest(NetworkResource networkResource, ISDNAddressString hlrNumber, List<IMSI> hlrList) throws MAPException {
        return addResetRequest(_Timer_Default, networkResource, hlrNumber, hlrList);
    }

    @Override
    public Long addResetRequest(int customInvokeTimeout, NetworkResource networkResource, ISDNAddressString hlrNumber, List<IMSI> hlrList)
            throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.resetContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2) && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1)))
            throw new MAPException("Bad application context name for ResetRequest: must be resetContext_V1 or V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getMediumTimer();
        else
        	customTimeout = customInvokeTimeout;

        ResetRequestImpl req = new ResetRequestImpl(networkResource, hlrNumber, hlrList, this.appCntx.getApplicationContextVersion().getVersion());
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) MAPOperationCode.reset, req, true, false);
    }

    @Override
    public Long addForwardCheckSSIndicationRequest() throws MAPException {
        return addForwardCheckSSIndicationRequest(_Timer_Default);
    }

    @Override
    public Long addForwardCheckSSIndicationRequest(int customInvokeTimeout) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
                        && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2) && (this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version1)))
            throw new MAPException("Bad application context name for ForwardCheckSSIndicationRequest: must be networkLocUpContext_V1, V2 or V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getShortTimer();
        else
            customTimeout=customInvokeTimeout;

        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) MAPOperationCode.forwardCheckSsIndication, null, true, false);
    }

    @Override
    public Long addRestoreDataRequest(IMSI imsi, LMSI lmsi, VLRCapability vlrCapability, MAPExtensionContainer extensionContainer, boolean restorationIndicator)
            throws MAPException {
        return addRestoreDataRequest(_Timer_Default, imsi, lmsi, vlrCapability, extensionContainer, restorationIndicator);
    }

    @Override
    public Long addRestoreDataRequest(int customInvokeTimeout, IMSI imsi, LMSI lmsi, VLRCapability vlrCapability, MAPExtensionContainer extensionContainer,
            boolean restorationIndicator) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3) && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)))
            throw new MAPException("Bad application context name for RestoreDataRequest: must be networkLocUpContext_V2 or networkLocUpContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
            customTimeout=getMediumTimer();
        else
            customTimeout=customInvokeTimeout;

        RestoreDataRequestImpl req = new RestoreDataRequestImpl(imsi, lmsi, vlrCapability, extensionContainer, restorationIndicator);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.restoreData, req, true, false);
    }

    @Override
    public void addRestoreDataResponse(long invokeId, ISDNAddressString hlrNumber, boolean msNotReachable, MAPExtensionContainer extensionContainer)
            throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3) && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)))
            throw new MAPException("Bad application context name for RestoreDataResponse: must be networkLocUpContext_V2 or networkLocUpContext_V3");

        RestoreDataResponseImpl resp = new RestoreDataResponseImpl(hlrNumber, msNotReachable, extensionContainer);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.restoreData, resp, false, true);
    }

    @Override
    public Long addActivateTraceModeRequest(IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
            MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
            TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration)
            throws MAPException {
        return this.addActivateTraceModeRequest(_Timer_Default, imsi, traceReference, traceType, omcId, extensionContainer, traceReference2, traceDepthList,
                traceNeTypeList, traceInterfaceList, traceEventList, traceCollectionEntity, mdtConfiguration);
    }

    @Override
    public Long addActivateTraceModeRequest(int customInvokeTimeout, IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
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
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.activateTraceMode, req, true, false);
    }

    @Override
    public void addActivateTraceModeResponse(long invokeId, MAPExtensionContainer extensionContainer, boolean traceSupportIndicator) throws MAPException {
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

        ActivateTraceModeResponseImpl req = null;
        if ((traceSupportIndicator || extensionContainer != null) && this.appCntx.getApplicationContextVersion().getVersion() >= 3)
            req = new ActivateTraceModeResponseImpl(extensionContainer, traceSupportIndicator);
            
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.activateTraceMode, req, false, true);
    }
}