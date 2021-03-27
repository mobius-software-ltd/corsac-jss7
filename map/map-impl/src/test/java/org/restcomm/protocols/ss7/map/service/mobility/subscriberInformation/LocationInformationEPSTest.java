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

import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgiImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TAIdImpl;
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
public class LocationInformationEPSTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 54, -128, 7, 11, 12, 13, 14, 15, 16, 17, -127, 5, 21, 22, 23, 24, 25, -125, 8, 31, 32, 33, 34,
                35, 36, 37, 38, -124, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -123, 0, -122, 1, 5, -121, 9, 41, 42, 43, 44, 45, 46,
                47, 48, 49 };
    }

    private byte[] getEncodedDataEUtranCgi() {
        return new byte[] { 11, 12, 13, 14, 15, 16, 17 };
    }

    private byte[] getTAId() {
        return new byte[] { 21, 22, 23, 24, 25 };
    }

    private byte[] getGeographicalInformation() {
        return new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 };
    }

    private byte[] getGeodeticInformation() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    private byte[] getDiameterIdentity() {
        return new byte[] { 41, 42, 43, 44, 45, 46, 47, 48, 49 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationInformationEPSImpl.class);
    	
        byte[] rawData = getEncodedData();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationInformationEPSImpl);
        LocationInformationEPSImpl impl = (LocationInformationEPSImpl)result.getResult();
        
        assertTrue(Arrays.equals(impl.getEUtranCellGlobalIdentity().getData(), this.getEncodedDataEUtranCgi()));
        assertTrue(Arrays.equals(impl.getTrackingAreaIdentity().getData(), this.getTAId()));
        assertTrue(Arrays.equals(impl.getGeographicalInformation().getData(), this.getGeographicalInformation()));
        assertTrue(Arrays.equals(impl.getGeodeticInformation().getData(), this.getGeodeticInformation()));
        assertTrue(impl.getCurrentLocationRetrieved());
        assertEquals((int) impl.getAgeOfLocationInformation(), 5);
        assertTrue(Arrays.equals(impl.getMmeName().getData(), this.getDiameterIdentity()));
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationInformationEPSImpl.class);
    	
        EUtranCgiImpl euc = new EUtranCgiImpl(this.getEncodedDataEUtranCgi());
        TAIdImpl ta = new TAIdImpl(this.getTAId());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(this.getGeographicalInformation());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(this.getGeodeticInformation());
        DiameterIdentityImpl di = new DiameterIdentityImpl(this.getDiameterIdentity());
        LocationInformationEPSImpl impl = new LocationInformationEPSImpl(euc, ta, null, ggi, gdi, true, 5, di);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}