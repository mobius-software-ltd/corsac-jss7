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

package org.restcomm.protocols.ss7.inap.isup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.inap.isup.HighLayerCompatibilityInapImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserTeleserviceInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class HighLayerCompatibilityInapTest {

    public byte[] getData() {
        return new byte[] { (byte) 151, 2, (byte) 145, (byte) 129 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(HighLayerCompatibilityInapImpl.class);
        ByteBuf data = Unpooled.wrappedBuffer(this.getData());
        ASNDecodeResult output=parser.decode(data);
        assertTrue(output.getResult() instanceof HighLayerCompatibilityInapImpl);
        HighLayerCompatibilityInapImpl elem = (HighLayerCompatibilityInapImpl)output.getResult();        
        UserTeleserviceInformation hlc = elem.getHighLayerCompatibility();
        assertEquals(hlc.getCodingStandard(), 0);
        assertEquals(hlc.getEHighLayerCharIdentification(), 0);
        assertEquals(hlc.getEVideoTelephonyCharIdentification(), 0);
        assertEquals(hlc.getInterpretation(), 4);
        assertEquals(hlc.getPresentationMethod(), 1);
        assertEquals(hlc.getHighLayerCharIdentification(), 1);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(HighLayerCompatibilityInapImpl.class);
        
    	HighLayerCompatibilityInapImpl elem = new HighLayerCompatibilityInapImpl(new UserTeleserviceInformationImpl(0, 4, 1, 1));
        ByteBuf data=parser.encode(elem);
        byte[] encodedData=new byte[data.readableBytes()];
        data.readBytes(encodedData);        
    }

    /*@Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        UserTeleserviceInformationImpl prim = new UserTeleserviceInformationImpl(
                UserTeleserviceInformation._CODING_STANDARD_NATIONAL, UserTeleserviceInformation._INTERPRETATION_FHGCI,
                UserTeleserviceInformation._PRESENTATION_METHOD_HLPP, UserTeleserviceInformation._HLCI_IVTI);
        HighLayerCompatibilityInapImpl original = new HighLayerCompatibilityInapImpl(prim);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "highLayerCompatibilityInap", HighLayerCompatibilityInapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        HighLayerCompatibilityInapImpl copy = reader.read("highLayerCompatibilityInap", HighLayerCompatibilityInapImpl.class);

        assertEquals(copy.getHighLayerCompatibility().getCodingStandard(), original.getHighLayerCompatibility()
                .getCodingStandard());
        assertEquals(copy.getHighLayerCompatibility().getInterpretation(), original.getHighLayerCompatibility()
                .getInterpretation());
        assertEquals(copy.getHighLayerCompatibility().getPresentationMethod(), original.getHighLayerCompatibility()
                .getPresentationMethod());
        assertEquals(copy.getHighLayerCompatibility().getHighLayerCharIdentification(), original.getHighLayerCompatibility()
                .getHighLayerCharIdentification());

    }*/
}
