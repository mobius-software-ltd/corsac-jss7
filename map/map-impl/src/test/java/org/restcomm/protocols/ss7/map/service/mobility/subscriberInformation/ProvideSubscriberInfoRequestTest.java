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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfoImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
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
public class ProvideSubscriberInfoRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 12, (byte) 128, 6, 17, 33, 34, 51, 67, 68, (byte) 162, 2, (byte) 128, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 68, -128, 6, 17, 33, 34, 51, 67, 68, -127, 4, 11, 22, 33, 44, -94, 2, -128, 0, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -124, 1, 4 };
    }

    private byte[] getLmsiData() {
        return new byte[] { 11, 22, 33, 44 };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProvideSubscriberInfoRequestImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideSubscriberInfoRequestImpl);
        ProvideSubscriberInfoRequestImpl asc = (ProvideSubscriberInfoRequestImpl)result.getResult();
        
        IMSIImpl imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));

        assertTrue(asc.getRequestedInfo().getLocationInformation());
        assertFalse(asc.getRequestedInfo().getSubscriberState());

        assertNull(asc.getLmsi());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getCallPriority());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideSubscriberInfoRequestImpl);
        asc = (ProvideSubscriberInfoRequestImpl)result.getResult();
        
        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));

        assertTrue(asc.getRequestedInfo().getLocationInformation());
        assertFalse(asc.getRequestedInfo().getSubscriberState());

        assertEquals(asc.getLmsi().getData(), getLmsiData());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));

        assertEquals(asc.getCallPriority(), EMLPPPriority.priorityLevel4);

    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProvideSubscriberInfoRequestImpl.class);
    	
        IMSIImpl imsi = new IMSIImpl("111222333444");
        RequestedInfoImpl requestedInfo = new RequestedInfoImpl(true, false, null, false, null, false, false, false);
        ProvideSubscriberInfoRequestImpl asc = new ProvideSubscriberInfoRequestImpl(imsi, null, requestedInfo, null, null);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        LMSIImpl lmsi = new LMSIImpl(getLmsiData());
        asc = new ProvideSubscriberInfoRequestImpl(imsi, lmsi, requestedInfo, MAPExtensionContainerTest.GetTestExtensionContainer(), EMLPPPriority.priorityLevel4);

        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}