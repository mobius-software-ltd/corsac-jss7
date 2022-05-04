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

package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingOptions;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
public class ForwardingOptionsImpl extends ASNOctetString implements ForwardingOptions {
	private static final int MASK_notificationForwarding = 0x80;
    private static final int MASK_redirectingPresentation = 0x40;
    private static final int MASK_notificationCalling = 0x20;
    private static final int MASK_forwardingReason = 0x0C;
    private static final int MASK_forwardingOptions = 0xEC;

    public ForwardingOptionsImpl() {
    	super("ForwardingOptions",1,1,false);
    }

    public ForwardingOptionsImpl(boolean notificationToForwardingParty, boolean redirectingPresentation,
            boolean notificationToCallingParty, ForwardingReason forwardingReason) {
    	super(translate(notificationToForwardingParty, redirectingPresentation, notificationToCallingParty, forwardingReason),"ForwardingOptions",1,1,false);
    }
    
    public static ByteBuf translate(boolean notificationToForwardingParty, boolean redirectingPresentation,
            boolean notificationToCallingParty, ForwardingReason forwardingReason) {
        int forwardingReasonCode = 3;
        if (forwardingReason != null) {
            forwardingReasonCode = forwardingReason.getCode();
        }

        int code = 0 & MASK_forwardingOptions; // bit 5 and bits 21 are 0 (unused)
        code = notificationToForwardingParty ? (code | MASK_notificationForwarding) : (code & ~MASK_notificationForwarding);
        code = redirectingPresentation ? (code | MASK_redirectingPresentation) : (code & ~MASK_redirectingPresentation);
        code = notificationToCallingParty ? (code | MASK_notificationCalling) : (code & ~MASK_notificationCalling);
        code = code | (forwardingReasonCode << 2) & MASK_forwardingReason;
        
        ByteBuf data=Unpooled.buffer(1);
        data.writeByte((byte)code);
        return data;
    }

    public boolean isNotificationToCallingParty() {
        return ((getData() & MASK_notificationCalling) >> 5 == 1);
    }

    public boolean isNotificationToForwardingParty() {
        return ((getData() & MASK_notificationForwarding) >> 7 == 1);
    }

    public boolean isRedirectingPresentation() {
        return ((getData() & MASK_redirectingPresentation) >> 6 == 1);
    }

    public ForwardingReason getForwardingReason() {
    	return ForwardingReason.getForwardingReason((getData() & MASK_forwardingReason)>>2);        
    }

    public int getData() {
    	ByteBuf value=getValue();
    	if(value.readableBytes()==0)
    		return 0;
    	
        return value.readByte();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ForwardingOptions [");

        sb.append("NotificationToCallingParty: ");
        sb.append(isNotificationToCallingParty());
        sb.append(',');
        sb.append("NotificationToForwardingParty: ");
        sb.append(isNotificationToForwardingParty());
        sb.append(',');
        sb.append("RedirectingPresentation: ");
        sb.append(isRedirectingPresentation());
        sb.append(',');
        sb.append("forwardingReason: ");
        sb.append(getForwardingReason());

        sb.append("]");
        return sb.toString();
    }
}