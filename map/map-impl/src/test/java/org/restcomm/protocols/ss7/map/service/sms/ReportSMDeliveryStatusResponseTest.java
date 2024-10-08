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

package org.restcomm.protocols.ss7.map.service.sms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
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
public class ReportSMDeliveryStatusResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 49, 4, 6, -111, -120, 120, 119, 102, -10, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13,
                14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getEncodedDataV2() {
        return new byte[] { 4, 6, -111, -120, 120, 119, 102, -10 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ReportSMDeliveryStatusResponseImplV3.class);
    	parser.replaceClass(ReportSMDeliveryStatusResponseImplV1.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReportSMDeliveryStatusResponse);
        ReportSMDeliveryStatusResponse ind = (ReportSMDeliveryStatusResponse)result.getResult();  
        
        assertEquals(ind.getStoredMSISDN().getAddressNature(), AddressNature.international_number);
        assertEquals(ind.getStoredMSISDN().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(ind.getStoredMSISDN().getAddress(), "888777666");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));

        rawData = getEncodedDataV2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReportSMDeliveryStatusResponse);
        ind = (ReportSMDeliveryStatusResponse)result.getResult();  
        
        assertEquals(ind.getStoredMSISDN().getAddressNature(), AddressNature.international_number);
        assertEquals(ind.getStoredMSISDN().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(ind.getStoredMSISDN().getAddress(), "888777666");
        assertNull(ind.getExtensionContainer());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ReportSMDeliveryStatusResponseImplV3.class);
    	parser.replaceClass(ReportSMDeliveryStatusResponseImplV1.class);
    	
        ISDNAddressStringImpl storedMSISDN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "888777666");
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ReportSMDeliveryStatusResponse ind = new ReportSMDeliveryStatusResponseImplV3(storedMSISDN, extensionContainer);

        ByteBuf buffer=parser.encode(ind);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        ind = new ReportSMDeliveryStatusResponseImplV1(storedMSISDN);
        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedDataV2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
