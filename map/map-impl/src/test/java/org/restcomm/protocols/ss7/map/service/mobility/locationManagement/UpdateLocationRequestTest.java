/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LocationArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
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
public class UpdateLocationRequestTest {

	private byte[] getEncodedData() {
        return new byte[] { 48, 25, 4, 5, 17, 17, 33, 34, 34, -127, 4, -111, 34, 34, -8, 4, 4, -111, 34, 34, -7, -90, 4, -123,
                2, 6, -128 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 80, 4, 5, 17, 17, 33, 34, 51, -127, 4, -111, 34, 34, -8, 4, 4, -111, 34, 34, -7, -118, 4, 1, 3,
                5, 8, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3,
                5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -90, 4, -123, 2, 6, -128, -117, 0, -116, 0, -113, 0, -112, 0 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48, 64, 4, 5, 17, 17, 33, 34, 51, -127, 4, -111, 34, 34, -8, 4, 4, -111, 34, 34, -7, -118, 4, 1, 3,
                5, 8, -90, 4, -123, 2, 6, -128, -117, 0, -116, 0, -126, 5, 4, 1, 1, 1, 1, -83, 10, -128, 8, 33, 67, 101,
                -121, 9, -112, 120, -10, -82, 4, -127, 2, 0, 123, -113, 0, -112, 0 };
    }

    private byte[] getEncodedData_V1() {
        return new byte[] { 48, 19, 4, 5, 17, 17, 33, 34, 51, -128, 4, -111, 34, 34, -16, 4, 4, -111, 34, 34, -15 };
    }

    private byte[] getLmsiData() {
        return new byte[] { 1, 3, 5, 8 };
    }

    private byte[] getGSNAddressData() {
        return new byte[] { 1, 1, 1, 1 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(UpdateLocationRequestImpl.class);
    	
    	byte[] data = this.getEncodedData();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof UpdateLocationRequestImpl);
        UpdateLocationRequestImpl asc = (UpdateLocationRequestImpl)result.getResult();
        
        IMSI imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("1111122222"));

        assertNull(asc.getRoamingNumber());
        ISDNAddressString mscNumber = asc.getMscNumber();
        assertTrue(mscNumber.getAddress().equals("22228"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        ISDNAddressString vlrNumber = asc.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22229"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        VLRCapability vlrCap = asc.getVlrCapability();
        assertTrue(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertFalse(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());

        assertNull(asc.getLmsi());
        assertNull(asc.getExtensionContainer());

        assertFalse(asc.getInformPreviousNetworkEntity());
        assertFalse(asc.getCsLCSNotSupportedByUE());
        assertFalse(asc.getSkipSubscriberDataUpdate());
        assertFalse(asc.getRestorationIndicator());

        data = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof UpdateLocationRequestImpl);
        asc = (UpdateLocationRequestImpl)result.getResult();
        
        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("1111122233"));

        assertNull(asc.getRoamingNumber());
        mscNumber = asc.getMscNumber();
        assertTrue(mscNumber.getAddress().equals("22228"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        vlrNumber = asc.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22229"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        vlrCap = asc.getVlrCapability();
        assertTrue(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertFalse(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());

        assertTrue(ByteBufUtil.equals(asc.getLmsi().getValue(),Unpooled.wrappedBuffer(getLmsiData())));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));

        assertTrue(asc.getInformPreviousNetworkEntity());
        assertTrue(asc.getCsLCSNotSupportedByUE());
        assertTrue(asc.getSkipSubscriberDataUpdate());
        assertTrue(asc.getRestorationIndicator());

        data = getEncodedData3();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof UpdateLocationRequestImpl);
        asc = (UpdateLocationRequestImpl)result.getResult();

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("1111122233"));

