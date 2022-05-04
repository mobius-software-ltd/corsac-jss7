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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConferenceTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.GenericNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingCategory;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ForwardServiceInteractionIndImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.NAOliInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartysCategoryIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.GenericNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectingPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectionInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CallingPartyCategoryImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.OriginalCalledNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.RedirectingNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.RedirectionInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
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
public class ConnectRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 9, (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 17, (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16, (byte) 174, 6, 4, 4, 1, 2, 3, 4 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 75, (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16, (byte) 129, 3, 0, 0, 8, (byte) 134, 6, (byte) 131,
                20, 7, 1, 9, 0, (byte) 170, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1,
                (byte) 255, (byte) 156, 1, 10, (byte) 157, 6, (byte) 131, 20, 7, 1, 9, 0, (byte) 158, 2, 3, 97, (byte) 174, 6,
                4, 4, 1, 2, 3, 4, (byte) 159, 55, 0, (byte) 159, 56, 0, (byte) 159, 57, 1, 40 };
    }

    public byte[] getData4() {
        return new byte[] { 48, 52, (byte) 160, 7, 4, 5, 2, 16, 121, 34, 16, (byte) 139, 4, 11, 12, 13, 14, (byte) 175, 5, (byte) 160, 3, (byte) 129, 1, 2,
                (byte) 147, 7, 4, 0, 0, 0, 112, 119, 119, (byte) 181, 3, (byte) 128, 1, 5, (byte) 159, 31, 4, 21, 22, 23, 24, (byte) 159, 32, 0, (byte) 159,
                58, 0, (byte) 159, 59, 0 };
    }

    public byte[] getDataGenericNumber() {
        return new byte[] { 1, 2, 3, 4 };
    }

    public byte[] getOriginalCalledPartyID() {
        return new byte[] { -125, 20, 7, 1, 9, 0 };
    }

    public byte[] getCallingPartysCategory() {
        return new byte[] { 10 };
    }

    public byte[] getRedirectingPartyID() {
        return new byte[] { -125, 20, 7, 1, 9, 0 };
    }

    public byte[] getRedirectionInformation() {
        return new byte[] { 3, 97 };
    }

    public byte[] getCarrierData() {
        return new byte[] { 11, 12, 13, 14 };
    }

    public byte[] getCUGInterlockData() {
        return new byte[] { 21, 22, 23, 24 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ConnectRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ConnectRequestImpl);
        
        ConnectRequestImpl elem = (ConnectRequestImpl)result.getResult();        
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getInternalNetworkNumberIndicator(), 0);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNatureOfAddressIndicator(), 2);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNumberingPlanIndicator(), 1);
        assertTrue(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress()
                .equals("972201"));
        assertNull(elem.getCarrier());
        assertNull(elem.getServiceInteractionIndicatorsTwo());
        assertNull(elem.getChargeNumber());
        assertNull(elem.getLegToBeConnected());
        assertNull(elem.getCUGInterlock());
        assertFalse(elem.getCugOutgoingAccess());
        assertFalse(elem.getBorInterrogationRequested());
        assertFalse(elem.getSuppressNCSI());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ConnectRequestImpl);
        
        elem = (ConnectRequestImpl)result.getResult();  
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getInternalNetworkNumberIndicator(), 0);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNatureOfAddressIndicator(), 2);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNumberingPlanIndicator(), 1);
        assertTrue(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress()
                .equals("972201"));
        assertEquals(elem.getGenericNumbers().size(), 1);
        assertTrue(ByteBufUtil.equals(GenericNumberIsupImpl.translate(elem.getGenericNumbers().get(0).getGenericNumber()),Unpooled.wrappedBuffer(getDataGenericNumber())));
        assertNull(elem.getCarrier());
        assertNull(elem.getServiceInteractionIndicatorsTwo());
        assertNull(elem.getChargeNumber());
        assertNull(elem.getLegToBeConnected());
        assertNull(elem.getCUGInterlock());
        assertFalse(elem.getCugOutgoingAccess());
        assertFalse(elem.getBorInterrogationRequested());
        assertFalse(elem.getSuppressNCSI());

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ConnectRequestImpl);
        
        elem = (ConnectRequestImpl)result.getResult();  
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getInternalNetworkNumberIndicator(), 0);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNatureOfAddressIndicator(), 2);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNumberingPlanIndicator(), 1);
        assertTrue(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress()
                .equals("972201"));
        assertEquals(elem.getGenericNumbers().size(), 1);
        assertTrue(ByteBufUtil.equals(GenericNumberIsupImpl.translate(elem.getGenericNumbers().get(0).getGenericNumber()),Unpooled.wrappedBuffer(getDataGenericNumber())));
        assertEquals(elem.getAlertingPattern().getAlertingCategory(), AlertingCategory.Category5);
        assertTrue(ByteBufUtil.equals(OriginalCalledNumberIsupImpl.translate(elem.getOriginalCalledPartyID().getOriginalCalledNumber()),Unpooled.wrappedBuffer(getOriginalCalledPartyID())));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        
        assertEquals(elem.getCallingPartysCategory().getCallingPartyCategory().getCallingPartyCategory(), getCallingPartysCategory()[0]);
        assertTrue(ByteBufUtil.equals(RedirectingPartyIDIsupImpl.translate(elem.getRedirectingPartyID().getRedirectingNumber()),Unpooled.wrappedBuffer(getRedirectingPartyID())));
        
        ByteBuf buffer=Unpooled.buffer();
        ((RedirectionInformationIsupImpl)elem.getRedirectionInformation()).encode(parser,buffer);
        assertNotNull(buffer);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, getRedirectionInformation()));
        assertTrue(elem.getSuppressionOfAnnouncement());
        assertTrue(elem.getOCSIApplicable());
        assertEquals((int) elem.getNAOliInfo().getData(), 40);
        assertNull(elem.getCarrier());
        assertNull(elem.getServiceInteractionIndicatorsTwo());
        assertNull(elem.getChargeNumber());
        assertNull(elem.getLegToBeConnected());
        assertNull(elem.getCUGInterlock());
        assertFalse(elem.getCugOutgoingAccess());
        assertFalse(elem.getBorInterrogationRequested());
        assertFalse(elem.getSuppressNCSI());

        rawData = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ConnectRequestImpl);
        
        elem = (ConnectRequestImpl)result.getResult();  
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getInternalNetworkNumberIndicator(), 0);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNatureOfAddressIndicator(), 2);
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
                .getNumberingPlanIndicator(), 1);
        assertTrue(elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber().getAddress()
                .equals("972201"));

        assertNull(elem.getGenericNumbers());
        assertNull(elem.getAlertingPattern());
        assertNull(elem.getOriginalCalledPartyID());
        assertNull(elem.getExtensions());
        assertNull(elem.getCallingPartysCategory());
        assertNull(elem.getRedirectingPartyID());
        assertNull(elem.getRedirectionInformation());
        assertFalse(elem.getSuppressionOfAnnouncement());
        assertFalse(elem.getOCSIApplicable());
        assertNull(elem.getNAOliInfo());
        assertTrue(ByteBufUtil.equals(elem.getCarrier().getValue(), Unpooled.wrappedBuffer(getCarrierData())));
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getForwardServiceInteractionInd().getConferenceTreatmentIndicator(),
                ConferenceTreatmentIndicator.rejectConferenceRequest);
        assertEquals(elem.getChargeNumber().getLocationNumber().getAddress(), "0000077777");
        assertEquals(elem.getLegToBeConnected().getSendingSideID(), LegType.leg5);
        assertTrue(ByteBufUtil.equals(elem.getCUGInterlock().getValue(), Unpooled.wrappedBuffer(getCUGInterlockData())));
        assertTrue(elem.getCugOutgoingAccess());
        assertTrue(elem.getBorInterrogationRequested());
        assertTrue(elem.getSuppressNCSI());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ConnectRequestImpl.class);
    	
        List<CalledPartyNumberIsup> calledPartyNumbers = new ArrayList<CalledPartyNumberIsup>();
        CalledPartyNumberImpl cpn = new CalledPartyNumberImpl(2, "972201", 1, 2);
        CalledPartyNumberIsupImpl calledPartyNumber = new CalledPartyNumberIsupImpl(cpn);
        calledPartyNumbers.add(calledPartyNumber);
        DestinationRoutingAddressImpl destinationRoutingAddress = new DestinationRoutingAddressImpl(calledPartyNumbers);

        ConnectRequestImpl elem = new ConnectRequestImpl(destinationRoutingAddress, null, null, null, null, null, null, null,
                null, null, null, null, null, false, false, false, null, false, false);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        List<GenericNumberIsup> genericNumbers = new ArrayList<GenericNumberIsup>();
        GenericNumberIsupImpl genericNumberCap = new GenericNumberIsupImpl(new GenericNumberImpl(Unpooled.wrappedBuffer(getDataGenericNumber())));
        genericNumbers.add(genericNumberCap);
        elem = new ConnectRequestImpl(destinationRoutingAddress, null, null, null, null, null, null, null, genericNumbers,
                null, null, null, null, false, false, false, null, false, false);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingCategory.Category5);
        OriginalCalledNumberIsupImpl originalCalledPartyID = new OriginalCalledNumberIsupImpl(new OriginalCalledNumberImpl(Unpooled.wrappedBuffer(getOriginalCalledPartyID())));
        CallingPartysCategoryIsupImpl callingPartysCategory = new CallingPartysCategoryIsupImpl(new CallingPartyCategoryImpl(getCallingPartysCategory()[0]));
        RedirectingPartyIDIsupImpl redirectingPartyID = new RedirectingPartyIDIsupImpl(new RedirectingNumberImpl(Unpooled.wrappedBuffer(getRedirectingPartyID())));
        RedirectionInformationIsupImpl redirectionInformation = new RedirectionInformationIsupImpl(new RedirectionInformationImpl(Unpooled.wrappedBuffer(getRedirectionInformation())));
        NAOliInfoImpl naoliInfo = new NAOliInfoImpl(40);


        elem = new ConnectRequestImpl(destinationRoutingAddress, alertingPattern, originalCalledPartyID,
                CAPExtensionsTest.createTestCAPExtensions(), null, callingPartysCategory, redirectingPartyID,
                redirectionInformation, genericNumbers, null, null, null, null, false, true, true, naoliInfo, false, false);
        rawData = this.getData3();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));        

        CarrierImpl carrier = new CarrierImpl(Unpooled.wrappedBuffer(getCarrierData()));
        ForwardServiceInteractionIndImpl forwardServiceInteractionInd = new ForwardServiceInteractionIndImpl(ConferenceTreatmentIndicator.rejectConferenceRequest, null, null);
        ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl(forwardServiceInteractionInd, null, null,
                null, false, null, null, null);
        LocationNumber locationNumber = new LocationNumberImpl();
        locationNumber.setNatureOfAddresIndicator(LocationNumber._NAI_INTERNATIONAL_NUMBER);
        locationNumber.setAddress("0000077777");
        LocationNumberIsupImpl chargeNumber = new LocationNumberIsupImpl(locationNumber);
        LegIDImpl legToBeConnected = new LegIDImpl(null,LegType.leg5);
        CUGInterlockImpl cugInterlock = new CUGInterlockImpl(Unpooled.wrappedBuffer(getCUGInterlockData()));
        elem = new ConnectRequestImpl(destinationRoutingAddress, null, null, null, carrier, null, null, null, null, serviceInteractionIndicatorsTwo,
                chargeNumber, legToBeConnected, cugInterlock, true, false, false, null, true, true);
        rawData = this.getData4();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));        

        // DestinationRoutingAddressImpl destinationRoutingAddress, AlertingPatternCap alertingPattern,
        // OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions, Carrier carrier, CallingPartysCategoryInap
        // callingPartysCategory,
        // RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation,
        // ArrayList<GenericNumberCap> genericNumbers,
        // ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberCap chargeNumber, LegID
        // legToBeConnected, CUGInterlock cugInterlock,
        // boolean cugOutgoingAccess, boolean suppressionOfAnnouncement, boolean ocsIApplicable, NAOliInfo naoliInfo, boolean
        // borInterrogationRequested
    }
}
