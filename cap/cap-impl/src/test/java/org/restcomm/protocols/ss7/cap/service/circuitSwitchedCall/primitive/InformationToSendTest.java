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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InbandInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InformationToSendImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InformationToSendWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MessageIDImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ToneImpl;
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
public class InformationToSendTest {

    public byte[] getData1() {
        return new byte[] { 48, 10, (byte) 160, 8, (byte) 160, 3, (byte) 128, 1, 91, (byte) 129, 1, 1 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 8, (byte) 161, 6, (byte) 128, 1, 5, (byte) 129, 1, 100 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InformationToSendWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InformationToSendWrapperImpl);
        
        InformationToSendWrapperImpl elem = (InformationToSendWrapperImpl)result.getResult();                
        assertEquals((int) elem.getInformationToSend().getInbandInfo().getMessageID().getElementaryMessageID(), 91);
        assertEquals((int) elem.getInformationToSend().getInbandInfo().getNumberOfRepetitions(), 1);
        assertNull(elem.getInformationToSend().getInbandInfo().getDuration());
        assertNull(elem.getInformationToSend().getInbandInfo().getInterval());
        assertNull(elem.getInformationToSend().getTone());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InformationToSendWrapperImpl);
        
        elem = (InformationToSendWrapperImpl)result.getResult(); 
        assertNull(elem.getInformationToSend().getInbandInfo());
        assertEquals(elem.getInformationToSend().getTone().getToneID(), 5);
        assertEquals((int) elem.getInformationToSend().getTone().getDuration(), 100);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InformationToSendWrapperImpl.class);
    	
        MessageIDImpl messageID = new MessageIDImpl(91);
        InbandInfoImpl inbandInfo = new InbandInfoImpl(messageID, 1, null, null);
        // MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval
        InformationToSendImpl elem = new InformationToSendImpl(inbandInfo);
        InformationToSendWrapperImpl wrapper = new InformationToSendWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        ToneImpl tone = new ToneImpl(5, 100);
        // int toneID, Integer duration
        elem = new InformationToSendImpl(tone);
        wrapper = new InformationToSendWrapperImpl(elem);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}