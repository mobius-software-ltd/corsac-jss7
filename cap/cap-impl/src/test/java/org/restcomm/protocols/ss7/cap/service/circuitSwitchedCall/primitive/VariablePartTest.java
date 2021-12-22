/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        Integer integer = 15;
        VariablePartImpl original = new VariablePartImpl(integer);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "variablePart", VariablePartImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        VariablePartImpl copy = reader.read("variablePart", VariablePartImpl.class);

        assertEquals(copy.getInteger(), original.getInteger());
        assertNull(copy.getNumber());
        assertNull(copy.getTime());
        assertNull(copy.getDate());
        assertNull(copy.getPrice());


        int encodingScheme = GenericDigits._ENCODING_SCHEME_BCD_ODD;
        int typeOfDigits = GenericDigits._TOD_BGCI;
        byte[] digits = new byte[] { 35, 0x21, 0x43, 0x65 };
        GenericDigits genericDigits = new GenericDigitsImpl(encodingScheme, typeOfDigits, digits);
        Digits number = new DigitsImpl(genericDigits);
        original = new VariablePartImpl(number);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "variablePart", VariablePartImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("variablePart", VariablePartImpl.class);

        assertNull(copy.getInteger());
        assertEquals(copy.getNumber().getGenericDigits().getEncodingScheme(), encodingScheme);
        assertEquals(copy.getNumber().getGenericDigits().getTypeOfDigits(), typeOfDigits);
        assertEquals(copy.getNumber().getGenericDigits().getEncodedDigits(), digits);
        assertNull(copy.getTime());
        assertNull(copy.getDate());
        assertNull(copy.getPrice());


        int hour = 2;
        int minute = 14;
        VariablePartTimeImpl time = new VariablePartTimeImpl(hour, minute);
        original = new VariablePartImpl(time);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "variablePart", VariablePartImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("variablePart", VariablePartImpl.class);

        assertNull(copy.getInteger());
        assertNull(copy.getNumber());
        assertEquals(copy.getTime().getHour(), hour);
        assertEquals(copy.getTime().getMinute(), minute);
        assertNull(copy.getDate());
        assertNull(copy.getPrice());


        int year = 2014;
        int month = 4;
        int day = 16;
        VariablePartDateImpl date = new VariablePartDateImpl(year, month, day);
        original = new VariablePartImpl(date);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "variablePart", VariablePartImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("variablePart", VariablePartImpl.class);

        assertNull(copy.getInteger());
        assertNull(copy.getNumber());
        assertNull(copy.getTime());
        assertEquals(copy.getDate().getYear(), year);
        assertEquals(copy.getDate().getMonth(), month);
        assertEquals(copy.getDate().getDay(), day);
        assertNull(copy.getPrice());


        int integerPart = 234;
        int hundredthPart = 21;
        VariablePartPriceImpl price = new VariablePartPriceImpl(integerPart, hundredthPart);
        original = new VariablePartImpl(price);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "variablePart", VariablePartImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("variablePart", VariablePartImpl.class);

        assertNull(copy.getInteger());
        assertNull(copy.getNumber());
        assertNull(copy.getTime());
        assertNull(copy.getDate());
        assertEquals(copy.getPrice().getPriceIntegerPart(), integerPart);
        assertEquals(copy.getPrice().getPriceHundredthPart(), hundredthPart);
    }*/
}