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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertFalse;
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
 * @author kiss.balazs@alerant.hu
 *
 */
public class SpecializedResourceReportRequestTest {

    public byte[] getData1() {
        return new byte[] { (byte) 159, 50, 0 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 159, 51, 0 };
    }

    public byte[] getData3() {
        return new byte[] { 5, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(SpecializedResourceReportRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SpecializedResourceReportRequestImpl);
        
        SpecializedResourceReportRequestImpl elem = (SpecializedResourceReportRequestImpl)result.getResult();         
        assertTrue(elem.getAllAnnouncementsComplete());
        assertFalse(elem.getFirstAnnouncementStarted());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SpecializedResourceReportRequestImpl);
        
        elem = (SpecializedResourceReportRequestImpl)result.getResult();  
        assertFalse(elem.getAllAnnouncementsComplete());
        assertTrue(elem.getFirstAnnouncementStarted());

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SpecializedResourceReportRequestImpl);
        
        elem = (SpecializedResourceReportRequestImpl)result.getResult();
        assertFalse(elem.getAllAnnouncementsComplete());
        assertFalse(elem.getFirstAnnouncementStarted());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(SpecializedResourceReportRequestImpl.class);
    	
        SpecializedResourceReportRequestImpl elem = new SpecializedResourceReportRequestImpl(true, false);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        // boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted, boolean isCAPVersion4orLater

        elem = new SpecializedResourceReportRequestImpl(false, true);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new SpecializedResourceReportRequestImpl(false, false);
        rawData = this.getData3();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));      
    }
}
