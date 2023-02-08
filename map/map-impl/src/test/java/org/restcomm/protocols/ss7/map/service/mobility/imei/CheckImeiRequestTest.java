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

package org.restcomm.protocols.ss7.map.service.mobility.imei;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author normandes
 * @author yulianoifa
 *
 */
public class CheckImeiRequestTest {

    // Real Trace
    private byte[] getEncodedDataV2() {
        return new byte[] { 0x04, 0x08, 0x53, 0x08, 0x19, 0x10, (byte) 0x86, 0x35, 0x55, (byte) 0xf0 };
    }

    private byte[] getEncodedDataV3() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 14, 4, 8, 83, 8, 25, 16, -122, 53, 85, -16, 3, 2, 6, -128 };
    }

    private byte[] getEncodedDataV3Full() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 55, 4, 8, 83, 8, 25, 16, -122, 53, 85, -16, 3, 2, 6, -128, 48, 39, -96, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31,
                32, 33 };
    }

    // Huawei trace with IMSI
    private byte[] getEncodedDataV2_Huawei() {
        return new byte[] { 0x04, 0x08, 0x53, 0x46, 0x76, 0x40, (byte) 0x94, (byte) 0x98, 0x19, (byte) 0xf0, 0x00, 0x08, 0x27,
                0x34, 0x04, 0x03, 0x30, 0x58, 0x67, (byte) 0xf3 };
    }

    private byte[] getEncodedDataImeiLengthLessThan15() {
        return new byte[] { 4, 1, -15 };
    }

    @Test(groups = { "functional.decode", "imei" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CheckImeiRequestImplV1.class);
    	parser.replaceClass(CheckImeiRequestImplV3.class);
    	
        // Testing version 3
    	byte[] data = getEncodedDataV3();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CheckImeiRequest);
        CheckImeiRequest checkImeiImpl = (CheckImeiRequest)result.getResult();

        assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("358091016853550"));
        assertTrue(checkImeiImpl.getRequestedEquipmentInfo().getEquipmentStatus());
        assertFalse(checkImeiImpl.getRequestedEquipmentInfo().getBmuef());
        assertNull(checkImeiImpl.getIMSI());

        // Testing version 3 Full
        data = getEncodedDataV3Full();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CheckImeiRequest);
        checkImeiImpl = (CheckImeiRequest)result.getResult();

        assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("358091016853550"));
        assertTrue(checkImeiImpl.getRequestedEquipmentInfo().getEquipmentStatus());
        assertFalse(checkImeiImpl.getRequestedEquipmentInfo().getBmuef());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(checkImeiImpl.getExtensionContainer()));
        assertNull(checkImeiImpl.getIMSI());

        // Testing version 1 and 2
        data = getEncodedDataV2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CheckImeiRequest);
        checkImeiImpl = (CheckImeiRequest)result.getResult();

        assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("358091016853550"));
        assertNull(checkImeiImpl.getIMSI());

        // Testing version 1 and 2 with Huawei trace
        data = getEncodedDataV2_Huawei();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CheckImeiRequest);
        checkImeiImpl = (CheckImeiRequest)result.getResult();

        assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("356467044989910"));
        assertTrue(checkImeiImpl.getIMSI().getData().equals("724340300385763"));

        // Testing IMEI length != 15
        data = getEncodedDataImeiLengthLessThan15();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CheckImeiRequest);
        checkImeiImpl = (CheckImeiRequest)result.getResult();

        assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("1"));
        assertNull(checkImeiImpl.getIMSI());
    }

    @Test(groups = { "functional.encode", "imei" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CheckImeiRequestImplV1.class);
    	parser.replaceClass(CheckImeiRequestImplV3.class);
    	
        // Testing version 3
        IMEIImpl imei = new IMEIImpl("358091016853550");
        RequestedEquipmentInfoImpl requestedEquipmentInfo = new RequestedEquipmentInfoImpl(true, false);

        CheckImeiRequest checkImei = new CheckImeiRequestImplV3(imei, requestedEquipmentInfo, null);
        byte[] data=getEncodedDataV3();
        ByteBuf buffer=parser.encode(checkImei);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // Testing version 3 Full
        imei = new IMEIImpl("358091016853550");
        requestedEquipmentInfo = new RequestedEquipmentInfoImpl(true, false);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        checkImei = new CheckImeiRequestImplV3(imei, requestedEquipmentInfo, extensionContainer);
        data=getEncodedDataV3Full();
        buffer=parser.encode(checkImei);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // Testing version 1 and 2
        imei = new IMEIImpl("358091016853550");
        checkImei = new CheckImeiRequestImplV1(imei, null);
        data=getEncodedDataV2();
        buffer=parser.encode(checkImei);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));       

        // Testing version 1 and 2 with Huawei trace
        imei = new IMEIImpl("356467044989910");
        IMSIImpl imsi = new IMSIImpl("724340300385763");
        checkImei = new CheckImeiRequestImplV1(imei, imsi);
        data=getEncodedDataV2_Huawei();
        buffer=parser.encode(checkImei);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));


        // Testing IMEI length != 15
        imei = new IMEIImpl("1");
        checkImei = new CheckImeiRequestImplV1(imei, null);
        data=getEncodedDataImeiLengthLessThan15();
        buffer=parser.encode(checkImei);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}