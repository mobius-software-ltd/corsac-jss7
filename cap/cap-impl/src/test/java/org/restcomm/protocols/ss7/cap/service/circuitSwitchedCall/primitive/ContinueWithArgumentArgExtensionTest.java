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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ContinueWithArgumentArgExtensionImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LegOrCallSegmentImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.primitives.ReceivingLegIDImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
*
* @author sergey vetyutnev
*
*/
public class ContinueWithArgumentArgExtensionTest {

    public byte[] getData1() {
        return new byte[] { 48, 11, (byte) 128, 0, (byte) 129, 0, (byte) 130, 0, (byte) 163, 3, (byte) 128, 1, 12 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 9, (byte) 129, 0, (byte) 163, 5, (byte) 161, 3, (byte) 129, 1, 4 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ContinueWithArgumentArgExtensionImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ContinueWithArgumentArgExtensionImpl);
        
        ContinueWithArgumentArgExtensionImpl elem = (ContinueWithArgumentArgExtensionImpl)result.getResult();        
        assertTrue(elem.getSuppressDCsi());
        assertTrue(elem.getSuppressNCsi());
        assertTrue(elem.getSuppressOutgoingCallBarring());
        assertEquals((int) elem.getLegOrCallSegment().getCallSegmentID(), 12);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ContinueWithArgumentArgExtensionImpl);
        
        elem = (ContinueWithArgumentArgExtensionImpl)result.getResult();
        assertFalse(elem.getSuppressDCsi());
        assertTrue(elem.getSuppressNCsi());
        assertFalse(elem.getSuppressOutgoingCallBarring());
        assertEquals(elem.getLegOrCallSegment().getLegID().getReceivingLegID().getReceivingSideID(), LegType.leg4);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ContinueWithArgumentArgExtensionImpl.class);
    	
        LegOrCallSegmentImpl legOrCallSegment = new LegOrCallSegmentImpl(12);
        ContinueWithArgumentArgExtensionImpl elem = new ContinueWithArgumentArgExtensionImpl(true, true, true, legOrCallSegment);
//        boolean suppressDCSI, boolean suppressNCSI,
//        boolean suppressOutgoingCallBarring, LegOrCallSegment legOrCallSegment
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        LegIDImpl legID = new LegIDImpl(new ReceivingLegIDImpl(LegType.leg4) , null);
        legOrCallSegment = new LegOrCallSegmentImpl(legID);
        elem = new ContinueWithArgumentArgExtensionImpl(false, true, false, legOrCallSegment);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerializaion() throws Exception {
        LegOrCallSegmentImpl legOrCallSegment = new LegOrCallSegmentImpl(12);
        ContinueWithArgumentArgExtensionImpl original = new ContinueWithArgumentArgExtensionImpl(true, false, true, legOrCallSegment);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "continueWithArgumentArgExtension", ContinueWithArgumentArgExtensionImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ContinueWithArgumentArgExtensionImpl copy = reader.read("continueWithArgumentArgExtension", ContinueWithArgumentArgExtensionImpl.class);

        assertEquals(original.getSuppressDCsi(), copy.getSuppressDCsi());
        assertEquals(original.getSuppressNCsi(), copy.getSuppressNCsi());
        assertEquals(original.getSuppressOutgoingCallBarring(), copy.getSuppressOutgoingCallBarring());
        assertEquals((int) original.getLegOrCallSegment().getCallSegmentID(), (int) copy.getLegOrCallSegment().getCallSegmentID());


        original = new ContinueWithArgumentArgExtensionImpl(false, true, true, null);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "continueWithArgumentArgExtension", ContinueWithArgumentArgExtensionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("continueWithArgumentArgExtension", ContinueWithArgumentArgExtensionImpl.class);

        assertEquals(original.getSuppressDCsi(), copy.getSuppressDCsi());
        assertEquals(original.getSuppressNCsi(), copy.getSuppressNCsi());
        assertEquals(original.getSuppressOutgoingCallBarring(), copy.getSuppressOutgoingCallBarring());
        assertNull(original.getLegOrCallSegment());
        assertNull(copy.getLegOrCallSegment());
    }*/
}
