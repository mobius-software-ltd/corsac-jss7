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

package org.restcomm.protocols.ss7.cap.api.service.gprs;

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSCause;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSChargingID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSMSClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.RAIdentity;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface CAPDialogGprs extends CAPDialog {

    Integer addInitialDpGprsRequest(int serviceKey, GPRSEventType gprsEventType, ISDNAddressString msisdn, IMSI imsi,
            TimeAndTimezone timeAndTimezone, GPRSMSClass gprsMSClass, EndUserAddress endUserAddress,
            QualityOfService qualityOfService, AccessPointName accessPointName, RAIdentity routeingAreaIdentity,
            GPRSChargingID chargingID, SGSNCapabilities sgsnCapabilities, LocationInformationGPRS locationInformationGPRS,
            PDPInitiationType pdpInitiationType, CAPINAPExtensions extensions, GSNAddress gsnAddress, boolean secondaryPDPContext,
            IMEI imei) throws CAPException;

    Integer addInitialDpGprsRequest(int customInvokeTimeout, int serviceKey, GPRSEventType gprsEventType,
            ISDNAddressString msisdn, IMSI imsi, TimeAndTimezone timeAndTimezone, GPRSMSClass gprsMSClass,
            EndUserAddress endUserAddress, QualityOfService qualityOfService, AccessPointName accessPointName,
            RAIdentity routeingAreaIdentity, GPRSChargingID chargingID, SGSNCapabilities sgsnCapabilities,
            LocationInformationGPRS locationInformationGPRS, PDPInitiationType pdpInitiationType, CAPINAPExtensions extensions,
            GSNAddress gsnAddress, boolean secondaryPDPContext, IMEI imei) throws CAPException;

    Integer addRequestReportGPRSEventRequest(List<GPRSEvent> gprsEvent, PDPID pdpID) throws CAPException;

    Integer addRequestReportGPRSEventRequest(int customInvokeTimeout, List<GPRSEvent> gprsEvent, PDPID pdpID)
            throws CAPException;

    Integer addApplyChargingGPRSRequest(ChargingCharacteristics chargingCharacteristics, Integer tariffSwitchInterval,
            PDPID pdpID) throws CAPException;

    Integer addApplyChargingGPRSRequest(int customInvokeTimeout, ChargingCharacteristics chargingCharacteristics,
            Integer tariffSwitchInterval, PDPID pdpID) throws CAPException;

    Integer addEntityReleasedGPRSRequest(GPRSCause gprsCause, PDPID pdpID) throws CAPException;

    Integer addEntityReleasedGPRSRequest(int customInvokeTimeout, GPRSCause gprsCause, PDPID pdpID) throws CAPException;

    void addEntityReleasedGPRSResponse(int invokeId) throws CAPException;

    Integer addConnectGPRSRequest(AccessPointName accessPointName, PDPID pdpID) throws CAPException;

    Integer addConnectGPRSRequest(int customInvokeTimeout, AccessPointName accessPointName, PDPID pdpID)
            throws CAPException;

    Integer addContinueGPRSRequest(PDPID pdpID) throws CAPException;

    Integer addContinueGPRSRequest(int customInvokeTimeout, PDPID pdpID) throws CAPException;

    Integer addReleaseGPRSRequest(GPRSCause gprsCause, PDPID pdpID) throws CAPException;

    Integer addReleaseGPRSRequest(int customInvokeTimeout, GPRSCause gprsCause, PDPID pdpID) throws CAPException;

    Integer addResetTimerGPRSRequest(TimerID timerID, int timerValue) throws CAPException;

    Integer addResetTimerGPRSRequest(int customInvokeTimeout, TimerID timerID, int timerValue) throws CAPException;

    Integer addFurnishChargingInformationGPRSRequest(
            CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics) throws CAPException;

    Integer addFurnishChargingInformationGPRSRequest(int customInvokeTimeout,
            CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics) throws CAPException;

    Integer addCancelGPRSRequest(PDPID pdpID) throws CAPException;

    Integer addCancelGPRSRequest(int customInvokeTimeout, PDPID pdpID) throws CAPException;

    Integer addSendChargingInformationGPRSRequest(
            CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics) throws CAPException;

    Integer addSendChargingInformationGPRSRequest(int customInvokeTimeout,
            CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics) throws CAPException;

    Integer addApplyChargingReportGPRSRequest(ChargingResult chargingResult, QualityOfService qualityOfService,
            boolean active, PDPID pdpID, ChargingRollOver chargingRollOver) throws CAPException;

    Integer addApplyChargingReportGPRSRequest(int customInvokeTimeout, ChargingResult chargingResult,
            QualityOfService qualityOfService, boolean active, PDPID pdpID, ChargingRollOver chargingRollOver)
            throws CAPException;

    void addApplyChargingReportGPRSResponse(int invokeId) throws CAPException;

    Integer addEventReportGPRSRequest(GPRSEventType gprsEventType, MiscCallInfo miscGPRSInfo,
            GPRSEventSpecificInformation gprsEventSpecificInformation, PDPID pdpID) throws CAPException;

    Integer addEventReportGPRSRequest(int customInvokeTimeout, GPRSEventType gprsEventType, MiscCallInfo miscGPRSInfo,
            GPRSEventSpecificInformation gprsEventSpecificInformation, PDPID pdpID) throws CAPException;

    void addEventReportGPRSResponse(int invokeId) throws CAPException;

    Integer addActivityTestGPRSRequest() throws CAPException;

    Integer addActivityTestGPRSRequest(int customInvokeTimeout) throws CAPException;

    void addActivityTestGPRSResponse(int invokeId) throws CAPException;

}