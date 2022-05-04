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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
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
public class DeleteSubscriberDataResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 3, (byte) 128, 1, 2 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 44, (byte) 128, 1, 2, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
                42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberManagement" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(DeleteSubscriberDataResponseImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DeleteSubscriberDataResponseImpl);
        DeleteSubscriberDataResponseImpl asc = (DeleteSubscriberDataResponseImpl)result.getResult(); 
        
        assertEquals(asc.getRegionalSubscriptionResponse(), RegionalSubscriptionResponse.zoneCodesConflict);
        assertNull(asc.getExtensionContainer());

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DeleteSubscriberDataResponseImpl);
        asc = (DeleteSubscriberDataResponseImpl)result.getResult(); 

        assertEquals(asc.getRegionalSubscriptionResponse(), RegionalSubscriptionResponse.zoneCodesConflict);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(DeleteSubscriberDataResponseImpl.class);
    	
        DeleteSubscriberDataResponseImpl asc = new DeleteSubscriberDataResponseImpl(RegionalSubscriptionResponse.zoneCodesConflict, null);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData=this.getEncodedData();
        assertTrue(Arrays.equals(encodedData, rawData));


        asc = new DeleteSubscriberDataResponseImpl(RegionalSubscriptionResponse.zoneCodesConflict, MAPExtensionContainerTest.GetTestExtensionContainer());

        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData=this.getEncodedData2();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}