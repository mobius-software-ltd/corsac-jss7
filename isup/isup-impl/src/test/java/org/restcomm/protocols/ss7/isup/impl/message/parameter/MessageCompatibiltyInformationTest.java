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

import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 * Class to test BCI
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class MessageCompatibiltyInformationTest extends ParameterHarness {

    public MessageCompatibiltyInformationTest() {
        super();

        super.badBodies.add(Unpooled.wrappedBuffer(new byte[1]));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[3]));

        super.goodBodies.add(Unpooled.wrappedBuffer(getBody1()));
        super.goodBodies.add(Unpooled.wrappedBuffer(getBody2()));
    }

    private ByteBuf getBody1() {

        byte[] body = new byte[] { (byte) 0x81 };
        return Unpooled.wrappedBuffer(body);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        MessageCompatibilityInformationImpl at = new MessageCompatibilityInformationImpl(getBody1());
        final String[] getterMethodNames = new String[]{"isTransitAtIntermediateExchangeIndicator",
                "isReleaseCallIndicator",
                "isSendNotificationIndicator",
                "isDiscardMessageIndicator",
                "isPassOnNotPossibleIndicator",
                "getBandInterworkingIndicator"};
        final Object[][] expectedValues = new Object[][]{
                new Object[]{true,false,false,false,false,0}
        };
        testValues(at, "getMessageCompatibilityInstructionIndicators", getterMethodNames, expectedValues);
    }

    private ByteBuf getBody2() {

        byte[] body = new byte[] { 0x42, (byte) 0x81 };
        return Unpooled.wrappedBuffer(body);
    }
    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        MessageCompatibilityInformationImpl at = new MessageCompatibilityInformationImpl(getBody2());
        final String[] getterMethodNames = new String[]{"isTransitAtIntermediateExchangeIndicator",
                "isReleaseCallIndicator",
                "isSendNotificationIndicator",
                "isDiscardMessageIndicator",
                "isPassOnNotPossibleIndicator",
                "getBandInterworkingIndicator"};
        final Object[][] expectedValues = new Object[][]{
                new Object[]{false,true,false,false,false,2},
                new Object[]{true,false,false,false,false,0}
        };
        testValues(at, "getMessageCompatibilityInstructionIndicators", getterMethodNames, expectedValues);
    }
    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new org.restcomm.protocols.ss7.isup.impl.message.parameter.MessageCompatibilityInformationImpl();
    }
    
}
