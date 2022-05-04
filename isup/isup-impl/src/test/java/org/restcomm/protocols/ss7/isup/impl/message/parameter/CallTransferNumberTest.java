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
 * 
 * @author baranowb
 * @author yulianoifa
 * 
 */
public class CallTransferNumberTest extends ParameterHarness {
    /**
     * @throws IOException
     */
    public CallTransferNumberTest() throws IOException {
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[1]));

        super.goodBodies.add(Unpooled.wrappedBuffer(getBody1()));
        super.goodBodies.add(Unpooled.wrappedBuffer(getBody2()));
    }

    private ByteBuf getBody1() throws IOException {
        ByteBuf bos = Unpooled.buffer();
        // we will use odd number of digits, so we leave zero as MSB
        bos.writeByte(CallTransferNumberImpl._NAI_NATIONAL_SN);
        bos.writeByte((CallTransferNumberImpl._NPI_ISDN << 4) | (CallTransferNumberImpl._APRI_RESTRICTED << 2)
                | (CallTransferNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED));
        bos.writeBytes(getEightDigits());
        return bos;
    }

    private ByteBuf getBody2() throws IOException {
    	ByteBuf bos = Unpooled.buffer();
        bos.writeByte(0x80 | CallTransferNumberImpl._NAI_NRNCWCDN);
        bos.writeByte((CallTransferNumberImpl._NPI_TELEX << 4) | (CallTransferNumberImpl._APRI_ALLOWED << 2) | (CallTransferNumberImpl._SI_NETWORK_PROVIDED));
        bos.writeBytes(getSevenDigits());
        return bos;
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        CallTransferNumberImpl bci = new CallTransferNumberImpl(getBody1());

        String[] methodNames = { 
                "isOddFlag","getNatureOfAddressIndicator",
                "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator","getScreeningIndicator", 
                "getAddress" };
        Object[] expectedValues = {
                false,CallTransferNumberImpl._NAI_NATIONAL_SN,
                CallTransferNumberImpl._NPI_ISDN,CallTransferNumberImpl._APRI_RESTRICTED,CallTransferNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED,
                getEightDigitsString()};
        super.testValues(bci, methodNames, expectedValues);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        CallTransferNumberImpl bci = new CallTransferNumberImpl(getBody2());


        String[] methodNames = { 
                "isOddFlag","getNatureOfAddressIndicator",
                "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator","getScreeningIndicator", 
                "getAddress" };
        Object[] expectedValues = {
                true,CallTransferNumberImpl._NAI_NRNCWCDN,
                CallTransferNumberImpl._NPI_TELEX,CallTransferNumberImpl._APRI_ALLOWED,CallTransferNumberImpl._SI_NETWORK_PROVIDED,
                getSevenDigitsString()};
        super.testValues(bci, methodNames, expectedValues);
    }

    @Override
    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new CallTransferNumberImpl();
    }
}
