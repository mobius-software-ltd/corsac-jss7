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

import java.util.Arrays;

import org.restcomm.protocols.ss7.isup.impl.message.parameter.RedirectionInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionInformation;
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
public class RedirectionInformationInapTest {

    public byte[] getData() {
        return new byte[] { (byte) 158, 2, 3, 97 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(RedirectionInformationInapImpl.class);
        ByteBuf data = Unpooled.wrappedBuffer(this.getData());
        ASNDecodeResult output=parser.decode(data);
        assertTrue(output.getResult() instanceof RedirectionInformationInapImpl);
        RedirectionInformationInapImpl elem = (RedirectionInformationInapImpl)output.getResult();        
        RedirectionInformation ri = elem.getRedirectionInformation();
        assertEquals(ri.getOriginalRedirectionReason(), 0);
        assertEquals(ri.getRedirectingIndicator(), 3);
        assertEquals(ri.getRedirectionCounter(), 1);
        assertEquals(ri.getRedirectionReason(), 6);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(RedirectionInformationInapImpl.class);
        
    	RedirectionInformationInapImpl elem = new RedirectionInformationInapImpl(new RedirectionInformationImpl(3, 0, 1, 6));
        ByteBuf data=parser.encode(elem);
        byte[] encodedData=new byte[data.readableBytes()];
        data.readBytes(encodedData);        
        assertTrue(Arrays.equals(encodedData, this.getData()));
    }

    /*@Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        RedirectionInformationImpl prim = new RedirectionInformationImpl(RedirectionInformation._RI_CALL_D,
                RedirectionInformation._ORR_NO_REPLY, 4, RedirectionInformation._RI_CALL_REROUTED);
        RedirectionInformationInapImpl original = new RedirectionInformationInapImpl(prim);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "redirectionInformationInap", RedirectionInformationInapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        RedirectionInformationInapImpl copy = reader.read("redirectionInformationInap", RedirectionInformationInapImpl.class);

        assertEquals(copy.getRedirectionInformation().getRedirectingIndicator(), original.getRedirectionInformation()
                .getRedirectingIndicator());
        assertEquals(copy.getRedirectionInformation().getOriginalRedirectionReason(), original.getRedirectionInformation()
                .getOriginalRedirectionReason());
        assertEquals(copy.getRedirectionInformation().getRedirectionCounter(), original.getRedirectionInformation()
                .getRedirectionCounter());
        assertEquals(copy.getRedirectionInformation().getRedirectionReason(), original.getRedirectionInformation()
                .getRedirectionReason());

    }*/
}
