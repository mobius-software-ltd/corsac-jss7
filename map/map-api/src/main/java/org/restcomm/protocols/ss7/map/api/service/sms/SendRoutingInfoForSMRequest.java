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

package org.restcomm.protocols.ss7.map.api.service.sms;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;

/**
 *
<code>
MAP V1-2-3:

MAP V3: sendRoutingInfoForSM OPERATION ::= { --Timer m
  ARGUMENT RoutingInfoForSM-Arg
  RESULT RoutingInfoForSM-Res
  ERRORS { systemFailure | dataMissing | unexpectedDataValue | facilityNotSupported | unknownSubscriber | teleserviceNotProvisioned | callBarred | absentSubscriberSM }
  CODE local:45
}

MAP V2: SendRoutingInfoForSM ::= OPERATION --Timer m
  ARGUMENT routingInfoForSM-Arg RoutingInfoForSM-Arg
  RESULT routingInfoForSM-Res RoutingInfoForSM-Res
  ERRORS { SystemFailure, DataMissing, UnexpectedDataValue, FacilityNotSupported, UnknownSubscriber, TeleserviceNotProvisioned, AbsentSubscriber, CallBarred }

MAP V3: RoutingInfoForSM-Arg ::= SEQUENCE {
  msisdn                  [0] ISDN-AddressStringImpl,
  sm-RP-PRI               [1] BOOLEAN,
  serviceCentreAddress    [2] AddressStringImpl,
  extensionContainer      [6] ExtensionContainer OPTIONAL,
  ... ,
  gprsSupportIndicator    [7] NULL OPTIONAL,
  -- gprsSupportIndicator is set only if the SMS-GMSC supports
  -- receiving of two numbers from the HLR
  sm-RP-MTI               [8] SM-RP-MTI OPTIONAL,
  sm-RP-SMEA              [9] SM-RP-SMEA OPTIONAL,
  sm-deliveryNotIntended  [10] SM-DeliveryNotIntended OPTIONAL,
  ip-sm-gwGuidanceIndicator   [11] NULL OPTIONAL,
  imsi                    [12] IMSI OPTIONAL,
  t4-Trigger-Indicator    [14] NULL OPTIONAL,
  singleAttemptDelivery   [13] NULL OPTIONAL,
  correlationID           [15] CorrelationID OPTIONAL
}

MAP V2: RoutingInfoForSM-Arg ::= SEQUENCE {
  msisdn                  [0] ISDN-AddressStringImpl,
  sm-RP-PRI               [1] BOOLEAN,
  serviceCentreAddress     [2] AddressStringImpl,
  teleservice             [5] TeleserviceCode OPTIONAL,
  -- teleservice must be absent in version greater 1 ...
}
</code>
 *
 *
 * @author sergey vetyutnev
 * @author eva ogallar
 * @author yulianoifa
 *
 */
public interface SendRoutingInfoForSMRequest extends SmsMessage {

    ISDNAddressString getMsisdn();

    boolean getSm_RP_PRI();

    AddressString getServiceCentreAddress();

    MAPExtensionContainer getExtensionContainer();

    boolean getGprsSupportIndicator();

    SM_RP_MTI getSM_RP_MTI();

    SM_RP_SMEA getSM_RP_SMEA();

    SMDeliveryNotIntended getSmDeliveryNotIntended();

    boolean getIpSmGwGuidanceIndicator();

    IMSI getImsi();

    boolean getT4TriggerIndicator();

    boolean getSingleAttemptDelivery();

    CorrelationID getCorrelationID();

    // for MAP V1 only
    TeleserviceCode getTeleservice();
}
