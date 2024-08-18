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

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.restcomm.protocols.ss7.isup.ParameterException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class GenericNotificationIndicatorTest extends ParameterHarness {

    public GenericNotificationIndicatorTest() {
        super();
        super.goodBodies.add(Unpooled.wrappedBuffer(new byte[] { 67, 12, 13, 14, 15, 16, 17, (byte) (18 | (0x01 << 7)) }));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[1]));
    }

    private ByteBuf getBody() {
        return Unpooled.wrappedBuffer(super.goodBodies.get(0));
    }

    @Test
    public void testBody1EncodedValues() throws IOException, ParameterException {
        GenericNotificationIndicatorImpl eci = new GenericNotificationIndicatorImpl(getBody());
        ByteBuf body = getBody();
        ByteBuf encoded=Unpooled.buffer();
        eci.encode(encoded);
        assertTrue(ParameterHarness.byteBufEquals(body, encoded));

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new GenericNotificationIndicatorImpl(Unpooled.wrappedBuffer(new byte[2]));
    }

}
