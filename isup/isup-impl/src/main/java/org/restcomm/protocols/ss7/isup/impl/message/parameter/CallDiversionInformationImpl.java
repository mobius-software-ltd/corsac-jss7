/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

/**
 * Start time:14:32:32 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CallDiversionInformation;

/**
 * Start time:14:32:32 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 *
 */
public class CallDiversionInformationImpl extends AbstractISUPParameter implements CallDiversionInformation {
	private int redirectingReason = 0;

    private int notificationSubscriptionOptions = 0;

    public CallDiversionInformationImpl(int notificationSubscriptionOptions, int redirectingReason) {
        super();
        this.notificationSubscriptionOptions = notificationSubscriptionOptions;
        this.redirectingReason = redirectingReason;
    }

    public CallDiversionInformationImpl() {
        super();
    }

    public CallDiversionInformationImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new ParameterException("byte[] must not be null or have different size than 1");
        }

        int v = b.readByte();
        this.notificationSubscriptionOptions = v & 0x07;
        this.redirectingReason = (v >> 3) & 0x0F;
    }

    public void encode(ByteBuf b) throws ParameterException {
        int v = 0;
        v |= this.notificationSubscriptionOptions & 0x07;
        v |= (this.redirectingReason & 0x0F) << 3;
        b.writeByte((byte) v);
    }

    public int getNotificationSubscriptionOptions() {
        return notificationSubscriptionOptions;
    }

    public void setNotificationSubscriptionOptions(int notificationSubscriptionOptions) {
        this.notificationSubscriptionOptions = notificationSubscriptionOptions;
    }

    public int getRedirectingReason() {
        return redirectingReason;
    }

    public void setRedirectingReason(int redirectingReason) {
        this.redirectingReason = redirectingReason;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}