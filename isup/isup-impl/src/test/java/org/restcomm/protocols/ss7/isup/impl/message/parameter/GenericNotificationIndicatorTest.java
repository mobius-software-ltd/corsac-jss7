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

import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

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

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws IOException, ParameterException {
        GenericNotificationIndicatorImpl eci = new GenericNotificationIndicatorImpl(getBody());
        ByteBuf body = getBody();
        ByteBuf encoded=Unpooled.buffer();
        eci.encode(encoded);
        boolean equal = ParameterHarness.byteBufEquals(body, encoded);
        assertTrue(equal, "Body index: \n" + makeCompare(body, encoded));

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
