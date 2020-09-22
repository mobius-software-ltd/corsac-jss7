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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 *
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class ExtForwOptionsImpl extends ASNOctetString {
	private static int _MASK_NotificationToForwardingParty = 0x80;
    private static int _MASK_RedirectingPresentation = 0x40;
    private static int _MASK_NotificationToCallingParty = 0x20;
    private static int _MASK_ForwardingReason = 0x0C;

    public ExtForwOptionsImpl() {
    }

    public ExtForwOptionsImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public ExtForwOptionsImpl(boolean notificationToForwardingParty, boolean redirectingPresentation,
            boolean notificationToCallingParty, ExtForwOptionsForwardingReason extForwOptionsForwardingReason) {
        byte[] data = new byte[1];

        if (notificationToForwardingParty) {
            data[0] |= _MASK_NotificationToForwardingParty;
        }

        if (redirectingPresentation) {
            data[0] |= _MASK_RedirectingPresentation;
        }

        if (notificationToCallingParty) {
            data[0] |= _MASK_NotificationToCallingParty;
        }

        if (extForwOptionsForwardingReason != null) {
            data[0] |= (extForwOptionsForwardingReason.getCode() << 2);
        }
        
        setValue(Unpooled.wrappedBuffer(data));
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public boolean getNotificationToForwardingParty() {
    	byte[] data=getData();
        /*
         * -- bit 8: notification to forwarding party -- 0 no notification -- 1 notification
         */
        if (data == null || data.length < 1)
            return false;

        return ((data[0] & _MASK_NotificationToForwardingParty) != 0 ? true : false);
    }

    public boolean getRedirectingPresentation() {
    	byte[] data=getData();    			
        /*
         * -- bit 7: redirecting presentation -- 0 no presentation -- 1 presentation
         */
        if (data == null || data.length < 1)
            return false;

        return ((data[0] & _MASK_RedirectingPresentation) > 0 ? true : false);
    }

    public boolean getNotificationToCallingParty() {
    	byte[] data=getData();
        /*
         * -- bit 6: notification to calling party -- 0 no notification -- 1 notification
         */
        if (data == null || data.length < 1)
            return false;

        return ((data[0] & _MASK_NotificationToCallingParty) > 0 ? true : false);
    }

    public ExtForwOptionsForwardingReason getExtForwOptionsForwardingReason() {
    	byte[] data=getData();
        if (data == null || data.length < 1)
            return null;

        return ExtForwOptionsForwardingReason.getInstance((int) ((data[0] & _MASK_ForwardingReason) >> 2));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtForwOptions [");

        if (this.getNotificationToForwardingParty()) {
            sb.append("notificationToForwardingParty, ");
        }
        if (this.getRedirectingPresentation()) {
            sb.append("redirectingPresentation, ");
        }
        if (this.getNotificationToCallingParty()) {
            sb.append("notificationToCallingParty, ");
        }
        sb.append("ExtForwOptionsForwardingReason=");
        sb.append(getExtForwOptionsForwardingReason());

        sb.append("]");

        return sb.toString();
    }
}
