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

package org.restcomm.protocols.ss7.commonapp.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class LegIDTest {

    private byte[] getData1() {
        return new byte[] { (byte) 128, 1, 2 };
    }

    private byte[] getData2() {
        return new byte[] { (byte) 129, 1, 1 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ReceivingLegIDImpl.class);
    	parser.loadClass(SendingLegIDImpl.class);
    	
        ByteBuf data = Unpooled.wrappedBuffer(this.getData1());
        ASNDecodeResult result=parser.decode(data);        
        SendingLegIDImpl legId = (SendingLegIDImpl)result.getResult();
        assertNotNull(legId.getSendingSideID());
        assertEquals(legId.getSendingSideID(), LegType.leg2);

        data =  Unpooled.wrappedBuffer(this.getData2());
        result=parser.decode(data);        
        ReceivingLegIDImpl rLegId=(ReceivingLegIDImpl)result.getResult();
        assertNotNull(rLegId.getReceivingSideID());
        assertEquals(rLegId.getReceivingSideID(), LegType.leg1);

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(ReceivingLegIDImpl.class);
    	parser.loadClass(SendingLegIDImpl.class);
    	        
    	SendingLegIDImpl legId = new SendingLegIDImpl(LegType.leg2);
        ByteBuf data=parser.encode(legId);
        byte[] encodedData=new byte[data.readableBytes()];
        data.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, this.getData1()));

        ReceivingLegIDImpl rLegId = new ReceivingLegIDImpl(LegType.leg1);
        data=parser.encode(rLegId);
        encodedData=new byte[data.readableBytes()];
        data.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, this.getData2()));
    }
}
