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

package org.restcomm.protocols.ss7.map.service.oam;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.oam.BssRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.HlrRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.MscRecordType;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceTypeInvokingEvent;
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
public class TraceTypeTest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 1, 39 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 4, 1, (byte) 133 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(TraceTypeImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TraceTypeImpl);
        TraceTypeImpl asc = (TraceTypeImpl)result.getResult();
        
        assertEquals(asc.getBssRecordType(), BssRecordType.Radio);
        assertEquals(asc.getMscRecordType(), MscRecordType.Detailed);
        assertEquals(asc.getTraceTypeInvokingEvent(), TraceTypeInvokingEvent.InvokingEvent_3);
        assertFalse(asc.isPriorityIndication());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TraceTypeImpl);
        asc = (TraceTypeImpl)result.getResult();
        
        assertEquals(asc.getHlrRecordType(), HlrRecordType.Detailed);
        assertEquals(asc.getTraceTypeInvokingEvent(), TraceTypeInvokingEvent.InvokingEvent_1);
        assertTrue(asc.isPriorityIndication());
    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(TraceTypeImpl.class);
    	
        TraceTypeImpl asc = new TraceTypeImpl(BssRecordType.Radio, MscRecordType.Detailed, TraceTypeInvokingEvent.InvokingEvent_3, false);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
        
        asc = new TraceTypeImpl(HlrRecordType.Detailed, TraceTypeInvokingEvent.InvokingEvent_1, true);

        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}