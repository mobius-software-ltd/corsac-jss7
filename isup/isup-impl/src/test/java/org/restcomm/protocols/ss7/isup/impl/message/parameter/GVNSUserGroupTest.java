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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.junit.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class GVNSUserGroupTest extends ParameterHarness {

    /**
     * @throws IOException
     */
    public GVNSUserGroupTest() throws IOException {

        super.goodBodies.add(Unpooled.wrappedBuffer(getBody(getSixDigits(), false)));

    }

    private ByteBuf getBody(byte[] digits, boolean isODD) throws IOException {
        ByteBuf bos = Unpooled.buffer();
        // we will use odd number of digits, so we leave zero as MSB
        int header = digits.length;
        if (isODD) {
            header |= 0x01 << 7;
        }
        bos.writeByte(header);
        bos.writeBytes(digits);
        return bos;
    }

    @Test
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        GVNSUserGroupImpl bci = new GVNSUserGroupImpl(getBody(getSixDigits(), false));

        String[] methodNames = { "getAddress", "getGugLengthIndicator" };
        Object[] expectedValues = { getSixDigitsString(), 3 };
        super.testValues(bci, methodNames, expectedValues);
    }

    @Test
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        GVNSUserGroupImpl bci = new GVNSUserGroupImpl(getBody(getFiveDigits(), true));

        String[] methodNames = { "getAddress", "getGugLengthIndicator" };
        Object[] expectedValues = { getFiveDigitsString(), 3 };
        super.testValues(bci, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() {
        return new GVNSUserGroupImpl("12");
    }

}
