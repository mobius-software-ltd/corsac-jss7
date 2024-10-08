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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 * @author yulianoifa
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

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
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

    @Test
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