/*
 * Mobius Software LTD
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class AreaIdentificationTest {

    public byte[] getData1() {
        return new byte[] { 4, 2, 82, (byte) 240 };
    };

    public byte[] getData1Val() {
        return new byte[] { 82, (byte) 240 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 3, 82, (byte) 240, 112 };
    };

    public byte[] getData3() {
        return new byte[] { 4, 5, 82, (byte) 128, 118, 17, 92 };
    };

    public byte[] getData4() {
        return new byte[] { 4, 6, 82, (byte) 128, 118, 17, 92, (byte) 200 };
    };

    public byte[] getData5() {
        return new byte[] { 4, 7, 82, (byte) 128, 118, 17, 92, (byte) 214, (byte) 216 };
    };

    public byte[] getData6() {
        return new byte[] { 4, 7, 82, (byte) 128, 118, (byte) 248, 0, 0, 1 };
    };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AreaIdentificationImpl.class);
    	
        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AreaIdentificationImpl);
        AreaIdentificationImpl prim = (AreaIdentificationImpl)result.getResult();
        
        assertNotNull(prim.getValue());
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(getData1Val()), prim.getValue()));

        assertEquals(prim.getMCC(), 250);
        try {
            prim.getMNC();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getLac();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getRac();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getUtranCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        // assertEquals(prim.getMNC(), 1);
        // assertEquals(prim.getLac(), 4444);
        // assertEquals(prim.getCellId(), 3333);
        // prim.getRac();
        // prim.getUtranCellId();

        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AreaIdentificationImpl);
        prim = (AreaIdentificationImpl)result.getResult();

        assertNotNull(prim.getValue());

        assertEquals(prim.getMCC(), 250);
        assertEquals(prim.getMNC(), 7);
        try {
            prim.getLac();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getRac();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getUtranCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        // assertEquals(prim.getLac(), 4444);
        // assertEquals(prim.getCellId(), 3333);
        // prim.getRac();
        // prim.getUtranCellId();

        data = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AreaIdentificationImpl);
        prim = (AreaIdentificationImpl)result.getResult();
        
        assertNotNull(prim.getValue());

        assertEquals(prim.getMCC(), 250);
        assertEquals(prim.getMNC(), 678);
        assertEquals(prim.getLac(), 4444);
        try {
            prim.getCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getRac();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getUtranCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        // assertEquals(prim.getCellId(), 3333);
        // prim.getRac();
        // prim.getUtranCellId();

        data = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AreaIdentificationImpl);
        prim = (AreaIdentificationImpl)result.getResult();

        assertNotNull(prim.getValue());

        assertEquals(prim.getMCC(), 250);
        assertEquals(prim.getMNC(), 678);
        assertEquals(prim.getLac(), 4444);
        assertEquals(prim.getRac(), 200);
        try {
            prim.getCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getUtranCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        // assertEquals(prim.getCellId(), 3333);
        // prim.getUtranCellId();

        data = this.getData5();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AreaIdentificationImpl);
        prim = (AreaIdentificationImpl)result.getResult();

        assertNotNull(prim.getValue());

        assertEquals(prim.getMCC(), 250);
        assertEquals(prim.getMNC(), 678);
        assertEquals(prim.getLac(), 4444);
        assertEquals(prim.getCellId(), 55000);

        data = this.getData6();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AreaIdentificationImpl);
        prim = (AreaIdentificationImpl)result.getResult();

        assertNotNull(prim.getValue());

        assertEquals(prim.getMCC(), 250);
        assertEquals(prim.getMNC(), 678);
        assertEquals(prim.getUtranCellId(), (int) (4160749569L));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AreaIdentificationImpl.class);
    	
        AreaIdentificationImpl prim = new AreaIdentificationImpl(AreaType.countryCode, 250, 0, 0, 0);
        byte[] data=getData1();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        prim = new AreaIdentificationImpl(AreaType.plmnId, 250, 7, 0, 0);
        data=getData2();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));       

        prim = new AreaIdentificationImpl(AreaType.locationAreaId, 250, 678, 4444, 0);
        data=getData3();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));       

        prim = new AreaIdentificationImpl(AreaType.routingAreaId, 250, 678, 4444, 200);
        data=getData4();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));       

        prim = new AreaIdentificationImpl(AreaType.cellGlobalId, 250, 678, 4444, 55000);
        data=getData5();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));       

        prim = new AreaIdentificationImpl(AreaType.utranCellId, 250, 678, 0, (int) (4160749569L));
        data=getData6();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));       
    }
}
