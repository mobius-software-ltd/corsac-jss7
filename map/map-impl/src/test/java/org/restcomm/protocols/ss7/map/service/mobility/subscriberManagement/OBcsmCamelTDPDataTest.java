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

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
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
public class OBcsmCamelTDPDataTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 17, 10, 1, 2, 2, 1, 3, -128, 6, -111, 51, 35, 34, 17, -15, -127, 1, 1 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 58, 10, 1, 2, 2, 1, 3, -128, 6, -111, 51, 35, 34, 17, -15, -127, 1, 1, -94, 39, -96, 32, 48,
                10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26,
                -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberManagement" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(OBcsmCamelTDPDataImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OBcsmCamelTDPDataImpl);
        OBcsmCamelTDPDataImpl ind = (OBcsmCamelTDPDataImpl)result.getResult();
        
        assertEquals(ind.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertEquals(ind.getServiceKey(), 3);
        assertEquals(ind.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(ind.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(ind.getGsmSCFAddress().getAddress().equals("333222111"));
        assertEquals(ind.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(ind.getExtensionContainer());

        rawData = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OBcsmCamelTDPDataImpl);
        ind = (OBcsmCamelTDPDataImpl)result.getResult();

        assertEquals(ind.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertEquals(ind.getServiceKey(), 3);
        assertEquals(ind.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(ind.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(ind.getGsmSCFAddress().getAddress().equals("333222111"));
        assertEquals(ind.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(OBcsmCamelTDPDataImpl.class);
    	
        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "333222111");
        OBcsmCamelTDPDataImpl ind = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.collectedInfo, 3, gsmSCFAddress, DefaultCallHandling.releaseCall, null);

        ByteBuf buffer=parser.encode(ind);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        ind = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.collectedInfo, 3, gsmSCFAddress,
                DefaultCallHandling.releaseCall, MAPExtensionContainerTest.GetTestExtensionContainer());

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}