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

package org.restcomm.protocols.ss7.map.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

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
public class GlobalCellIdTest {

    public byte[] getData() {
        return new byte[] { 4, 7, 82, (byte) 240, 16, 17, 92, 13, 5 };
    };

    public byte[] getDataVal() {
        return new byte[] { 82, (byte) 240, 16, 17, 92, 13, 5 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 5, 82, (byte) 240, 16, 17, 92 };
    };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GlobalCellIdImpl.class);
    	
    	byte[] data = this.getData();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GlobalCellIdImpl);
        GlobalCellIdImpl prim = (GlobalCellIdImpl)result.getResult();        
        
        assertEquals(prim.getMcc(), 250);
        assertEquals(prim.getMnc(), 1);
        assertEquals(prim.getLac(), 4444);
        assertEquals(prim.getCellId(), 3333);


        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GlobalCellIdImpl);
        prim = (GlobalCellIdImpl)result.getResult();     

        assertEquals(prim.getMcc(), 250);
        assertEquals(prim.getMnc(), 1);
        assertEquals(prim.getLac(), 4444);
        assertEquals(prim.getCellId(), 0);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GlobalCellIdImpl.class);
    	
        GlobalCellIdImpl prim = new GlobalCellIdImpl(250, 1, 4444, 3333);
        ByteBuf buffer=parser.encode(prim);
        byte[] data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(data, this.getData()));
    }
}
