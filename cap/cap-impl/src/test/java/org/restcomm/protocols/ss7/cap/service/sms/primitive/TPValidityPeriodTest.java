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
package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.smstpdu.AbsoluteTimeStamp;
import org.restcomm.protocols.ss7.commonapp.api.smstpdu.ValidityPeriod;
import org.restcomm.protocols.ss7.commonapp.api.smstpdu.ValidityPeriodFormat;
import org.restcomm.protocols.ss7.commonapp.smstpu.AbsoluteTimeStampImpl;
import org.testng.annotations.Test;

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
        ValidityPeriod vp = prim.getValidityPeriod();
        assertEquals(vp.getValidityPeriodFormat(), ValidityPeriodFormat.fieldPresentRelativeFormat);
        assertEquals((int)vp.getRelativeFormatValue(), 4);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TPValidityPeriodImpl);
        
        prim = (TPValidityPeriodImpl)result.getResult(); 
        vp = prim.getValidityPeriod();
        assertEquals(vp.getValidityPeriodFormat(), ValidityPeriodFormat.fieldPresentAbsoluteFormat);
        AbsoluteTimeStamp afv = vp.getAbsoluteFormatValue();
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