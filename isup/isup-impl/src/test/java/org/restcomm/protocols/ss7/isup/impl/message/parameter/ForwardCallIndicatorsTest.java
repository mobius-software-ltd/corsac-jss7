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

import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.junit.Test;

/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 * Class to test BCI
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class ForwardCallIndicatorsTest extends ParameterHarness {

    public ForwardCallIndicatorsTest() {
        super();

        super.badBodies.add(Unpooled.wrappedBuffer(getBadBody1()));
        super.badBodies.add(Unpooled.wrappedBuffer(getBadBody2()));
        super.goodBodies.add(Unpooled.wrappedBuffer(getBody1()));
    }

    private ByteBuf getBadBody1() {

        byte[] body = new byte[]{(byte) 0x81};
        return Unpooled.wrappedBuffer(body);
     }
    
    private ByteBuf getBadBody2() {

        byte[] body = new byte[]{(byte) 0x81,3,3};
        return Unpooled.wrappedBuffer(body);
     }
    
    private ByteBuf getBody1() {

        byte[] body = new byte[]{(byte) 0xAA,0x02};
        return Unpooled.wrappedBuffer(body);
     }

    @Test
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        ForwardCallIndicatorsImpl at = new ForwardCallIndicatorsImpl(getBody1());

        String[] methodNames = { "isNationalCallIdentificator",
                "getEndToEndMethodIndicator",
                "isInterworkingIndicator",
                "isEndToEndInformationIndicator",
                "isIsdnUserPartIndicator",
                "getIsdnUserPartReferenceIndicator",
                "getSccpMethodIndicator",
                "isIsdnAccessIndicator" };
        Object[] expectedValues = { false, 1,true,false,true,2,
                                     1,false};

        super.testValues(at, methodNames, expectedValues);
    }

    
    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new org.restcomm.protocols.ss7.isup.impl.message.parameter.ForwardCallIndicatorsImpl();
    }

}