/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.tcap.tc.dialog.events;

import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.DraftParsedMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUnifiedMessage;

/**
 * @author sergey vetyutnev
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
