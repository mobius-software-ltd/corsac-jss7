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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FCIBCCCAMELSequence1Impl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FreeFormatDataImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
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
public class FurnishChargingInformationTest {

    public byte[] getData1() {
        return new byte[] { 4, 16, (byte) 160, 14, (byte) 128, 4, 4, 5, 6, 7, (byte) 161, 3, (byte) 128, 1, 2, (byte) 130, 1, 1 };
    }

    public byte[] getDataFFD() {
        return new byte[] { 4, 5, 6, 7 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FurnishChargingInformationRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof FurnishChargingInformationRequestImpl);
        
        FurnishChargingInformationRequestImpl elem = (FurnishChargingInformationRequestImpl)result.getResult();        
        assertTrue(Arrays.equals(elem.getFCIBCCCAMELsequence1().getFreeFormatData().getData(), this.getDataFFD()));
        assertEquals(elem.getFCIBCCCAMELsequence1().getPartyToCharge(), LegType.leg2);
        assertEquals(elem.getFCIBCCCAMELsequence1().getAppendFreeFormatData(), AppendFreeFormatData.append);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FurnishChargingInformationRequestImpl.class);
    	
        FreeFormatDataImpl ffd = new FreeFormatDataImpl(getDataFFD());
        FCIBCCCAMELSequence1Impl fci = new FCIBCCCAMELSequence1Impl(ffd, LegType.leg2, AppendFreeFormatData.append);
        FurnishChargingInformationRequestImpl elem = new FurnishChargingInformationRequestImpl(fci);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        SendingSideIDImpl partyToCharge = new SendingSideIDImpl(LegType.leg2);
        FreeFormatData ffd = new FreeFormatDataImpl(getDataFFD());
        FCIBCCCAMELsequence1Impl fci = new FCIBCCCAMELsequence1Impl(ffd, partyToCharge, AppendFreeFormatData.append);
        FurnishChargingInformationRequestImpl original = new FurnishChargingInformationRequestImpl(fci);
        original.setInvokeId(26);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "furnishChargingInformationRequest", FurnishChargingInformationRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        FurnishChargingInformationRequestImpl copy = reader.read("furnishChargingInformationRequest", FurnishChargingInformationRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertTrue(Arrays.equals(copy.getFCIBCCCAMELsequence1().getFreeFormatData().getData(), this.getDataFFD()));
        assertEquals(copy.getFCIBCCCAMELsequence1().getPartyToCharge().getSendingSideID(), LegType.leg2);
        assertEquals(copy.getFCIBCCCAMELsequence1().getAppendFreeFormatData(), AppendFreeFormatData.append);
    }*/
}
