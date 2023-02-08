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

package org.restcomm.protocols.ss7.commonapp.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

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
public class CellGlobalIdOrServiceAreaIdFixedLengthTest {

    public byte[] getData() {
        return new byte[] { 4, 7, 82, (byte) 240, 16, 17, 92, 13, 5 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 7, 16, 97, 66, 1, 77, 1, (byte) 188 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
    	
        byte[] data = this.getData();
        ASNDecodeResult result = parser.decode(Unpooled.wrappedBuffer(data));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CellGlobalIdOrServiceAreaIdFixedLengthImpl);
        CellGlobalIdOrServiceAreaIdFixedLengthImpl prim = (CellGlobalIdOrServiceAreaIdFixedLengthImpl)result.getResult();
        
        assertEquals(prim.getMCC(), 250);
        assertEquals(prim.getMNC(), 1);
        assertEquals(prim.getLac(), 4444);
        assertEquals(prim.getCellIdOrServiceAreaCode(), 3333);

        data = this.getData2();
        result = parser.decode(Unpooled.wrappedBuffer(data));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CellGlobalIdOrServiceAreaIdFixedLengthImpl);
        prim = (CellGlobalIdOrServiceAreaIdFixedLengthImpl)result.getResult();
        
        assertEquals(prim.getMCC(), 11);
        assertEquals(prim.getMNC(), 246);
        assertEquals(prim.getLac(), 333);
        assertEquals(prim.getCellIdOrServiceAreaCode(), 444);
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CellGlobalIdOrServiceAreaIdFixedLengthImpl.class);
    	
        CellGlobalIdOrServiceAreaIdFixedLengthImpl prim = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(250, 1, 4444, 3333);
        ByteBuf buffer = parser.encode(prim);
        byte[] data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(data, this.getData()));

        prim = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(11, 246, 333, 444);
        buffer = parser.encode(prim);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(data, this.getData2()));
    }
}
