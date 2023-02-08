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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeature;
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
public class SSInfoTest {

    private byte[] getEncodedData1() {
        return new byte[] { 48, 9, (byte) 160, 7, 48, 5, 48, 3, (byte) 132, 1, 4 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 9, (byte) 161, 7, 48, 5, 48, 3, (byte) 132, 1, 4 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48, 5, (byte) 163, 3, (byte) 132, 1, 4 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SSInfoImpl.class);
                
        byte[] rawData = getEncodedData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SSInfoImpl);
        SSInfoImpl impl = (SSInfoImpl)result.getResult();
        
        assertEquals(impl.getForwardingInfo().getForwardingFeatureList().size(), 1);
        assertFalse(impl.getForwardingInfo().getForwardingFeatureList().get(0).getSsStatus().getQBit());
        assertTrue(impl.getForwardingInfo().getForwardingFeatureList().get(0).getSsStatus().getPBit());
        assertFalse(impl.getForwardingInfo().getForwardingFeatureList().get(0).getSsStatus().getRBit());
        assertFalse(impl.getForwardingInfo().getForwardingFeatureList().get(0).getSsStatus().getABit());

        assertNull(impl.getCallBarringInfo());
        assertNull(impl.getSsData());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SSInfoImpl);
        impl = (SSInfoImpl)result.getResult();

        assertEquals(impl.getCallBarringInfo().getCallBarringFeatureList().size(), 1);
        assertFalse(impl.getCallBarringInfo().getCallBarringFeatureList().get(0).getSsStatus().getQBit());
        assertTrue(impl.getCallBarringInfo().getCallBarringFeatureList().get(0).getSsStatus().getPBit());
        assertFalse(impl.getCallBarringInfo().getCallBarringFeatureList().get(0).getSsStatus().getRBit());
        assertFalse(impl.getCallBarringInfo().getCallBarringFeatureList().get(0).getSsStatus().getABit());

        assertNull(impl.getForwardingInfo());
        assertNull(impl.getSsData());


        rawData = getEncodedData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SSInfoImpl);
        impl = (SSInfoImpl)result.getResult();

        assertFalse(impl.getSsData().getSsStatus().getQBit());
        assertTrue(impl.getSsData().getSsStatus().getPBit());
        assertFalse(impl.getSsData().getSsStatus().getRBit());
        assertFalse(impl.getSsData().getSsStatus().getABit());

        assertNull(impl.getCallBarringInfo());
        assertNull(impl.getForwardingInfo());
    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SSInfoImpl.class);
                
        List<ForwardingFeature> forwardingFeatureList = new ArrayList<ForwardingFeature>();
        SSStatusImpl ssStatus = new SSStatusImpl(false, true, false, false);
        ForwardingFeatureImpl ff = new ForwardingFeatureImpl(null, ssStatus, null, null, null, null, null);
        forwardingFeatureList.add(ff);
        ForwardingInfoImpl forwardingInfo = new ForwardingInfoImpl(null, forwardingFeatureList);
        SSInfoImpl impl = new SSInfoImpl(forwardingInfo);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));


        List<CallBarringFeature> callBarringFeatureList = new ArrayList<CallBarringFeature>();
        CallBarringFeatureImpl cbf = new CallBarringFeatureImpl(null, ssStatus);
        callBarringFeatureList.add(cbf);
        CallBarringInfoImpl callBarringInfo = new CallBarringInfoImpl(null, callBarringFeatureList);
        impl = new SSInfoImpl(callBarringInfo);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));


        SSDataImpl ssData = new SSDataImpl(null, ssStatus, null, null, null, null);
        impl = new SSInfoImpl(ssData);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}