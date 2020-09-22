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

package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNWrappedTag
public class NoteSubscriberPresentRequestImpl extends SmsMessageImpl implements NoteSubscriberPresentRequest {
	private static final long serialVersionUID = 1L;

	private IMSIImpl imsi;

    public NoteSubscriberPresentRequestImpl() {
    }

    public NoteSubscriberPresentRequestImpl(IMSIImpl imsi) {
        this.imsi = imsi;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.noteSubscriberPresent_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.noteSubscriberPresent;
    }

    @Override
    public IMSIImpl getIMSI() {
        return imsi;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NoteSubscriberPresentRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
