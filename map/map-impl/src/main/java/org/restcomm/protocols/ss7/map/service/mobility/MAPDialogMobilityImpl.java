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
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.TMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AccessType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContextImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.FailureCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIuImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingAreaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EctDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AgeIndicatorImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriorityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CategoryImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataWithdrawImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdrawImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationWithdrawImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdrawImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfigurationImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference2Impl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReferenceImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
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
            MAPServiceMobility mapService, AddressStringImpl origReference, AddressStringImpl destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }

	public Long addSendAuthenticationInfoRequest(IMSIImpl imsi) throws MAPException {
        return this.addSendAuthenticationInfoRequest(_Timer_Default, imsi);
    }

    public Long addSendAuthenticationInfoRequest(int customInvokeTimeout, IMSIImpl imsi) throws MAPException {

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
    
    public Long addSendAuthenticationInfoRequest(IMSIImpl imsi, int numberOfRequestedVectors, boolean segmentationProhibited,
            boolean immediateResponsePreferred, ReSynchronisationInfoImpl reSynchronisationInfo,
            MAPExtensionContainerImpl extensionContainer, RequestingNodeType requestingNodeType, PlmnIdImpl requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException {
        return this.addSendAuthenticationInfoRequest(_Timer_Default, imsi, numberOfRequestedVectors, segmentationProhibited,
                immediateResponsePreferred, reSynchronisationInfo, extensionContainer, requestingNodeType, requestingPlmnId,
                numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS);
    }

    public Long addSendAuthenticationInfoRequest(int customInvokeTimeout, IMSIImpl imsi, int numberOfRequestedVectors,
            boolean segmentationProhibited, boolean immediateResponsePreferred, ReSynchronisationInfoImpl reSynchronisationInfo,
            MAPExtensionContainerImpl extensionContainer, RequestingNodeType requestingNodeType, PlmnIdImpl requestingPlmnId,
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

    public void addSendAuthenticationInfoResponse(long invokeId, AuthenticationSetListImpl authenticationSetList) throws MAPException {
        doAddSendAuthenticationInfoResponse(false, invokeId, authenticationSetList);
    }

    public void addSendAuthenticationInfoResponse_NonLast(long invokeId, AuthenticationSetListImpl authenticationSetList) throws MAPException {
        doAddSendAuthenticationInfoResponse(true, invokeId, authenticationSetList);
    }

    protected void doAddSendAuthenticationInfoResponse(boolean nonLast, long invokeId,
            AuthenticationSetListImpl authenticationSetList) throws MAPException {

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
    
    public void addSendAuthenticationInfoResponse(long invokeId, AuthenticationSetListImpl authenticationSetList,
            MAPExtensionContainerImpl extensionContainer, EpsAuthenticationSetListImpl epsAuthenticationSetList) throws MAPException {
        doAddSendAuthenticationInfoResponse(invokeId, authenticationSetList, extensionContainer,
                epsAuthenticationSetList);
    }

    protected void doAddSendAuthenticationInfoResponse(long invokeId,
            AuthenticationSetListImpl authenticationSetList, MAPExtensionContainerImpl extensionContainer,
            EpsAuthenticationSetListImpl epsAuthenticationSetList) throws MAPException {

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
    public Long addAuthenticationFailureReportRequest(IMSIImpl imsi, FailureCause failureCause, MAPExtensionContainerImpl extensionContainer, Boolean reAttempt,
            AccessType accessType, byte[] rand, ISDNAddressStringImpl vlrNumber, ISDNAddressStringImpl sgsnNumber) throws MAPException {
        return this.addAuthenticationFailureReportRequest(_Timer_Default, imsi, failureCause, extensionContainer, reAttempt, accessType, rand, vlrNumber,
                sgsnNumber);
    }

    @Override
    public Long addAuthenticationFailureReportRequest(int customInvokeTimeout, IMSIImpl imsi, FailureCause failureCause, MAPExtensionContainerImpl extensionContainer,
            Boolean reAttempt, AccessType accessType, byte[] rand, ISDNAddressStringImpl vlrNumber, ISDNAddressStringImpl sgsnNumber) throws MAPException {

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
    public void addAuthenticationFailureReportResponse(long invokeId, MAPExtensionContainerImpl extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.authenticationFailureReportContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException("Bad application context name for authenticationFailureReportResponse: must be authenticationFailureReportContext_V3");

        AuthenticationFailureReportResponseImpl req=null;
        if (extensionContainer != null)
            req = new AuthenticationFailureReportResponseImpl(extensionContainer);
            
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.authenticationFailureReport, req, false, true);
    }

    public Long addUpdateLocationRequest(IMSIImpl imsi, ISDNAddressStringImpl mscNumber, ISDNAddressStringImpl roamingNumber,
            ISDNAddressStringImpl vlrNumber, LMSIImpl lmsi, MAPExtensionContainerImpl extensionContainer, VLRCapabilityImpl vlrCapability,
            boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE, GSNAddressImpl vGmlcAddress, ADDInfoImpl addInfo,
            PagingAreaImpl pagingArea, boolean skipSubscriberDataUpdate, boolean restorationIndicator) throws MAPException {
        return addUpdateLocationRequest(_Timer_Default, imsi, mscNumber, roamingNumber, vlrNumber, lmsi, extensionContainer,
                vlrCapability, informPreviousNetworkEntity, csLCSNotSupportedByUE, vGmlcAddress, addInfo, pagingArea,
                skipSubscriberDataUpdate, restorationIndicator);
    }

    public Long addUpdateLocationRequest(int customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl mscNumber,
            ISDNAddressStringImpl roamingNumber, ISDNAddressStringImpl vlrNumber, LMSIImpl lmsi, MAPExtensionContainerImpl extensionContainer,
            VLRCapabilityImpl vlrCapability, boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE,
            GSNAddressImpl vGmlcAddress, ADDInfoImpl addInfo, PagingAreaImpl pagingArea, boolean skipSubscriberDataUpdate,
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

    public void addUpdateLocationResponse(long invokeId, ISDNAddressStringImpl hlrNumber) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1)
            throw new MAPException(
                    "Bad application context name for UpdateLocationResponse: must be networkLocUpContext_V1");

        UpdateLocationResponseImplV1 req = new UpdateLocationResponseImplV1(this.appCntx.getApplicationContextVersion().getVersion(), hlrNumber);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.updateLocation, req, false, true);
    }
    
    public void addUpdateLocationResponse(long invokeId, ISDNAddressStringImpl hlrNumber, MAPExtensionContainerImpl extensionContainer,
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
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl)
     */
    public long addAnyTimeInterrogationRequest(SubscriberIdentityImpl subscriberIdentity, RequestedInfoImpl requestedInfo,
            ISDNAddressStringImpl gsmSCFAddress, MAPExtensionContainerImpl extensionContainer) throws MAPException {

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
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl)
     */
    public long addAnyTimeInterrogationRequest(long customInvokeTimeout, SubscriberIdentityImpl subscriberIdentity,
            RequestedInfoImpl requestedInfo, ISDNAddressStringImpl gsmSCFAddress, MAPExtensionContainerImpl extensionContainer)
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
    public void addAnyTimeInterrogationResponse(long invokeId, SubscriberInfoImpl subscriberInfo,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {
        doAddAnyTimeInterrogationResponse(false, invokeId, subscriberInfo, extensionContainer);
    }

    public void addAnyTimeInterrogationResponse_NonLast(long invokeId, SubscriberInfoImpl subscriberInfo,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {
        doAddAnyTimeInterrogationResponse(true, invokeId, subscriberInfo, extensionContainer);
    }

    protected void doAddAnyTimeInterrogationResponse(boolean nonLast, long invokeId, SubscriberInfoImpl subscriberInfo,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.anyTimeEnquiryContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for AnyTimeInterrogationRequest: must be networkLocUpContext_V3");

        AnyTimeInterrogationResponseImpl req = new AnyTimeInterrogationResponseImpl(subscriberInfo, extensionContainer);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.anyTimeInterrogation, req, false, !nonLast);
    }

    public long addAnyTimeSubscriptionInterrogationRequest(SubscriberIdentityImpl subscriberIdentity,
            RequestedSubscriptionInfoImpl requestedSubscriptionInfo, ISDNAddressStringImpl gsmSCFAddress,
            MAPExtensionContainerImpl extensionContainer, boolean isLongFTNSupported) throws MAPException {
        return this.addAnyTimeSubscriptionInterrogationRequest(_Timer_Default, subscriberIdentity, requestedSubscriptionInfo,
                gsmSCFAddress, extensionContainer, isLongFTNSupported);
    }

    public long addAnyTimeSubscriptionInterrogationRequest(int customInvokeTimeout, SubscriberIdentityImpl subscriberIdentity,
            RequestedSubscriptionInfoImpl requestedSubscriptionInfo, ISDNAddressStringImpl gsmSCFAddress, MAPExtensionContainerImpl extensionContainer,
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

    public void addAnyTimeSubscriptionInterrogationResponse(long invokeId, CallForwardingDataImpl callForwardingData,
            CallBarringDataImpl callBarringData, ODBInfoImpl odbInfo, CAMELSubscriptionInfoImpl camelSubscriptionInfo,
            SupportedCamelPhasesImpl supportedVlrCamelPhases, SupportedCamelPhasesImpl supportedSgsnCamelPhases,
            MAPExtensionContainerImpl extensionContainer, OfferedCamel4CSIsImpl offeredCamel4CSIsInVlr,
            OfferedCamel4CSIsImpl offeredCamel4CSIsInSgsn, List<MSISDNBSImpl> msisdnBsList,
            List<CSGSubscriptionDataImpl> csgSubscriptionDataList, CallWaitingDataImpl callWaitingData, CallHoldDataImpl callHoldData,
            ClipDataImpl clipData, ClirDataImpl clirData, EctDataImpl ectData) throws MAPException {
        doAddAnyTimeSubscriptionInterrogationResponse(false, invokeId, callForwardingData, callBarringData, odbInfo,
                camelSubscriptionInfo, supportedVlrCamelPhases, supportedSgsnCamelPhases, extensionContainer,
                offeredCamel4CSIsInVlr, offeredCamel4CSIsInSgsn, msisdnBsList, csgSubscriptionDataList, callWaitingData,
                callHoldData, clipData, clirData, ectData);
    }

    public void addAnyTimeSubscriptionInterrogationResponse_NonLast(long invokeId, CallForwardingDataImpl callForwardingData,
            CallBarringDataImpl callBarringData, ODBInfoImpl odbInfo, CAMELSubscriptionInfoImpl camelSubscriptionInfo,
            SupportedCamelPhasesImpl supportedVlrCamelPhases, SupportedCamelPhasesImpl supportedSgsnCamelPhases,
            MAPExtensionContainerImpl extensionContainer, OfferedCamel4CSIsImpl offeredCamel4CSIsInVlr,
            OfferedCamel4CSIsImpl offeredCamel4CSIsInSgsn, List<MSISDNBSImpl> msisdnBsList,
            List<CSGSubscriptionDataImpl> csgSubscriptionDataList, CallWaitingDataImpl callWaitingData, CallHoldDataImpl callHoldData,
            ClipDataImpl clipData, ClirDataImpl clirData, EctDataImpl ectData) throws MAPException {
        doAddAnyTimeSubscriptionInterrogationResponse(true, invokeId, callForwardingData, callBarringData, odbInfo,
                camelSubscriptionInfo, supportedVlrCamelPhases, supportedSgsnCamelPhases, extensionContainer,
                offeredCamel4CSIsInVlr, offeredCamel4CSIsInSgsn, msisdnBsList, csgSubscriptionDataList, callWaitingData,
                callHoldData, clipData, clirData, ectData);
    }

    protected void doAddAnyTimeSubscriptionInterrogationResponse(boolean nonLast, long invokeId,
            CallForwardingDataImpl callForwardingData, CallBarringDataImpl callBarringData, ODBInfoImpl odbInfo,
            CAMELSubscriptionInfoImpl camelSubscriptionInfo, SupportedCamelPhasesImpl supportedVlrCamelPhases,
            SupportedCamelPhasesImpl supportedSgsnCamelPhases, MAPExtensionContainerImpl extensionContainer,
            OfferedCamel4CSIsImpl offeredCamel4CSIsInVlr, OfferedCamel4CSIsImpl offeredCamel4CSIsInSgsn,
            List<MSISDNBSImpl> msisdnBsList, List<CSGSubscriptionDataImpl> csgSubscriptionDataList,
            CallWaitingDataImpl callWaitingData, CallHoldDataImpl callHoldData, ClipDataImpl clipData, ClirDataImpl clirData, EctDataImpl ectData)
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
    public long addProvideSubscriberInfoRequest(IMSIImpl imsi, LMSIImpl lmsi, RequestedInfoImpl requestedInfo, MAPExtensionContainerImpl extensionContainer,
            EMLPPPriority callPriority) throws MAPException {
        return this.addProvideSubscriberInfoRequest(_Timer_Default, imsi, lmsi, requestedInfo, extensionContainer, callPriority);
    }

    @Override
    public long addProvideSubscriberInfoRequest(long customInvokeTimeout, IMSIImpl imsi, LMSIImpl lmsi, RequestedInfoImpl requestedInfo,
            MAPExtensionContainerImpl extensionContainer, EMLPPPriority callPriority) throws MAPException {

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
    public void addProvideSubscriberInfoResponse(long invokeId, SubscriberInfoImpl subscriberInfo,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {
        doAddProvideSubscriberInfoResponse(false, invokeId, subscriberInfo, extensionContainer);
    }

    @Override
    public void addProvideSubscriberInfoResponse_NonLast(long invokeId, SubscriberInfoImpl subscriberInfo,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {
        doAddProvideSubscriberInfoResponse(true, invokeId, subscriberInfo, extensionContainer);
    }


    protected void doAddProvideSubscriberInfoResponse(boolean nonLast, long invokeId, SubscriberInfoImpl subscriberInfo,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.subscriberInfoEnquiryContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for ProvideSubscriberInfoResponse: must be subscriberInfoEnquiryContext_V3");

        ProvideSubscriberInfoResponseImpl req = new ProvideSubscriberInfoResponseImpl(subscriberInfo, extensionContainer);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.provideSubscriberInfo, req, false, !nonLast);
    }

    @Override
    public Long addCheckImeiRequest(IMEIImpl imei) throws MAPException {

        return this.addCheckImeiRequest(_Timer_Default, imei);
    }

    @Override
    public Long addCheckImeiRequest(long customInvokeTimeout, IMEIImpl imei) throws MAPException {

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
    public Long addCheckImeiRequest(IMEIImpl imei, RequestedEquipmentInfoImpl requestedEquipmentInfo,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {

        return this.addCheckImeiRequest(_Timer_Default, imei, requestedEquipmentInfo, extensionContainer);
    }

    @Override
    public Long addCheckImeiRequest(long customInvokeTimeout, IMEIImpl imei, RequestedEquipmentInfoImpl requestedEquipmentInfo,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {

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
    public void addCheckImeiResponse(long invokeId, EquipmentStatus equipmentStatus, UESBIIuImpl bmuef,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {
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
    public Long addCheckImeiRequest_Huawei(IMEIImpl imei, IMSIImpl imsi) throws MAPException {

        return this.addCheckImeiRequest_Huawei(_Timer_Default, imei, imsi);
    }

    @Override
    public Long addCheckImeiRequest_Huawei(long customInvokeTimeout, IMEIImpl imei, IMSIImpl imsi) throws MAPException {

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
    public Long addInsertSubscriberDataRequest(IMSIImpl imsi, ISDNAddressStringImpl msisdn, CategoryImpl category,
            SubscriberStatus subscriberStatus, List<ExtBearerServiceCodeImpl> bearerServiceList,
            List<ExtTeleserviceCodeImpl> teleserviceList, List<ExtSSInfoImpl> provisionedSS, ODBDataImpl odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCodeImpl> regionalSubscriptionData,
            List<VoiceBroadcastDataImpl> vbsSubscriptionData, List<VoiceGroupCallDataImpl> vgcsSubscriptionData,
            VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo) throws MAPException {

        return this.addInsertSubscriberDataRequest(_Timer_Default, imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData,
                vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo);
    }

    @Override
    public Long addInsertSubscriberDataRequest(long customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl msisdn,
            CategoryImpl category, SubscriberStatus subscriberStatus, List<ExtBearerServiceCodeImpl> bearerServiceList,
            List<ExtTeleserviceCodeImpl> teleserviceList, List<ExtSSInfoImpl> provisionedSS, ODBDataImpl odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCodeImpl> regionalSubscriptionData,
            List<VoiceBroadcastDataImpl> vbsSubscriptionData, List<VoiceGroupCallDataImpl> vgcsSubscriptionData,
            VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo) throws MAPException {

        return this.addInsertSubscriberDataRequest(customInvokeTimeout, imsi, msisdn, category, subscriberStatus,
                bearerServiceList, teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature,
                regionalSubscriptionData, vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo, null, null,
                null, false, null, null, false, null, null, null, null, null, null, null, null, false, null, null, false, null,
                null, null, false, false, null);
    }

    @Override
    public Long addInsertSubscriberDataRequest(IMSIImpl imsi, ISDNAddressStringImpl msisdn, CategoryImpl category,
            SubscriberStatus subscriberStatus, List<ExtBearerServiceCodeImpl> bearerServiceList,
            List<ExtTeleserviceCodeImpl> teleserviceList, List<ExtSSInfoImpl> provisionedSS, ODBDataImpl odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCodeImpl> regionalSubscriptionData,
            List<VoiceBroadcastDataImpl> vbsSubscriptionData, List<VoiceGroupCallDataImpl> vgcsSubscriptionData,
            VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo, MAPExtensionContainerImpl extensionContainer,
            NAEAPreferredCIImpl naeaPreferredCI, GPRSSubscriptionDataImpl gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformationImpl lsaInformation, boolean lmuIndicator, LCSInformationImpl lcsInformation, Integer istAlertTimer,
            AgeIndicatorImpl superChargerSupportedInHLR, MCSSInfoImpl mcSsInfo,
            CSAllocationRetentionPriorityImpl csAllocationRetentionPriority, SGSNCAMELSubscriptionInfoImpl sgsnCamelSubscriptionInfo,
            ChargingCharacteristicsImpl chargingCharacteristics, AccessRestrictionDataImpl accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionDataImpl epsSubscriptionData, List<CSGSubscriptionDataImpl> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressStringImpl sgsnNumber, DiameterIdentityImpl mmeName,
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
    public Long addInsertSubscriberDataRequest(long customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl msisdn,
            CategoryImpl category, SubscriberStatus subscriberStatus, List<ExtBearerServiceCodeImpl> bearerServiceList,
            List<ExtTeleserviceCodeImpl> teleserviceList, List<ExtSSInfoImpl> provisionedSS, ODBDataImpl odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCodeImpl> regionalSubscriptionData,
            List<VoiceBroadcastDataImpl> vbsSubscriptionData, List<VoiceGroupCallDataImpl> vgcsSubscriptionData,
            VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo, MAPExtensionContainerImpl extensionContainer,
            NAEAPreferredCIImpl naeaPreferredCI, GPRSSubscriptionDataImpl gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformationImpl lsaInformation, boolean lmuIndicator, LCSInformationImpl lcsInformation, Integer istAlertTimer,
            AgeIndicatorImpl superChargerSupportedInHLR, MCSSInfoImpl mcSsInfo,
            CSAllocationRetentionPriorityImpl csAllocationRetentionPriority, SGSNCAMELSubscriptionInfoImpl sgsnCamelSubscriptionInfo,
            ChargingCharacteristicsImpl chargingCharacteristics, AccessRestrictionDataImpl accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionDataImpl epsSubscriptionData, List<CSGSubscriptionDataImpl> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressStringImpl sgsnNumber, DiameterIdentityImpl mmeName,
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
    public void addInsertSubscriberDataResponse(long invokeId, List<ExtTeleserviceCodeImpl> teleserviceList,
            List<ExtBearerServiceCodeImpl> bearerServiceList, List<SSCodeImpl> ssList, ODBGeneralDataImpl odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse) throws MAPException {

        this.addInsertSubscriberDataResponse(invokeId, teleserviceList, bearerServiceList, ssList, odbGeneralData,
                regionalSubscriptionResponse, null, null, null, null);
    }

    @Override
    public void addInsertSubscriberDataResponse(long invokeId, List<ExtTeleserviceCodeImpl> teleserviceList,
            List<ExtBearerServiceCodeImpl> bearerServiceList, List<SSCodeImpl> ssList, ODBGeneralDataImpl odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse, SupportedCamelPhasesImpl supportedCamelPhases,
            MAPExtensionContainerImpl extensionContainer, OfferedCamel4CSIsImpl offeredCamel4CSIs, SupportedFeaturesImpl supportedFeatures)
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
    public Long addDeleteSubscriberDataRequest(IMSIImpl imsi, List<ExtBasicServiceCodeImpl> basicServiceList, List<SSCodeImpl> ssList,
            boolean roamingRestrictionDueToUnsupportedFeature, ZoneCodeImpl regionalSubscriptionIdentifier, boolean vbsGroupIndication,
            boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainerImpl extensionContainer,
            GPRSSubscriptionDataWithdrawImpl gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
            LSAInformationWithdrawImpl lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdrawImpl specificCSIWithdraw,
            boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdrawImpl epsSubscriptionDataWithdraw,
            boolean apnOiReplacementWithdraw, boolean csgSubscriptionDeleted) throws MAPException {

        return this.addDeleteSubscriberDataRequest(_Timer_Default, imsi, basicServiceList, ssList, roamingRestrictionDueToUnsupportedFeature,
                regionalSubscriptionIdentifier, vbsGroupIndication, vgcsGroupIndication, camelSubscriptionInfoWithdraw, extensionContainer,
                gprsSubscriptionDataWithdraw, roamingRestrictedInSgsnDueToUnsuppportedFeature, lsaInformationWithdraw, gmlcListWithdraw,
                istInformationWithdraw, specificCSIWithdraw, chargingCharacteristicsWithdraw, stnSrWithdraw, epsSubscriptionDataWithdraw,
                apnOiReplacementWithdraw, csgSubscriptionDeleted);
    }

    @Override
    public Long addDeleteSubscriberDataRequest(long customInvokeTimeout, IMSIImpl imsi, List<ExtBasicServiceCodeImpl> basicServiceList, List<SSCodeImpl> ssList,
            boolean roamingRestrictionDueToUnsupportedFeature, ZoneCodeImpl regionalSubscriptionIdentifier, boolean vbsGroupIndication,
            boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainerImpl extensionContainer,
            GPRSSubscriptionDataWithdrawImpl gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
            LSAInformationWithdrawImpl lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdrawImpl specificCSIWithdraw,
            boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdrawImpl epsSubscriptionDataWithdraw,
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
            MAPExtensionContainerImpl extensionContainer) throws MAPException {

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
    public Long addCancelLocationRequest(IMSIImpl imsi, IMSIWithLMSIImpl imsiWithLmsi)
            throws MAPException {

        return this.addCancelLocationRequest(_Timer_Default, imsi, imsiWithLmsi);
    }

    @Override
    public Long addCancelLocationRequest(int customInvokeTimeout, IMSIImpl imsi, IMSIWithLMSIImpl imsiWithLmsi) throws MAPException {

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
    public Long addCancelLocationRequest(IMSIImpl imsi, IMSIWithLMSIImpl imsiWithLmsi, CancellationType cancellationType,
            MAPExtensionContainerImpl extensionContainer, TypeOfUpdate typeOfUpdate, boolean mtrfSupportedAndAuthorized,
            boolean mtrfSupportedAndNotAuthorized, ISDNAddressStringImpl newMSCNumber, ISDNAddressStringImpl newVLRNumber, LMSIImpl newLmsi)
            throws MAPException {

        return this.addCancelLocationRequest(_Timer_Default, imsi, imsiWithLmsi, cancellationType, extensionContainer,
                typeOfUpdate, mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi);
    }

    @Override
    public Long addCancelLocationRequest(int customInvokeTimeout, IMSIImpl imsi, IMSIWithLMSIImpl imsiWithLmsi,
            CancellationType cancellationType, MAPExtensionContainerImpl extensionContainer, TypeOfUpdate typeOfUpdate,
            boolean mtrfSupportedAndAuthorized, boolean mtrfSupportedAndNotAuthorized, ISDNAddressStringImpl newMSCNumber,
            ISDNAddressStringImpl newVLRNumber, LMSIImpl newLmsi) throws MAPException {

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
    public void addCancelLocationResponse(long invokeId, MAPExtensionContainerImpl extensionContainer) throws MAPException {

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
    public Long addSendIdentificationRequest(TMSIImpl tmsi, Integer numberOfRequestedVectors, boolean segmentationProhibited,
            MAPExtensionContainerImpl extensionContainer, ISDNAddressStringImpl mscNumber, LAIFixedLengthImpl previousLAI,
            Integer hopCounter, boolean mtRoamingForwardingSupported, ISDNAddressStringImpl newVLRNumber, LMSIImpl lmsi)
            throws MAPException {
        return this.addSendIdentificationRequest(_Timer_Default, tmsi, numberOfRequestedVectors, segmentationProhibited,
                extensionContainer, mscNumber, previousLAI, hopCounter, mtRoamingForwardingSupported, newVLRNumber, lmsi);
    }

    @Override
    public Long addSendIdentificationRequest(int customInvokeTimeout, TMSIImpl tmsi) throws MAPException {

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
    public Long addSendIdentificationRequest(TMSIImpl tmsi) throws MAPException {
        return this.addSendIdentificationRequest(_Timer_Default, tmsi);
    }

    @Override
    public Long addSendIdentificationRequest(int customInvokeTimeout, TMSIImpl tmsi, Integer numberOfRequestedVectors,
            boolean segmentationProhibited, MAPExtensionContainerImpl extensionContainer, ISDNAddressStringImpl mscNumber,
            LAIFixedLengthImpl previousLAI, Integer hopCounter, boolean mtRoamingForwardingSupported,
            ISDNAddressStringImpl newVLRNumber, LMSIImpl lmsi) throws MAPException {

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
    public void addSendIdentificationResponse(long invokeId, IMSIImpl imsi, AuthenticationSetListImpl authenticationSetList) throws MAPException {
        doAddSendIdentificationResponse(false, invokeId, imsi, authenticationSetList);
    }

    @Override
    public void addSendIdentificationResponse_NonLast(long invokeId, IMSIImpl imsi, AuthenticationSetListImpl authenticationSetList) throws MAPException {
        doAddSendIdentificationResponse(true, invokeId, imsi, authenticationSetList);
    }

    protected void doAddSendIdentificationResponse(boolean nonLast, long invokeId, IMSIImpl imsi,AuthenticationSetListImpl authenticationSetList) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.interVlrInfoRetrievalContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException(
                    "Bad application context name for AddSendIdentificationResponse: must be interVlrInfoRetrievalContext_V2");
        SendIdentificationResponseImplV1 req = new SendIdentificationResponseImplV1(imsi, authenticationSetList, this.appCntx.getApplicationContextVersion().getVersion());
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.sendIdentification, req, false, !nonLast);
    }
    
    @Override
    public void addSendIdentificationResponse(long invokeId, IMSIImpl imsi, AuthenticationSetListImpl authenticationSetList,
            CurrentSecurityContextImpl currentSecurityContext, MAPExtensionContainerImpl extensionContainer) throws MAPException {
        doAddSendIdentificationResponse(invokeId, imsi, authenticationSetList, currentSecurityContext,
                extensionContainer);
    }

    protected void doAddSendIdentificationResponse(long invokeId, IMSIImpl imsi,
            AuthenticationSetListImpl authenticationSetList, CurrentSecurityContextImpl currentSecurityContext,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {
        if (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for AddSendIdentificationResponse: must be interVlrInfoRetrievalContext_V2 or V3");
        
        SendIdentificationResponseImplV3 req = new SendIdentificationResponseImplV3(imsi, authenticationSetList,
                currentSecurityContext, extensionContainer, this.appCntx.getApplicationContextVersion().getVersion());
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.sendIdentification, req, false, true);
    }

    @Override
    public Long addUpdateGprsLocationRequest(int customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl sgsnNumber,
            GSNAddressImpl sgsnAddress, MAPExtensionContainerImpl extensionContainer, SGSNCapabilityImpl sgsnCapability,
            boolean informPreviousNetworkEntity, boolean psLCSNotSupportedByUE, GSNAddressImpl vGmlcAddress, ADDInfoImpl addInfo,
            EPSInfoImpl epsInfo, boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
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
    public Long addUpdateGprsLocationRequest(IMSIImpl imsi, ISDNAddressStringImpl sgsnNumber, GSNAddressImpl sgsnAddress,
            MAPExtensionContainerImpl extensionContainer, SGSNCapabilityImpl sgsnCapability, boolean informPreviousNetworkEntity,
            boolean psLCSNotSupportedByUE, GSNAddressImpl vGmlcAddress, ADDInfoImpl addInfo, EPSInfoImpl epsInfo,
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
    public void addUpdateGprsLocationResponse(long invokeId, ISDNAddressStringImpl hlrNumber,
            MAPExtensionContainerImpl extensionContainer, boolean addCapability, boolean sgsnMmeSeparationSupported)
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
    public Long addPurgeMSRequest(int customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl vlrNumber) throws MAPException {
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
    public Long addPurgeMSRequest(IMSIImpl imsi, ISDNAddressStringImpl vlrNumber) throws MAPException {
        return addPurgeMSRequest(_Timer_Default, imsi, vlrNumber);
    }
    
    @Override
    public Long addPurgeMSRequest(int customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl vlrNumber,
            ISDNAddressStringImpl sgsnNumber, MAPExtensionContainerImpl extensionContainer) throws MAPException {
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
    public Long addPurgeMSRequest(IMSIImpl imsi, ISDNAddressStringImpl vlrNumber, ISDNAddressStringImpl sgsnNumber,
            MAPExtensionContainerImpl extensionContainer) throws MAPException {
        return addPurgeMSRequest(_Timer_Default, imsi, vlrNumber, sgsnNumber, extensionContainer);
    }

    @Override
    public void addPurgeMSResponse(long invokeId, boolean freezeTMSI, boolean freezePTMSI,
            MAPExtensionContainerImpl extensionContainer, boolean freezeMTMSI) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.msPurgingContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3) && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)))
            throw new MAPException("Bad application context name for PurgeMSResponse: must be msPurgingContext_V3 OR  msPurgingContext_V2");

        PurgeMSResponseImpl resp = null;        
        if (this.appCntx.getApplicationContextVersion().getVersion() >= 3 && (freezeTMSI || freezePTMSI || extensionContainer != null || freezeMTMSI))
            resp =new PurgeMSResponseImpl(freezeTMSI, freezePTMSI, extensionContainer, freezeMTMSI);

        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.purgeMS, resp, false, true);
    }

    @Override
    public Long addResetRequest(NetworkResource networkResource, ISDNAddressStringImpl hlrNumber, List<IMSIImpl> hlrList) throws MAPException {
        return addResetRequest(_Timer_Default, networkResource, hlrNumber, hlrList);
    }

    @Override
    public Long addResetRequest(int customInvokeTimeout, NetworkResource networkResource, ISDNAddressStringImpl hlrNumber, List<IMSIImpl> hlrList)
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
    public Long addRestoreDataRequest(IMSIImpl imsi, LMSIImpl lmsi, VLRCapabilityImpl vlrCapability, MAPExtensionContainerImpl extensionContainer, boolean restorationIndicator)
            throws MAPException {
        return addRestoreDataRequest(_Timer_Default, imsi, lmsi, vlrCapability, extensionContainer, restorationIndicator);
    }

    @Override
    public Long addRestoreDataRequest(int customInvokeTimeout, IMSIImpl imsi, LMSIImpl lmsi, VLRCapabilityImpl vlrCapability, MAPExtensionContainerImpl extensionContainer,
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
    public void addRestoreDataResponse(long invokeId, ISDNAddressStringImpl hlrNumber, boolean msNotReachable, MAPExtensionContainerImpl extensionContainer)
            throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3) && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)))
            throw new MAPException("Bad application context name for RestoreDataResponse: must be networkLocUpContext_V2 or networkLocUpContext_V3");

        RestoreDataResponseImpl resp = new RestoreDataResponseImpl(hlrNumber, msNotReachable, extensionContainer);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.restoreData, resp, false, true);
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

        ActivateTraceModeResponseImpl req = null;
        if ((traceSupportIndicator || extensionContainer != null) && this.appCntx.getApplicationContextVersion().getVersion() >= 3)
            req = new ActivateTraceModeResponseImpl(extensionContainer, traceSupportIndicator);
            
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.activateTraceMode, req, false, true);
    }
}