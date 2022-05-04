/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariablePartDateImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariablePartImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariablePartPriceImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariablePartTimeImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariablePartWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.testng.annotations.Test;

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
public class VariablePartTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 128, 1, 17 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 6, (byte) 129, 4, 96, 18, 17, 16 };
    }

    public byte[] getGenericDigitsData() {
        return new byte[] { 18, 17, 16 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 4, (byte) 130, 2, 0, 52 };
    }

    public byte[] getData4() {
        return new byte[] { 48, 6, (byte) 131, 4, 2, 33, 48, 18 };
    }

    public byte[] getData5() {
        return new byte[] { 48, 6, (byte) 132, 4, 0, 1, 0, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(VariablePartWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VariablePartWrapperImpl);
        
        VariablePartWrapperImpl elem = (VariablePartWrapperImpl)result.getResult();    
        assertNotNull(elem.getVariablePart());
        assertEquals(elem.getVariablePart().size(), 1);
        assertEquals((int) elem.getVariablePart().get(0).getInteger(), 17);
        assertNull(elem.getVariablePart().get(0).getNumber());
        assertNull(elem.getVariablePart().get(0).getTime());
        assertNull(elem.getVariablePart().get(0).getDate());
        assertNull(elem.getVariablePart().get(0).getPrice());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VariablePartWrapperImpl);
        
        elem = (VariablePartWrapperImpl)result.getResult(); 
        assertNotNull(elem.getVariablePart());
        assertEquals(elem.getVariablePart().size(), 1);
        assertNull(elem.getVariablePart().get(0).getInteger());
        assertEquals(elem.getVariablePart().get(0).getNumber().getGenericDigits().getEncodingScheme(), 3);
        assertEquals(elem.getVariablePart().get(0).getNumber().getGenericDigits().getTypeOfDigits(), 0);
        ByteBuf value=elem.getVariablePart().get(0).getNumber().getGenericDigits().getEncodedDigits();
        assertNotNull(value);
        byte[] data=new byte[value.readableBytes()];
        value.readBytes(data);
        assertTrue(Arrays.equals(data, getGenericDigitsData()));
        assertNull(elem.getVariablePart().get(0).getTime());
        assertNull(elem.getVariablePart().get(0).getDate());
        assertNull(elem.getVariablePart().get(0).getPrice());

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VariablePartWrapperImpl);
        
        elem = (VariablePartWrapperImpl)result.getResult(); 
        assertNotNull(elem.getVariablePart());
        assertEquals(elem.getVariablePart().size(), 1);
        assertNull(elem.getVariablePart().get(0).getInteger());
        assertNull(elem.getVariablePart().get(0).getNumber());
        assertEquals(elem.getVariablePart().get(0).getTime().getHour(), 0);
        assertEquals(elem.getVariablePart().get(0).getTime().getMinute(), 43);
        assertNull(elem.getVariablePart().get(0).getDate());
        assertNull(elem.getVariablePart().get(0).getPrice());

        rawData = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VariablePartWrapperImpl);
        
        elem = (VariablePartWrapperImpl)result.getResult(); 
        assertNotNull(elem.getVariablePart());
        assertEquals(elem.getVariablePart().size(), 1);
        assertNull(elem.getVariablePart().get(0).getInteger());
        assertNull(elem.getVariablePart().get(0).getNumber());
        assertNull(elem.getVariablePart().get(0).getTime());
        assertEquals(elem.getVariablePart().get(0).getDate().getYear(), 2012);
        assertEquals(elem.getVariablePart().get(0).getDate().getMonth(), 3);
        assertEquals(elem.getVariablePart().get(0).getDate().getDay(), 21);
        assertNull(elem.getVariablePart().get(0).getPrice());

        rawData = this.getData5();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VariablePartWrapperImpl);
        
        elem = (VariablePartWrapperImpl)result.getResult(); 
        assertNotNull(elem.getVariablePart());
        assertEquals(elem.getVariablePart().size(), 1);
        assertNull(elem.getVariablePart().get(0).getInteger());
        assertNull(elem.getVariablePart().get(0).getNumber());
        assertNull(elem.getVariablePart().get(0).getTime());
        assertNull(elem.getVariablePart().get(0).getDate());
        assertEquals(elem.getVariablePart().get(0).getPrice().getPriceIntegerPart(), 1000);
        assertEquals(elem.getVariablePart().get(0).getPrice().getPriceHundredthPart(), 0);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(VariablePartWrapperImpl.class);
    	
        VariablePartImpl elem = new VariablePartImpl(17);
        VariablePartWrapperImpl wrapper = new VariablePartWrapperImpl(Arrays.asList(new VariablePartImpl[] { elem }));
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        GenericDigitsImpl genericDigits = new GenericDigitsImpl(3, 0, Unpooled.wrappedBuffer(getGenericDigitsData()));
        // int encodingScheme, int typeOfDigits, int[] digits
        DigitsIsupImpl digits = new DigitsIsupImpl(genericDigits);
        elem = new VariablePartImpl(digits);
        wrapper = new VariablePartWrapperImpl(Arrays.asList(new VariablePartImpl[] { elem }));
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        VariablePartTimeImpl time = new VariablePartTimeImpl(0, 43);
        elem = new VariablePartImpl(time);
        wrapper = new VariablePartWrapperImpl(Arrays.asList(new VariablePartImpl[] { elem }));
        rawData = this.getData3();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        VariablePartDateImpl date = new VariablePartDateImpl(2012, 3, 21);
        elem = new VariablePartImpl(date);
        wrapper = new VariablePartWrapperImpl(Arrays.asList(new VariablePartImpl[] { elem }));
        rawData = this.getData4();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        VariablePartPriceImpl price = new VariablePartPriceImpl(1000, 0);
        elem = new VariablePartImpl(price);
        wrapper = new VariablePartWrapperImpl(Arrays.asList(new VariablePartImpl[] { elem }));
        rawData = this.getData5();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}