/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapesImpl;
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
public class SupportedGADShapesTest {

    private byte[] getEncodedData() {
        return new byte[] { (byte) 0x03, 0x02, 0x01, (byte) 0xfe };
    }

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

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SupportedGADShapesImpl.class);
    	
        byte[] data = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SupportedGADShapesImpl);
        SupportedGADShapesImpl supportedLCSCapabilityTest = (SupportedGADShapesImpl)result.getResult();
        
        assertEquals((boolean) supportedLCSCapabilityTest.getEllipsoidArc(), true);
        assertEquals((boolean) supportedLCSCapabilityTest.getEllipsoidPoint(), true);
        assertEquals((boolean) supportedLCSCapabilityTest.getEllipsoidPointWithAltitude(), true);
        assertEquals((boolean) supportedLCSCapabilityTest.getEllipsoidPointWithAltitudeAndUncertaintyElipsoid(), true);
        assertEquals((boolean) supportedLCSCapabilityTest.getEllipsoidPointWithUncertaintyCircle(), true);
        assertEquals((boolean) supportedLCSCapabilityTest.getEllipsoidPointWithUncertaintyEllipse(), true);
        assertEquals((boolean) supportedLCSCapabilityTest.getPolygon(), true);
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SupportedGADShapesImpl.class);
    	
        SupportedGADShapesImpl supportedLCSCapabilityTest = new SupportedGADShapesImpl(true, true, true, true, true, true, true);
        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(supportedLCSCapabilityTest);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}