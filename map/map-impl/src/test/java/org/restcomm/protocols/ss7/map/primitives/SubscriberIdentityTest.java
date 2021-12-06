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

package org.restcomm.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
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
 * Trace are from Brazil Operator
 *
 * @author amit bhayani
 *
 */
public class SubscriberIdentityTest {
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

    byte[] data = new byte[] { 48, 10, (byte) 0x80, 0x08, 0x27, (byte) 0x94, (byte) 0x99, 0x09, 0x00, 0x00, 0x00, (byte) 0xf7 };
    byte[] dataMsIsdn = new byte[] { 48, 9, -127, 7, -111, 34, 34, 34, 51, 51, -13 };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(SubscriberIdentityWrapperImpl.class);
    	              
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SubscriberIdentityWrapperImpl);
        SubscriberIdentityWrapperImpl subsIdent = (SubscriberIdentityWrapperImpl)result.getResult();
        
        assertNotNull(subsIdent.getSubscriberIdentity());
        IMSI imsi = subsIdent.getSubscriberIdentity().getIMSI();
        ISDNAddressString msisdn = subsIdent.getSubscriberIdentity().getMSISDN();
        assertNotNull(imsi);
        assertNull(msisdn);
        assertTrue(imsi.getData().equals("724999900000007"));

        result=parser.decode(Unpooled.wrappedBuffer(dataMsIsdn));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SubscriberIdentityWrapperImpl);
        subsIdent = (SubscriberIdentityWrapperImpl)result.getResult();
        
        assertNotNull(subsIdent.getSubscriberIdentity());
        imsi = subsIdent.getSubscriberIdentity().getIMSI();
        msisdn = subsIdent.getSubscriberIdentity().getMSISDN();
        assertNull(imsi);
        assertNotNull(msisdn);
        assertTrue(msisdn.getAddress().equals("22222233333"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(SubscriberIdentityWrapperImpl.class);
    	
        IMSI imsi = this.MAPParameterFactory.createIMSI("724999900000007");
        SubscriberIdentityWrapperImpl subsIdent = new SubscriberIdentityWrapperImpl(new SubscriberIdentityImpl(imsi));
        ByteBuf buffer=parser.encode(subsIdent);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22222233333");
        subsIdent = new SubscriberIdentityWrapperImpl(new SubscriberIdentityImpl(msisdn));
        buffer=parser.encode(subsIdent);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(dataMsIsdn, encodedData));
    }
}