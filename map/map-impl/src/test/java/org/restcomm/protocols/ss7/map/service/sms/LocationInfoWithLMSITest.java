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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class LocationInfoWithLMSITest {

    private byte[] getEncodedData() {
        return new byte[] { -96, 15, -127, 7, -111, -105, 48, 115, 0, 34, -14, 4, 4, 0, 3, 98, 49 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { -96, 67, (byte) 129, 6, (byte) 168, 33, 67, 101, (byte) 135, 9, 4, 4, 4, 3, 2, 1, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 133, 0,
                (byte) 166, 8, (byte) 129, 6, (byte) 185, (byte) 137, 103, 69, 35, (byte) 241 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationInfoWithLMSIImpl.class);
    	        
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationInfoWithLMSIImpl);
        LocationInfoWithLMSIImpl liw = (LocationInfoWithLMSIImpl)result.getResult();
        
        ISDNAddressString nnm = liw.getNetworkNodeNumber();
        assertEquals(nnm.getAddressNature(), AddressNature.international_number);
        assertEquals(nnm.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(nnm.getAddress(), "79033700222");
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(new byte[] { 0, 3, 98, 49 }), liw.getLMSI().getValue()));
        assertFalse(liw.getGprsNodeIndicator());
        assertNull(liw.getAdditionalNumber());

        rawData = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationInfoWithLMSIImpl);
        liw = (LocationInfoWithLMSIImpl)result.getResult();

        nnm = liw.getNetworkNodeNumber();
        assertEquals(nnm.getAddressNature(), AddressNature.national_significant_number);
        assertEquals(nnm.getNumberingPlan(), NumberingPlan.national);
        assertEquals(nnm.getAddress(), "1234567890");
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(new byte[] { 4, 3, 2, 1 }), liw.getLMSI().getValue()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(liw.getExtensionContainer()));
        nnm = liw.getAdditionalNumber().getSGSNNumber();
        assertEquals(nnm.getAddressNature(), AddressNature.network_specific_number);
        assertEquals(nnm.getNumberingPlan(), NumberingPlan.private_plan);
        assertEquals(nnm.getAddress(), "987654321");
        assertTrue(liw.getGprsNodeIndicator());
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationInfoWithLMSIImpl.class);
    	                
        ISDNAddressStringImpl nnm = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79033700222");
        LMSIImpl lmsi = new LMSIImpl(Unpooled.wrappedBuffer(new byte[] { 0, 3, 98, 49 }));
        LocationInfoWithLMSIImpl liw = new LocationInfoWithLMSIImpl(nnm, lmsi, null, false, null);

        ByteBuf buffer=parser.encode(liw);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        nnm = new ISDNAddressStringImpl(AddressNature.national_significant_number, NumberingPlan.national, "1234567890");
        ISDNAddressStringImpl sgsnAn = new ISDNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.private_plan,
                "987654321");
        lmsi = new LMSIImpl(Unpooled.wrappedBuffer(new byte[] { 4, 3, 2, 1 }));
        AdditionalNumberImpl an = new AdditionalNumberImpl(null, sgsnAn);
        liw = new LocationInfoWithLMSIImpl(nnm, lmsi, MAPExtensionContainerTest.GetTestExtensionContainer(), true, an);
        buffer=parser.encode(liw);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}