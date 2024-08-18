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

package org.restcomm.protocols.ss7.cap.isup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Test;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.OriginalCalledNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginalCalledNumber;

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
public class OriginalCalledNumberCapTest {

    public byte[] getData() {
        return new byte[] { 4, 6, (byte) 131, 20, 7, 1, 9, 0 };
    }

    public byte[] getIntData() {
        return new byte[] { (byte) 131, 20, 7, 1, 9, 0 };
    }

    public byte[] getData2() {
        return new byte[] { 4, 11, 4, 16, 76, (byte) 152, 8, (byte) 148, 113, 7, 41, (byte) 146, 115 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(OriginalCalledNumberIsupImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OriginalCalledNumberIsupImpl);
        
        OriginalCalledNumberIsupImpl elem = (OriginalCalledNumberIsupImpl)result.getResult();        
        OriginalCalledNumber ocn = elem.getOriginalCalledNumber();
        assertEquals(ocn.getNatureOfAddressIndicator(), 3);
        assertTrue(ocn.getAddress().equals("7010900"));
        assertEquals(ocn.getNumberingPlanIndicator(), 1);
        assertEquals(ocn.getAddressRepresentationRestrictedIndicator(), 1);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OriginalCalledNumberIsupImpl);
        
        elem = (OriginalCalledNumberIsupImpl)result.getResult();      
        ocn = elem.getOriginalCalledNumber();
        assertEquals(ocn.getNumberingPlanIndicator(), 1);
        assertEquals(ocn.getAddressRepresentationRestrictedIndicator(), 0);
        assertEquals(ocn.getNatureOfAddressIndicator(), 4);
        assertEquals(ocn.getAddress(), "c48980491770922937");
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(OriginalCalledNumberIsupImpl.class);
    	
        OriginalCalledNumber cpn = new OriginalCalledNumberImpl(3, "7010900", 1, 1);
        OriginalCalledNumberIsupImpl elem = new OriginalCalledNumberIsupImpl(cpn);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        cpn = new OriginalCalledNumberImpl(4, "c48980491770922937", 1, 0);
        elem = new OriginalCalledNumberIsupImpl(cpn);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int addressRepresentationREstrictedIndicator
    }
}
