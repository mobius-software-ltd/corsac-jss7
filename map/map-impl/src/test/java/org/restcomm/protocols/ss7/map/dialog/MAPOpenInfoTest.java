/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.dialog;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPOpenInfoTest {

	private byte[] getDataFull() {
        return new byte[] { -96, 61, -128, 9, -106, 2, 36, -128, 3, 0, -128, 0, -14, -127, 7, -111, 19, 38, -104, -122, 3, -16,
                48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21,
                22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getDataEri() {
        return new byte[] { (byte) 160, 36, (byte) 128, 9, (byte) 150, 2, 36, (byte) 128, 3, 0, (byte) 128, 0, (byte) 242,
                (byte) 129, 7, (byte) 145, 19, 38, (byte) 152, (byte) 134, 3, (byte) 240, (byte) 130, 7, (byte) 145, 17, 33,
                34, 17, 33, 34, (byte) 131, 5, (byte) 145, (byte) 128, 55, 33, (byte) 244 };

//        return new byte[] { -96, 35, -128, 6, 17, 33, 34, 17, 33, 34, -127, 7, -111, 19, 38, -104, -122, 3, -16, -126, 9, -106,
//                2, 36, -128, 3, 0, -128, 0, -14, -125, 5, -111, -128, 55, 33, -12 };
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPOpenInfoImpl.class);
    	
    	byte[] data = new byte[] { (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03,
                0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98, (byte) 0x86,
                0x03, (byte) 0xf0, 0x00, 0x00 };

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data),true);
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPOpenInfoImpl);
        MAPOpenInfoImpl mapOpenInfoImpl = (MAPOpenInfoImpl)result.getResult();
        
        AddressString destRef = mapOpenInfoImpl.getDestReference();
        AddressString origRef = mapOpenInfoImpl.getOrigReference();

        assertNotNull(destRef);

        assertEquals(destRef.getAddressNature(), AddressNature.international_number);
        assertEquals(destRef.getNumberingPlan(), NumberingPlan.land_mobile);
        assertTrue(destRef.getAddress().endsWith("204208300008002"));

        assertNotNull(origRef);

        assertEquals(origRef.getAddressNature(), AddressNature.international_number);
        assertEquals(origRef.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(origRef.getAddress().equals("31628968300"));
        assertFalse(mapOpenInfoImpl.getEriStyle());

        result=parser.decode(Unpooled.wrappedBuffer(this.getDataFull()),true);
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPOpenInfoImpl);
        mapOpenInfoImpl = (MAPOpenInfoImpl)result.getResult();
        
        destRef = mapOpenInfoImpl.getDestReference();
        origRef = mapOpenInfoImpl.getOrigReference();

        assertNotNull(destRef);

        assertEquals(destRef.getAddressNature(), AddressNature.international_number);
        assertEquals(destRef.getNumberingPlan(), NumberingPlan.land_mobile);
        assertTrue(destRef.getAddress().equals("204208300008002"));

        assertNotNull(origRef);

        assertEquals(origRef.getAddressNature(), AddressNature.international_number);
        assertEquals(origRef.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(origRef.getAddress().equals("31628968300"));

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mapOpenInfoImpl.getExtensionContainer()));
        assertFalse(mapOpenInfoImpl.getEriStyle());

        result=parser.decode(Unpooled.wrappedBuffer(this.getDataEri()),true);
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPOpenInfoImpl);
        mapOpenInfoImpl = (MAPOpenInfoImpl)result.getResult();
        
        destRef = mapOpenInfoImpl.getDestReference();
        origRef = mapOpenInfoImpl.getOrigReference();

        assertNotNull(destRef);

        assertEquals(destRef.getAddressNature(), AddressNature.international_number);
        assertEquals(destRef.getNumberingPlan(), NumberingPlan.land_mobile);
        assertTrue(destRef.getAddress().equals("204208300008002"));

        assertNotNull(origRef);

        assertEquals(origRef.getAddressNature(), AddressNature.international_number);
        assertEquals(origRef.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(origRef.getAddress().equals("31628968300"));

        assertNull(mapOpenInfoImpl.getExtensionContainer());
        assertTrue(mapOpenInfoImpl.getEriStyle());
        assertTrue(mapOpenInfoImpl.getEriMsisdn().getAddress().equals("111222111222"));

        AddressString eriVlrNo = mapOpenInfoImpl.getEriVlrNo();
        assertEquals(eriVlrNo.getAddressNature(), AddressNature.international_number);
        assertEquals(eriVlrNo.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(eriVlrNo.getAddress().equals("0873124"));

    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPOpenInfoImpl.class);
    	
        MAPParameterFactory servFact = new MAPParameterFactoryImpl();

        MAPOpenInfoImpl mapOpenInfoImpl = new MAPOpenInfoImpl();
        AddressString destReference = servFact.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");
        mapOpenInfoImpl.setDestReference(destReference);
        AddressString origReference = servFact.createAddressString(AddressNature.international_number, NumberingPlan.ISDN,
                "31628968300");
        mapOpenInfoImpl.setOrigReference(origReference);
        
        ByteBuf buffer=parser.encode(mapOpenInfoImpl);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(new byte[] { (byte) 0xa0, (byte) 0x14, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24,
                (byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26,
                (byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0 }, data));

        mapOpenInfoImpl = new MAPOpenInfoImpl();
        destReference = servFact.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
                "204208300008002");
        mapOpenInfoImpl.setDestReference(destReference);
        origReference = servFact.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
        mapOpenInfoImpl.setOrigReference(origReference);
        mapOpenInfoImpl.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        buffer=parser.encode(mapOpenInfoImpl);
        data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertTrue(Arrays.equals(this.getDataFull(), data));

        // Eri
        mapOpenInfoImpl = new MAPOpenInfoImpl();
        destReference = servFact.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
                "204208300008002");
        mapOpenInfoImpl.setDestReference(destReference);
        origReference = servFact.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
        mapOpenInfoImpl.setOrigReference(origReference);
        
        mapOpenInfoImpl.setEriMsisdn(servFact.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222111222"));
        mapOpenInfoImpl.setEriVlrNo(servFact.createAddressString(AddressNature.international_number, NumberingPlan.ISDN,
                "0873124"));

        buffer=parser.encode(mapOpenInfoImpl);
        data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(this.getDataEri(), data));
    }
}