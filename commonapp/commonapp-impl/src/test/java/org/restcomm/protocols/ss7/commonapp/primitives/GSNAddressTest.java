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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.junit.Test;

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
public class GSNAddressTest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 5, 4, (byte) 192, (byte) 168, 4, 22 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 4, 17, 80, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4 };
    }

    private byte[] getData() {
        return new byte[] { (byte) 192, (byte) 168, 4, 22 };
    }

    private byte[] getData2() {
        return new byte[] { 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GSNAddressImpl.class);
    	
    	byte[] rawData = this.getEncodedData();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GSNAddressImpl);
        GSNAddressImpl pi = (GSNAddressImpl)result.getResult();        
        
        assertEquals(pi.getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(pi.getGSNAddressData(),Unpooled.wrappedBuffer(getData())));

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GSNAddressImpl);
        pi = (GSNAddressImpl)result.getResult();        
        
        assertEquals(pi.getGSNAddressAddressType(), GSNAddressAddressType.IPv6);
        assertTrue(ByteBufUtil.equals(pi.getGSNAddressData(),Unpooled.wrappedBuffer(getData2())));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GSNAddressImpl.class);
    	
        GSNAddressImpl pi = new GSNAddressImpl(GSNAddressAddressType.IPv4, Unpooled.wrappedBuffer(getData()));
        ByteBuf buffer = parser.encode(pi);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        pi = new GSNAddressImpl(GSNAddressAddressType.IPv6, Unpooled.wrappedBuffer(getData2()));
        buffer = parser.encode(pi);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);

        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

    }
}
