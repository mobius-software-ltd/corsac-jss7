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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
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
public class ExtBasicServiceCodeTest {

    private byte[] getEncodedData1() {
        return new byte[] { 48, 3, (byte) 130, 1, 22 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 3, (byte) 131, 1, 17 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48, 3, (byte) 131, 1, 16 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtBasicServiceCodeImpl.class);
    	        
        byte[] rawData = getEncodedData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtBasicServiceCodeImpl);
        ExtBasicServiceCodeImpl impl = (ExtBasicServiceCodeImpl)result.getResult();
        
        assertEquals(impl.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.dataCDA_9600bps);
        assertNull(impl.getExtTeleservice());

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtBasicServiceCodeImpl);
        impl = (ExtBasicServiceCodeImpl)result.getResult();
        assertEquals(impl.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.telephony);
        assertNull(impl.getExtBearerService());

        rawData = getEncodedData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtBasicServiceCodeImpl);
        impl = (ExtBasicServiceCodeImpl)result.getResult();
        assertEquals(impl.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);
        assertNull(impl.getExtBearerService());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtBasicServiceCodeImpl.class);
    	        
    	ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.dataCDA_9600bps);
    	ExtBasicServiceCodeImpl impl = new ExtBasicServiceCodeImpl(b);
    	ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
    	byte[] rawData = getEncodedData1();
    	assertTrue(Arrays.equals(rawData, encodedData));
        
    	ExtTeleserviceCodeImpl t = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.telephony);
    	impl = new ExtBasicServiceCodeImpl(t);
    	buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
    	rawData = getEncodedData2();
    	assertTrue(Arrays.equals(rawData, encodedData));
        
    	t = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
    	impl = new ExtBasicServiceCodeImpl(t);
    	buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
    	rawData = getEncodedData3();
    	assertTrue(Arrays.equals(rawData, encodedData));        
    }
}