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
package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.QoSSubscribedImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class GPRSQoSImpl implements GPRSQoS {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = QoSSubscribedImpl.class)
    private QoSSubscribed shortQoSFormat;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = ExtQoSSubscribedImpl.class)
    private ExtQoSSubscribed longQoSFormat;

    public GPRSQoSImpl() {

    }

    public GPRSQoSImpl(QoSSubscribed shortQoSFormat) {
        this.shortQoSFormat = shortQoSFormat;
    }

    public GPRSQoSImpl(ExtQoSSubscribed longQoSFormat) {
        this.longQoSFormat = longQoSFormat;
    }

    public QoSSubscribed getShortQoSFormat() {
        return this.shortQoSFormat;
    }

    public ExtQoSSubscribed getLongQoSFormat() {
        return this.longQoSFormat;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GPRSQoS [");

        if (this.shortQoSFormat != null) {
            sb.append("shortQoSFormat=");
            sb.append(this.shortQoSFormat.toString());
        }

        if (this.longQoSFormat != null) {
            sb.append("longQoSFormat=");
            sb.append(this.longQoSFormat.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
