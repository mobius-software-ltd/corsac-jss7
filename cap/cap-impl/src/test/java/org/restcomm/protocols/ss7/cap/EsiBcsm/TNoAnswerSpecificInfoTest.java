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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Amit Bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class TNoAnswerSpecificInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 13, (byte) 159, 50, 0, (byte) 159, 52, 7, (byte) 128, (byte) 144, 17, 33, 34, 51, 3 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TNoAnswerSpecificInfoImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TNoAnswerSpecificInfoImpl);
        
        TNoAnswerSpecificInfoImpl elem = (TNoAnswerSpecificInfoImpl)result.getResult();        
        assertTrue(elem.getCallForwarded());
        assertEquals(elem.getForwardingDestinationNumber().getCalledPartyNumber().getAddress(), "111222333");
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TNoAnswerSpecificInfoImpl.class);
    	    	
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(0, "111222333", 1, 1);
        CalledPartyNumberIsupImpl forwardingDestinationNumber = new CalledPartyNumberIsupImpl(calledPartyNumber);
        TNoAnswerSpecificInfoImpl elem = new TNoAnswerSpecificInfoImpl(true, forwardingDestinationNumber);
        // boolean callForwarded, CalledPartyNumberCap forwardingDestinationNumber

        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
