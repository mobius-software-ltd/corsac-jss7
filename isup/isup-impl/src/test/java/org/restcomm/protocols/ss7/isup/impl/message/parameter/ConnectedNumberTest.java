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
import org.restcomm.protocols.ss7.isup.message.parameter.ConnectedNumber;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class ConnectedNumberTest extends ParameterHarness {

    /**
     * @throws IOException
     */
    public ConnectedNumberTest() throws IOException {
//        super.badBodies.add(new byte[1]);
//
        super.goodBodies.add(Unpooled.wrappedBuffer(getBody1()));
        super.goodBodies.add(Unpooled.wrappedBuffer(getBody2()));
        super.goodBodies.add(Unpooled.wrappedBuffer(getBody3()));
    }

    private ByteBuf getBody1() throws IOException {
    	ByteBuf bos = Unpooled.buffer();
        // we will use odd number of digits, so we leave zero as MSB
        // 0 because - _APRI_NOT_AVAILABLE
        int v = 0;
        bos.writeByte(v);
        v = 0;
        v = ConnectedNumberImpl._APRI_NOT_AVAILABLE << 2;
        v |= ConnectedNumberImpl._SI_NETWORK_PROVIDED;
        bos.writeByte(v & 0x7F);
        return bos;
    }

    private ByteBuf getBody2() throws IOException {
    	ByteBuf bos = Unpooled.buffer();

        // We have odd number
        int v = ConnectedNumber._NAI_SUBSCRIBER_NUMBER | (0x01 << 7);
        bos.writeByte(v);
        v = 0;
        v = ConnectedNumberImpl._NPI_TELEX << 4;
        v |= ConnectedNumberImpl._APRI_ALLOWED << 2;
        v |= ConnectedNumberImpl._SI_NETWORK_PROVIDED;
        bos.writeByte(v & 0x7F);
        bos.writeBytes(super.getFiveDigits());
        return bos;
    }

    private ByteBuf getBody3() throws IOException {
        ByteBuf bos = Unpooled.buffer();

        // We have odd number
        int v = ConnectedNumber._NAI_SUBSCRIBER_NUMBER;
        bos.writeByte(v);
        v = 0;
        v = ConnectedNumberImpl._NPI_TELEX << 4;
        v |= ConnectedNumberImpl._APRI_ALLOWED << 2;
        v |= ConnectedNumberImpl._SI_NETWORK_PROVIDED;
        bos.writeByte(v & 0x7F);
        bos.writeBytes(super.getSixDigits());
        return bos;
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        ConnectedNumberImpl bci = new ConnectedNumberImpl(getBody1());

        String[] methodNames = { "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator",
                "getNatureOfAddressIndicator", "getScreeningIndicator", "isOddFlag", "getAddress" };
        Object[] expectedValues = { 0, ConnectedNumberImpl._APRI_NOT_AVAILABLE,
                0, ConnectedNumberImpl._SI_NETWORK_PROVIDED, false,
                null };
        super.testValues(bci, methodNames, expectedValues);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        ConnectedNumberImpl bci = new ConnectedNumberImpl(getBody2());

        String[] methodNames = { "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator",
                "getNatureOfAddressIndicator", "getScreeningIndicator", "isOddFlag", "getAddress" };
        Object[] expectedValues = { ConnectedNumberImpl._NPI_TELEX, ConnectedNumberImpl._APRI_ALLOWED,
                ConnectedNumber._NAI_SUBSCRIBER_NUMBER, ConnectedNumberImpl._SI_NETWORK_PROVIDED, true,
                super.getFiveDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody3EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        ConnectedNumberImpl bci = new ConnectedNumberImpl(getBody3());

        String[] methodNames = { "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator",
                "getNatureOfAddressIndicator", "getScreeningIndicator", "isOddFlag", "getAddress" };
        Object[] expectedValues = { ConnectedNumberImpl._NPI_TELEX, ConnectedNumberImpl._APRI_ALLOWED,
                ConnectedNumber._NAI_SUBSCRIBER_NUMBER, ConnectedNumberImpl._SI_NETWORK_PROVIDED, false,
                super.getSixDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() {
        return new ConnectedNumberImpl(0, null, 0, 0, 0);
    }

}
