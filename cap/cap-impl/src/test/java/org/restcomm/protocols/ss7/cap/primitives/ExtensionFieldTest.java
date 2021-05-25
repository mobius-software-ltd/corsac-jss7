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

package org.restcomm.protocols.ss7.cap.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.primitives.CriticalityType;
import org.restcomm.protocols.ss7.cap.api.primitives.ExtensionFieldImpl;
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
public class ExtensionFieldTest {

    public byte[] getData1() {
        return new byte[] { 48, 5, 2, 1, 2, (byte) 129, 0 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 7, 6, 2, 40, 22, (byte) 129, 1, (byte) 255 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 11, 2, 2, 8, (byte) 174, 10, 1, 1, (byte) 129, 2, (byte) 253, (byte) 213 };
    }

    public List<Long> getDataOid() {
        return Arrays.asList(new Long[] { 1L, 0L, 22L });
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ExtensionFieldImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtensionFieldImpl);
        
        ExtensionFieldImpl elem = (ExtensionFieldImpl)result.getResult();
        assertEquals((int) elem.getLocalCode(), 2);
        assertEquals(elem.getCriticalityType(), CriticalityType.typeIgnore);
        assertEquals(elem.getData().length,0);
       
        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtensionFieldImpl);
        
        elem = (ExtensionFieldImpl)result.getResult();
        assertEquals(elem.getGlobalCode(), this.getDataOid());
        assertEquals(elem.getCriticalityType(), CriticalityType.typeIgnore);
        assertEquals(elem.getData().length,1);
        assertEquals(elem.getData()[0],-1);
        
        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtensionFieldImpl);
        
        elem = (ExtensionFieldImpl)result.getResult();
        assertEquals((int) elem.getLocalCode(), 2222);
        assertEquals(elem.getCriticalityType(), CriticalityType.typeAbort);
        assertEquals(elem.getData().length,2);
        assertEquals(elem.getData()[0],-3);        
        assertEquals(elem.getData()[1],-43);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ExtensionFieldImpl.class);
    	
        ExtensionFieldImpl elem = new ExtensionFieldImpl(2, CriticalityType.typeIgnore, new byte[] {});
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new ExtensionFieldImpl(this.getDataOid(), null, new byte[] { -1 });
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new ExtensionFieldImpl(2222, CriticalityType.typeAbort, new byte[] { -3, -43 });
        rawData = this.getData3();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*private byte[] getDataSer() {
        return new byte[] { 1, (byte) 255, 3 };
    }

    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        ExtensionFieldImpl original = new ExtensionFieldImpl(234, CriticalityType.typeIgnore, getDataSer());

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "extensionField", ExtensionFieldImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ExtensionFieldImpl copy = reader.read("extensionField", ExtensionFieldImpl.class);

        assertEquals((int) copy.getLocalCode(), (int) original.getLocalCode());
        assertTrue(Arrays.equals(copy.getGlobalCode(), original.getGlobalCode()));
        assertEquals(copy.getCriticalityType(), original.getCriticalityType());
        assertEquals(copy.getData(), original.getData());

        original = new ExtensionFieldImpl(getDataOid(), null, getDataSer());

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "extensionField", ExtensionFieldImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("extensionField", ExtensionFieldImpl.class);

        assertNull(copy.getLocalCode());
        assertNull(original.getLocalCode());
        assertTrue(Arrays.equals(copy.getGlobalCode(), original.getGlobalCode()));
        assertEquals(copy.getCriticalityType(), original.getCriticalityType());
        assertEquals(copy.getData(), original.getData());

    }*/
}
