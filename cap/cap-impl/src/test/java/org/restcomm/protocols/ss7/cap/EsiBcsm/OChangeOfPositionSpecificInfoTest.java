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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterion;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OChangeOfPositionSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationImpl;
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
public class OChangeOfPositionSpecificInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 7, (byte) 191, 50, 4, 2, 2, 0, (byte) 200 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 19, (byte) 191, 50, 4, 2, 2, 0, (byte) 200, (byte) 191, 51, 9, (byte) 133, 5, 82, (byte) 240, 16, (byte) 128, (byte) 232,
                (byte) 135, 0 };
    }

    @Test(groups = { "functional.decode", "EsiBcsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(OChangeOfPositionSpecificInfoImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OChangeOfPositionSpecificInfoImpl);
        
        OChangeOfPositionSpecificInfoImpl elem = (OChangeOfPositionSpecificInfoImpl)result.getResult();        
        assertEquals((int) elem.getLocationInformation().getAgeOfLocationInformation(), 200);
        assertNull(elem.getMetDPCriteriaList());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OChangeOfPositionSpecificInfoImpl);
        
        elem = (OChangeOfPositionSpecificInfoImpl)result.getResult();  
        assertEquals((int) elem.getLocationInformation().getAgeOfLocationInformation(), 200);

        assertEquals(elem.getMetDPCriteriaList().size(), 2);
        assertEquals(elem.getMetDPCriteriaList().get(0).getLeavingLocationAreaId().getLac(), 33000);
        assertTrue(elem.getMetDPCriteriaList().get(1).getInterSystemHandOverToGSM());
    }

    @Test(groups = { "functional.encode", "EsiBcsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(OChangeOfPositionSpecificInfoImpl.class);
    	
        LocationInformationImpl locationInformation = new LocationInformationImpl(200, null, null, null, null, null, null, null, null, false, false, null, null);
        OChangeOfPositionSpecificInfo elem = new OChangeOfPositionSpecificInfoImpl(locationInformation, null);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        List<MetDPCriterion> metDPCriteriaList = new ArrayList<MetDPCriterion>();
        LAIFixedLengthImpl value = new LAIFixedLengthImpl(250, 1, 33000);
        MetDPCriterionImpl met1 = new MetDPCriterionImpl(value, MetDPCriterionImpl.LAIFixedLength_Option.leavingLocationAreaId);
        metDPCriteriaList.add(met1);
        MetDPCriterionImpl met2 = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToGSM);
        metDPCriteriaList.add(met2);
        elem = new OChangeOfPositionSpecificInfoImpl(locationInformation, metDPCriteriaList);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "EsiBcsm" })
    public void testXMLSerializaion() throws Exception {
        LocationInformation locationInformation = new LocationInformationImpl(200, null, null, null, null, null, null, null, null, false, false, null, null);
        OChangeOfPositionSpecificInfoImpl original = new OChangeOfPositionSpecificInfoImpl(locationInformation, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "oChangeOfPositionSpecificInfo", OChangeOfPositionSpecificInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        OChangeOfPositionSpecificInfoImpl copy = reader.read("oChangeOfPositionSpecificInfo", OChangeOfPositionSpecificInfoImpl.class);

        assertEquals((int) copy.getLocationInformation().getAgeOfLocationInformation(), (int) original.getLocationInformation().getAgeOfLocationInformation());
        assertNull(copy.getMetDPCriteriaList());


        ArrayList<MetDPCriterion> metDPCriteriaList = new ArrayList<MetDPCriterion>();
        LAIFixedLength value = new LAIFixedLengthImpl(250, 1, 33000);
        MetDPCriterion met1 = new MetDPCriterionImpl(value, MetDPCriterionImpl.LAIFixedLength_Option.leavingLocationAreaId);
        metDPCriteriaList.add(met1);
        MetDPCriterion met2 = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToGSM);
        metDPCriteriaList.add(met2);
        original = new OChangeOfPositionSpecificInfoImpl(locationInformation, metDPCriteriaList);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "oChangeOfPositionSpecificInfo", OChangeOfPositionSpecificInfoImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("oChangeOfPositionSpecificInfo", OChangeOfPositionSpecificInfoImpl.class);

        assertEquals((int) copy.getLocationInformation().getAgeOfLocationInformation(), (int) original.getLocationInformation().getAgeOfLocationInformation());
        assertEquals(copy.getMetDPCriteriaList().size(), original.getMetDPCriteriaList().size());
        assertEquals(copy.getMetDPCriteriaList().get(0).getLeavingLocationAreaId().getLac(), original.getMetDPCriteriaList().get(0).getLeavingLocationAreaId()
                .getLac());
        assertEquals(copy.getMetDPCriteriaList().get(1).getInterSystemHandOverToGSM(), original.getMetDPCriteriaList().get(1).getInterSystemHandOverToGSM());
    }*/
}
