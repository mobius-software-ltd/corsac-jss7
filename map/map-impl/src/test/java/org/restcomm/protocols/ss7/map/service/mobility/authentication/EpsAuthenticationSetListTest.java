/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpcAv;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.testng.annotations.Test;

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
public class EpsAuthenticationSetListTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, -127, -98, 48, 76, 4, 16, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 2, 2, 2, 2, 4,
                16, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 32, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 48, 78, 4, 16, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4,
                6, 22, 22, 22, 22, 22, 22, 4, 16, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 32, 4, 4, 4, 4, 4, 4, 4,
                4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 };
    }

    static protected byte[] getXresData() {
        return new byte[] { 22, 22, 22, 22, 22, 22 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(EpsAuthenticationSetListImpl.class);

        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EpsAuthenticationSetListImpl);
        EpsAuthenticationSetListImpl asc = (EpsAuthenticationSetListImpl)result.getResult();
        
        List<EpcAv> epcAvs = asc.getEpcAv();
        assertEquals(epcAvs.size(), 2);

        assertTrue(ByteBufUtil.equals(epcAvs.get(0).getRand(), Unpooled.wrappedBuffer(EpcAvTest.getRandData())));
        assertTrue(ByteBufUtil.equals(epcAvs.get(0).getXres(), Unpooled.wrappedBuffer(EpcAvTest.getXresData())));
        assertTrue(ByteBufUtil.equals(epcAvs.get(0).getAutn(), Unpooled.wrappedBuffer(EpcAvTest.getAutnData())));
        assertTrue(ByteBufUtil.equals(epcAvs.get(0).getKasme(), Unpooled.wrappedBuffer(EpcAvTest.getKasmeData())));

        assertTrue(ByteBufUtil.equals(epcAvs.get(1).getRand(), Unpooled.wrappedBuffer(EpcAvTest.getRandData())));
        assertTrue(ByteBufUtil.equals(epcAvs.get(1).getXres(), Unpooled.wrappedBuffer(getXresData())));
        assertTrue(ByteBufUtil.equals(epcAvs.get(1).getAutn(), Unpooled.wrappedBuffer(EpcAvTest.getAutnData())));
        assertTrue(ByteBufUtil.equals(epcAvs.get(1).getKasme(), Unpooled.wrappedBuffer(EpcAvTest.getKasmeData())));
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(EpsAuthenticationSetListImpl.class);

        EpcAvImpl d1 = new EpcAvImpl(Unpooled.wrappedBuffer(EpcAvTest.getRandData()), Unpooled.wrappedBuffer(EpcAvTest.getXresData()), 
        		Unpooled.wrappedBuffer(EpcAvTest.getAutnData()),Unpooled.wrappedBuffer(EpcAvTest.getKasmeData()), null);
        EpcAvImpl d2 = new EpcAvImpl(Unpooled.wrappedBuffer(EpcAvTest.getRandData()), Unpooled.wrappedBuffer(getXresData()), 
        		Unpooled.wrappedBuffer(EpcAvTest.getAutnData()), Unpooled.wrappedBuffer(EpcAvTest.getKasmeData()),null);
        List<EpcAv> arr = new ArrayList<EpcAv>();
        arr.add(d1);
        arr.add(d2);
        EpsAuthenticationSetList asc = new EpsAuthenticationSetListImpl(arr);

        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}