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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LAC;
import org.restcomm.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationAreaTest {

    public byte[] getData1() {
        return new byte[] { 48, 7, (byte) 128, 5, 66, (byte) 249, 16, 54, (byte) 186 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 4, (byte) 129, 2, 54, (byte) 186 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationAreaImpl.class);
    	
        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationAreaImpl);
        LocationAreaImpl prim = (LocationAreaImpl)result.getResult();
        
        LAIFixedLength lai = prim.getLAIFixedLength();
        assertEquals(lai.getMCC(), 249);
        assertEquals(lai.getMNC(), 1);
        assertEquals(lai.getLac(), 14010);
        assertNull(prim.getLAC());

        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationAreaImpl);
        prim = (LocationAreaImpl)result.getResult();
        
        assertNull(prim.getLAIFixedLength());
        LAC lac = prim.getLAC();
        assertEquals(lac.getLac(), 14010);
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationAreaImpl.class);
    	
        LAIFixedLengthImpl lai = new LAIFixedLengthImpl(249, 1, 14010);
        LocationAreaImpl prim = new LocationAreaImpl(lai);
        byte[] data=this.getData1();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        LACImpl lac = new LACImpl(14010);
        prim = new LocationAreaImpl(lac);
        data=this.getData2();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}