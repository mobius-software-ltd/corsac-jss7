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

package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.TMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
<code>
MAP V2-3:

MAP V3:
sendIdentification OPERATION ::= {
  --Timer s
  ARGUMENT SendIdentificationArg
  RESULT SendIdentificationRes
  ERRORS { dataMissing | unidentifiedSubscriber}
  CODE local:55
}

MAP V2:
SendIdentification::= OPERATION
  --Timer s]
  ARGUMENT tmsi TMSI
  RESULT sendIdentificationRes SendIdentificationRes
  ERRORS { DataMissing, UnidentifiedSubscriber}


MAP V3: SendIdentificationArg ::= SEQUENCE {
  tmsi                        TMSI,
  numberOfRequestedVectors    NumberOfRequestedVectors OPTIONAL,
  -- within a dialogue numberOfRequestedVectors shall be present in
  -- the first service request and shall not be present in subsequent service requests.
  -- If received in a subsequent service request it shall be discarded.
  segmentationProhibited      NULL OPTIONAL,
  extensionContainer          ExtensionContainer OPTIONAL,
  ...,
  msc-Number                  ISDN-AddressStringImpl OPTIONAL,
  previous-LAI                [0] LAIFixedLength OPTIONAL,
  hopCounter                  [1] HopCounter OPTIONAL,
  mtRoamingForwardingSupported [2] NULL OPTIONAL,
  newVLR-Number               [3] ISDN-AddressStringImpl OPTIONAL,
  new-lmsi                    [4] LMSI OPTIONAL
}

NumberOfRequestedVectors ::= INTEGER (1..5)
HopCounter ::= INTEGER (0..3)
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendIdentificationRequest extends MobilityMessage {

    TMSIImpl getTmsi();

    Integer getNumberOfRequestedVectors();

    boolean getSegmentationProhibited();

    MAPExtensionContainerImpl getExtensionContainer();

    ISDNAddressStringImpl getMscNumber();

    LAIFixedLengthImpl getPreviousLAI();

    Integer getHopCounter();

    boolean getMtRoamingForwardingSupported();

    ISDNAddressStringImpl getNewVLRNumber();

    LMSIImpl getNewLmsi();

}
