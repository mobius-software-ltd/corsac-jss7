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

package org.restcomm.protocols.ss7.inap.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.SendingLegIDImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LegIDTest {

    private byte[] getData1() {
        return new byte[] { (byte) 128, 1, 2 };
    }

    private byte[] getData2() {
        return new byte[] { (byte) 129, 1, 1 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ReceivingLegIDImpl.class);
    	parser.loadClass(SendingLegIDImpl.class);
    	
        ByteBuf data = Unpooled.wrappedBuffer(this.getData1());
        ASNDecodeResult result=parser.decode(data);        
        SendingLegIDImpl legId = (SendingLegIDImpl)result.getResult();
        assertNotNull(legId.getSendingSideID());
        assertEquals(legId.getSendingSideID(), LegType.leg2);

        data =  Unpooled.wrappedBuffer(this.getData2());
        result=parser.decode(data);        
        ReceivingLegIDImpl rLegId=(ReceivingLegIDImpl)result.getResult();
        assertNotNull(rLegId.getReceivingSideID());
        assertEquals(rLegId.getReceivingSideID(), LegType.leg1);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ReceivingLegIDImpl.class);
    	parser.loadClass(SendingLegIDImpl.class);
    	        
    	SendingLegIDImpl legId = new SendingLegIDImpl(LegType.leg2);
        ByteBuf data=parser.encode(legId);
        byte[] encodedData=new byte[data.readableBytes()];
        data.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, this.getData1()));

        ReceivingLegIDImpl rLegId = new ReceivingLegIDImpl(LegType.leg1);
        data=parser.encode(rLegId);
        encodedData=new byte[data.readableBytes()];
        data.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, this.getData2()));
    }

    /*@Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        LegIDImpl original = new LegIDImpl(true, LegType.leg1);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "legID", LegIDImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        LegIDImpl copy = reader.read("legID", LegIDImpl.class);

        assertEquals(copy.getSendingSideID(), original.getSendingSideID());
        assertEquals(copy.getReceivingSideID(), original.getReceivingSideID());

        original = new LegIDImpl(false, LegType.leg2);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "legID", LegIDImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("legID", LegIDImpl.class);

        assertEquals(copy.getSendingSideID(), original.getSendingSideID());
        assertEquals(copy.getReceivingSideID(), original.getReceivingSideID());
    }*/
}
