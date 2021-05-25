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

import org.restcomm.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FCIBCCCAMELSequence1Impl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FreeFormatDataImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.primitives.SendingLegIDImpl;
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
public class FCIBCCCAMELSequence1Test {

    public byte[] getData1() {
        return new byte[] { 48, 14, (byte) 128, 4, 4, 5, 6, 7, (byte) 161, 3, (byte) 128, 1, 2, (byte) 130, 1, 1 };
    }

    public byte[] getDataFFD() {
        return new byte[] { 4, 5, 6, 7 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FCIBCCCAMELSequence1Impl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof FCIBCCCAMELSequence1Impl);
        
        FCIBCCCAMELSequence1Impl elem = (FCIBCCCAMELSequence1Impl)result.getResult();        
        assertTrue(Arrays.equals(elem.getFreeFormatData().getData(), this.getDataFFD()));
        assertEquals(elem.getPartyToCharge().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getAppendFreeFormatData(), AppendFreeFormatData.append);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FCIBCCCAMELSequence1Impl.class);
    	
        SendingLegIDImpl partyToCharge = new SendingLegIDImpl(LegType.leg2);
        FreeFormatDataImpl ffd = new FreeFormatDataImpl(getDataFFD());
        FCIBCCCAMELSequence1Impl elem = new FCIBCCCAMELSequence1Impl(ffd, partyToCharge, AppendFreeFormatData.append);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // byte[] freeFormatData, SendingSideID partyToCharge, AppendFreeFormatData appendFreeFormatData
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        FreeFormatData ffd = new FreeFormatDataImpl(getDataFFD());
        SendingSideIDImpl partyToCharge = new SendingSideIDImpl(LegType.leg2);
        FCIBCCCAMELsequence1Impl original = new FCIBCCCAMELsequence1Impl(ffd, partyToCharge, AppendFreeFormatData.append);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "fciBCCCAMELsequence1", FCIBCCCAMELsequence1Impl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        FCIBCCCAMELsequence1Impl copy = reader.read("fciBCCCAMELsequence1", FCIBCCCAMELsequence1Impl.class);

        assertEquals(copy.getFreeFormatData().getData(), getDataFFD());
        assertEquals(copy.getPartyToCharge().getSendingSideID(), LegType.leg2);
        assertEquals(copy.getAppendFreeFormatData(), AppendFreeFormatData.append);

        original = new FCIBCCCAMELsequence1Impl(ffd, null, null);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "fciBCCCAMELsequence1", FCIBCCCAMELsequence1Impl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("fciBCCCAMELsequence1", FCIBCCCAMELsequence1Impl.class);

        assertEquals(copy.getFreeFormatData().getData(), getDataFFD());
        assertNull(copy.getPartyToCharge());
        assertNull(copy.getAppendFreeFormatData());
    }*/
}