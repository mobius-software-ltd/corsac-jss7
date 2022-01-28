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
package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.TMSIImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SendIdentificationRequestTest {

    public byte[] getData1() {
        return new byte[] { 4, 4, 1, 2, 3, 4 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 82, 4, 4, 1, 2, 3, 4, 2, 1, 2, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 4, 4, -111, 34,
                50, -12, -128, 5, 16, 97, 66, 1, 77, -127, 1, 4, -126, 0, -125, 4, -111, 34, 50, -11, -124, 4, 1, 2, 3, 4 };
    };

    public byte[] getDataTmsi() {
        return new byte[] { 1, 2, 3, 4 };
    };

    public byte[] getDataLmsi() {
        return new byte[] { 1, 2, 3, 4 };
    };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendIdentificationRequestImplV1.class);
    	parser.replaceClass(SendIdentificationRequestImplV3.class);
    	
    	// version 2
    	byte[] data = this.getData1();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendIdentificationRequest);
        SendIdentificationRequest prim = (SendIdentificationRequest)result.getResult(); 

        assertTrue(ByteBufUtil.equals(prim.getTmsi().getValue(), Unpooled.wrappedBuffer(getDataTmsi())));

        // version 3
        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendIdentificationRequest);
        prim = (SendIdentificationRequest)result.getResult(); 

        assertTrue(ByteBufUtil.equals(prim.getTmsi().getValue(), Unpooled.wrappedBuffer(getDataTmsi())));
        assertTrue(prim.getNumberOfRequestedVectors().equals(2));
        assertTrue(prim.getSegmentationProhibited());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        ISDNAddressString mscNumber = prim.getMscNumber();
        assertTrue(mscNumber.getAddress().equals("22234"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertEquals(prim.getPreviousLAI().getMCC(), 11);
        assertEquals(prim.getPreviousLAI().getMNC(), 246);
        assertEquals(prim.getPreviousLAI().getLac(), 333);

        assertTrue(prim.getHopCounter().equals(4));
        assertTrue(prim.getMtRoamingForwardingSupported());

        ISDNAddressString newVLRNumber = prim.getNewVLRNumber();
        assertTrue(newVLRNumber.getAddress().equals("22235"));
        assertEquals(newVLRNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(newVLRNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(ByteBufUtil.equals(prim.getNewLmsi().getValue(),Unpooled.wrappedBuffer(getDataLmsi())));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendIdentificationRequestImplV1.class);
    	parser.replaceClass(SendIdentificationRequestImplV3.class);
    	
    	// version 2
        TMSIImpl tmsi = new TMSIImpl(Unpooled.wrappedBuffer(getDataTmsi()));
        SendIdentificationRequest prim = new SendIdentificationRequestImplV1(tmsi, 2);
        byte[] data=getData1();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // version 3
        tmsi = new TMSIImpl(Unpooled.wrappedBuffer(getDataTmsi()));
        Integer numberOfRequestedVectors = 2;
        boolean segmentationProhibited = true;
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");

        LAIFixedLengthImpl previousLAI = new LAIFixedLengthImpl(11, 246, 333);
        Integer hopCounter = 4;
        boolean mtRoamingForwardingSupported = true;
        ISDNAddressStringImpl newVLRNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22235");
        LMSIImpl lmsi = new LMSIImpl(Unpooled.wrappedBuffer(getDataLmsi()));
        prim = new SendIdentificationRequestImplV3(tmsi, numberOfRequestedVectors, segmentationProhibited, extensionContainer, mscNumber, previousLAI, hopCounter, mtRoamingForwardingSupported, newVLRNumber, lmsi, 3);
        data=getData2();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}