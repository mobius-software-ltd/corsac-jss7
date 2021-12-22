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

import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.TimeIfTariffSwitchImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.TimeInformationImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.TimeInformationWrapperImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class TimeInformationTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 128, 1, 26 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 6, (byte) 161, 4, (byte) 128, 2, 3, (byte) 232 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TimeInformationWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TimeInformationWrapperImpl);
        
        TimeInformationWrapperImpl elem = (TimeInformationWrapperImpl)result.getResult();                
        assertEquals((int) elem.getTimeInformation().getTimeIfNoTariffSwitch(), 26);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TimeInformationWrapperImpl);
        
        elem = (TimeInformationWrapperImpl)result.getResult(); 
        assertEquals(elem.getTimeInformation().getTimeIfTariffSwitch().getTimeSinceTariffSwitch(), 1000);
        assertNull(elem.getTimeInformation().getTimeIfTariffSwitch().getTariffSwitchInterval());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TimeInformationWrapperImpl.class);
    	
        TimeInformationImpl elem = new TimeInformationImpl(26);
        TimeInformationWrapperImpl wrapper = new TimeInformationWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        TimeIfTariffSwitchImpl tit = new TimeIfTariffSwitchImpl(1000, null);
        elem = new TimeInformationImpl(tit);
        wrapper = new TimeInformationWrapperImpl(elem);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerializaion() throws Exception {
        TimeInformationImpl original = new TimeInformationImpl(26);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "timeInformation", TimeInformationImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        TimeInformationImpl copy = reader.read("timeInformation", TimeInformationImpl.class);

        assertEquals(copy.getTimeIfNoTariffSwitch(), original.getTimeIfNoTariffSwitch());
        assertNull(copy.getTimeIfTariffSwitch());

        TimeIfTariffSwitchImpl tit = new TimeIfTariffSwitchImpl(1000, null);
        original = new TimeInformationImpl(tit);

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "timeInformation", TimeInformationImpl.class);
        writer.close();

        rawData = baos.toByteArray();

        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("timeInformation", TimeInformationImpl.class);

        assertEquals(copy.getTimeIfTariffSwitch().getTariffSwitchInterval(), original.getTimeIfTariffSwitch()
                .getTariffSwitchInterval());
        assertNull(copy.getTimeIfNoTariffSwitch());
    }*/
}
