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
public class TraceInterfaceListTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 40, -128, 2, 3, 8, -127, 2, 5, 32, -126, 2, 6, 64, -125, 2, 7, -128, -124, 2, 4, 16, -123, 2, 7, -128, -122, 2, 3, 8, -121, 2, 7, -128, -120, 2, 2, 4, -119, 2, 6, 64 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(TraceInterfaceListImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TraceInterfaceListImpl);
        TraceInterfaceListImpl asc = (TraceInterfaceListImpl)result.getResult();
        
        assertTrue(asc.getMscSList().getMapB());
        assertFalse(asc.getMscSList().getA());

        assertTrue(asc.getMgwList().getIuUp());
        assertFalse(asc.getMgwList().getMc());

        assertTrue(asc.getSgsnList().getIu());
        assertFalse(asc.getSgsnList().getGe());

        assertTrue(asc.getGgsnList().getGn());
        assertFalse(asc.getGgsnList().getGmb());

        assertTrue(asc.getRncList().getUu());
        assertFalse(asc.getRncList().getIub());

        assertTrue(asc.getBmscList().getGmb());

        assertTrue(asc.getMmeList().getS11());
        assertFalse(asc.getMmeList().getS10());

        assertTrue(asc.getSgwList().getS4());
        assertFalse(asc.getSgwList().getS5());

        assertTrue(asc.getPgwList().getGx());
        assertFalse(asc.getPgwList().getS2a());

        assertTrue(asc.getEnbList().getX2());
        assertFalse(asc.getEnbList().getS1Mme());

    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(TraceInterfaceListImpl.class);
    	
    	MSCSInterfaceListImpl mscSList = new MSCSInterfaceListImpl(false, false, false, false, true, false, false, false, false, false);
    	MGWInterfaceListImpl mgwList = new MGWInterfaceListImpl(false, false, true);
    	SGSNInterfaceListImpl sgsnList = new SGSNInterfaceListImpl(false, true, false, false, false, false, false, false, false, false, false);
    	GGSNInterfaceListImpl ggsnList = new GGSNInterfaceListImpl(true, false, false);
    	RNCInterfaceListImpl rncList = new RNCInterfaceListImpl(false, false, false, true);
        BMSCInterfaceListImpl bmscList = new BMSCInterfaceListImpl(true);
        MMEInterfaceListImpl mmeList = new MMEInterfaceListImpl(false, false, false, false, true);
        SGWInterfaceListImpl sgwList = new SGWInterfaceListImpl(true, false, false, false, false);
        PGWInterfaceListImpl pgwList = new PGWInterfaceListImpl(false, false, false, false, false, true, false, false);
        ENBInterfaceListImpl enbList = new ENBInterfaceListImpl(false, true, false);
        TraceInterfaceListImpl asc = new TraceInterfaceListImpl(mscSList, mgwList, sgsnList, ggsnList, rncList, bmscList, mmeList, sgwList, pgwList, enbList);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}