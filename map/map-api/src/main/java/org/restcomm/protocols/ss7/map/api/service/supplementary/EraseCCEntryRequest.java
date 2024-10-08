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

package org.restcomm.protocols.ss7.map.api.service.supplementary;

/**
 *
 MAP V3:
 *
 * eraseCC-Entry OPERATION ::= { --Timer m ARGUMENT EraseCC-EntryArg RESULT EraseCC-EntryRes ERRORS { systemFailure |
 * dataMissing | unexpectedDataValue | callBarred | illegalSS-Operation | ss-ErrorStatus} CODE local:77 }
 *
 * EraseCC-EntryArg ::= SEQUENCE { ss-Code [0] SS-Code, ccbs-Index [1] CCBS-Index OPTIONAL, ...}
 *
 * CCBS-Index ::= INTEGER (1..5)
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface EraseCCEntryRequest extends SupplementaryMessage {

	SSCode getSsEvent();

    Integer getCCBSIndex();

}
