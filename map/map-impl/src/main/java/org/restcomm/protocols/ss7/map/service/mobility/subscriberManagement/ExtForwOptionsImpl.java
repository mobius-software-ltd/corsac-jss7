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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptions;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class ExtForwOptionsImpl extends ASNSingleByte implements ExtForwOptions {
	private static int _MASK_NotificationToForwardingParty = 0x80;
    private static int _MASK_RedirectingPresentation = 0x40;
    private static int _MASK_NotificationToCallingParty = 0x20;
    private static int _MASK_ForwardingReason = 0x0C;

    public ExtForwOptionsImpl() {
    }

    public ExtForwOptionsImpl(boolean notificationToForwardingParty, boolean redirectingPresentation,
            boolean notificationToCallingParty, ExtForwOptionsForwardingReason extForwOptionsForwardingReason) {
    	super(translate(notificationToForwardingParty, redirectingPresentation, notificationToCallingParty, extForwOptionsForwardingReason));
    }
    
    public static Integer translate(boolean notificationToForwardingParty, boolean redirectingPresentation,
            boolean notificationToCallingParty, ExtForwOptionsForwardingReason extForwOptionsForwardingReason) {
        Integer value=0;

        if (notificationToForwardingParty) {
            value |= _MASK_NotificationToForwardingParty;
        }

        if (redirectingPresentation) {
            value |= _MASK_RedirectingPresentation;
        }

        if (notificationToCallingParty) {
            value |= _MASK_NotificationToCallingParty;
        }

        if (extForwOptionsForwardingReason != null) {
            value |= (extForwOptionsForwardingReason.getCode() << 2);
        }
        
        return value;
    }

    public boolean getNotificationToForwardingParty() {
    	Integer value=getValue();
        /*
         * -- bit 8: notification to forwarding party -- 0 no notification -- 1 notification
         */
    	if (value == null)            
            return false;

        return ((value & _MASK_NotificationToForwardingParty) != 0 ? true : false);
    }

    public boolean getRedirectingPresentation() {
    	Integer value=getValue();
        /*
         * -- bit 7: redirecting presentation -- 0 no presentation -- 1 presentation
         */
    	if (value == null)
    		return false;

        return ((value & _MASK_RedirectingPresentation) > 0 ? true : false);
    }

    public boolean getNotificationToCallingParty() {
    	Integer value=getValue();
        /*
         * -- bit 6: notification to calling party -- 0 no notification -- 1 notification
         */
    	if (value == null)
    		return false;

        return ((value & _MASK_NotificationToCallingParty) > 0 ? true : false);
    }

    public ExtForwOptionsForwardingReason getExtForwOptionsForwardingReason() {
    	Integer value=getValue();
        if (value == null)
            return null;

        return ExtForwOptionsForwardingReason.getInstance((int) ((value & _MASK_ForwardingReason) >> 2));
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
