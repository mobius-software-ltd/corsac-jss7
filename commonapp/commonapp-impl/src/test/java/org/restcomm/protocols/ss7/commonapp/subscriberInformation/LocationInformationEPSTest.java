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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.DiameterIdentityImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class LocationInformationEPSTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 54, -128, 7, 11, 12, 13, 14, 15, 16, 17, -127, 5, 21, 22, 23, 24, 25, -125, 8, 16, 32, 33, 34,
                35, 36, 37, 38, -124, 10, 1, 16, 3, 4, 5, 6, 7, 8, 9, 10, -123, 0, -122, 1, 5, -121, 9, 41, 42, 43, 44, 45, 46,
                47, 48, 49 };
    }

    private byte[] getEncodedDataEUtranCgi() {
        return new byte[] { 11, 12, 13, 14, 15, 16, 17 };
    }

    private byte[] getTAId() {
        return new byte[] { 21, 22, 23, 24, 25 };
    }

    private byte[] getGeographicalInformation() {
        return new byte[] { 16, 32, 33, 34, 35, 36, 37, 38 };
    }

    private byte[] getGeodeticInformation() {
        return new byte[] { 1, 16, 3, 4, 5, 6, 7, 8, 9, 10 };
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
        
        assertTrue(ByteBufUtil.equals(impl.getEUtranCellGlobalIdentity().getValue(), Unpooled.wrappedBuffer(this.getEncodedDataEUtranCgi())));
        assertTrue(ByteBufUtil.equals(impl.getTrackingAreaIdentity().getValue(), Unpooled.wrappedBuffer(this.getTAId())));
        assertTrue(ByteBufUtil.equals(impl.getGeographicalInformation().getValue(), Unpooled.wrappedBuffer(this.getGeographicalInformation())));
        assertTrue(ByteBufUtil.equals(impl.getGeodeticInformation().getValue(), Unpooled.wrappedBuffer(this.getGeodeticInformation())));
        assertTrue(impl.getCurrentLocationRetrieved());
        assertEquals((int) impl.getAgeOfLocationInformation(), 5);
        assertTrue(ByteBufUtil.equals(impl.getMmeName().getValue(), Unpooled.wrappedBuffer(this.getDiameterIdentity())));
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LocationInformationEPSImpl.class);
    	
        EUtranCgiImpl euc = new EUtranCgiImpl(Unpooled.wrappedBuffer(this.getEncodedDataEUtranCgi()));
        TAIdImpl ta = new TAIdImpl(Unpooled.wrappedBuffer(this.getTAId()));
        
        ByteBuf geoBuffer=Unpooled.wrappedBuffer(getGeographicalInformation());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(GeographicalInformationImpl.decodeTypeOfShape(geoBuffer.readByte() & 0x0FF), GeographicalInformationImpl.decodeLatitude(geoBuffer), GeographicalInformationImpl.decodeLongitude(geoBuffer), GeographicalInformationImpl.decodeUncertainty(geoBuffer.readByte() & 0x0FF));
        
        ByteBuf geodeticBuffer=Unpooled.wrappedBuffer(getGeodeticInformation());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(geodeticBuffer.readByte() & 0x0FF, GeographicalInformationImpl.decodeTypeOfShape(geodeticBuffer.readByte() & 0x0FF), GeographicalInformationImpl.decodeLatitude(geodeticBuffer), GeographicalInformationImpl.decodeLongitude(geodeticBuffer), GeographicalInformationImpl.decodeUncertainty(geodeticBuffer.readByte() & 0x0FF),geodeticBuffer.readByte() & 0x0FF);
        
        DiameterIdentityImpl di = new DiameterIdentityImpl(Unpooled.wrappedBuffer(this.getDiameterIdentity()));
        LocationInformationEPSImpl impl = new LocationInformationEPSImpl(euc, ta, null, ggi, gdi, true, 5, di);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}