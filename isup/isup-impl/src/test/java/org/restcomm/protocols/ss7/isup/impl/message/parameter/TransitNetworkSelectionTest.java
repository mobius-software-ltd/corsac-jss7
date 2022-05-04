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
 * Start time:14:11:03 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class TransitNetworkSelectionTest extends ParameterHarness {

    /**
     * @throws IOException
     */
    public TransitNetworkSelectionTest() throws IOException {
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[1]));

    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        TransitNetworkSelectionImpl bci = new TransitNetworkSelectionImpl(getBody(false,
                TransitNetworkSelectionImpl._NIP_PDNIC, TransitNetworkSelectionImpl._TONI_ITU_T, getSixDigits()));

        String[] methodNames = { "isOddFlag", "getNetworkIdentificationPlan", "getTypeOfNetworkIdentification", "getAddress" };
        Object[] expectedValues = { false, TransitNetworkSelectionImpl._NIP_PDNIC, TransitNetworkSelectionImpl._TONI_ITU_T,
                getSixDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    private ByteBuf getBody(boolean isODD, int _NIP, int _TONI, byte[] digits) throws IOException {
    	ByteBuf bos = Unpooled.buffer();
        // we will use odd number of digits, so we leave zero as MSB

        if (isODD) {
            bos.writeByte(0x80 | (_TONI << 4) | _NIP);
        } else {
            bos.writeByte((_TONI << 4) | _NIP);
        }

        bos.writeBytes(digits);
        return bos;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() {
        return new TransitNetworkSelectionImpl("11", 1, 1);
    }

}
