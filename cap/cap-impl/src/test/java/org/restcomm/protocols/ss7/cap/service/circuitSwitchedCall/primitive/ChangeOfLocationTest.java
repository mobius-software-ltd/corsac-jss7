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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ChangeOfLocationAltImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ChangeOfLocationImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ChangeOfLocationListWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.junit.Test;

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
public class ChangeOfLocationTest {

    public byte[] getData1() {
        return new byte[] { 48,9, (byte) 128, 7, 2, (byte) 241, 16, 85, (byte) 240, 0, 55 };
    }

    public byte[] getData2() {
        return new byte[] { 48,9, (byte) 129, 7, 2, (byte) 241, 16, 85, (byte) 240, 0, 55 };
    }

    public byte[] getData3() {
        return new byte[] { 48,7, (byte) 130, 5, (byte) 145, (byte) 240, 16, 85, (byte) 240 };
    }

    public byte[] getData4() {
        return new byte[] { 48, 2, (byte) 131, 0 };
    }

    public byte[] getData5() {
        return new byte[] { 48, 2, (byte) 133, 0 };
    }

    public byte[] getData6() {
        return new byte[] { 48, 2, (byte) 132, 0 };
    }

    public byte[] getData7() {
        return new byte[] { 48, 2, (byte) 166, 0 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ChangeOfLocationListWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChangeOfLocationListWrapperImpl);
        
        ChangeOfLocationListWrapperImpl elem = (ChangeOfLocationListWrapperImpl)result.getResult();        
        assertNotNull(elem.getChangeOfLocationList());
        assertEquals(elem.getChangeOfLocationList().size(),1);
        assertEquals(elem.getChangeOfLocationList().get(0).getCellGlobalId().getLac(), 22000);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChangeOfLocationListWrapperImpl);
        
        elem = (ChangeOfLocationListWrapperImpl)result.getResult(); 
        assertNotNull(elem.getChangeOfLocationList());
        assertEquals(elem.getChangeOfLocationList().size(),1);
        assertEquals(elem.getChangeOfLocationList().get(0).getServiceAreaId().getLac(), 22000);

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChangeOfLocationListWrapperImpl);
        
        elem = (ChangeOfLocationListWrapperImpl)result.getResult(); 
        assertNotNull(elem.getChangeOfLocationList());
        assertEquals(elem.getChangeOfLocationList().size(),1);
        assertEquals(elem.getChangeOfLocationList().get(0).getLocationAreaId().getLac(), 22000);

        rawData = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChangeOfLocationListWrapperImpl);
        
        elem = (ChangeOfLocationListWrapperImpl)result.getResult(); 
        assertNotNull(elem.getChangeOfLocationList());
        assertEquals(elem.getChangeOfLocationList().size(),1);
        assertTrue(elem.getChangeOfLocationList().get(0).isInterSystemHandOver());

        rawData = this.getData5();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChangeOfLocationListWrapperImpl);
        
        elem = (ChangeOfLocationListWrapperImpl)result.getResult(); 
        assertNotNull(elem.getChangeOfLocationList());
        assertEquals(elem.getChangeOfLocationList().size(),1);
        assertTrue(elem.getChangeOfLocationList().get(0).isInterMSCHandOver());

        rawData = this.getData6();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChangeOfLocationListWrapperImpl);
        
        elem = (ChangeOfLocationListWrapperImpl)result.getResult(); 
        assertNotNull(elem.getChangeOfLocationList());
        assertEquals(elem.getChangeOfLocationList().size(),1);
        assertTrue(elem.getChangeOfLocationList().get(0).isInterPLMNHandOver());

        rawData = this.getData7();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ChangeOfLocationListWrapperImpl);
        
        elem = (ChangeOfLocationListWrapperImpl)result.getResult(); 
        assertNotNull(elem.getChangeOfLocationList());
        assertEquals(elem.getChangeOfLocationList().size(),1);
        assertNotNull(elem.getChangeOfLocationList().get(0).getChangeOfLocationAlt());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ChangeOfLocationListWrapperImpl.class);
    	
        CellGlobalIdOrServiceAreaIdFixedLengthImpl value = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(201, 1, 22000, 55);
        // int mcc, int mnc, int lac, int cellIdOrServiceAreaCode
        ChangeOfLocationImpl elem = new ChangeOfLocationImpl(value, ChangeOfLocationImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.cellGlobalId);
        ChangeOfLocationListWrapperImpl wrapper=new ChangeOfLocationListWrapperImpl(Arrays.asList(new ChangeOfLocationImpl[] { elem }));
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new ChangeOfLocationImpl(value, ChangeOfLocationImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.serviceAreaId);
        wrapper=new ChangeOfLocationListWrapperImpl(Arrays.asList(new ChangeOfLocationImpl[] { elem }));
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        LAIFixedLengthImpl lai = new LAIFixedLengthImpl(190, 1, 22000);
        // int mcc, int mnc, int lac
        elem = new ChangeOfLocationImpl(lai);
        wrapper=new ChangeOfLocationListWrapperImpl(Arrays.asList(new ChangeOfLocationImpl[] { elem }));
        rawData = this.getData3();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interSystemHandOver);
        wrapper=new ChangeOfLocationListWrapperImpl(Arrays.asList(new ChangeOfLocationImpl[] { elem }));
        rawData = this.getData4();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interMSCHandOver);
        wrapper=new ChangeOfLocationListWrapperImpl(Arrays.asList(new ChangeOfLocationImpl[] { elem }));
        rawData = this.getData5();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interPLMNHandOver);
        wrapper=new ChangeOfLocationListWrapperImpl(Arrays.asList(new ChangeOfLocationImpl[] { elem }));
        rawData = this.getData6();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        ChangeOfLocationAltImpl changeOfLocationAlt = new ChangeOfLocationAltImpl();
        elem = new ChangeOfLocationImpl(changeOfLocationAlt);
        wrapper=new ChangeOfLocationListWrapperImpl(Arrays.asList(new ChangeOfLocationImpl[] { elem }));
        rawData = this.getData7();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
