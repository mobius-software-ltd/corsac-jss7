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

package org.restcomm.protocols.ss7.cap.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.TimeAndTimezoneImpl;
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
public class TimeAndTimezoneTest {

    public byte[] getData1() {
        return new byte[] { 4, 8, 2, 17, 33, 3, 1, 112, (byte) 129, 35 };
    }

    public byte[] getData2() {
        return new byte[] { 4, 8, 2, 17, 33, 3, 1, 112, (byte) 129, 43 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TimeAndTimezoneImpl.class);
    	
        byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TimeAndTimezoneImpl);
        
        TimeAndTimezoneImpl elem = (TimeAndTimezoneImpl)result.getResult();        
        assertEquals(elem.getYear(), 2011);
        assertEquals(elem.getMonth(), 12);
        assertEquals(elem.getDay(), 30);
        assertEquals(elem.getHour(), 10);
        assertEquals(elem.getMinute(), 7);
        assertEquals(elem.getSecond(), 18);
        assertEquals(elem.getTimeZone(), 32);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TimeAndTimezoneImpl);
        
        elem = (TimeAndTimezoneImpl)result.getResult();    
        assertEquals(elem.getYear(), 2011);
        assertEquals(elem.getMonth(), 12);
        assertEquals(elem.getDay(), 30);
        assertEquals(elem.getHour(), 10);
        assertEquals(elem.getMinute(), 7);
        assertEquals(elem.getSecond(), 18);
        assertEquals(elem.getTimeZone(), -32);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TimeAndTimezoneImpl.class);
    	
        TimeAndTimezoneImpl elem = new TimeAndTimezoneImpl(2011, 12, 30, 10, 7, 18, 32);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new TimeAndTimezoneImpl(2011, 12, 30, 10, 7, 18, -32);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
