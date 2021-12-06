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

package org.restcomm.protocols.ss7.map.service.mobility.imei;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author normandes
 *
 */
public class CheckImeiResponseTest {

    // Real Trace
    private byte[] getEncodedDataV2() {
        return new byte[] { 0x0a, 0x01, 0x00 };
    }

    private byte[] getEncodedDataV3() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 13, 10, 1, 0, 48, 8, -128, 2, 7, -128, -127, 2, 0, 0 };
    }

    private byte[] getEncodedDataV3Full() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 54, 10, 1, 0, 48, 8, -128, 2, 7, -128, -127, 2, 0, 0, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
                11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "imei" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CheckImeiResponseImplV1.class);
    	parser.replaceClass(CheckImeiResponseImplV3.class);
    	
        // Testing version 3
        byte[] data = getEncodedDataV3();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CheckImeiResponse);
        CheckImeiResponse checkImeiImpl = (CheckImeiResponse)result.getResult();
        
        assertEquals(checkImeiImpl.getEquipmentStatus(), EquipmentStatus.whiteListed);
        assertTrue(checkImeiImpl.getBmuef().getUESBI_IuA().isBitSet(0));
        assertFalse(checkImeiImpl.getBmuef().getUESBI_IuB().isBitSet(0));

        // Testing version 3 Full
        data = getEncodedDataV3Full();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CheckImeiResponse);
        checkImeiImpl = (CheckImeiResponse)result.getResult();

        assertEquals(checkImeiImpl.getEquipmentStatus(), EquipmentStatus.whiteListed);
        assertTrue(checkImeiImpl.getBmuef().getUESBI_IuA().isBitSet(0));
        assertFalse(checkImeiImpl.getBmuef().getUESBI_IuB().isBitSet(0));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(checkImeiImpl.getExtensionContainer()));

        // Testing version 1 and 2
        data = getEncodedDataV2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CheckImeiResponse);
        checkImeiImpl = (CheckImeiResponse)result.getResult();

        assertEquals(checkImeiImpl.getEquipmentStatus(), EquipmentStatus.whiteListed);
    }

    @Test(groups = { "functional.encode", "imei" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CheckImeiResponseImplV1.class);
    	parser.replaceClass(CheckImeiResponseImplV3.class);
    	
        // Testing version 3
        UESBIIuAImpl impUESBIIuA = new UESBIIuAImpl();
        impUESBIIuA.setBit(0);
        
        UESBIIuBImpl impUESBIIuB = new UESBIIuBImpl();

        UESBIIuImpl bmuef = new UESBIIuImpl(impUESBIIuA, impUESBIIuB);
        CheckImeiResponse checkImei = new CheckImeiResponseImplV3(3, EquipmentStatus.whiteListed, bmuef, null);
        byte[] data=getEncodedDataV3();
        ByteBuf buffer=parser.encode(checkImei);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // Testing version 3 Full
        impUESBIIuA = new UESBIIuAImpl();
        impUESBIIuA.setBit(0);
        
        impUESBIIuB = new UESBIIuBImpl();

        bmuef = new UESBIIuImpl(impUESBIIuA, impUESBIIuB);

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        checkImei = new CheckImeiResponseImplV3(3, EquipmentStatus.whiteListed, bmuef, extensionContainer);
        data=getEncodedDataV3Full();
        buffer=parser.encode(checkImei);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // Testing version 1 and 2
        checkImei = new CheckImeiResponseImplV1(2, EquipmentStatus.whiteListed);
        data=getEncodedDataV2();
        buffer=parser.encode(checkImei);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}