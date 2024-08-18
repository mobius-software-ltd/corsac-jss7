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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintuplet;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.QuintupletList;
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
public class QuintupletListTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 80, 48, 78, 4, 16, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 2, 2, 2, 2, 4, 16, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 16, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 16, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(QuintupletListImpl.class);

        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof QuintupletListImpl);
        QuintupletListImpl prim = (QuintupletListImpl)result.getResult();
        
        assertEquals(prim.getAuthenticationQuintuplets().size(), 1);

        assertTrue(ByteBufUtil.equals(prim.getAuthenticationQuintuplets().get(0).getRand(),Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getRandData())));
        assertTrue(ByteBufUtil.equals(prim.getAuthenticationQuintuplets().get(0).getXres(),Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getXresData())));
        assertTrue(ByteBufUtil.equals(prim.getAuthenticationQuintuplets().get(0).getCk(), Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getCkData())));
        assertTrue(ByteBufUtil.equals(prim.getAuthenticationQuintuplets().get(0).getIk(), Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getIkData())));
        assertTrue(ByteBufUtil.equals(prim.getAuthenticationQuintuplets().get(0).getAutn(),Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getAutnData())));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(QuintupletListImpl.class);

        AuthenticationQuintupletImpl d1 = new AuthenticationQuintupletImpl(Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getRandData()),
        		Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getXresData()), Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getCkData()),
        		Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getIkData()), Unpooled.wrappedBuffer(AuthenticationQuintupletTest.getAutnData()));

        List<AuthenticationQuintuplet> arr = new ArrayList<AuthenticationQuintuplet>();
        arr.add(d1);
        QuintupletList asc = new QuintupletListImpl(arr);

        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}