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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InbandInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageIDImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageIDTextImpl;
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
public class InbandInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 5, (byte) 160, 3, (byte) 128, 1, 11 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 19, (byte) 160, 8, (byte) 161, 6, (byte) 128, 4, 73, 110, 102, 111, (byte) 129, 1, 3,
                (byte) 130, 1, 2, (byte) 131, 1, 1 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InbandInfoImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InbandInfoImpl);
        
        InbandInfoImpl elem = (InbandInfoImpl)result.getResult();                
        assertEquals((int) elem.getMessageID().getElementaryMessageID(), 11);
        assertNull(elem.getNumberOfRepetitions());
        assertNull(elem.getDuration());
        assertNull(elem.getInterval());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InbandInfoImpl);
        
        elem = (InbandInfoImpl)result.getResult(); 
        assertTrue(elem.getMessageID().getText().getMessageContent().equals("Info"));
        assertEquals((int) elem.getNumberOfRepetitions(), 3);
        assertEquals((int) elem.getDuration(), 2);
        assertEquals((int) elem.getInterval(), 1);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InbandInfoImpl.class);
    	
        MessageIDImpl messageID = new MessageIDImpl(11);
        InbandInfoImpl elem = new InbandInfoImpl(messageID, null, null, null);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        MessageIDTextImpl text = new MessageIDTextImpl("Info", null);
        messageID = new MessageIDImpl(text);
        elem = new InbandInfoImpl(messageID, 3, 2, 1);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        MessageID messageID = new MessageIDImpl(10);
        InbandInfoImpl original = new InbandInfoImpl(messageID, null, null, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "inbandInfo", InbandInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        InbandInfoImpl copy = reader.read("inbandInfo", InbandInfoImpl.class);

        assertEquals((int) copy.getMessageID().getElementaryMessageID(), 10);
        assertNull(copy.getNumberOfRepetitions());
        assertNull(copy.getDuration());
        assertNull(copy.getInterval());


        original = new InbandInfoImpl(messageID, 1, 2, 3);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "inbandInfo", InbandInfoImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("inbandInfo", InbandInfoImpl.class);

        assertEquals((int) copy.getMessageID().getElementaryMessageID(), 10);
        assertEquals((int) copy.getNumberOfRepetitions(), 1);
        assertEquals((int) copy.getDuration(), 2);
        assertEquals((int) copy.getInterval(), 3);
    }*/
}