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

package org.restcomm.protocols.ss7.map.api.smstpdu;

import java.util.Map;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface UserDataHeader {

    int _InformationElementIdentifier_ConcatenatedShortMessages8bit = 0x00;
    int _InformationElementIdentifier_ApplicationPortAddressingScheme16BitAddress = 0x05;
    int _InformationElementIdentifier_ConcatenatedShortMessages16bit = 0x08;
    int _InformationElementIdentifier_NationalLanguageSingleShift = 0x24;
    int _InformationElementIdentifier_NationalLanguageLockingShift = 0x25;

    void getEncodedData(ByteBuf buf);

    Map<Integer, ByteBuf> getAllData();

    int getLength();
    
    void addInformationElement(int informationElementIdentifier, ByteBuf encodedData);

    void addInformationElement(UserDataHeaderElement informationElement);

    ByteBuf getInformationElementData(int informationElementIdentifier);

    NationalLanguageLockingShiftIdentifier getNationalLanguageLockingShift();

    NationalLanguageSingleShiftIdentifier getNationalLanguageSingleShift();

    ConcatenatedShortMessagesIdentifier getConcatenatedShortMessagesIdentifier();

    ApplicationPortAddressing16BitAddress getApplicationPortAddressing16BitAddress();

}