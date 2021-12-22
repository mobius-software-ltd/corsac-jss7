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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BearerServiceCodeImpl;
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
public class CallBarringFeatureTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 6, (byte) 130, 1, 38, (byte) 132, 1, 13 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallBarringFeatureImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallBarringFeatureImpl);
        CallBarringFeatureImpl impl = (CallBarringFeatureImpl)result.getResult();
        
        assertEquals(impl.getBasicService().getBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
        SSStatus ssStatus = impl.getSsStatus();
        assertTrue(ssStatus.getQBit());
        assertTrue(ssStatus.getPBit());
        assertFalse(ssStatus.getRBit());
        assertTrue(ssStatus.getABit());
    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallBarringFeatureImpl.class);
    	
        BearerServiceCodeImpl bearerService = new BearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        BasicServiceCodeImpl basicServiceCode = new BasicServiceCodeImpl(bearerService);
        SSStatusImpl ssStatus = new SSStatusImpl(true, true, false, true);

        CallBarringFeatureImpl impl = new CallBarringFeatureImpl(basicServiceCode, ssStatus);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
