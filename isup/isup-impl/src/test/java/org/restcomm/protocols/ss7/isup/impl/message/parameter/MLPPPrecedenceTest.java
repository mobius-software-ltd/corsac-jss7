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

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:17:14:12 2009-04-24<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
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
