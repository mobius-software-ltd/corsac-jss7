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

package org.restcomm.protocols.ss7.tcap.tc.dialog.events;

import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.DraftParsedMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUnifiedMessage;

/**
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class DraftParsedMessageImpl implements DraftParsedMessage {

    private TCUnifiedMessage message;
    private String parsingErrorReason;


    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("DraftParsedMessage [");
        if (message.getOriginatingTransactionId() != null) {
            sb.append(", originationDialogId=");
            sb.append(message.getOriginatingTransactionId());
        }
        if (message.getDestinationTransactionId() != null) {
            sb.append(", destinationDialogId=");
            sb.append(message.getDestinationTransactionId());
        }
        if (parsingErrorReason != null) {
            sb.append(", parsingErrorReason=");
            sb.append(parsingErrorReason);
        }
        sb.append("]");

        return sb.toString();
    }


	@Override
	public TCUnifiedMessage getMessage() {
		return message;
	}

	@Override
	public String getParsingErrorReason() {
		return parsingErrorReason;
	}

	public void setMessage(TCUnifiedMessage message) {
		this.message = message;
	}

	public void setParsingErrorReason(String parsingErrorReason) {
		this.parsingErrorReason = parsingErrorReason;
	}
}
