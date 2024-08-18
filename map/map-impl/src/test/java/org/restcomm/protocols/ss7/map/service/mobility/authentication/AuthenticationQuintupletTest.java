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
public class AuthenticationQuintupletTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 78, 4, 16, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 2, 2, 2, 2, 4, 16, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 16, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 16, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
    }

    static protected byte[] getRandData() {
        return new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
    }

    static protected byte[] getXresData() {
        return new byte[] { 2, 2, 2, 2 };
    }

    static protected byte[] getCkData() {
        return new byte[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 };
    }

    static protected byte[] getIkData() {
        return new byte[] { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 };
    }

    static protected byte[] getAutnData() {
        return new byte[] { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AuthenticationQuintupletImpl.class);

        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AuthenticationQuintupletImpl);
        AuthenticationQuintupletImpl asc = (AuthenticationQuintupletImpl)result.getResult();
        
        assertTrue(ByteBufUtil.equals(asc.getRand(), Unpooled.wrappedBuffer(getRandData())));
        assertTrue(ByteBufUtil.equals(asc.getXres(), Unpooled.wrappedBuffer(getXresData())));
        assertTrue(ByteBufUtil.equals(asc.getCk(), Unpooled.wrappedBuffer(getCkData())));
        assertTrue(ByteBufUtil.equals(asc.getIk(), Unpooled.wrappedBuffer(getIkData())));
        assertTrue(ByteBufUtil.equals(asc.getAutn(), Unpooled.wrappedBuffer(getAutnData())));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AuthenticationQuintupletImpl.class);

        AuthenticationQuintupletImpl asc = new AuthenticationQuintupletImpl(Unpooled.wrappedBuffer(getRandData()), 
        		Unpooled.wrappedBuffer(getXresData()), Unpooled.wrappedBuffer(getCkData()),
        		Unpooled.wrappedBuffer(getIkData()), Unpooled.wrappedBuffer(getAutnData()));

        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}
