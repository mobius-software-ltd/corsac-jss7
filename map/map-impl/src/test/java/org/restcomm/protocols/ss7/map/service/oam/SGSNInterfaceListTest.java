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

package org.restcomm.protocols.ss7.map.service.oam;

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
public class SGSNInterfaceListTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 3, 5, (byte) 182, (byte) 160 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SGSNInterfaceListImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SGSNInterfaceListImpl);
        SGSNInterfaceListImpl asc = (SGSNInterfaceListImpl)result.getResult();
        
        assertTrue(asc.getGb());
        assertFalse(asc.getIu());
        assertTrue(asc.getGn());
        assertTrue(asc.getMapGr());
        assertFalse(asc.getMapGd());
        assertTrue(asc.getMapGf());
        assertTrue(asc.getGs());
        assertFalse(asc.getGe());
        assertTrue(asc.getS3());
        assertFalse(asc.getS4());
        assertTrue(asc.getS6d());

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SGSNInterfaceListImpl.class);
    	
        SGSNInterfaceListImpl asc = new SGSNInterfaceListImpl(true, false, true, true, false, true, true, false, true, false, true);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}