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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.map.api.primitives.AlertingPatternImpl;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AlertingPatternCapImpl extends ASNOctetString {
	public AlertingPatternCapImpl() {
    }

    public AlertingPatternCapImpl(byte[] data) {
    	setValue(Unpooled.wrappedBuffer(data));
    }

    public AlertingPatternCapImpl(AlertingPatternImpl alertingPattern) {
        setAlertingPattern(alertingPattern);
    }

    public void setAlertingPattern(AlertingPatternImpl alertingPattern) {

        if (alertingPattern == null || alertingPattern.getData()==null)
            return;

        byte[] data = new byte[3];
        data[2] = alertingPattern.getData().byteValue();
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

    public AlertingPatternImpl getAlertingPattern() {
    	ByteBuf value=getValue();
        if (value != null && value.readableBytes() == 3) {
            value.skipBytes(2);
        	return new AlertingPatternImpl(value.readByte());
        }
        else
            return null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AlertingPatternCap [");
        AlertingPatternImpl ap = this.getAlertingPattern();
        if (ap != null) {
            sb.append("AlertingPattern=");
            sb.append(ap.toString());
        }
        sb.append("]");

        return sb.toString();
    }
}
