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
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSendImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSendWrapperImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageIDImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ToneImpl;
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
public class InformationToSendTest {

    public byte[] getData1() {
        return new byte[] { 48, 10, (byte) 160, 8, (byte) 160, 3, (byte) 128, 1, 91, (byte) 129, 1, 1 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 8, (byte) 161, 6, (byte) 128, 1, 5, (byte) 129, 1, 100 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InformationToSendWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InformationToSendWrapperImpl);
        
        InformationToSendWrapperImpl elem = (InformationToSendWrapperImpl)result.getResult();                
        assertEquals((int) elem.getInformationToSend().getInbandInfo().getMessageID().getElementaryMessageID(), 91);
        assertEquals((int) elem.getInformationToSend().getInbandInfo().getNumberOfRepetitions(), 1);
        assertNull(elem.getInformationToSend().getInbandInfo().getDuration());
        assertNull(elem.getInformationToSend().getInbandInfo().getInterval());
        assertNull(elem.getInformationToSend().getTone());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InformationToSendWrapperImpl);
        
        elem = (InformationToSendWrapperImpl)result.getResult(); 
        assertNull(elem.getInformationToSend().getInbandInfo());
        assertEquals(elem.getInformationToSend().getTone().getToneID(), 5);
        assertEquals((int) elem.getInformationToSend().getTone().getDuration(), 100);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InformationToSendWrapperImpl.class);
    	
        MessageIDImpl messageID = new MessageIDImpl(91);
        InbandInfoImpl inbandInfo = new InbandInfoImpl(messageID, 1, null, null);
        // MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval
        InformationToSendImpl elem = new InformationToSendImpl(inbandInfo);
        InformationToSendWrapperImpl wrapper = new InformationToSendWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        ToneImpl tone = new ToneImpl(5, 100);
        // int toneID, Integer duration
        elem = new InformationToSendImpl(tone);
        wrapper = new InformationToSendWrapperImpl(elem);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        MessageID messageID = new MessageIDImpl(10);
        InbandInfo inbandInfo = new InbandInfoImpl(messageID, null, null, null);
        InformationToSendImpl original = new InformationToSendImpl(inbandInfo);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "informationToSend", InformationToSendImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        InformationToSendImpl copy = reader.read("informationToSend", InformationToSendImpl.class);

        assertEquals((int) copy.getInbandInfo().getMessageID().getElementaryMessageID(), 10);
        assertNull(copy.getTone());

        Tone tone = new ToneImpl(15, null);
        original = new InformationToSendImpl(tone);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "informationToSend", InformationToSendImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("informationToSend", InformationToSendImpl.class);

        assertNull(copy.getInbandInfo());
        assertEquals(copy.getTone().getToneID(), 15);
    }*/
}