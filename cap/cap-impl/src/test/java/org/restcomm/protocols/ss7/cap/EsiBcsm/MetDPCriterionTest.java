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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
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
public class MetDPCriterionTest {

    public byte[] getData1() {
        return new byte[] { 48, 9, (byte) 128, 7, 2, (byte) 241, 16, 85, (byte) 240, 0, 55 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 9, (byte) 129, 7, 2, (byte) 241, 16, 85, (byte) 240, 0, 55 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 9, (byte) 130, 7, 2, (byte) 241, 16, 85, (byte) 240, 0, 55 };
    }

    public byte[] getData4() {
        return new byte[] { 48, 9, (byte) 131, 7, 2, (byte) 241, 16, 85, (byte) 240, 0, 55 };
    }

    public byte[] getData5() {
        return new byte[] { 48, 7, (byte) 132, 5, (byte) 145, (byte) 240, 16, 85, (byte) 240 };
    }

    public byte[] getData6() {
        return new byte[] { 48, 7, (byte) 133, 5, (byte) 145, (byte) 240, 16, 85, (byte) 240 };
    }

    public byte[] getData7() {
        return new byte[] { 48, 2, (byte) 134, 0 };
    }

    public byte[] getData8() {
        return new byte[] { 48, 2, (byte) 135, 0 };
    }

    public byte[] getData9() {
        return new byte[] { 48, 2, (byte) 136, 0 };
    }

    public byte[] getData10() {
        return new byte[] { 48, 2, (byte) 137, 0 };
    }

    public byte[] getData11() {
        return new byte[] { 48, 2, (byte) 170, 0 };
    }

    @Test(groups = { "functional.decode", "EsiBcsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(MetDPCriterionWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MetDPCriterionWrapperImpl);
        
        MetDPCriterionWrapperImpl elem = (MetDPCriterionWrapperImpl)result.getResult();        
        assertNotNull(elem.getMetDPCriterion());
        assertEquals(elem.getMetDPCriterion().size(),1);
        assertEquals(elem.getMetDPCriterion().get(0).getEnteringCellGlobalId().getLac(), 22000);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MetDPCriterionWrapperImpl);
        
        elem = (MetDPCriterionWrapperImpl)result.getResult();        
        assertNotNull(elem.getMetDPCriterion());
        assertEquals(elem.getMetDPCriterion().size(),1);
        assertEquals(elem.getMetDPCriterion().get(0).getLeavingCellGlobalId().getLac(), 22000);

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MetDPCriterionWrapperImpl);
        
        elem = (MetDPCriterionWrapperImpl)result.getResult();        
        assertNotNull(elem.getMetDPCriterion());
        assertEquals(elem.getMetDPCriterion().size(),1);
        assertEquals(elem.getMetDPCriterion().get(0).getEnteringServiceAreaId().getLac(), 22000);

