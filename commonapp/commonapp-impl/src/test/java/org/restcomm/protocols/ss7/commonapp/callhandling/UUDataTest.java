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

package org.restcomm.protocols.ss7.commonapp.callhandling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class UUDataTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 128, 1, (byte) 140 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 53, (byte) 128, 1, (byte) 140, (byte) 129, 5, 1, 2, 3, 4, 5, (byte) 130, 0, (byte) 163, 39, (byte) 160, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    }

    public byte[] getUUIData() {
        return new byte[] { 1, 2, 3, 4, 5 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(UUDataImpl.class);

        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof UUDataImpl);
        UUDataImpl elem = (UUDataImpl)result.getResult();
        
        assertEquals(elem.getUUIndicator().getData(), new Integer(140));
        assertNull(elem.getUUI());
        assertFalse(elem.getUusCFInteraction());
        assertNull(elem.getExtensionContainer());


        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof UUDataImpl);
        elem = (UUDataImpl)result.getResult();

        assertEquals(elem.getUUIndicator().getData(), new Integer(140));
        assertTrue(ByteBufUtil.equals(elem.getUUI().getValue(), Unpooled.wrappedBuffer(getUUIData())));
        assertTrue(elem.getUusCFInteraction());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(elem.getExtensionContainer()));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(UUDataImpl.class);

        UUIndicatorImpl uuIndicator = new UUIndicatorImpl(140);
        UUDataImpl elem = new UUDataImpl(uuIndicator, null, false, null);
        
        byte[] data=getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        UUIImpl uuI = new UUIImpl(Unpooled.wrappedBuffer(getUUIData()));
        elem = new UUDataImpl(uuIndicator, uuI, true, MAPExtensionContainerTest.GetTestExtensionContainer());

        data=getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}