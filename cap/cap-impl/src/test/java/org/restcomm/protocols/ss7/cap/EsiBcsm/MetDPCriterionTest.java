/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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
* @author yulianoifa
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
}
