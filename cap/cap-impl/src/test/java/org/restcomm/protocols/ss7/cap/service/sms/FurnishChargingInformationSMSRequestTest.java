/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.restcomm.protocols.ss7.cap.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMSImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FreeFormatDataSMSImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class FurnishChargingInformationSMSRequestTest {

    public byte[] getData() {
        return new byte[] { 4, 15, -96, 13, -128, 8, 48, 6, -128, 1, 3, -118, 1, 1, -127, 1, 1 };
//        return new byte[] { 4, 17, 48, 15, -96, 13, -128, 8, 48, 6, -128, 1, 3, -118, 1, 1, -127, 1, 1 };
    };

    public byte[] getFreeFormatData() {
        return new byte[] { 48, 6, -128, 1, 3, -118, 1, 1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FurnishChargingInformationSMSRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof FurnishChargingInformationSMSRequestImpl);
        
        FurnishChargingInformationSMSRequestImpl prim = (FurnishChargingInformationSMSRequestImpl)result.getResult();        
        FCIBCCCAMELSequence1SMSImpl fcIBCCCAMELsequence1 = prim.getFCIBCCCAMELsequence1();
        assertNotNull(fcIBCCCAMELsequence1);
        assertTrue(Arrays.equals(fcIBCCCAMELsequence1.getFreeFormatData().getData(), this.getFreeFormatData()));
        assertEquals(fcIBCCCAMELsequence1.getAppendFreeFormatData(), AppendFreeFormatData.append);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FurnishChargingInformationSMSRequestImpl.class);
    	    	
        FreeFormatDataSMSImpl freeFormatData = new FreeFormatDataSMSImpl(getFreeFormatData());
        FCIBCCCAMELSequence1SMSImpl fcIBCCCAMELsequence1 = new FCIBCCCAMELSequence1SMSImpl(freeFormatData,
                AppendFreeFormatData.append);

        FurnishChargingInformationSMSRequestImpl prim = new FurnishChargingInformationSMSRequestImpl(fcIBCCCAMELsequence1);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
    
    /*@Test(groups = {"functional.xml.serialize", "primitives"})
    public void testXMLSerialize() throws Exception {

        FreeFormatDataSMS freeFormatData = new FreeFormatDataSMSImpl(getFreeFormatData());
        FCIBCCCAMELsequence1SMSImpl fcIBCCCAMELsequence1 = new FCIBCCCAMELsequence1SMSImpl(freeFormatData, AppendFreeFormatData.append);
        FurnishChargingInformationSMSRequestImpl original = new FurnishChargingInformationSMSRequestImpl(fcIBCCCAMELsequence1);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "furnishChargingInformationSMSRequest", FurnishChargingInformationSMSRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        FurnishChargingInformationSMSRequestImpl copy = reader.read("furnishChargingInformationSMSRequest", FurnishChargingInformationSMSRequestImpl.class);

        assertNotNull(copy.getFCIBCCCAMELsequence1());
        assertTrue(Arrays.equals(copy.getFCIBCCCAMELsequence1().getFreeFormatData().getData(), this.getFreeFormatData()));
        assertEquals(copy.getFCIBCCCAMELsequence1().getAppendFreeFormatData(), AppendFreeFormatData.append);
    }*/
}