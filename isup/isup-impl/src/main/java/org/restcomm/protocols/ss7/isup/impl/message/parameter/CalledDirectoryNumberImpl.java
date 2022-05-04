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
import org.restcomm.protocols.ss7.isup.message.parameter.CalledDirectoryNumber;

/**
 * Start time:16:42:16 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CalledDirectoryNumberImpl extends AbstractNAINumber implements CalledDirectoryNumber {
	protected int numberingPlanIndicator;

    protected int internalNetworkNumberIndicator;

    /**
     * @param representation
     */
    public CalledDirectoryNumberImpl(ByteBuf representation) throws ParameterException {
        super(representation);

        getNumberingPlanIndicator();
    }

    public CalledDirectoryNumberImpl() {
        super();

    }

    public CalledDirectoryNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator,
            int internalNetworkNumberIndicator) {
        super(natureOfAddresIndicator, address);
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.internalNetworkNumberIndicator = internalNetworkNumberIndicator;
    }

    public void decodeBody(ByteBuf buffer) throws IllegalArgumentException {
        if (buffer.readableBytes() == 0) {
            throw new IllegalArgumentException("No more data to read.");
        }
        int b = buffer.readByte() & 0xff;

        this.internalNetworkNumberIndicator = (b & 0x80) >> 7;
        this.numberingPlanIndicator = (b >> 4) & 0x07;
    }

    public void encodeBody(ByteBuf buffer) {
        int c = (this.numberingPlanIndicator & 0x07) << 4;
        c |= (this.internalNetworkNumberIndicator << 7);
        buffer.writeByte(c);
    }

    public int getNumberingPlanIndicator() {
        return this.numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public int getInternalNetworkNumberIndicator() {
        return internalNetworkNumberIndicator;
    }

    public void setInternalNetworkNumberIndicator(int internalNetworkNumberIndicator) {
        this.internalNetworkNumberIndicator = internalNetworkNumberIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
