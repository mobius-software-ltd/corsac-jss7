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

import org.restcomm.protocols.ss7.cap.api.isup.CauseCapImpl;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
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
public class DisconnectLegRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 29, (byte) 160, 3, (byte) 129, 1, 6, (byte) 129, 2, (byte) 128, (byte) 134, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48,
                9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DisconnectLegRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DisconnectLegRequestImpl);
        
        DisconnectLegRequestImpl elem = (DisconnectLegRequestImpl)result.getResult();        
        assertEquals(elem.getLegToBeReleased().getReceivingSideID(), LegType.leg6);
        CauseIndicators ci = elem.getReleaseCause().getCauseIndicators();
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getCauseValue(), 6);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DisconnectLegRequestImpl.class);
    	
        LegIDImpl legToBeReleased = new LegIDImpl(LegType.leg6,null);
        CauseIndicatorsImpl causeIndicators = new CauseIndicatorsImpl(0, 0, 0, 6, null);
        CauseCapImpl releaseCause = new CauseCapImpl(causeIndicators);
        DisconnectLegRequestImpl elem = new DisconnectLegRequestImpl(legToBeReleased, releaseCause, CAPExtensionsTest.createTestCAPExtensions());

        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        LegIDImpl legToBeReleased = new LegIDImpl(false, LegType.leg6);
        CauseIndicatorsImpl causeIndicators = new CauseIndicatorsImpl(0, 0, 0, 6, null);
        CauseCapImpl releaseCause = new CauseCapImpl(causeIndicators);
        DisconnectLegRequestImpl original = new DisconnectLegRequestImpl(legToBeReleased, releaseCause, CAPExtensionsTest.createTestCAPExtensions());

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "disconnectLegRequest", DisconnectLegRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        DisconnectLegRequestImpl copy = reader.read("disconnectLegRequest", DisconnectLegRequestImpl.class);

        assertEquals(original.getLegToBeReleased().getReceivingSideID(), copy.getLegToBeReleased().getReceivingSideID());
        CauseIndicators ci = original.getReleaseCause().getCauseIndicators();
        CauseIndicators ci2 = copy.getReleaseCause().getCauseIndicators();
        assertEquals(ci.getCodingStandard(), ci2.getCodingStandard());
        assertEquals(ci.getCauseValue(), ci2.getCauseValue());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(original.getExtensions()));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));


        original = new DisconnectLegRequestImpl(legToBeReleased, null, null);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "disconnectLegRequest", DisconnectLegRequestImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("disconnectLegRequest", DisconnectLegRequestImpl.class);

        assertEquals(original.getLegToBeReleased().getReceivingSideID(), copy.getLegToBeReleased().getReceivingSideID());
        assertNull(original.getReleaseCause());
        assertNull(copy.getReleaseCause());
        assertNull(original.getExtensions());
        assertNull(copy.getExtensions());
    }*/
}
