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

package org.restcomm.protocols.ss7.map.api.service.mobility.handover;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.AccessNetworkSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V3: PrepareHO-Res ::= [3] SEQUENCE { handoverNumber [0] ISDN-AddressStringImpl OPTIONAL, relocationNumberList [1]
 * RelocationNumberList OPTIONAL, an-APDU [2] AccessNetworkSignalInfo OPTIONAL, multicallBearerInfo [3] MulticallBearerInfo
 * OPTIONAL, multipleBearerNotSupported NULL OPTIONAL, selectedUMTS-Algorithms [5] SelectedUMTS-Algorithms OPTIONAL,
 * chosenRadioResourceInformation [6] ChosenRadioResourceInformation OPTIONAL, extensionContainer [4] ExtensionContainer
 * OPTIONAL, ..., iuSelectedCodec [7] Codec OPTIONAL, iuAvailableCodecsList [8] CodecList OPTIONAL, aoipSelectedCodecTarget [9]
 * AoIPCodec OPTIONAL, aoipAvailableCodecsListMap [10] AoIPCodecsList OPTIONAL }
 *
 * MAP V2: PrepareHO-Res ::= SEQUENCE { handoverNumber ISDN-AddressStringImpl OPTIONAL, bss-APDU ExternalSignalInfo OPTIONAL, ...}
 *
 * RelocationNumberList ::= SEQUENCE SIZE (1..7) OF RelocationNumber
 *
 * MulticallBearerInfo ::= INTEGER (1..7)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface PrepareHandoverResponse extends MobilityMessage {

    ISDNAddressStringImpl getHandoverNumber();

    List<RelocationNumber> getRelocationNumberList();

    AccessNetworkSignalInfo getAnAPDU();

    Integer getMulticallBearerInfo();

    boolean getMultipleBearerNotSupported();

    SelectedUMTSAlgorithms getSelectedUMTSAlgorithms();

    ChosenRadioResourceInformation getChosenRadioResourceInformation();

    MAPExtensionContainerImpl getExtensionContainer();

    Codec getIuSelectedCodec();

    CodecList getIuAvailableCodecsList();

    AoIPCodec getAoipSelectedCodecTarget();

    AoIPCodecsList getAoipAvailableCodecsListMap();

    // this parameter is for MAP V2 only
    ExternalSignalInfoImpl getBssAPDU();

}
