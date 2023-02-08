/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.UserToUserIndicators;

import io.netty.buffer.ByteBuf;

/**
 * Start time:12:44:04 2009-04-04<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class UserToUserIndicatorsImpl extends AbstractISUPParameter implements UserToUserIndicators {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private boolean response;
    private int serviceOne;
    private int serviceTwo;
    private int serviceThree;
    private boolean networkDiscardIndicator;

    public UserToUserIndicatorsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public UserToUserIndicatorsImpl() {
        super();

    }

    public UserToUserIndicatorsImpl(boolean response, int serviceOne, int serviceTwo, int serviceThree,
            boolean networkDiscardIndicator) {
        super();
        this.response = response;
        this.serviceOne = serviceOne;
        this.serviceTwo = serviceTwo;
        this.serviceThree = serviceThree;
        this.networkDiscardIndicator = networkDiscardIndicator;
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new ParameterException("buffer  must  not be null and length must be 1");
        }
        try {
        	byte curr=b.readByte();
            this.setResponse((curr & 0x01) == _TURN_ON);
            this.setServiceOne((curr >> 1));
            this.setServiceTwo((curr >> 3));
            this.setServiceThree((curr >> 5));
            this.setNetworkDiscardIndicator(((curr >> 7) & 0x01) == _TURN_ON);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        int v = this.response ? _TURN_ON : _TURN_OFF;
        v |= (this.serviceOne & 0x03) << 1;
        v |= (this.serviceTwo & 0x03) << 3;
        v |= (this.serviceThree & 0x03) << 5;
        v |= (this.networkDiscardIndicator ? _TURN_ON : _TURN_OFF) << 7;
        buffer.writeByte((byte) v);
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public int getServiceOne() {
        return serviceOne;
    }

    public void setServiceOne(int serviceOne) {
        this.serviceOne = serviceOne & 0x03;
    }

    public int getServiceTwo() {
        return serviceTwo;
    }

    public void setServiceTwo(int serviceTwo) {
        this.serviceTwo = serviceTwo & 0x03;
    }

    public int getServiceThree() {
        return serviceThree;
    }

    public void setServiceThree(int serviceThree) {
        this.serviceThree = serviceThree & 0x03;
    }

    public boolean isNetworkDiscardIndicator() {
        return networkDiscardIndicator;
    }

    public void setNetworkDiscardIndicator(boolean networkDiscardIndicator) {
        this.networkDiscardIndicator = networkDiscardIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
