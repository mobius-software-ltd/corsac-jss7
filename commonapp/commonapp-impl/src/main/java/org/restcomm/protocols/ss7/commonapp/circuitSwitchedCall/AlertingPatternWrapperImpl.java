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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AlertingPatternWrapper;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.primitives.AlertingPatternImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public class AlertingPatternWrapperImpl extends ASNOctetString implements AlertingPatternWrapper {
	public AlertingPatternWrapperImpl() {
    }

    public AlertingPatternWrapperImpl(ByteBuf value) {
    	super(value);
    }

    public AlertingPatternWrapperImpl(AlertingPattern alertingPattern) {
        super(translateAlertingPattern(alertingPattern));
    }

    public static ByteBuf translateAlertingPattern(AlertingPattern alertingPattern) {

        if (alertingPattern == null || alertingPattern.getData()==null)
            return Unpooled.EMPTY_BUFFER;

        ByteBuf result=Unpooled.buffer(3);
        result.writeByte(0);
        result.writeByte(0);
        result.writeByte(alertingPattern.getData().byteValue());
        return result;
    }

    public AlertingPattern getAlertingPattern() {
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
        AlertingPattern ap = this.getAlertingPattern();
        if (ap != null) {
            sb.append("AlertingPattern=");
            sb.append(ap.toString());
        }
        sb.append("]");

        return sb.toString();
    }
}
