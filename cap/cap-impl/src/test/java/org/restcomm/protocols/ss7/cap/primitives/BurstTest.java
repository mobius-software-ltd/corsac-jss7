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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.primitives.BurstImpl;
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
public class BurstTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 129, 1, 10 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 15, (byte) 128, 1, 1, (byte) 129, 1, 10, (byte) 130, 1, 2, (byte) 131, 1, 11, (byte) 132, 1, 12 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BurstImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BurstImpl);
        
        BurstImpl elem = (BurstImpl)result.getResult();
        assertNull(elem.getNumberOfBursts());
        assertEquals((int) elem.getBurstInterval(), 10);
        assertNull(elem.getNumberOfTonesInBurst());
        assertNull(elem.getToneDuration());
        assertNull(elem.getToneInterval());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BurstImpl);
        
        elem = (BurstImpl)result.getResult();
        assertEquals((int) elem.getNumberOfBursts(), 1);
        assertEquals((int) elem.getBurstInterval(), 10);
        assertEquals((int) elem.getNumberOfTonesInBurst(), 2);
        assertEquals((int) elem.getToneDuration(), 11);
        assertEquals((int) elem.getToneInterval(), 12);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BurstImpl.class);
    	
        BurstImpl elem = new BurstImpl(null, 10, null, null, null);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new BurstImpl(1, 10, 2, 11, 12);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        BurstImpl original = new BurstImpl(null, 10, null, null, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "burst", BurstImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        BurstImpl copy = reader.read("burst", BurstImpl.class);

        assertNull(copy.getNumberOfBursts());
        assertEquals((int) copy.getBurstInterval(), (int) original.getBurstInterval());
        assertNull(copy.getNumberOfTonesInBurst());
        assertNull(copy.getToneDuration());
        assertNull(copy.getToneInterval());


        original = new BurstImpl(1, 10, 2, 11, 12);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "burst", BurstImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("burst", BurstImpl.class);

        assertEquals((int) copy.getNumberOfBursts(), (int) original.getNumberOfBursts());
        assertEquals((int) copy.getBurstInterval(), (int) original.getBurstInterval());
        assertEquals((int) copy.getNumberOfTonesInBurst(), (int) original.getNumberOfTonesInBurst());
        assertEquals((int) copy.getToneDuration(), (int) original.getToneDuration());
        assertEquals((int) copy.getToneInterval(), (int) original.getToneInterval());
    }*/
}
