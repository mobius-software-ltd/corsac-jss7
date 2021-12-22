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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
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
public class DestinationRoutingAddressTest {

    public byte[] getData1() {
        return new byte[] { 48, 7, 4, 5, 2, 16, 121, 34, 16 };
    }

    public byte[] getIntData1() {
        return new byte[] { 2, 16, 121, 34, 16 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DestinationRoutingAddressImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DestinationRoutingAddressImpl);
        
        DestinationRoutingAddressImpl elem = (DestinationRoutingAddressImpl)result.getResult();                
        assertNotNull(elem.getCalledPartyNumber());
        assertEquals(elem.getCalledPartyNumber().size(), 1);
        assertTrue(Arrays.equals(elem.getCalledPartyNumber().get(0).getData(), this.getIntData1()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DestinationRoutingAddressImpl.class);
    	
    	List<CalledPartyNumberIsup> cpnl = new ArrayList<CalledPartyNumberIsup>();
        CalledPartyNumberIsupImpl cpn = new CalledPartyNumberIsupImpl(getIntData1());
        cpnl.add(cpn);
        DestinationRoutingAddressImpl elem = new DestinationRoutingAddressImpl(cpnl);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerialize() throws Exception {

        ArrayList<CalledPartyNumberCap> cpnl = new ArrayList<CalledPartyNumberCap>();
        CalledPartyNumberCapImpl cpn = new CalledPartyNumberCapImpl(getIntData1());
        cpnl.add(cpn);
        DestinationRoutingAddressImpl original = new DestinationRoutingAddressImpl(cpnl);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "destinationRoutingAddress", DestinationRoutingAddressImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        DestinationRoutingAddressImpl copy = reader.read("destinationRoutingAddress", DestinationRoutingAddressImpl.class);

        assertEquals(copy.getCalledPartyNumber().size(), original.getCalledPartyNumber().size());
        assertEquals(copy.getCalledPartyNumber().get(0).getData(), original.getCalledPartyNumber().get(0).getData());

    }*/
}
