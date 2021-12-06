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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

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
public class AOCBeforeAnswerTest {

    public byte[] getData1() {
        return new byte[] { 48, 21, (byte) 160, 6, (byte) 131, 1, 4, (byte) 132, 1, 5, (byte) 161, 11, (byte) 160, 6,
                (byte) 128, 1, 1, (byte) 134, 1, 7, (byte) 129, 1, 100 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AOCBeforeAnswerImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AOCBeforeAnswerImpl);
        
        AOCBeforeAnswerImpl elem = (AOCBeforeAnswerImpl)result.getResult();                
        assertNull(elem.getAOCInitial().getE1());
        assertNull(elem.getAOCInitial().getE2());
        assertNull(elem.getAOCInitial().getE3());
        assertEquals((int) elem.getAOCInitial().getE4(), 4);
        assertEquals((int) elem.getAOCInitial().getE5(), 5);
        assertNull(elem.getAOCInitial().getE6());
        assertNull(elem.getAOCInitial().getE7());
        assertEquals((int) elem.getAOCSubsequent().getCAI_GSM0224().getE1(), 1);
        assertNull(elem.getAOCSubsequent().getCAI_GSM0224().getE2());
        assertNull(elem.getAOCSubsequent().getCAI_GSM0224().getE3());
        assertNull(elem.getAOCSubsequent().getCAI_GSM0224().getE4());
        assertNull(elem.getAOCSubsequent().getCAI_GSM0224().getE5());
        assertNull(elem.getAOCSubsequent().getCAI_GSM0224().getE6());
        assertEquals((int) elem.getAOCSubsequent().getCAI_GSM0224().getE7(), 7);
        assertEquals((int) elem.getAOCSubsequent().getTariffSwitchInterval(), 100);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AOCBeforeAnswerImpl.class);
    	
        CAI_GSM0224Impl aocInitial = new CAI_GSM0224Impl(null, null, null, 4, 5, null, null);
        CAI_GSM0224Impl cai_GSM0224 = new CAI_GSM0224Impl(1, null, null, null, null, null, 7);
        AOCSubsequentImpl aocSubsequent = new AOCSubsequentImpl(cai_GSM0224, 100);
        AOCBeforeAnswerImpl elem = new AOCBeforeAnswerImpl(aocInitial, aocSubsequent);
        // CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
