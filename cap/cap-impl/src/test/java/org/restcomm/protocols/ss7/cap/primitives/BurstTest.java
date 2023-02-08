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

package org.restcomm.protocols.ss7.cap.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.BurstImpl;
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
public class BurstTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 129, 1, 10 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 15, (byte) 128, 1, 1, (byte) 129, 1, 10, (byte) 130, 1, 2, (byte) 131, 1, 11, (byte) 132, 1, 12 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BurstImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BurstImpl);
        
        BurstImpl elem = (BurstImpl)result.getResult();
        assertEquals((int) elem.getNumberOfBursts(),1);
        assertEquals((int) elem.getBurstInterval(), 10);
        assertEquals((int) elem.getNumberOfTonesInBurst(),3);
        assertEquals((int) elem.getToneDuration(),2);
        assertEquals((int) elem.getToneInterval(),2);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BurstImpl);
        
        elem = (BurstImpl)result.getResult();
        assertEquals((int) elem.getNumberOfBursts(), 1);
        assertEquals((int) elem.getBurstInterval(), 10);
        assertEquals((int) elem.getNumberOfTonesInBurst(), 2);
        assertEquals((int) elem.getToneDuration(), 11);
        assertEquals((int) elem.getToneInterval(), 12);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BurstImpl.class);
    	
        BurstImpl elem = new BurstImpl(null, 10, null, null, null);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new BurstImpl(1, 10, 2, 11, 12);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
