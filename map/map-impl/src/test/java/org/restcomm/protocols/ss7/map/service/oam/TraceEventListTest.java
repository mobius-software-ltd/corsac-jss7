/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.service.oam;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.oam.BMSCEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.GGSNEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MGWEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MMEEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MSCSEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.PGWEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.SGSNEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.SGWEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventListImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
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