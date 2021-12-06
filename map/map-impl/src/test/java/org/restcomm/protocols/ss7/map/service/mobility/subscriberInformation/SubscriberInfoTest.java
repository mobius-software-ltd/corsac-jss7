/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoise;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author abhayani
 * @author sergey vetyutnev
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
        assertTrue(Arrays.equals(locInfo.getGeographicalInformation().getData(), dataGeographicalInformation));
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
        assertTrue(Arrays.equals(subscriberInfo.getMSClassmark2().getData(), dataMsClassMark2));
        assertTrue(Arrays.equals(subscriberInfo.getGPRSMSClass().getMSNetworkCapability().getData(), dataMSNetworkCapability));
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
        GeographicalInformationImpl gi = new GeographicalInformationImpl(dataGeographicalInformation);
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
        MSClassmark2Impl msClassmark2 = new MSClassmark2Impl(dataMsClassMark2);
        MSNetworkCapabilityImpl mSNetworkCapability = new MSNetworkCapabilityImpl(dataMSNetworkCapability);
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