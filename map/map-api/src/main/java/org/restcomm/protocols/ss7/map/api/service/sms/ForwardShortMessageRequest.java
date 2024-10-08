/*
 * Mobius Software LTD
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

/**
 *
 MAP V1-2:
 *
 * ForwardSM ::= OPERATION --Timer ml ARGUMENT forwardSM-Arg ForwardSM-Arg RESULT ERRORS { SystemFailure, DataMissing, --
 * DataMissing must not be used in version 1 UnexpectedDataValue, FacilityNotSupported, UnidentifiedSubscriber,
 * IllegalSubscriber, IllegalEquipment, -- IllegalEquipment must not be used in version 1 AbsentSubscriber,
 * SubscriberBusyForMT-SMS, -- SubscriberBusyForMT-SMS must not be used in version 1 SM-DeliveryFailure}
 *
 * ForwardSM-Arg ::= SEQUENCE { sm-RP-DA SM-RP-DA, sm-RP-OA SM-RP-OA, sm-RP-UI SignalInfo, moreMessagesToSend NULL OPTIONAL, --
 * moreMessagesToSend must be absent in version 1 ...}
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ForwardShortMessageRequest extends SmsMessage {

    SM_RP_DA getSM_RP_DA();

    SM_RP_OA getSM_RP_OA();

    SmsSignalInfo getSM_RP_UI();

    boolean getMoreMessagesToSend();

}
