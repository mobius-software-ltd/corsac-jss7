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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;
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
public class MidCallEventsTest {

    public byte[] getData1() {
        return new byte[] { 48, 7, (byte) 131, 5, 99, 1, 2, 3, 4 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 7, (byte) 132, 5, 99, 1, 2, 3, 4 };
    }

    public byte[] getDigitsData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(MidCallEventsWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MidCallEventsWrapperImpl);
        
        MidCallEventsWrapperImpl elem = (MidCallEventsWrapperImpl)result.getResult();        
        assertEquals(elem.getMidCallEventsWrapper().getDTMFDigitsCompleted().getGenericDigits().getEncodingScheme(), GenericDigits._ENCODING_SCHEME_BINARY);
        ByteBuf buffer=elem.getMidCallEventsWrapper().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits();
        assertNotNull(buffer);
        byte[] data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertArrayEquals(data, getDigitsData());
        assertNull(elem.getMidCallEventsWrapper().getDTMFDigitsTimeOut());


        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MidCallEventsWrapperImpl);
        
        elem = (MidCallEventsWrapperImpl)result.getResult();    
        assertEquals(elem.getMidCallEventsWrapper().getDTMFDigitsTimeOut().getGenericDigits().getEncodingScheme(), GenericDigits._ENCODING_SCHEME_BINARY);
        buffer=elem.getMidCallEventsWrapper().getDTMFDigitsTimeOut().getGenericDigits().getEncodedDigits();
        assertNotNull(buffer);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertArrayEquals(data, getDigitsData());
        assertNull(elem.getMidCallEventsWrapper().getDTMFDigitsCompleted());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(MidCallEventsWrapperImpl.class);
    	
        GenericDigitsImpl genericDigits = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BINARY, GenericDigits._TOD_BGCI, Unpooled.wrappedBuffer(getDigitsData()));
        // int encodingScheme, int typeOfDigits, byte[] digits
        DigitsIsupImpl dtmfDigits = new DigitsIsupImpl(genericDigits);
        MidCallEventsImpl elem = new MidCallEventsImpl(dtmfDigits, true);
        MidCallEventsWrapperImpl wrapper=new MidCallEventsWrapperImpl(elem);
//        Digits dtmfDigits, boolean isDtmfDigitsCompleted
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new MidCallEventsImpl(dtmfDigits, false);
        wrapper=new MidCallEventsWrapperImpl(elem);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
