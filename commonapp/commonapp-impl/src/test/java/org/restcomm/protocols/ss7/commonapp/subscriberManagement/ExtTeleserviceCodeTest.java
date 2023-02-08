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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
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
public class ExtTeleserviceCodeTest {

    byte[] data = new byte[] { 4, 1, 0x11 };
    byte[] dataEncoded = new byte[] { 0x11 };

    byte[] data2 = new byte[] { 4, 1, 16 };
    byte[] data3 = new byte[] { 4, 1, 34 };

    @Test(groups = { "functional.decode", "subscriberManagement" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtTeleserviceCodeImpl.class);
    	
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtTeleserviceCodeImpl);
        ExtTeleserviceCodeImpl impl = (ExtTeleserviceCodeImpl)result.getResult();
        
        assertEquals(impl.getTeleserviceCodeValue(), TeleserviceCodeValue.telephony);

        result=parser.decode(Unpooled.wrappedBuffer(data2));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtTeleserviceCodeImpl);
        impl = (ExtTeleserviceCodeImpl)result.getResult();
        
        assertEquals(impl.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        result=parser.decode(Unpooled.wrappedBuffer(data3));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtTeleserviceCodeImpl);
        impl = (ExtTeleserviceCodeImpl)result.getResult();

        assertEquals(impl.getTeleserviceCodeValue(), TeleserviceCodeValue.shortMessageMO_PP);
    }

    @Test(groups = { "functional.encode", "subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtTeleserviceCodeImpl.class);
    	
        ExtTeleserviceCodeImpl impl = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.telephony);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = data;
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = data2;
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMO_PP);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = data3;
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
