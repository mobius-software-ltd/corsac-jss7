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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

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
public class AuthenticationTripletTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 34, 4, 16, 15, (byte) 254, 18, (byte) 164, (byte) 207, 43, (byte) 221, (byte) 185, (byte) 178,
                (byte) 158, 109, 83, (byte) 180, (byte) 169, 77, (byte) 128, 4, 4, (byte) 224, 82, (byte) 239, (byte) 242, 4,
                8, 31, 72, (byte) 163, 97, 78, (byte) 239, (byte) 204, 0 };
    }

    static protected byte[] getRandData() {
        return new byte[] { 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128 };
    }

    static protected byte[] getSresData() {
        return new byte[] { -32, 82, -17, -14 };
    }

    static protected byte[] getKcData() {
        return new byte[] { 31, 72, -93, 97, 78, -17, -52, 0 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AuthenticationTripletImpl.class);

    	byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AuthenticationTripletImpl);
        AuthenticationTripletImpl asc = (AuthenticationTripletImpl)result.getResult();
        
        assertTrue(ByteBufUtil.equals(asc.getRand(), Unpooled.wrappedBuffer(getRandData())));
        assertTrue(ByteBufUtil.equals(asc.getSres(), Unpooled.wrappedBuffer(getSresData())));
        assertTrue(ByteBufUtil.equals(asc.getKc(), Unpooled.wrappedBuffer(getKcData())));

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AuthenticationTripletImpl.class);

        AuthenticationTripletImpl asc = new AuthenticationTripletImpl(Unpooled.wrappedBuffer(getRandData()), 
        		Unpooled.wrappedBuffer(getSresData()), Unpooled.wrappedBuffer(getKcData()));
        byte[] data=getEncodedData();        		
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}
