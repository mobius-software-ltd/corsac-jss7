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
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;

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
public class LocationNumberCapTest {

    public byte[] getData() {
        return new byte[] { 4, 8, (byte) 132, (byte) 151, 8, 2, (byte) 151, 1, 32, 0 };
    }

    public byte[] getIntData() {
        return new byte[] { (byte) 132, (byte) 151, 8, 2, (byte) 151, 1, 32, 0 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(LocationNumberIsupImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationNumberIsupImpl);
        
        LocationNumberIsupImpl elem = (LocationNumberIsupImpl)result.getResult();         
        LocationNumber ln = elem.getLocationNumber();
        assertEquals(ln.getNatureOfAddressIndicator(), 4);
        assertTrue(ln.getAddress().equals("80207910020"));
        assertEquals(ln.getNumberingPlanIndicator(), 1);
        assertEquals(ln.getInternalNetworkNumberIndicator(), 1);
        assertEquals(ln.getAddressRepresentationRestrictedIndicator(), 1);
        assertEquals(ln.getScreeningIndicator(), 3);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(LocationNumberIsupImpl.class);
    	
        LocationNumber cpn = new LocationNumberImpl(4, "80207910020", 1, 1, 1, 3);
        LocationNumberIsupImpl elem = new LocationNumberIsupImpl(cpn);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator, int
        // addressRepresentationREstrictedIndicator,
        // int screeningIndicator
    }
}
