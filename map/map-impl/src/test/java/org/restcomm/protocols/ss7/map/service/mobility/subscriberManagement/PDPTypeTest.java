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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeValue;
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
public class PDPTypeTest {

    public byte[] getData1() {
        return new byte[] { 4, 2, (byte) 240, 1 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 2, (byte) 241, 33 };
    };

    public byte[] getData3() {
        return new byte[] { 4, 2, (byte) 241, 87 };
    };

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(PDPTypeImpl.class);
    	
        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PDPTypeImpl);
        PDPTypeImpl prim = (PDPTypeImpl)result.getResult();
        assertEquals(prim.getPDPTypeValue(), PDPTypeValue.PPP);


        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PDPTypeImpl);
        prim = (PDPTypeImpl)result.getResult();
        assertEquals(prim.getPDPTypeValue(), PDPTypeValue.IPv4);


        data = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PDPTypeImpl);
        prim = (PDPTypeImpl)result.getResult();
        assertEquals(prim.getPDPTypeValue(), PDPTypeValue.IPv6);
    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(PDPTypeImpl.class);
    	
        PDPTypeImpl prim = new PDPTypeImpl(PDPTypeValue.PPP);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = this.getData1();
        assertEquals(encodedData, rawData);

        prim = new PDPTypeImpl(PDPTypeValue.IPv4);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getData2();
        assertEquals(encodedData, rawData);

        prim = new PDPTypeImpl(PDPTypeValue.IPv6);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getData3();
        assertEquals(encodedData, rawData);
    }
}