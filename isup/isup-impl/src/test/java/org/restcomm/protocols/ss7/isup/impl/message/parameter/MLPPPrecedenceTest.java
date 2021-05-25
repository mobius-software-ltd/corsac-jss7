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
 * Start time:17:14:12 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:17:14:12 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class MLPPPrecedenceTest extends ParameterHarness {

    public MLPPPrecedenceTest() {
        super();
        super.goodBodies.add(Unpooled.wrappedBuffer(new byte[6]));

        super.badBodies.add(Unpooled.wrappedBuffer(new byte[5]));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[7]));

    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws IOException, ParameterException {
        // FIXME: This one fails....
        int serDomain = 15;
        MLPPPrecedenceImpl eci = new MLPPPrecedenceImpl(getBody(MLPPPrecedenceImpl._LFB_INDICATOR_ALLOWED,
                MLPPPrecedenceImpl._PLI_PRIORITY, new byte[] { 3, 4 }, serDomain));

        String[] methodNames = { "getLfb", "getPrecedenceLevel", "getMllpServiceDomain" };
        Object[] expectedValues = { (byte) MLPPPrecedenceImpl._LFB_INDICATOR_ALLOWED, (byte) MLPPPrecedenceImpl._PLI_PRIORITY,
                serDomain };

        super.testValues(eci, methodNames, expectedValues);
    }

    private ByteBuf getBody(int lfbIndicatorAllowed, int precedenceLevel, byte[] bs, int mllpServiceDomain) throws IOException {
    	ByteBuf bos = Unpooled.buffer();
        byte b = (byte) ((lfbIndicatorAllowed & 0x03) << 5);
        b |= precedenceLevel & 0x0F;
        bos.writeByte(b);
        bos.writeBytes(bs);

        bos.writeByte(mllpServiceDomain >> 16);
        bos.writeByte(mllpServiceDomain >> 8);
        bos.writeByte(mllpServiceDomain);
        return bos;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        MLPPPrecedenceImpl component = new MLPPPrecedenceImpl(Unpooled.wrappedBuffer(new byte[6]));
        return component;
    }

}
