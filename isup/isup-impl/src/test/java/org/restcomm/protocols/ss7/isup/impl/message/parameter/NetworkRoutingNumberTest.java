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
public class NetworkRoutingNumberTest extends ParameterHarness {

    /**
     * @throws IOException
     */
    public NetworkRoutingNumberTest() throws IOException {
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[1]));

        super.goodBodies.add(Unpooled.wrappedBuffer(getBody(false, getSixDigits(), NetworkRoutingNumberImpl._NPI_ISDN_NP,
                NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF)));
        super.goodBodies.add(Unpooled.wrappedBuffer(getBody(true, getFiveDigits(), NetworkRoutingNumberImpl._NPI_ISDN_NP,
                NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF)));
        // This will fail, cause this body has APRI allowed, so hardcoded body
        // does nto match encoded body :)
        // super.goodBodies.add(getBody2());
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        NetworkRoutingNumberImpl bci = new NetworkRoutingNumberImpl(getBody(false, getSixDigits(),
                NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF));

        String[] methodNames = { "isOddFlag", "getNumberingPlanIndicator", "getNatureOfAddressIndicator", "getAddress" };
        Object[] expectedValues = { false, NetworkRoutingNumberImpl._NPI_ISDN_NP,
                NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF, getSixDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        NetworkRoutingNumberImpl bci = new NetworkRoutingNumberImpl(getBody(true, getFiveDigits(),
                NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF));

        String[] methodNames = { "isOddFlag", "getNumberingPlanIndicator", "getNatureOfAddressIndicator", "getAddress" };
        Object[] expectedValues = { true, NetworkRoutingNumberImpl._NPI_ISDN_NP,
                NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF, getFiveDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    private ByteBuf getBody(boolean isODD, byte[] digits, int npiIsdnNp, int naiNrniNetworkSnf) throws IOException {
        int b = 0;
        if (isODD) {
            b |= 0x01 << 7;
        }
        b |= npiIsdnNp << 4;
        b |= naiNrniNetworkSnf;
        ByteBuf bos = Unpooled.buffer();
        bos.writeByte(b);
        bos.writeBytes(digits);
        return bos;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() {
        return new NetworkRoutingNumberImpl("1", 1, 1);
    }

}
