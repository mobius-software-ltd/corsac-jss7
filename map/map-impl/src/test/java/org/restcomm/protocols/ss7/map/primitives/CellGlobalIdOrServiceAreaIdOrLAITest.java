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

package org.restcomm.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 *
 */
public class CellGlobalIdOrServiceAreaIdOrLAITest {
    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl.class);
    	
        byte[] data = new byte[] { (byte) 0x80, 0x07, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b };

        ASNDecodeResult result = parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl);
        CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl cellGlobalIdOrServiceAreaIdOrLAI = (CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl)result.getResult();
        
        assertNotNull(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength());
        assertNull(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength());
        
        CellGlobalIdOrServiceAreaIdFixedLength lai = cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength();
        assertEquals(lai.getMCC(), 506);
        assertEquals(lai.getMNC(), 700);
        assertEquals(lai.getLac(), 0x0809);
        assertEquals(lai.getCellIdOrServiceAreaCode(), 0x0a0b);
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl.class);
    	
        byte[] data = new byte[] { (byte) 0x80, 0x07, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b };

        CellGlobalIdOrServiceAreaIdFixedLengthImpl par = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(new byte[] { 0x05, 0x06,
                0x07, 0x08, 0x09, 0x0a, 0x0b });
        CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(par);
        CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl wrapper=new CellGlobalIdOrServiceAreaIdOrLAIPrimitiveImpl(cellGlobalIdOrServiceAreaIdOrLAI);
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        assertTrue(Arrays.equals(data, encodedData));

    }

    /*@Test(groups = { "functional.xml.serialize", "service.lsm" })
    public void testSerialization() throws Exception {
        CellGlobalIdOrServiceAreaIdFixedLength par = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(250, 1, 4444, 3333);
        CellGlobalIdOrServiceAreaIdOrLAIImpl original = new CellGlobalIdOrServiceAreaIdOrLAIImpl(par);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "cellGlobalIdOrServiceAreaIdOrLAI", CellGlobalIdOrServiceAreaIdOrLAIImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CellGlobalIdOrServiceAreaIdOrLAIImpl copy = reader.read("cellGlobalIdOrServiceAreaIdOrLAI",
                CellGlobalIdOrServiceAreaIdOrLAIImpl.class);

        assertEquals(copy.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), original
                .getCellGlobalIdOrServiceAreaIdFixedLength().getMCC());
        assertEquals(copy.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), original
                .getCellGlobalIdOrServiceAreaIdFixedLength().getMNC());
        assertEquals(copy.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), original
                .getCellGlobalIdOrServiceAreaIdFixedLength().getLac());
        assertEquals(copy.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), original
                .getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode());

        LAIFixedLengthImpl par2 = new LAIFixedLengthImpl(250, 1, 4444);
        original = new CellGlobalIdOrServiceAreaIdOrLAIImpl(par2);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "cellGlobalIdOrServiceAreaIdOrLAI", CellGlobalIdOrServiceAreaIdOrLAIImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("cellGlobalIdOrServiceAreaIdOrLAI", CellGlobalIdOrServiceAreaIdOrLAIImpl.class);

        assertEquals(copy.getLAIFixedLength().getMCC(), original.getLAIFixedLength().getMCC());
        assertEquals(copy.getLAIFixedLength().getMNC(), original.getLAIFixedLength().getMNC());
        assertEquals(copy.getLAIFixedLength().getLac(), original.getLAIFixedLength().getLac());
    }*/
}
