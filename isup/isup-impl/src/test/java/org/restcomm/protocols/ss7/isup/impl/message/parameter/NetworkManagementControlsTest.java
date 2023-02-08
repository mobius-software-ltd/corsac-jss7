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

import static org.testng.Assert.fail;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:13:20:04 2009-04-26<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class NetworkManagementControlsTest extends ParameterHarness {

    public NetworkManagementControlsTest() {
        super();

        super.goodBodies.add(Unpooled.wrappedBuffer(new byte[] { (byte)0x80}));
        super.goodBodies.add(Unpooled.wrappedBuffer(new byte[] { (byte)0x81 }));
        super.goodBodies.add(Unpooled.wrappedBuffer(new byte[] { 0x01, 0x01, 0x01, (byte)0x81 }));
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws IOException, ParameterException {

        boolean[] bools = new boolean[] { true, true, false, true, false, true, true };
        NetworkManagementControlsImpl eci = new NetworkManagementControlsImpl(getBody1(bools));
        ByteBuf encoded=Unpooled.buffer();
        eci.encode(encoded);
        
        for (int index = 0; index < bools.length; index++) {
        	byte curr=encoded.readByte();
            if (bools[index] != eci.isTARControlEnabled(curr)) {
                fail("Failed to get TAR bits, at index: " + index);
            }

            if (encoded.readableBytes()==0) {
                if (((curr >> 7) & 0x01) != 1) {
                    fail("Last byte must have MSB turned on to indicate last byte, this one does not.");
                }
            }
        }

    }

    private ByteBuf getBody1(boolean[] tarEnabled) {
        //boolean[] bools = new boolean[] { true, true, false, true, false, true, true };
        NetworkManagementControlsImpl eci = new NetworkManagementControlsImpl();
        ByteBuf b = Unpooled.buffer(tarEnabled.length);
        for (int index = 0; index < tarEnabled.length; index++) {
            b.writeByte(eci.createTAREnabledByte(tarEnabled[index]));
        }
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new NetworkManagementControlsImpl(Unpooled.wrappedBuffer(new byte[1]));
    }

}
