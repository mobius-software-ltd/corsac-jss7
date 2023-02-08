/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class PromptAndCollectUserInformationResponseTest {

    public byte[] getData1() {
        return new byte[] { (byte) 128, 4, 65, 44, 55, 66 };
    }

    public byte[] getDigits() {
        return new byte[] { 44, 55, 66 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(PromptAndCollectUserInformationResponseImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PromptAndCollectUserInformationResponseImpl);
        
        PromptAndCollectUserInformationResponseImpl elem = (PromptAndCollectUserInformationResponseImpl)result.getResult();                
        assertEquals(elem.getDigitsResponse().getGenericDigits().getEncodingScheme(), 2);
        assertEquals(elem.getDigitsResponse().getGenericDigits().getTypeOfDigits(), 1);
        
        ByteBuf buffer=elem.getDigitsResponse().getGenericDigits().getEncodedDigits();
        assertNotNull(buffer);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, this.getDigits()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(PromptAndCollectUserInformationResponseImpl.class);
    	
        GenericDigitsImpl genericDigits = new GenericDigitsImpl(2, 1, Unpooled.wrappedBuffer(getDigits()));
        // int encodingScheme, int typeOfDigits, int[] digits
        DigitsIsupImpl digitsResponse = new DigitsIsupImpl(genericDigits);

        PromptAndCollectUserInformationResponseImpl elem = new PromptAndCollectUserInformationResponseImpl(digitsResponse);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
