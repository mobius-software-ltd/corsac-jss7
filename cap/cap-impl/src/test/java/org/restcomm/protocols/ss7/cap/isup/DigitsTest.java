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
package org.restcomm.protocols.ss7.cap.isup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Test;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class DigitsTest {

    public byte[] getData1() {
        return new byte[] { 4, 5, 65, 5, 6, 7, 8 };
    }

    public byte[] getData2() {
        return new byte[] { 4, 7, 3, (byte) 132, 33, 7, 1, 9, 0 };
    }

    public byte[] getGenericDigitsInt() {
        return new byte[] { 5, 6, 7, 8 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DigitsIsupImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DigitsIsupImpl);
        
        DigitsIsupImpl elem = (DigitsIsupImpl)result.getResult();                
        elem.setIsGenericDigits();
        GenericDigits gd = elem.getGenericDigits();
        assertEquals(gd.getEncodingScheme(), 2);
        assertEquals(gd.getTypeOfDigits(), 1);
        
        ByteBuf value=gd.getEncodedDigits();
        byte[] data=new byte[value.readableBytes()];
        value.readBytes(data);
        assertTrue(Arrays.equals(data, getGenericDigitsInt()));

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DigitsIsupImpl);
        
        elem = (DigitsIsupImpl)result.getResult();                
        elem.setIsGenericNumber();
        GenericNumber gn = elem.getGenericNumber();
        assertEquals(gn.getNatureOfAddressIndicator(), 4);
        assertTrue(gn.getAddress().equals("7010900"));
        assertEquals(gn.getNumberQualifierIndicator(), 3);
        assertEquals(gn.getNumberingPlanIndicator(), 2);
        assertEquals(gn.getAddressRepresentationRestrictedIndicator(), 0);
        assertEquals(gn.getScreeningIndicator(), 1);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DigitsIsupImpl.class);
    	
        GenericDigitsImpl genericDigits = new GenericDigitsImpl(2, 1, Unpooled.wrappedBuffer(getGenericDigitsInt()));
        DigitsIsupImpl elem = new DigitsIsupImpl(genericDigits);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        // int encodingScheme, int typeOfDigits, int[] digits

        GenericNumber rn = new GenericNumberImpl(4, "7010900", 3, 2, 0, false, 1);
        elem = new DigitsIsupImpl(rn);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        // int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int
        // addressRepresentationREstrictedIndicator,
        // boolean numberIncomplete, int screeningIndicator
    }
}
