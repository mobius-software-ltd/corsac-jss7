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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

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
public class GPRSSubscriptionDataWithdrawTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 2, 5, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 8, 48, 6, 2, 1, 1, 2, 1, 12 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(GPRSSubscriptionDataWithdrawImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSSubscriptionDataWithdrawImpl);
        GPRSSubscriptionDataWithdrawImpl asc = (GPRSSubscriptionDataWithdrawImpl)result.getResult();
        
        assertTrue(asc.getAllGPRSData());
        assertNull(asc.getContextIdList());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSSubscriptionDataWithdrawImpl);
        asc = (GPRSSubscriptionDataWithdrawImpl)result.getResult();

        assertFalse(asc.getAllGPRSData());
        assertEquals(asc.getContextIdList().size(), 2);
        assertEquals((int) asc.getContextIdList().get(0), 1);
        assertEquals((int) asc.getContextIdList().get(1), 12);
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(GPRSSubscriptionDataWithdrawImpl.class);
    	
        GPRSSubscriptionDataWithdrawImpl asc = new GPRSSubscriptionDataWithdrawImpl(true);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        ArrayList<Integer> arr = new ArrayList<Integer>();
        arr.add(1);
        arr.add(12);
        asc = new GPRSSubscriptionDataWithdrawImpl(arr);

        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
