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
package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriodImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.ValidityPeriodFormat;
import org.restcomm.protocols.ss7.map.api.smstpdu.ValidityPeriodImpl;
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
public class TPValidityPeriodTest {

    public byte[] getData() {
        return new byte[] { 4, 1, 4 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 7, 49, 48, 16, 17, 2, 69, 0 };
    };
	
	public byte[] getTPValidityPeriod() {
		return new byte[] { 4 };
	};
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TPValidityPeriodImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TPValidityPeriodImpl);
        
        TPValidityPeriodImpl prim = (TPValidityPeriodImpl)result.getResult();        
        assertTrue(Arrays.equals(prim.getData(), getTPValidityPeriod()));
        ValidityPeriodImpl vp = prim.getValidityPeriod();
        assertEquals(vp.getValidityPeriodFormat(), ValidityPeriodFormat.fieldPresentRelativeFormat);
        assertEquals((int)vp.getRelativeFormatValue(), 4);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TPValidityPeriodImpl);
        
        prim = (TPValidityPeriodImpl)result.getResult(); 
        vp = prim.getValidityPeriod();
        assertEquals(vp.getValidityPeriodFormat(), ValidityPeriodFormat.fieldPresentAbsoluteFormat);
        AbsoluteTimeStampImpl afv = vp.getAbsoluteFormatValue();
        assertEquals(afv.getYear(), 13);
        assertEquals(afv.getMonth(), 3);
        assertEquals(afv.getDay(), 1);
        assertEquals(afv.getHour(), 11);
        assertEquals(afv.getMinute(), 20);
        assertEquals(afv.getSecond(), 54);
	}

	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TPValidityPeriodImpl.class);
    	
    	TPValidityPeriodImpl prim = new TPValidityPeriodImpl(4);
    	byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        AbsoluteTimeStampImpl absoluteFormatValue = new AbsoluteTimeStampImpl(13, 3, 1, 11, 20, 54, 0);
        prim = new TPValidityPeriodImpl(absoluteFormatValue);
        rawData = this.getData2();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
	}
}