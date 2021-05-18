/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.cap.api.service.gprs;

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimerID;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingResultImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOverImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddressImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSCauseImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfServiceImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilitiesImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentityImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPDialogGprs extends CAPDialog {

    Long addInitialDpGprsRequest(int serviceKey, GPRSEventType gprsEventType, ISDNAddressStringImpl msisdn, IMSIImpl imsi,
            TimeAndTimezoneImpl timeAndTimezone, GPRSMSClassImpl gprsMSClass, EndUserAddressImpl endUserAddress,
            QualityOfServiceImpl qualityOfService, AccessPointNameImpl accessPointName, RAIdentityImpl routeingAreaIdentity,
            GPRSChargingIDImpl chargingID, SGSNCapabilitiesImpl sgsnCapabilities, LocationInformationGPRSImpl locationInformationGPRS,
            PDPInitiationType pdpInitiationType, CAPExtensionsImpl extensions, GSNAddressImpl gsnAddress, boolean secondaryPDPContext,
            IMEIImpl imei) throws CAPException;

    Long addInitialDpGprsRequest(int customInvokeTimeout, int serviceKey, GPRSEventType gprsEventType,
            ISDNAddressStringImpl msisdn, IMSIImpl imsi, TimeAndTimezoneImpl timeAndTimezone, GPRSMSClassImpl gprsMSClass,
            EndUserAddressImpl endUserAddress, QualityOfServiceImpl qualityOfService, AccessPointNameImpl accessPointName,
            RAIdentityImpl routeingAreaIdentity, GPRSChargingIDImpl chargingID, SGSNCapabilitiesImpl sgsnCapabilities,
            LocationInformationGPRSImpl locationInformationGPRS, PDPInitiationType pdpInitiationType, CAPExtensionsImpl extensions,
            GSNAddressImpl gsnAddress, boolean secondaryPDPContext, IMEIImpl imei) throws CAPException;

    Long addRequestReportGPRSEventRequest(List<GPRSEventImpl> gprsEvent, PDPIDImpl pdpID) throws CAPException;

    Long addRequestReportGPRSEventRequest(int customInvokeTimeout, List<GPRSEventImpl> gprsEvent, PDPIDImpl pdpID)
            throws CAPException;

    Long addApplyChargingGPRSRequest(ChargingCharacteristicsImpl chargingCharacteristics, Integer tariffSwitchInterval,
            PDPIDImpl pdpID) throws CAPException;

    Long addApplyChargingGPRSRequest(int customInvokeTimeout, ChargingCharacteristicsImpl chargingCharacteristics,
            Integer tariffSwitchInterval, PDPIDImpl pdpID) throws CAPException;

    Long addEntityReleasedGPRSRequest(GPRSCauseImpl gprsCause, PDPIDImpl pdpID) throws CAPException;

    Long addEntityReleasedGPRSRequest(int customInvokeTimeout, GPRSCauseImpl gprsCause, PDPIDImpl pdpID) throws CAPException;

    void addEntityReleasedGPRSResponse(long invokeId) throws CAPException;

    Long addConnectGPRSRequest(AccessPointNameImpl accessPointName, PDPIDImpl pdpID) throws CAPException;

    Long addConnectGPRSRequest(int customInvokeTimeout, AccessPointNameImpl accessPointName, PDPIDImpl pdpID)
            throws CAPException;

    Long addContinueGPRSRequest(PDPIDImpl pdpID) throws CAPException;

    Long addContinueGPRSRequest(int customInvokeTimeout, PDPIDImpl pdpID) throws CAPException;

    Long addReleaseGPRSRequest(GPRSCauseImpl gprsCause, PDPIDImpl pdpID) throws CAPException;

    Long addReleaseGPRSRequest(int customInvokeTimeout, GPRSCauseImpl gprsCause, PDPIDImpl pdpID) throws CAPException;

    Long addResetTimerGPRSRequest(TimerID timerID, int timerValue) throws CAPException;

    Long addResetTimerGPRSRequest(int customInvokeTimeout, TimerID timerID, int timerValue) throws CAPException;

    Long addFurnishChargingInformationGPRSRequest(
            CAMELFCIGPRSBillingChargingCharacteristicsImpl fciGPRSBillingChargingCharacteristics) throws CAPException;

    Long addFurnishChargingInformationGPRSRequest(int customInvokeTimeout,
            CAMELFCIGPRSBillingChargingCharacteristicsImpl fciGPRSBillingChargingCharacteristics) throws CAPException;

    Long addCancelGPRSRequest(PDPIDImpl pdpID) throws CAPException;

    Long addCancelGPRSRequest(int customInvokeTimeout, PDPIDImpl pdpID) throws CAPException;

    Long addSendChargingInformationGPRSRequest(
            CAMELSCIGPRSBillingChargingCharacteristicsImpl sciGPRSBillingChargingCharacteristics) throws CAPException;

    Long addSendChargingInformationGPRSRequest(int customInvokeTimeout,
            CAMELSCIGPRSBillingChargingCharacteristicsImpl sciGPRSBillingChargingCharacteristics) throws CAPException;

    Long addApplyChargingReportGPRSRequest(ChargingResultImpl chargingResult, QualityOfServiceImpl qualityOfService,
            boolean active, PDPIDImpl pdpID, ChargingRollOverImpl chargingRollOver) throws CAPException;

    Long addApplyChargingReportGPRSRequest(int customInvokeTimeout, ChargingResultImpl chargingResult,
            QualityOfServiceImpl qualityOfService, boolean active, PDPIDImpl pdpID, ChargingRollOverImpl chargingRollOver)
            throws CAPException;

    void addApplyChargingReportGPRSResponse(long invokeId) throws CAPException;

    Long addEventReportGPRSRequest(GPRSEventType gprsEventType, MiscCallInfoImpl miscGPRSInfo,
            GPRSEventSpecificInformationImpl gprsEventSpecificInformation, PDPIDImpl pdpID) throws CAPException;

    Long addEventReportGPRSRequest(int customInvokeTimeout, GPRSEventType gprsEventType, MiscCallInfoImpl miscGPRSInfo,
            GPRSEventSpecificInformationImpl gprsEventSpecificInformation, PDPIDImpl pdpID) throws CAPException;

    void addEventReportGPRSResponse(long invokeId) throws CAPException;

    Long addActivityTestGPRSRequest() throws CAPException;

    Long addActivityTestGPRSRequest(int customInvokeTimeout) throws CAPException;

    void addActivityTestGPRSResponse(long invokeId) throws CAPException;

}