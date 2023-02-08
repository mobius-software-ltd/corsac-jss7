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
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CriticalityType;
import org.restcomm.protocols.ss7.commonapp.primitives.ExtensionFieldImpl;
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
public class ExtensionFieldTest {

    public byte[] getData1() {
        return new byte[] { 48, 5, 2, 1, 2, (byte) 129, 0 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 7, 6, 2, 40, 22, (byte) 129, 1, (byte) 255 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 11, 2, 2, 8, (byte) 174, 10, 1, 1, (byte) 129, 2, (byte) 253, (byte) 213 };
    }

    public List<Long> getDataOid() {
        return Arrays.asList(new Long[] { 1L, 0L, 22L });
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ExtensionFieldImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtensionFieldImpl);
        
        ExtensionFieldImpl elem = (ExtensionFieldImpl)result.getResult();
        assertEquals((int) elem.getLocalCode(), 2);
        assertEquals(elem.getCriticalityType(), CriticalityType.typeIgnore);
        assertEquals(elem.getValue().readableBytes(),0);
       
        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtensionFieldImpl);
        
        elem = (ExtensionFieldImpl)result.getResult();
        assertEquals(elem.getGlobalCode(), this.getDataOid());
        assertEquals(elem.getCriticalityType(), CriticalityType.typeIgnore);
        assertEquals(elem.getValue().readableBytes(),1);
        assertEquals(elem.getValue().readByte(),-1);
        
        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtensionFieldImpl);
        
        elem = (ExtensionFieldImpl)result.getResult();
        assertEquals((int) elem.getLocalCode(), 2222);
        assertEquals(elem.getCriticalityType(), CriticalityType.typeAbort);
        ByteBuf value=elem.getValue();
        assertEquals(value.readableBytes(),2);
        assertEquals(value.readByte(),-3);        
        assertEquals(value.readByte(),-43);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ExtensionFieldImpl.class);
    	
        ExtensionFieldImpl elem = new ExtensionFieldImpl(2, CriticalityType.typeIgnore, Unpooled.wrappedBuffer(new byte[] {}), false);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new ExtensionFieldImpl(this.getDataOid(), null, Unpooled.wrappedBuffer(new byte[] { -1 }), false);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new ExtensionFieldImpl(2222, CriticalityType.typeAbort, Unpooled.wrappedBuffer(new byte[] { -3, -43 }), false);
        rawData = this.getData3();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