        assertNull(asc.getRoamingNumber());
        mscNumber = asc.getMscNumber();
        assertTrue(mscNumber.getAddress().equals("22228"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        vlrNumber = asc.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22229"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        vlrCap = asc.getVlrCapability();
        assertTrue(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertFalse(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());

        assertTrue(ByteBufUtil.equals(asc.getLmsi().getValue(),Unpooled.wrappedBuffer(getLmsiData())));
        assertNull(asc.getExtensionContainer());

        assertTrue(asc.getInformPreviousNetworkEntity());
        assertTrue(asc.getCsLCSNotSupportedByUE());
        assertTrue(asc.getSkipSubscriberDataUpdate());
        assertTrue(asc.getRestorationIndicator());

        assertEquals(asc.getVGmlcAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(asc.getVGmlcAddress().getGSNAddressData(),Unpooled.wrappedBuffer(getGSNAddressData())));
        
        assertTrue(asc.getADDInfo().getImeisv().getIMEI().equals("123456789009876"));
        assertFalse(asc.getADDInfo().getSkipSubscriberDataUpdate());
        assertEquals(asc.getPagingArea().getLocationAreas().size(), 1);
        assertEquals(asc.getPagingArea().getLocationAreas().get(0).getLAC().getLac(), 123);

        data = getEncodedData_V1();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof UpdateLocationRequestImpl);
        asc = (UpdateLocationRequestImpl)result.getResult();

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("1111122233"));

        assertNull(asc.getMscNumber());
        ISDNAddressString roamingNumber = asc.getRoamingNumber();
        assertTrue(roamingNumber.getAddress().equals("22220"));
        assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);

        vlrNumber = asc.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22221"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertNull(asc.getVlrCapability());
        assertNull(asc.getLmsi());
        assertNull(asc.getExtensionContainer());
        assertFalse(asc.getInformPreviousNetworkEntity());
        assertFalse(asc.getCsLCSNotSupportedByUE());
        assertFalse(asc.getSkipSubscriberDataUpdate());
        assertFalse(asc.getRestorationIndicator());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(UpdateLocationRequestImpl.class);
    	
        IMSIImpl imsi = new IMSIImpl("1111122222");
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        ISDNAddressStringImpl vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22229");
        SupportedLCSCapabilitySetsImpl supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, false, false, false,
                false);
        VLRCapabilityImpl vlrCap = new VLRCapabilityImpl(null, null, false, null, null, false, supportedLCSCapabilitySets, null,
                null, false, false);
        UpdateLocationRequestImpl asc = new UpdateLocationRequestImpl(imsi, mscNumber, null, vlrNumber, null, null, vlrCap,
                false, false, null, null, null, false, false);
        
        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        imsi = new IMSIImpl("1111122233");
        mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22229");
        supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, false, false, false, false);
        vlrCap = new VLRCapabilityImpl(null, null, false, null, null, false, supportedLCSCapabilitySets, null, null, false,
                false);
        LMSIImpl lmsi = new LMSIImpl(Unpooled.wrappedBuffer(getLmsiData()));
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        asc = new UpdateLocationRequestImpl(imsi, mscNumber, null, vlrNumber, lmsi, extensionContainer, vlrCap, true, true,
                null, null, null, true, true);

        data=getEncodedData2();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        GSNAddressImpl vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4,Unpooled.wrappedBuffer(getGSNAddressData()));
        IMEIImpl imeisv = new IMEIImpl("123456789009876");
        ADDInfoImpl addInfo = new ADDInfoImpl(imeisv, false);
        List<LocationArea> locationAreas = new ArrayList<LocationArea>();
        LACImpl lac = new LACImpl(123);
        LocationAreaImpl la = new LocationAreaImpl(lac);
        locationAreas.add(la);
        PagingArea pagingArea = new PagingAreaImpl(locationAreas);
        asc = new UpdateLocationRequestImpl(imsi, mscNumber, null, vlrNumber, lmsi, null, vlrCap, true, true, vGmlcAddress,
                addInfo, pagingArea, true, true);

        data=getEncodedData3();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        imsi = new IMSIImpl("1111122233");
        ISDNAddressStringImpl roamingNumberNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22220");
        vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22221");
        asc = new UpdateLocationRequestImpl(imsi, null, roamingNumberNumber, vlrNumber, null, null, null, false, false,
                null, null, null, false, false);

        data=getEncodedData_V1();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}