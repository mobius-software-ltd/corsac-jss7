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
package org.restcomm.protocols.ss7.cap.service.gprs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSCauseImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class ReleaseGPRSRequestTest {

    public byte[] getData() {
        return new byte[] { 48, 6, -128, 1, 5, -127, 1, 2 };
    };

    public byte[] getDataLiveTrace() {
        return new byte[] { 0x30, 0x03, (byte) 0x80, 0x01, 0x00 };
    };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ReleaseGPRSRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReleaseGPRSRequestImpl);
        
        ReleaseGPRSRequestImpl prim = (ReleaseGPRSRequestImpl)result.getResult();        
        assertEquals(prim.getGPRSCause().getData(), 5);
        assertEquals(prim.getPDPID().getId(), 2);
    }

    @Test
    public void testDecodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ReleaseGPRSRequestImpl.class);
    	
    	byte[] rawData = this.getDataLiveTrace();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReleaseGPRSRequestImpl);
        
        ReleaseGPRSRequestImpl prim = (ReleaseGPRSRequestImpl)result.getResult();        
        assertEquals(prim.getGPRSCause().getData(), 0);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ReleaseGPRSRequestImpl.class);
    	
        GPRSCauseImpl gprsCause = new GPRSCauseImpl(5);
        PDPIDImpl pdpID = new PDPIDImpl(2);
        ReleaseGPRSRequestImpl prim = new ReleaseGPRSRequestImpl(gprsCause, pdpID);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test
    public void testEncodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ReleaseGPRSRequestImpl.class);
    	
        GPRSCauseImpl gprsCause = new GPRSCauseImpl(0);
        ReleaseGPRSRequestImpl prim = new ReleaseGPRSRequestImpl(gprsCause, null);
        byte[] rawData = this.getDataLiveTrace();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}