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
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfoImpl;
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
 * Trace is from Brazil Operator
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class SendRoutingInfoForLCSResponseTest {
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

    public byte[] getEncodedData() {
        // The trace is from Brazilian operator
        return new byte[] { 0x30, 0x14, (byte) 0xa0, 0x09, (byte) 0x81, 0x07, (byte) 0x91, 0x55, 0x16, 0x28, (byte) 0x81, 0x00,
                0x70, (byte) 0xa1, 0x07, 0x04, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x00 };
    }

    public byte[] getEncodedDataFull() {
        return new byte[] { 48, 95, -96, 9, -127, 7, -111, 85, 22, 40, -127, 0, 112, -95, 7, 4, 5, -111, 85, 22, 9, 0, -94, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -125, 5, 11, 12, 13, 14, 15, -124, 5, 21, 22, 23, 24, 25, -123, 5, 31, 32, 33, 34, 35, -122, 5, 41, 42, 43, 44, 45 };
    }

    public byte[] getEncodedGSNAddress1() {
        return new byte[] { 11, 12, 13, 14, 15 };
    }

    public byte[] getEncodedGSNAddress2() {
        return new byte[] { 21, 22, 23, 24, 25 };
    }

    public byte[] getEncodedGSNAddress3() {
        return new byte[] { 31, 32, 33, 34, 35 };
    }

    public byte[] getEncodedGSNAddress4() {
        return new byte[] { 41, 42, 43, 44, 45 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInfoForLCSResponseImpl.class);
    	
        byte[] data = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForLCSResponseImpl);
        SendRoutingInfoForLCSResponseImpl impl = (SendRoutingInfoForLCSResponseImpl)result.getResult();

        SubscriberIdentityImpl subsIdent = impl.getTargetMS();
        assertNotNull(subsIdent);

        IMSIImpl imsi = subsIdent.getIMSI();
        ISDNAddressStringImpl msisdn = subsIdent.getMSISDN();

        assertNotNull(msisdn);
        assertNull(imsi);

        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(msisdn.getAddress().equals("556182180007"));

        LCSLocationInfoImpl lcsLocInfo = impl.getLCSLocationInfo();
        assertNotNull(lcsLocInfo);

        ISDNAddressStringImpl networkNodeNumber = lcsLocInfo.getNetworkNodeNumber();
        assertNotNull(networkNodeNumber);
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(networkNodeNumber.getAddress().equals("55619000"));

        assertNull(impl.getExtensionContainer());
        assertNull(impl.getVgmlcAddress());
        assertNull(impl.getHGmlcAddress());
        assertNull(impl.getPprAddress());
        assertNull(impl.getAdditionalVGmlcAddress());

        data = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForLCSResponseImpl);
        impl = (SendRoutingInfoForLCSResponseImpl)result.getResult();

        subsIdent = impl.getTargetMS();
        assertNotNull(subsIdent);

        imsi = subsIdent.getIMSI();
        msisdn = subsIdent.getMSISDN();

        assertNotNull(msisdn);
        assertNull(imsi);

        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(msisdn.getAddress().equals("556182180007"));

        lcsLocInfo = impl.getLCSLocationInfo();
        assertNotNull(lcsLocInfo);

        networkNodeNumber = lcsLocInfo.getNetworkNodeNumber();
        assertNotNull(networkNodeNumber);
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(networkNodeNumber.getAddress().equals("55619000"));

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(impl.getExtensionContainer()));
        assertTrue(Arrays.equals(impl.getVgmlcAddress().getData(), getEncodedGSNAddress1()));
        assertTrue(Arrays.equals(impl.getHGmlcAddress().getData(), getEncodedGSNAddress2()));
        assertTrue(Arrays.equals(impl.getPprAddress().getData(), getEncodedGSNAddress3()));
        assertTrue(Arrays.equals(impl.getAdditionalVGmlcAddress().getData(), getEncodedGSNAddress4()));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInfoForLCSResponseImpl.class);
    	
        ISDNAddressStringImpl msisdn = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "556182180007");
        SubscriberIdentityImpl subsIdent = new SubscriberIdentityImpl(msisdn);

        ISDNAddressStringImpl networkNodeNumber = this.MAPParameterFactory.createISDNAddressString(
                AddressNature.international_number, NumberingPlan.ISDN, "55619000");

        LCSLocationInfoImpl lcsLocInfo = new LCSLocationInfoImpl(networkNodeNumber, null, null, false, null, null, null, null, null);

        SendRoutingInfoForLCSResponseImpl impl = new SendRoutingInfoForLCSResponseImpl(subsIdent, lcsLocInfo, null, null, null,
                null, null);

        byte[] data = getEncodedData();
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        GSNAddressImpl vgmlcAddress = new GSNAddressImpl(getEncodedGSNAddress1());
        GSNAddressImpl hGmlcAddress = new GSNAddressImpl(getEncodedGSNAddress2());
        GSNAddressImpl pprAddress = new GSNAddressImpl(getEncodedGSNAddress3());
        GSNAddressImpl additionalVGmlcAddress = new GSNAddressImpl(getEncodedGSNAddress4());

        impl = new SendRoutingInfoForLCSResponseImpl(subsIdent, lcsLocInfo,
                MAPExtensionContainerTest.GetTestExtensionContainer(), vgmlcAddress, hGmlcAddress, pprAddress,
                additionalVGmlcAddress);
        
        data = getEncodedDataFull();
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}
