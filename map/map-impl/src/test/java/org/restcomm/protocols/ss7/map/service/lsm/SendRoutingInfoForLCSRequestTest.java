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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 *
 */
public class SendRoutingInfoForLCSRequestTest {
    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getData() {
        // The trace is from Brazilian operator
        return new byte[] { 0x30, 0x13, (byte) 0x80, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70, (byte) 0xa1, 0x0a, (byte) 0x80,
                0x08, 0x27, (byte) 0x94, (byte) 0x99, 0x09, 0x00, 0x00, 0x00, (byte) 0xf7 };
    }

    public byte[] getDataFull() {
        return new byte[] { 48, 66, -128, 5, -111, 85, 22, 9, 112, -95, 10, -128, 8, 39, -108, -103, 9, 0, 0, 0, -9, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInfoForLCSRequestImpl.class);
    	
        byte[] data = getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForLCSRequestImpl);
        SendRoutingInfoForLCSRequestImpl rtgInfnoForLCSreqInd = (SendRoutingInfoForLCSRequestImpl)result.getResult();

        ISDNAddressStringImpl mlcNum = rtgInfnoForLCSreqInd.getMLCNumber();
        assertNotNull(mlcNum);
        assertEquals(mlcNum.getAddressNature(), AddressNature.international_number);
        assertEquals(mlcNum.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mlcNum.getAddress(), "55619007");

        SubscriberIdentityImpl subsIdent = rtgInfnoForLCSreqInd.getTargetMS();
        assertNotNull(subsIdent);

        IMSIImpl imsi = subsIdent.getIMSI();
        ISDNAddressStringImpl msisdn = subsIdent.getMSISDN();

        assertNotNull(imsi);
        assertNull(msisdn);

        assertEquals(imsi.getData(), "724999900000007");
        assertNull(rtgInfnoForLCSreqInd.getExtensionContainer());

        data = getDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForLCSRequestImpl);
        rtgInfnoForLCSreqInd = (SendRoutingInfoForLCSRequestImpl)result.getResult();

        mlcNum = rtgInfnoForLCSreqInd.getMLCNumber();
        assertNotNull(mlcNum);
        assertEquals(mlcNum.getAddressNature(), AddressNature.international_number);
        assertEquals(mlcNum.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mlcNum.getAddress(), "55619007");

        subsIdent = rtgInfnoForLCSreqInd.getTargetMS();
        assertNotNull(subsIdent);

        imsi = subsIdent.getIMSI();
        msisdn = subsIdent.getMSISDN();

        assertNotNull(imsi);
        assertNull(msisdn);

        assertEquals(imsi.getData(), "724999900000007");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(rtgInfnoForLCSreqInd.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInfoForLCSRequestImpl.class);
    	
        IMSIImpl imsi = this.MAPParameterFactory.createIMSI("724999900000007");
        SubscriberIdentityImpl subsIdent = new SubscriberIdentityImpl(imsi);

        ISDNAddressStringImpl mlcNumber = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "55619007");

        SendRoutingInfoForLCSRequestImpl rtgInfnoForLCSreqInd = new SendRoutingInfoForLCSRequestImpl(mlcNumber, subsIdent);
        byte[] data = getData();
        ByteBuf buffer=parser.encode(rtgInfnoForLCSreqInd);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        imsi = this.MAPParameterFactory.createIMSI("724999900000007");
        subsIdent = new SubscriberIdentityImpl(imsi);

        mlcNumber = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN,
                "55619007");

        rtgInfnoForLCSreqInd = new SendRoutingInfoForLCSRequestImpl(mlcNumber, subsIdent,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        data = getDataFull();
        buffer=parser.encode(rtgInfnoForLCSreqInd);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}
