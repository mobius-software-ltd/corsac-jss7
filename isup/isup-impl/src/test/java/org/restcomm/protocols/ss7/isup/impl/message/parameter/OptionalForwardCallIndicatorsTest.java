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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:16:20:47 2009-04-26<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class OptionalForwardCallIndicatorsTest extends ParameterHarness {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    public OptionalForwardCallIndicatorsTest() {
        super();
        super.goodBodies.add(Unpooled.wrappedBuffer(new byte[] { 7 }));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[] { 8, 8 }));
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        OptionalForwardCallIndicatorsImpl bci = new OptionalForwardCallIndicatorsImpl(getBody(
                OptionalForwardCallIndicatorsImpl._CUGCI_CUG_CALL_OAL, OptionalForwardCallIndicatorsImpl._SSI_ADDITIONAL_INFO,
                OptionalForwardCallIndicatorsImpl._CLIRI_REQUESTED));

        String[] methodNames = { "getClosedUserGroupCallIndicator", "isSimpleSegmentationIndicator",
                "isConnectedLineIdentityRequestIndicator" };
        Object[] expectedValues = { OptionalForwardCallIndicatorsImpl._CUGCI_CUG_CALL_OAL,
                OptionalForwardCallIndicatorsImpl._SSI_ADDITIONAL_INFO, OptionalForwardCallIndicatorsImpl._CLIRI_REQUESTED };
        super.testValues(bci, methodNames, expectedValues);
    }

    private ByteBuf getBody(int i, boolean _SSI, boolean _CLIRI) {

        byte v = (byte) i;
        v |= ((_SSI ? _TURN_ON : _TURN_OFF) << 2);
        v |= ((_CLIRI ? _TURN_ON : _TURN_OFF) << 7);
        return Unpooled.wrappedBuffer(new byte[] { (byte) v });
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new OptionalForwardCallIndicatorsImpl(Unpooled.wrappedBuffer(new byte[1]));
    }

}
