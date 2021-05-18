/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.inap.api.primitives;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x04,constructed=true,lengthIndefinite=false)
public class LegIDImpl {
	private ReceivingLegIDImpl receivingLegID;
    private SendingLegIDImpl sendingLegID;

    public LegIDImpl() {
    }

    public LegIDImpl(ReceivingLegIDImpl receivingLegID, SendingLegIDImpl sendingLegID) {
    	this.receivingLegID=receivingLegID;
    	this.sendingLegID=sendingLegID;
    }
    
    public ReceivingLegIDImpl getReceivingLegID() {
		return receivingLegID;
	}

	public SendingLegIDImpl getSendingLegID() {
		return sendingLegID;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(" [");
        if (this.receivingLegID != null) {
            sb.append("receivingLegID=");
            sb.append(receivingLegID);
        }
        if (this.sendingLegID != null) {
            sb.append(", sendingLegID=");
            sb.append(sendingLegID);
        }
        sb.append("]");

        return sb.toString();
    }
}
