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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ChargeIndicatorValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtTeleserviceCodeImpl;
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
public class TAnswerSpecificInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 25, (byte) 159, 50, 7, (byte) 128, (byte) 144, 17, 33, 34, 51, 3, (byte) 159, 52, 0, (byte) 191, 54, 3, (byte) 131, 1, 16,
                (byte) 191, 55, 3, (byte) 131, 1, 32 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 17, (byte) 159, 50, 7, (byte) 128, (byte) 144, 17, 33, 34, 51, 3, (byte) 159, 51, 0, (byte) 159, 53, 1, 1 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TAnswerSpecificInfoImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TAnswerSpecificInfoImpl);
        
        TAnswerSpecificInfoImpl elem = (TAnswerSpecificInfoImpl)result.getResult();        
        assertEquals(elem.getDestinationAddress().getCalledPartyNumber().getAddress(), "111222333");
        assertFalse(elem.getOrCall());
        assertTrue(elem.getForwardedCall());
        assertNull(elem.getChargeIndicator());
        assertEquals(elem.getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);
        assertEquals(elem.getExtBasicServiceCode2().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allShortMessageServices);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TAnswerSpecificInfoImpl);
        
        elem = (TAnswerSpecificInfoImpl)result.getResult();    
        assertEquals(elem.getDestinationAddress().getCalledPartyNumber().getAddress(), "111222333");
        assertTrue(elem.getOrCall());
        assertFalse(elem.getForwardedCall());
        assertEquals(elem.getChargeIndicator().getChargeIndicatorValue(), ChargeIndicatorValue.noCharge);
        assertNull(elem.getExtBasicServiceCode());
        assertNull(elem.getExtBasicServiceCode2());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TAnswerSpecificInfoImpl.class);
    	
    	CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(0, "111222333", 1, 1);
        CalledPartyNumberIsupImpl forwardingDestinationNumber = new CalledPartyNumberIsupImpl(calledPartyNumber);
        ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
        ExtTeleserviceCodeImpl extTeleservice2 = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allShortMessageServices);

        ExtBasicServiceCodeImpl extBasicSer = new ExtBasicServiceCodeImpl(extTeleservice);
        ExtBasicServiceCodeImpl extBasicSer2 = new ExtBasicServiceCodeImpl(extTeleservice2);

        TAnswerSpecificInfoImpl elem = new TAnswerSpecificInfoImpl(forwardingDestinationNumber, false, true, null,
                extBasicSer, extBasicSer2);
//        CalledPartyNumberCap destinationAddress, boolean orCall, boolean forwardedCall,
//        ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode, ExtBasicServiceCode extBasicServiceCode2
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        ChargeIndicatorImpl chargeIndicator = new ChargeIndicatorImpl(ChargeIndicatorValue.noCharge);
        elem = new TAnswerSpecificInfoImpl(forwardingDestinationNumber, true, false, chargeIndicator,
                null, null);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
