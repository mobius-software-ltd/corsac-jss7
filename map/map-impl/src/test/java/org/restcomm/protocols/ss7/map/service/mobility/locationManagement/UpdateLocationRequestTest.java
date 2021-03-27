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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LACImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.LocationAreaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingAreaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapabilityImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
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
public class UpdateLocationRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 25, 4, 5, 17, 17, 33, 34, 34, -127, 4, -111, 34, 34, -8, 4, 4, -111, 34, 34, -7, -90, 4, -123, 2, 7, -128 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 86, 4, 5, 17, 17, 33, 34, 51, -127, 4, -111, 34, 34, -8, 4, 4, -111, 34, 34, -7, -118, 4, 1, 3, 5, 8, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -90, 4, -123, 2, 7, -128, -117, 0, -116, 0, -113, 0, -112, 0 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48, 65, 4, 5, 17, 17, 33, 34, 51, -127, 4, -111, 34, 34, -8, 4, 4, -111, 34, 34, -7, -118, 4, 1, 3, 5, 8, -90, 4, -123, 2, 7, -128, -117, 0, -116, 0, -126, 6, 1, 1, 1, 1, 1, 1, -83, 10, -128, 8, 33, 67, 101, -121, 9, -112, 120, -10, -82, 4, -127, 2, 0, 123, -113, 0, -112, 0 };
    }

    private byte[] getEncodedData_V1() {
        return new byte[] { 48, 19, 4, 5, 17, 17, 33, 34, 51, -128, 4, -111, 34, 34, -16, 4, 4, -111, 34, 34, -15 };
    }

    private byte[] getLmsiData() {
        return new byte[] { 1, 3, 5, 8 };
    }

    private byte[] getGSNAddressData() {
        return new byte[] { 1, 1, 1, 1, 1, 1 };
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
        
        IMSIImpl imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("1111122222"));

        assertNull(asc.getRoamingNumber());
        ISDNAddressStringImpl mscNumber = asc.getMscNumber();
        assertTrue(mscNumber.getAddress().equals("22228"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        ISDNAddressStringImpl vlrNumber = asc.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22229"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        VLRCapabilityImpl vlrCap = asc.getVlrCapability();
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

        assertTrue(Arrays.equals(asc.getLmsi().getData(), getLmsiData()));
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

        assertTrue(Arrays.equals(asc.getLmsi().getData(), getLmsiData()));
        assertNull(asc.getExtensionContainer());

        assertTrue(asc.getInformPreviousNetworkEntity());
        assertTrue(asc.getCsLCSNotSupportedByUE());
        assertTrue(asc.getSkipSubscriberDataUpdate());
        assertTrue(asc.getRestorationIndicator());

        assertTrue(Arrays.equals(asc.getVGmlcAddress().getData(), getGSNAddressData()));
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
        ISDNAddressStringImpl roamingNumber = asc.getRoamingNumber();
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
        UpdateLocationRequestImpl asc = new UpdateLocationRequestImpl(3, imsi, mscNumber, null, vlrNumber, null, null, vlrCap,
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
        LMSIImpl lmsi = new LMSIImpl(getLmsiData());
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        asc = new UpdateLocationRequestImpl(3, imsi, mscNumber, null, vlrNumber, lmsi, extensionContainer, vlrCap, true, true,
                null, null, null, true, true);

        data=getEncodedData2();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        GSNAddressImpl vGmlcAddress = new GSNAddressImpl(getGSNAddressData());
        IMEIImpl imeisv = new IMEIImpl("123456789009876");
        ADDInfoImpl addInfo = new ADDInfoImpl(imeisv, false);
        ArrayList<LocationAreaImpl> locationAreas = new ArrayList<LocationAreaImpl>();
        LACImpl lac = new LACImpl(123);
        LocationAreaImpl la = new LocationAreaImpl(lac);
        locationAreas.add(la);
        PagingAreaImpl pagingArea = new PagingAreaImpl(locationAreas);
        asc = new UpdateLocationRequestImpl(3, imsi, mscNumber, null, vlrNumber, lmsi, null, vlrCap, true, true, vGmlcAddress,
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
        asc = new UpdateLocationRequestImpl(1, imsi, null, roamingNumberNumber, vlrNumber, null, null, null, false, false,
                null, null, null, false, false);

        data=getEncodedData_V1();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}