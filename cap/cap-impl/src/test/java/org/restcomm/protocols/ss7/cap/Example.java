package org.restcomm.protocols.ss7.cap;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CallingPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CauseCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.LocationNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

public class Example {

    private static SccpAddress createLocalAddress() {
        return new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,  1, 146);
    }

    private static SccpAddress createRemoteAddress() {
        return new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 146);
    }

    public static void startCallSsf() throws Exception {
        CallSsfExample client = new CallSsfExample();

        client.start();

        // starting a call
        SccpAddress origAddress = createLocalAddress();
        SccpAddress destAddress = createRemoteAddress();

        int serviceKey = 1;

        CalledPartyNumber calledPartyNumber = client.getCAPProvider().getISUPParameterFactory().createCalledPartyNumber();
        calledPartyNumber.setAddress("552348762");
        calledPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
        calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
        CalledPartyNumberCapImpl calledPartyNumberCap = client.getCAPProvider().getCAPParameterFactory()
                .createCalledPartyNumberCap(calledPartyNumber);

        CallingPartyNumber callingPartyNumber = client.getCAPProvider().getISUPParameterFactory().createCallingPartyNumber();
        callingPartyNumber.setAddress("55998223");
        callingPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        callingPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
        callingPartyNumber.setAddressRepresentationREstrictedIndicator(CallingPartyNumber._APRI_ALLOWED);
        callingPartyNumber.setScreeningIndicator(CallingPartyNumber._SI_NETWORK_PROVIDED);
        CallingPartyNumberCapImpl callingPartyNumberCap = client.getCAPProvider().getCAPParameterFactory()
                .createCallingPartyNumberCap(callingPartyNumber);

        LocationNumber locationNumber = client.getCAPProvider().getISUPParameterFactory().createLocationNumber();
        locationNumber.setAddress("55200001");
        locationNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        locationNumber.setNumberingPlanIndicator(LocationNumber._NPI_ISDN);
        locationNumber.setAddressRepresentationRestrictedIndicator(LocationNumber._APRI_ALLOWED);
        locationNumber.setScreeningIndicator(LocationNumber._SI_NETWORK_PROVIDED);
        locationNumber.setInternalNetworkNumberIndicator(LocationNumber._INN_ROUTING_ALLOWED);
        LocationNumberCapImpl locationNumberCap = client.getCAPProvider().getCAPParameterFactory()
                .createLocationNumberCap(locationNumber);

        ISDNAddressStringImpl vlrNumber = client.getCAPProvider().getMAPParameterFactory()
                .createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "552000002");
        LocationInformationImpl locationInformation = client
                .getCAPProvider()
                .getMAPParameterFactory()
                .createLocationInformation(10, null, vlrNumber, null, null, null, null, vlrNumber, null, false, false, null,
                        null);

        client.sendInitialDP(origAddress, destAddress, serviceKey, calledPartyNumberCap, callingPartyNumberCap,
                locationNumberCap, EventTypeBCSM.collectedInfo, locationInformation);

        // sending oAnswer in 5 sec
        Thread.sleep(5000);
        OAnswerSpecificInfoImpl oAnswerSpecificInfo = client.getCAPProvider().getCAPParameterFactory()
                .createOAnswerSpecificInfo(null, false, false, null, null, null);        
        MiscCallInfo miscCallInfo = client.getCAPProvider().getINAPParameterFactory()
                .createMiscCallInfo(MiscCallInfoMessageType.notification, null);
        client.sendEventReportBCSM_OAnswer(oAnswerSpecificInfo, LegType.leg2, miscCallInfo);

        // sending oDisconnect in 20 sec
        Thread.sleep(20000);
        CauseIndicators causeIndicators = client.getCAPProvider().getISUPParameterFactory().createCauseIndicators();
        causeIndicators.setLocation(CauseIndicators._LOCATION_USER);
        causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
        causeIndicators.setCauseValue(CauseIndicators._CV_ALL_CLEAR);
        CauseCapImpl releaseCause = client.getCAPProvider().getCAPParameterFactory().createCauseCap(causeIndicators);
        ODisconnectSpecificInfoImpl oDisconnectSpecificInfo = client.getCAPProvider().getCAPParameterFactory()
                .createODisconnectSpecificInfo(releaseCause);
        miscCallInfo = client.getCAPProvider().getINAPParameterFactory()
                .createMiscCallInfo(MiscCallInfoMessageType.notification, null);
        client.sendEventReportBCSM_ODisconnect(oDisconnectSpecificInfo, LegType.leg1, miscCallInfo);

        // wait for answer
        Thread.sleep(600000);

        client.stop();
    }

    public static void startCallScf() throws Exception {
        CallScfExample server = new CallScfExample();

        server.start();

        // wait for a request
        Thread.sleep(600000);

        server.stop();
    }
}
