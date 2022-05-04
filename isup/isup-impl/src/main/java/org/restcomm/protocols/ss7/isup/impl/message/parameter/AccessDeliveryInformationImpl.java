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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;

/**
 * Start time:13:31:04 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 *
 */
public class AccessDeliveryInformationImpl extends AbstractISUPParameter implements AccessDeliveryInformation {
	private int accessDeliveryIndicator;

    public AccessDeliveryInformationImpl(int accessDeliveryIndicator) {
        super();
        this.accessDeliveryIndicator = accessDeliveryIndicator;
    }

    public AccessDeliveryInformationImpl() {
        super();

    }

    public AccessDeliveryInformationImpl(ByteBuf representation) throws ParameterException {
        super();
        this.decode(representation);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new IllegalArgumentException("buffer must not be null or have different size than 1");
        }
        this.accessDeliveryIndicator = (byte) (b.readByte() & 0x01);
    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	buffer.writeByte(this.accessDeliveryIndicator);        
    }

    public int getAccessDeliveryIndicator() {
        return accessDeliveryIndicator;
    }

    public void setAccessDeliveryIndicator(int accessDeliveryIndicator) {
        this.accessDeliveryIndicator = accessDeliveryIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("AccessDeliveryInformation [");

        sb.append("accessDeliveryIndicator=");
        sb.append(accessDeliveryIndicator);

        sb.append("]");
        return sb.toString();
    }

}
