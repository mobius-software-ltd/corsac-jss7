/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationNumberMapTest {

    private byte[] getData() {
        return new byte[] { 4, 8, -125, -63, 8, 2, -105, 1, 32, 0 };
    }

    private byte[] getIntData() {
        return new byte[] { -125, -63, 8, 2, -105, 1, 32, 0 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationNumberMapImpl.class);
    	            	
        byte[] rawData = getData();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationNumberMapImpl);
        LocationNumberMapImpl impl = (LocationNumberMapImpl)result.getResult();
        
        LocationNumber ln = impl.getLocationNumber();

        assertTrue(Arrays.equals(impl.getData(), this.getIntData()));
        assertEquals(ln.getNatureOfAddressIndicator(), LocationNumber._NAI_NATIONAL_SN);
        assertTrue(ln.getAddress().equals("80207910020"));
        assertEquals(ln.getNumberingPlanIndicator(), LocationNumber._NPI_TELEX);
        assertEquals(ln.getInternalNetworkNumberIndicator(), LocationNumber._INN_ROUTING_NOT_ALLOWED);
        assertEquals(ln.getAddressRepresentationRestrictedIndicator(), LocationNumber._APRI_ALLOWED);
        assertEquals(ln.getScreeningIndicator(), LocationNumber._SI_USER_PROVIDED_VERIFIED_PASSED);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationNumberMapImpl.class);
        
        LocationNumberMapImpl impl = new LocationNumberMapImpl(this.getIntData());
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getData();
        assertTrue(Arrays.equals(rawData, encodedData));

        LocationNumberImpl ln = new LocationNumberImpl(LocationNumber._NAI_NATIONAL_SN, "80207910020",
                LocationNumber._NPI_TELEX, LocationNumber._INN_ROUTING_NOT_ALLOWED, LocationNumber._APRI_ALLOWED,
                LocationNumber._SI_USER_PROVIDED_VERIFIED_PASSED);
        impl = new LocationNumberMapImpl(ln);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}