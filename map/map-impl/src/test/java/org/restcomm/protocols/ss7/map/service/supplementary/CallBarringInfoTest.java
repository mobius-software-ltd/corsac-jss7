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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CallBarringInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.junit.Test;

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
public class CallBarringInfoTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 7, 48, 5, 48, 3, (byte) 132, 1, 14 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 10, 4, 1, 33, 48, 5, 48, 3, (byte) 132, 1, 14 };
    }

    @Test
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

    @Test
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