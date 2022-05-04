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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptions;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author daniel bichara
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class ExtForwOptionsImpl extends ASNSingleByte implements ExtForwOptions {
	private static int _MASK_NotificationToForwardingParty = 0x80;
    private static int _MASK_RedirectingPresentation = 0x40;
    private static int _MASK_NotificationToCallingParty = 0x20;
    private static int _MASK_ForwardingReason = 0x0C;

    public ExtForwOptionsImpl() {
    	super("ExtForwOptions",0,255,false);
    }

    public ExtForwOptionsImpl(boolean notificationToForwardingParty, boolean redirectingPresentation,
            boolean notificationToCallingParty, ExtForwOptionsForwardingReason extForwOptionsForwardingReason) {
    	super(translate(notificationToForwardingParty, redirectingPresentation, notificationToCallingParty, extForwOptionsForwardingReason),"ExtForwOptions",0,255,false);
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
