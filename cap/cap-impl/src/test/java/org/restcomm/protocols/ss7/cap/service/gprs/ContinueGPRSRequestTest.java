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
package org.restcomm.protocols.ss7.cap.service.gprs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ContinueGPRSRequestTest {

    public byte[] getData() {
        return new byte[] { 48, 3, -128, 1, 2 };
    };

    public byte[] getDataLiveTrace() {
        return new byte[] { 0x30, 0x00 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ContinueGPRSRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ContinueGPRSRequestImpl);
        
        ContinueGPRSRequestImpl prim = (ContinueGPRSRequestImpl)result.getResult();        
        assertEquals(prim.getPDPID().getId(), 2);
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ContinueGPRSRequestImpl.class);
    	
    	byte[] rawData = this.getDataLiveTrace();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ContinueGPRSRequestImpl);
        
        ContinueGPRSRequestImpl prim = (ContinueGPRSRequestImpl)result.getResult();        
        assertNull(prim.getPDPID());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ContinueGPRSRequestImpl.class);
    	
        PDPIDImpl pdpID = new PDPIDImpl(2);
        ContinueGPRSRequestImpl prim = new ContinueGPRSRequestImpl(pdpID);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ContinueGPRSRequestImpl.class);
    	
    	ContinueGPRSRequestImpl prim = new ContinueGPRSRequestImpl(null);
    	byte[] rawData = this.getDataLiveTrace();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
