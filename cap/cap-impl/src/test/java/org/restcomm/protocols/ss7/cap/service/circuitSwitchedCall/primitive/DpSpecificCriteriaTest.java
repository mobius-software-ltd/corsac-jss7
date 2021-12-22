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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ChangeOfLocation;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ChangeOfLocationImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaAltImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MidCallControlInfoImpl;
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
public class DpSpecificCriteriaTest {

    public byte[] getData1() {
        return new byte[] { 48, 4, (byte) 129, 2, 3, (byte) 232 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 5, (byte) 162, 3, (byte) 128, 1, 10 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 6, (byte) 163, 4, (byte) 160, 2, (byte) 131, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DpSpecificCriteriaWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DpSpecificCriteriaWrapperImpl);
        
        DpSpecificCriteriaWrapperImpl elem = (DpSpecificCriteriaWrapperImpl)result.getResult();                
        assertEquals((int) elem.getDpSpecificCriteria().getApplicationTimer(), 1000);
        assertNull(elem.getDpSpecificCriteria().getMidCallControlInfo());
        assertNull(elem.getDpSpecificCriteria().getDpSpecificCriteriaAlt());


        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DpSpecificCriteriaWrapperImpl);
        
        elem = (DpSpecificCriteriaWrapperImpl)result.getResult(); 
        assertNull(elem.getDpSpecificCriteria().getApplicationTimer());
        assertEquals((int) elem.getDpSpecificCriteria().getMidCallControlInfo().getMinimumNumberOfDigits(), 10);
        assertNull(elem.getDpSpecificCriteria().getDpSpecificCriteriaAlt());


        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DpSpecificCriteriaWrapperImpl);
        
        elem = (DpSpecificCriteriaWrapperImpl)result.getResult(); 
        assertNull(elem.getDpSpecificCriteria().getApplicationTimer());
        assertNull(elem.getDpSpecificCriteria().getMidCallControlInfo());
        assertTrue(elem.getDpSpecificCriteria().getDpSpecificCriteriaAlt().getChangeOfPositionControlInfo().get(0).isInterSystemHandOver());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DpSpecificCriteriaWrapperImpl.class);
    	
        DpSpecificCriteriaImpl elem = new DpSpecificCriteriaImpl(1000);
        DpSpecificCriteriaWrapperImpl wrapper = new DpSpecificCriteriaWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        MidCallControlInfoImpl midCallControlInfo = new MidCallControlInfoImpl(10, null, null, null, null, null);
        elem = new DpSpecificCriteriaImpl(midCallControlInfo);
        wrapper = new DpSpecificCriteriaWrapperImpl(elem);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        List<ChangeOfLocation> changeOfPositionControlInfo = new ArrayList<ChangeOfLocation>();
        ChangeOfLocationImpl changeOfLocation = new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interSystemHandOver);
        changeOfPositionControlInfo.add(changeOfLocation);
        DpSpecificCriteriaAltImpl dpSpecificCriteriaAlt = new DpSpecificCriteriaAltImpl(changeOfPositionControlInfo, null);
        elem = new DpSpecificCriteriaImpl(dpSpecificCriteriaAlt);
        wrapper = new DpSpecificCriteriaWrapperImpl(elem);
        rawData = this.getData3();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        DpSpecificCriteriaImpl original = new DpSpecificCriteriaImpl(1000);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "dpSpecificCriteria", DpSpecificCriteriaImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        DpSpecificCriteriaImpl copy = reader.read("dpSpecificCriteria", DpSpecificCriteriaImpl.class);

        assertEquals((int) copy.getApplicationTimer(), (int) original.getApplicationTimer());
        assertNull(copy.getMidCallControlInfo());
        assertNull(copy.getDpSpecificCriteriaAlt());


        MidCallControlInfo midCallControlInfo = new MidCallControlInfoImpl(10, null, null, null, null, null);
        original = new DpSpecificCriteriaImpl(midCallControlInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "dpSpecificCriteria", DpSpecificCriteriaImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("dpSpecificCriteria", DpSpecificCriteriaImpl.class);

        assertNull(copy.getApplicationTimer());
        assertEquals(copy.getMidCallControlInfo().getMinimumNumberOfDigits(), original.getMidCallControlInfo().getMinimumNumberOfDigits());
        assertNull(copy.getDpSpecificCriteriaAlt());


        ArrayList<ChangeOfLocation> changeOfPositionControlInfo = new ArrayList<ChangeOfLocation>();
        ChangeOfLocation changeOfLocation = new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interSystemHandOver);
        changeOfPositionControlInfo.add(changeOfLocation);
        DpSpecificCriteriaAlt dpSpecificCriteriaAlt = new DpSpecificCriteriaAltImpl(changeOfPositionControlInfo, null);
        original = new DpSpecificCriteriaImpl(dpSpecificCriteriaAlt);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "dpSpecificCriteria", DpSpecificCriteriaImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("dpSpecificCriteria", DpSpecificCriteriaImpl.class);

        assertNull(copy.getApplicationTimer());
        assertNull(copy.getMidCallControlInfo());
        assertEquals(copy.getDpSpecificCriteriaAlt().getChangeOfPositionControlInfo().get(0).isInterSystemHandOver(), original.getDpSpecificCriteriaAlt()
                .getChangeOfPositionControlInfo().get(0).isInterSystemHandOver());
    }*/
}
