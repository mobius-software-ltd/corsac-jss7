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
public class TraceEventListTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 32, -128, 2, 7, -128, -127, 2, 7, -128, -126, 2, 6, 64, -125, 2, 6, 64, -124, 2, 7, -128, -123, 2, 2, 36, -122, 2, 5, 32, -121, 2, 6, 64 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(TraceEventListImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TraceEventListImpl);
        TraceEventListImpl asc = (TraceEventListImpl)result.getResult();
        
        assertTrue(asc.getMscSList().getMoMtCall());
        assertFalse(asc.getMscSList().getSs());

        assertTrue(asc.getMgwList().getContext());

        assertTrue(asc.getSgsnList().getMoMtSms());
        assertFalse(asc.getSgsnList().getMbmsContext());

        assertTrue(asc.getGgsnList().getMbmsContext());
        assertFalse(asc.getGgsnList().getPdpContext());

        assertTrue(asc.getBmscList().getMbmsMulticastServiceActivation());

        assertTrue(asc.getMmeList().getInitialAttachTrackingAreaUpdateDetach());
        assertTrue(asc.getMmeList().getHandover());
        assertFalse(asc.getMmeList().getServiceRequestts());

        assertTrue(asc.getSgwList().getBearerActivationModificationDeletion());
        assertFalse(asc.getSgwList().getPdnConnectionCreation());

        assertTrue(asc.getPgwList().getConnectionTermination());
        assertFalse(asc.getPgwList().getBearerActivationModificationDeletion());

    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(TraceEventListImpl.class);
    	
    	MSCSEventListImpl mscSList = new MSCSEventListImpl(true, false, false, false, false);
    	MGWEventListImpl mgwList = new MGWEventListImpl(true);
    	SGSNEventListImpl sgsnList = new SGSNEventListImpl(false, true, false, false);
    	GGSNEventListImpl ggsnList = new GGSNEventListImpl(false, true);
    	BMSCEventListImpl bmscList = new BMSCEventListImpl(true);
    	MMEEventListImpl mmeList = new MMEEventListImpl(false, false, true, false, false, true);
    	SGWEventListImpl sgwList = new SGWEventListImpl(false, false, true);
    	PGWEventListImpl pgwList = new PGWEventListImpl(false, true, false);
        TraceEventListImpl asc = new TraceEventListImpl(mscSList, mgwList, sgsnList, ggsnList, bmscList, mmeList, sgwList, pgwList);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}