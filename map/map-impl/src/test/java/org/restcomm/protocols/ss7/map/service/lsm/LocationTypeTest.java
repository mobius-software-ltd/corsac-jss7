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

import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationTypeImpl;
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
public class LocationTypeTest {

	byte[] data = new byte[] { 0x30, 0x03, (byte) 0x80, 0x01, 0x00 };

	byte[] data2 = new byte[] { 0x30, 0x07, (byte) 0x80, 0x01, 0x00, (byte) 0x81, 0x02, 0x04, (byte) -16 };
	
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
    	parser.replaceClass(LocationTypeImpl.class);
    	
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationTypeImpl);
        LocationTypeImpl locType = (LocationTypeImpl)result.getResult();
        
        assertNotNull(locType.getLocationEstimateType());
        assertEquals(locType.getLocationEstimateType(), LocationEstimateType.currentLocation);
        assertNull(locType.getDeferredLocationEventType());

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationTypeImpl.class);
    	
        LocationTypeImpl locType = new LocationTypeImpl(LocationEstimateType.currentLocation, null);
        ByteBuf buffer=parser.encode(locType);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode1() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationTypeImpl.class);
    	
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data2));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationTypeImpl);
        LocationTypeImpl locType = (LocationTypeImpl)result.getResult();

        assertNotNull(locType.getLocationEstimateType());
        assertEquals(locType.getLocationEstimateType(), LocationEstimateType.currentLocation);
        assertNotNull(locType.getDeferredLocationEventType());

        assertNotNull(locType.getDeferredLocationEventType());
        assertTrue(locType.getDeferredLocationEventType().getEnteringIntoArea());
        assertTrue(locType.getDeferredLocationEventType().getMsAvailable());
        assertTrue(locType.getDeferredLocationEventType().getLeavingFromArea());
        assertTrue(locType.getDeferredLocationEventType().getBeingInsideArea());

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode1() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationTypeImpl.class);
    	
        DeferredLocationEventTypeImpl deferredLocationEventType = new DeferredLocationEventTypeImpl(true, true, true, true);

        LocationTypeImpl locType = new LocationTypeImpl(LocationEstimateType.currentLocation, deferredLocationEventType);
        ByteBuf buffer=parser.encode(locType);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data2, encodedData));
    }
}