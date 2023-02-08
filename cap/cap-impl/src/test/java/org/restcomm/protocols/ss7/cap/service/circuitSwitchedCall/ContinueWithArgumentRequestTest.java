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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.commonapp.api.isup.GenericNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingLevel;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BothwayThroughConnectionInd;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ContinueWithArgumentArgExtensionImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.NAOliInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartysCategoryIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.GenericNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CallingPartyCategoryImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyCategory;
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
public class ContinueWithArgumentRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 49, (byte) 129, 3, 0, 0, 2, (byte) 166, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255,
                (byte) 167, 3, (byte) 130, 1, 1, (byte) 140, 1, 4, (byte) 176, 8, 4, 6, 0, 0, 0, 17, 33, 34, (byte) 145, 4, 1, 2, 3, 4 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 84, (byte) 129, 3, 0, 0, 2, (byte) 166, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255,
                (byte) 167, 3, (byte) 130, 1, 1, (byte) 140, 1, 4, (byte) 176, 8, 4, 6, 0, 0, 0, 17, 33, 34, (byte) 145, 4, 1, 2, 3, 4, (byte) 146, 0,
                (byte) 159, 50, 5, 0, 0, 34, 50, 51, (byte) 159, 52, 4, 5, 6, 7, 8, (byte) 159, 55, 0, (byte) 159, 56, 1, 11, (byte) 159, 57, 0, (byte) 159,
                58, 0, (byte) 191, 59, 2, (byte) 128, 0 };
    }

    public byte[] getCUGInterlock() {
        return new byte[] { 1, 2, 3, 4 };
    }

    public byte[] getCarrier() {
        return new byte[] { 5, 6, 7, 8 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ContinueWithArgumentRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ContinueWithArgumentRequestImpl);
        
        ContinueWithArgumentRequestImpl elem = (ContinueWithArgumentRequestImpl)result.getResult();        
        assertEquals(elem.getAlertingPattern().getAlertingLevel(), AlertingLevel.Level2);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getBothwayThroughConnectionInd(), BothwayThroughConnectionInd.bothwayPathNotRequired);
        assertEquals(elem.getCallingPartysCategory().getCallingPartyCategory().getCallingPartyCategory(), CallingPartyCategory._CATEGORY_OL_RUSSIAN);
        assertEquals(elem.getGenericNumbers().size(), 1);
        GenericNumberIsup gn = elem.getGenericNumbers().get(0);
        assertEquals(gn.getGenericNumber().getAddress(), "111222");
        assertTrue(ByteBufUtil.equals(elem.getCugInterlock().getValue(),Unpooled.wrappedBuffer(getCUGInterlock())));
        assertFalse(elem.getCugOutgoingAccess());
        assertNull(elem.getChargeNumber());
        assertNull(elem.getCarrier());
        assertFalse(elem.getSuppressionOfAnnouncement());
        assertNull(elem.getNaOliInfo());
        assertFalse(elem.getBorInterrogationRequested());
        assertFalse(elem.getSuppressOCsi());
        assertNull(elem.getContinueWithArgumentArgExtension());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ContinueWithArgumentRequestImpl);
        
        elem = (ContinueWithArgumentRequestImpl)result.getResult();  
        assertEquals(elem.getAlertingPattern().getAlertingLevel(), AlertingLevel.Level2);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getBothwayThroughConnectionInd(), BothwayThroughConnectionInd.bothwayPathNotRequired);
        assertEquals(elem.getCallingPartysCategory().getCallingPartyCategory().getCallingPartyCategory(), CallingPartyCategory._CATEGORY_OL_RUSSIAN);
        assertEquals(elem.getGenericNumbers().size(), 1);
        gn = elem.getGenericNumbers().get(0);
        assertEquals(gn.getGenericNumber().getAddress(), "111222");
        assertTrue(ByteBufUtil.equals(elem.getCugInterlock().getValue(), Unpooled.wrappedBuffer(getCUGInterlock())));
        assertTrue(elem.getCugOutgoingAccess());
        assertEquals(elem.getChargeNumber().getLocationNumber().getAddress(), "222333");
        assertTrue(ByteBufUtil.equals(elem.getCarrier().getValue(),Unpooled.wrappedBuffer(getCarrier())));
        assertTrue(elem.getSuppressionOfAnnouncement());
        assertEquals((int) elem.getNaOliInfo().getData(), 11);
        assertTrue(elem.getBorInterrogationRequested());
        assertTrue(elem.getSuppressOCsi());
        assertTrue(elem.getContinueWithArgumentArgExtension().getSuppressDCsi());
        assertFalse(elem.getContinueWithArgumentArgExtension().getSuppressNCsi());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ContinueWithArgumentRequestImpl.class);
    	
        CallingPartyCategoryImpl callingPartyCategory = new CallingPartyCategoryImpl();
        callingPartyCategory.setCallingPartyCategory(CallingPartyCategory._CATEGORY_OL_RUSSIAN);
        CallingPartysCategoryIsupImpl callingPartysCategory = new CallingPartysCategoryIsupImpl(callingPartyCategory);
        ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl(null, null,
                BothwayThroughConnectionInd.bothwayPathNotRequired, null, false, null, null, null);
        AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingLevel.Level2);
        List<GenericNumberIsup> genericNumbers = new ArrayList<GenericNumberIsup>();
        GenericNumberImpl genericNumber = new GenericNumberImpl();
        genericNumber.setAddress("111222");
        GenericNumberIsupImpl gn = new GenericNumberIsupImpl(genericNumber);
        genericNumbers.add(gn);
        CUGInterlockImpl cugInterlock = new CUGInterlockImpl(Unpooled.wrappedBuffer(getCUGInterlock()));

        ContinueWithArgumentRequestImpl elem = new ContinueWithArgumentRequestImpl(alertingPattern, CAPExtensionsTest.createTestCAPExtensions(),
                serviceInteractionIndicatorsTwo, callingPartysCategory, genericNumbers, cugInterlock, false, null, null, false, null, false, false, null);

        // AlertingPatternCap alertingPattern, CAPExtensions extensions,
        // ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
        // CallingPartysCategoryInap callingPartysCategory,
        // ArrayList<GenericNumberCap> genericNumbers,
        // CUGInterlock cugInterlock, boolean cugOutgoingAccess,
        // LocationNumberCap chargeNumber, Carrier carrier,
        // boolean suppressionOfAnnouncement, NAOliInfo naOliInfo, boolean
        // borInterrogationRequested,
        // boolean suppressOCsi, ContinueWithArgumentArgExtension
        // continueWithArgumentArgExtension

        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        LocationNumberImpl locationNumber = new LocationNumberImpl();
        locationNumber.setAddress("222333");
        LocationNumberIsupImpl chargeNumber = new LocationNumberIsupImpl(locationNumber);
        CarrierImpl carrier = new CarrierImpl(Unpooled.wrappedBuffer(getCarrier()));
        NAOliInfoImpl naOliInfo = new NAOliInfoImpl(11);
        ContinueWithArgumentArgExtensionImpl continueWithArgumentArgExtension = new ContinueWithArgumentArgExtensionImpl(true, false, false, null);
        elem = new ContinueWithArgumentRequestImpl(alertingPattern, CAPExtensionsTest.createTestCAPExtensions(), serviceInteractionIndicatorsTwo,
                callingPartysCategory, genericNumbers, cugInterlock, true, chargeNumber, carrier, true, naOliInfo, true, true, continueWithArgumentArgExtension);

        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
