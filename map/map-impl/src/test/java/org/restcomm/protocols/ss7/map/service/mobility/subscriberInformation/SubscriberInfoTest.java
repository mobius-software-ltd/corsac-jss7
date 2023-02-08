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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberStateChoice;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GPRSMSClassImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.MSClassmark2Impl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.MSNetworkCapabilityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.SubscriberStateImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoise;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * @author abhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SubscriberInfoTest {

    // Real Trace
    byte[] data = new byte[] { (byte) 0x30, 0x32, (byte) 0xa0, 0x2c, 0x02, 0x01, 0x01, (byte) 0x80, 0x08, 0x10, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x81, 0x07, (byte) 0x91, 0x55, 0x43, 0x69, 0x26, (byte) 0x99, 0x01,
            (byte) 0xa3, 0x09, (byte) 0x80, 0x07, 0x27, (byte) 0xf4, 0x43, 0x79, (byte) 0x9e, 0x29, (byte) 0xa0, (byte) 0x86,
            0x07, (byte) 0x91, 0x55, 0x43, 0x69, 0x26, (byte) 0x99, 0x01, (byte) 0x89, 0x00, (byte) 0xa1, 0x02, (byte) 0x80,
            0x00 };
    byte[] dataGeographicalInformation = new byte[] { 16, 0, 0, 0, 0, 0, 0, 0 };
    byte[] dataFull = new byte[] { 48, 59, -96, 9, -127, 7, -111, 85, 68, 51, 34, 17, 0, -95, 2, -127, 0, -93, 9, -96, 7, -127,
            5, 98, -16, 17, 0, -112, -92, 2, -128, 0, -123, 8, 17, 34, 51, 68, 85, 102, 119, -120, -122, 3, 11, 12, 13, -89, 3,
            -128, 1, 12, -88, 7, -127, 5, 84, 118, 120, 86, -12 };
    byte[] dataMsClassMark2 = new byte[] { 11, 12, 13 };
    byte[] dataMSNetworkCapability = new byte[] { 12 };

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SubscriberInfoImpl.class);

    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SubscriberInfoImpl);
        SubscriberInfoImpl subscriberInfo = (SubscriberInfoImpl)result.getResult();

        LocationInformation locInfo = subscriberInfo.getLocationInformation();
        assertNotNull(locInfo);
        assertEquals((int) locInfo.getAgeOfLocationInformation(), 1);
        assertTrue(ByteBufUtil.equals(locInfo.getGeographicalInformation().getValue(),Unpooled.wrappedBuffer(dataGeographicalInformation)));
        ISDNAddressString vlrN = locInfo.getVlrNumber();
        assertTrue(vlrN.getAddress().equals("553496629910"));
        assertEquals(vlrN.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrN.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 724);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 34);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 31134);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength()
                .getCellIdOrServiceAreaCode(), 10656);
        ISDNAddressString mscN = locInfo.getVlrNumber();
        assertTrue(mscN.getAddress().equals("553496629910"));
        assertEquals(mscN.getAddressNature(), AddressNature.international_number);
        assertEquals(mscN.getNumberingPlan(), NumberingPlan.ISDN);
        assertFalse(locInfo.getCurrentLocationRetrieved());
        assertTrue(locInfo.getSaiPresent());

        SubscriberState subState = subscriberInfo.getSubscriberState();
        assertEquals(subState.getSubscriberStateChoice(), SubscriberStateChoice.assumedIdle);

        result=parser.decode(Unpooled.wrappedBuffer(dataFull));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SubscriberInfoImpl);
        subscriberInfo = (SubscriberInfoImpl)result.getResult();

        locInfo = subscriberInfo.getLocationInformation();
        vlrN = locInfo.getVlrNumber();
        assertTrue(vlrN.getAddress().equals("554433221100"));
        subState = subscriberInfo.getSubscriberState();
        assertEquals(subState.getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
        LAIFixedLength lai = subscriberInfo.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI()
                .getLAIFixedLength();
        assertEquals(lai.getMCC(), 260);
        assertEquals(lai.getMNC(), 11);
        assertEquals(lai.getLac(), 144);
        assertEquals(subscriberInfo.getPSSubscriberState().getChoice(), PSSubscriberStateChoise.notProvidedFromSGSNorMME);
        assertTrue(subscriberInfo.getIMEI().getIMEI().equals("1122334455667788"));
        assertTrue(ByteBufUtil.equals(subscriberInfo.getMSClassmark2().getValue(), Unpooled.wrappedBuffer(dataMsClassMark2)));
        assertTrue(ByteBufUtil.equals(subscriberInfo.getGPRSMSClass().getMSNetworkCapability().getValue(),Unpooled.wrappedBuffer(dataMSNetworkCapability)));
        assertTrue(subscriberInfo.getMNPInfoRes().getIMSI().getData().equals("456787654"));
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SubscriberInfoImpl.class);
        
        ISDNAddressStringImpl vlrN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553496629910");
        ISDNAddressStringImpl mscN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553496629910");
        CellGlobalIdOrServiceAreaIdFixedLengthImpl c0 = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(724, 34, 31134, 10656);
        CellGlobalIdOrServiceAreaIdOrLAIImpl c = new CellGlobalIdOrServiceAreaIdOrLAIImpl(c0);
        
        ByteBuf geoBuffer=Unpooled.wrappedBuffer(dataGeographicalInformation);
        GeographicalInformationImpl gi = new GeographicalInformationImpl(GeographicalInformationImpl.decodeTypeOfShape(geoBuffer.readByte() & 0x0FF), GeographicalInformationImpl.decodeLatitude(geoBuffer), GeographicalInformationImpl.decodeLongitude(geoBuffer), GeographicalInformationImpl.decodeUncertainty(geoBuffer.readByte() & 0x0FF));
        
        LocationInformationImpl li = new LocationInformationImpl(1, gi, vlrN, null, c, null, null, mscN, null, false, true,
                null, null);
        SubscriberStateImpl ss = new SubscriberStateImpl(SubscriberStateChoice.assumedIdle, null);

        SubscriberInfoImpl impl = new SubscriberInfoImpl(li, ss, null, null, null, null, null, null, null);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = data;
        assertTrue(Arrays.equals(rawData, encodedData));

        vlrN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "554433221100");
        li = new LocationInformationImpl(null, null, vlrN, null, null, null, null, null, null, false, false, null, null);
        ss = new SubscriberStateImpl(SubscriberStateChoice.camelBusy, null);
        LAIFixedLengthImpl laiFixedLength = new LAIFixedLengthImpl(260, 11, 144);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cg = new CellGlobalIdOrServiceAreaIdOrLAIImpl(laiFixedLength);
        LocationInformationGPRSImpl liGprs = new LocationInformationGPRSImpl(cg, null, null, null, null, null, false, null,
                false, null);
        PSSubscriberStateImpl psSubscriberState = new PSSubscriberStateImpl(PSSubscriberStateChoise.notProvidedFromSGSNorMME,
                null, null);
        IMEIImpl imei = new IMEIImpl("1122334455667788");
        MSClassmark2Impl msClassmark2 = new MSClassmark2Impl(Unpooled.wrappedBuffer(dataMsClassMark2));
        MSNetworkCapabilityImpl mSNetworkCapability = new MSNetworkCapabilityImpl(Unpooled.wrappedBuffer(dataMSNetworkCapability));
        GPRSMSClassImpl gprsMSClass = new GPRSMSClassImpl(mSNetworkCapability, null);
        IMSIImpl imsi2 = new IMSIImpl("456787654");
        MNPInfoResImpl mnpInfoRes = new MNPInfoResImpl(null, imsi2, null, null, null);

        impl = new SubscriberInfoImpl(li, ss, null, liGprs, psSubscriberState, imei, msClassmark2, gprsMSClass, mnpInfoRes);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = dataFull;
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}