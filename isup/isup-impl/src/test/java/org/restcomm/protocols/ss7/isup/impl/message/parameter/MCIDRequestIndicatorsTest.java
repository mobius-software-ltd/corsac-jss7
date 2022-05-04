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
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class MCIDRequestIndicatorsTest extends ParameterHarness {
    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    public MCIDRequestIndicatorsTest() {
        super();
        super.goodBodies.add(Unpooled.wrappedBuffer(new byte[] { 3 }));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[2]));
    }

    private ByteBuf getBody(boolean mcidRequest, boolean holdingRequested) {
        int b0 = 0;

        b0 |= (mcidRequest ? _TURN_ON : _TURN_OFF);
        b0 |= ((holdingRequested ? _TURN_ON : _TURN_OFF)) << 1;

        return Unpooled.wrappedBuffer(new byte[] { (byte) b0 });
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws ParameterException {
        MCIDRequestIndicatorsImpl eci = new MCIDRequestIndicatorsImpl(getBody(MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED,
                MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED));

        String[] methodNames = { "isMcidRequestIndicator", "isHoldingIndicator" };
        Object[] expectedValues = { MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED,
                MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED };
        super.testValues(eci, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new MCIDRequestIndicatorsImpl(Unpooled.wrappedBuffer(new byte[1]));
    }

}