        rawData = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MetDPCriterionWrapperImpl);
        
        elem = (MetDPCriterionWrapperImpl)result.getResult();        
        assertNotNull(elem.getMetDPCriterion());
        assertEquals(elem.getMetDPCriterion().size(),1);
        assertEquals(elem.getMetDPCriterion().get(0).getLeavingServiceAreaId().getLac(), 22000);

        rawData = this.getData5();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MetDPCriterionWrapperImpl);
        
        elem = (MetDPCriterionWrapperImpl)result.getResult();        
        assertNotNull(elem.getMetDPCriterion());
        assertEquals(elem.getMetDPCriterion().size(),1);
        assertEquals(elem.getMetDPCriterion().get(0).getEnteringLocationAreaId().getLac(), 22000);

        rawData = this.getData6();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MetDPCriterionWrapperImpl);
        
        elem = (MetDPCriterionWrapperImpl)result.getResult();        
        assertNotNull(elem.getMetDPCriterion());
        assertEquals(elem.getMetDPCriterion().size(),1);
        assertEquals(elem.getMetDPCriterion().get(0).getLeavingLocationAreaId().getLac(), 22000);

        rawData = this.getData7();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MetDPCriterionWrapperImpl);
        
        elem = (MetDPCriterionWrapperImpl)result.getResult();        
        assertNotNull(elem.getMetDPCriterion());
        assertEquals(elem.getMetDPCriterion().size(),1);
        assertTrue(elem.getMetDPCriterion().get(0).getInterSystemHandOverToUMTS());

        rawData = this.getData8();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MetDPCriterionWrapperImpl);
        
        elem = (MetDPCriterionWrapperImpl)result.getResult();        
        assertNotNull(elem.getMetDPCriterion());
        assertEquals(elem.getMetDPCriterion().size(),1);
        assertTrue(elem.getMetDPCriterion().get(0).getInterSystemHandOverToGSM());

        rawData = this.getData9();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MetDPCriterionWrapperImpl);
        
        elem = (MetDPCriterionWrapperImpl)result.getResult();        
        assertNotNull(elem.getMetDPCriterion());
        assertEquals(elem.getMetDPCriterion().size(),1);
        assertTrue(elem.getMetDPCriterion().get(0).getInterPLMNHandOver());

        rawData = this.getData10();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MetDPCriterionWrapperImpl);
        
        elem = (MetDPCriterionWrapperImpl)result.getResult();        
        assertNotNull(elem.getMetDPCriterion());
        assertEquals(elem.getMetDPCriterion().size(),1);
        assertTrue(elem.getMetDPCriterion().get(0).getInterMSCHandOver());

        rawData = this.getData11();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MetDPCriterionWrapperImpl);
        
        elem = (MetDPCriterionWrapperImpl)result.getResult();        
        assertNotNull(elem.getMetDPCriterion());
        assertEquals(elem.getMetDPCriterion().size(),1);
        assertNotNull(elem.getMetDPCriterion().get(0).getMetDPCriterionAlt());
    }

    @Test(groups = { "functional.encode", "EsiBcsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(MetDPCriterionWrapperImpl.class);
    	
        CellGlobalIdOrServiceAreaIdFixedLengthImpl value = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(201, 1, 22000, 55);
        // int mcc, int mnc, int lac, int cellIdOrServiceAreaCode
        MetDPCriterionImpl elem = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringCellGlobalId);
        MetDPCriterionWrapperImpl wrapper=new MetDPCriterionWrapperImpl(Arrays.asList(new MetDPCriterionImpl[] { elem}));
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingCellGlobalId);
        wrapper=new MetDPCriterionWrapperImpl(Arrays.asList(new MetDPCriterionImpl[] { elem}));
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringServiceAreaId);
        wrapper=new MetDPCriterionWrapperImpl(Arrays.asList(new MetDPCriterionImpl[] { elem}));
        rawData = this.getData3();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingServiceAreaId);
        wrapper=new MetDPCriterionWrapperImpl(Arrays.asList(new MetDPCriterionImpl[] { elem}));
        rawData = this.getData4();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        LAIFixedLengthImpl lai = new LAIFixedLengthImpl(190, 1, 22000);
        // int mcc, int mnc, int lac
        elem = new MetDPCriterionImpl(lai, MetDPCriterionImpl.LAIFixedLength_Option.enteringLocationAreaId);
        wrapper=new MetDPCriterionWrapperImpl(Arrays.asList(new MetDPCriterionImpl[] { elem}));
        rawData = this.getData5();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new MetDPCriterionImpl(lai, MetDPCriterionImpl.LAIFixedLength_Option.leavingLocationAreaId);
        wrapper=new MetDPCriterionWrapperImpl(Arrays.asList(new MetDPCriterionImpl[] { elem}));
        rawData = this.getData6();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToUMTS);
        wrapper=new MetDPCriterionWrapperImpl(Arrays.asList(new MetDPCriterionImpl[] { elem}));
        rawData = this.getData7();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToGSM);
        wrapper=new MetDPCriterionWrapperImpl(Arrays.asList(new MetDPCriterionImpl[] { elem}));
        rawData = this.getData8();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interPLMNHandOver);
        wrapper=new MetDPCriterionWrapperImpl(Arrays.asList(new MetDPCriterionImpl[] { elem}));
        rawData = this.getData9();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interMSCHandOver);
        wrapper=new MetDPCriterionWrapperImpl(Arrays.asList(new MetDPCriterionImpl[] { elem}));
        rawData = this.getData10();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        MetDPCriterionAltImpl metDPCriterionAlt = new MetDPCriterionAltImpl();
        elem = new MetDPCriterionImpl(metDPCriterionAlt);
        wrapper=new MetDPCriterionWrapperImpl(Arrays.asList(new MetDPCriterionImpl[] { elem}));
        rawData = this.getData11();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "EsiBcsm" })
    public void testXMLSerializaion() throws Exception {
        CellGlobalIdOrServiceAreaIdFixedLength value = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(201, 1, 22000, 55);
        // int mcc, int mnc, int lac, int cellIdOrServiceAreaCode
        MetDPCriterionImpl original = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringCellGlobalId);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        MetDPCriterionImpl copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getEnteringCellGlobalId().getLac(), original.getEnteringCellGlobalId().getLac());


        original = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingCellGlobalId);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getLeavingCellGlobalId().getLac(), original.getLeavingCellGlobalId().getLac());


        original = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringServiceAreaId);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getEnteringServiceAreaId().getLac(), original.getEnteringServiceAreaId().getLac());


        original = new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingServiceAreaId);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getLeavingServiceAreaId().getLac(), original.getLeavingServiceAreaId().getLac());


        LAIFixedLength lai = new LAIFixedLengthImpl(190, 1, 22000);
        original = new MetDPCriterionImpl(lai, MetDPCriterionImpl.LAIFixedLength_Option.enteringLocationAreaId);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getEnteringLocationAreaId().getLac(), original.getEnteringLocationAreaId().getLac());


        original = new MetDPCriterionImpl(lai, MetDPCriterionImpl.LAIFixedLength_Option.leavingLocationAreaId);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getLeavingLocationAreaId().getLac(), original.getLeavingLocationAreaId().getLac());


        original = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToUMTS);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getInterSystemHandOverToUMTS(), original.getInterSystemHandOverToUMTS());


        original = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToGSM);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getInterSystemHandOverToGSM(), original.getInterSystemHandOverToGSM());


        original = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interPLMNHandOver);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getInterPLMNHandOver(), original.getInterPLMNHandOver());


        original = new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interMSCHandOver);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertEquals(copy.getInterMSCHandOver(), original.getInterMSCHandOver());


        MetDPCriterionAlt metDPCriterionAlt = new MetDPCriterionAltImpl();
        original = new MetDPCriterionImpl(metDPCriterionAlt);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "metDPCriterion", MetDPCriterionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("metDPCriterion", MetDPCriterionImpl.class);

        assertNotNull(copy.getMetDPCriterionAlt());
    }*/
}
