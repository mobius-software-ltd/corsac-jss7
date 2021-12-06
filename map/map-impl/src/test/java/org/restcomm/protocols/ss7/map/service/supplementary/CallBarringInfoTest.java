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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
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
public class CallBarringInfoTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 7, 48, 5, 48, 3, (byte) 132, 1, 14 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 10, 4, 1, 33, 48, 5, 48, 3, (byte) 132, 1, 14 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallBarringInfoImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallBarringInfoImpl);
        CallBarringInfoImpl impl = (CallBarringInfoImpl)result.getResult();
        
        assertNull(impl.getSsCode());

        assertEquals(impl.getCallBarringFeatureList().size(), 1);
        assertFalse(impl.getCallBarringFeatureList().get(0).getSsStatus().getABit());
        assertTrue(impl.getCallBarringFeatureList().get(0).getSsStatus().getPBit());

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallBarringInfoImpl);
        impl = (CallBarringInfoImpl)result.getResult();

        assertEquals(impl.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);

        assertEquals(impl.getCallBarringFeatureList().size(), 1);
        assertFalse(impl.getCallBarringFeatureList().get(0).getSsStatus().getABit());
        assertTrue(impl.getCallBarringFeatureList().get(0).getSsStatus().getPBit());
    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallBarringInfoImpl.class);
    	
        List<CallBarringFeature> forwardingFeatureList = new ArrayList<CallBarringFeature>();
        SSStatus ssStatus = new SSStatusImpl(true, true, true, false);
        CallBarringFeature callBarringFeature = new CallBarringFeatureImpl(null, ssStatus);
        forwardingFeatureList.add(callBarringFeature);
        CallBarringInfo impl = new CallBarringInfoImpl(null, forwardingFeatureList);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        impl = new CallBarringInfoImpl(ssCode, forwardingFeatureList);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}