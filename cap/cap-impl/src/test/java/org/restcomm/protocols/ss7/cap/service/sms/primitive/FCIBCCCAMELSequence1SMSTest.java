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
package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
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
public class FCIBCCCAMELSequence1SMSTest {
	
	public byte[] getData() {
		return new byte[] { -96, 13, -128, 8, 48, 6, -128, 1, 3, -118, 1, 1, -127, 1, 1 };
	};
	
	public byte[] getFreeFormatData() {
		return new byte[] { 48, 6, -128, 1, 3, -118, 1, 1 };
	};
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FCIBCCCAMELSequence1SMSImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof FCIBCCCAMELSequence1SMSImpl);
        
        FCIBCCCAMELSequence1SMSImpl prim = (FCIBCCCAMELSequence1SMSImpl)result.getResult();                
		assertTrue(Arrays.equals(prim.getFreeFormatData().getData(), this.getFreeFormatData()));
		assertEquals(prim.getAppendFreeFormatData(), AppendFreeFormatData.append);		
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FCIBCCCAMELSequence1SMSImpl.class);
    	
    	FreeFormatDataSMSImpl freeFormatData = new FreeFormatDataSMSImpl(getFreeFormatData());
		FCIBCCCAMELSequence1SMSImpl prim = new FCIBCCCAMELSequence1SMSImpl(freeFormatData, AppendFreeFormatData.append);
		byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
	}

	/*@Test(groups = {"functional.xml.serialize", "primitives"})
	public void testXMLSerialize() throws Exception {

		FreeFormatDataSMS freeFormatData = new FreeFormatDataSMSImpl(getFreeFormatData());
		FCIBCCCAMELsequence1SMSImpl original = new FCIBCCCAMELsequence1SMSImpl(freeFormatData, AppendFreeFormatData.append);

		// Writes the area to a file.
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
		writer.setIndentation("\t");
		writer.write(original, "fciBCCCAMELsequence1SMS", FCIBCCCAMELsequence1SMSImpl.class);
		writer.close();

		byte[] rawData = baos.toByteArray();
		String serializedEvent = new String(rawData);

		System.out.println(serializedEvent);

		ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
		XMLObjectReader reader = XMLObjectReader.newInstance(bais);
		FCIBCCCAMELsequence1SMSImpl copy = reader.read("fciBCCCAMELsequence1SMS", FCIBCCCAMELsequence1SMSImpl.class);

		assertEquals(copy.getFreeFormatData().getData(), this.getFreeFormatData());
		assertEquals(copy.getAppendFreeFormatData(), AppendFreeFormatData.append);

		original = new FCIBCCCAMELsequence1SMSImpl(freeFormatData, null);

		// Writes the area to a file.
		baos = new ByteArrayOutputStream();
		writer = XMLObjectWriter.newInstance(baos);
		writer.setIndentation("\t");
		writer.write(original, "fciBCCCAMELsequence1SMS", FCIBCCCAMELsequence1SMSImpl.class);
		writer.close();

		rawData = baos.toByteArray();
		serializedEvent = new String(rawData);

		System.out.println(serializedEvent);

		bais = new ByteArrayInputStream(rawData);
		reader = XMLObjectReader.newInstance(bais);
		copy = reader.read("fciBCCCAMELsequence1SMS", FCIBCCCAMELsequence1SMSImpl.class);

		assertEquals(copy.getFreeFormatData().getData(), this.getFreeFormatData());
		assertNull(copy.getAppendFreeFormatData());
	}*/
}
