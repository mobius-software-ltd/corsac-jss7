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

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtTeleserviceCodeImpl;
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
public class TServiceChangeSpecificInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 5, (byte) 160, 3, (byte) 131, 1, 99 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TServiceChangeSpecificInfoImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TServiceChangeSpecificInfoImpl);
        
        TServiceChangeSpecificInfoImpl elem = (TServiceChangeSpecificInfoImpl)result.getResult();        
        assertEquals(elem.getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.facsimileGroup4);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TServiceChangeSpecificInfoImpl.class);
    	
    	ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.facsimileGroup4);
        ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(extTeleservice);
        TServiceChangeSpecificInfoImpl elem = new TServiceChangeSpecificInfoImpl(extBasicServiceCode);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
