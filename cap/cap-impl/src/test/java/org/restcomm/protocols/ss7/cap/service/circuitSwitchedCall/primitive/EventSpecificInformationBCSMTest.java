/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.EsiBcsm.CallAcceptedSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.DpSpecificInfoAltImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.MidCallEventsImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.OAbandonSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.OAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.OCalledPartyBusySpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.OChangeOfPositionSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.ODisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.OMidCallSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.ONoAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.OServiceChangeSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.OTermSeizedSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.TAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.TBusySpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.TChangeOfPositionSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.TDisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.TMidCallSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.EsiBcsm.TNoAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.EventSpecificInformationBCSMImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.EventSpecificInformationBCSMWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;
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
public class EventSpecificInformationBCSMTest {

    public byte[] getData1() {
        return new byte[] { 48, 6, (byte) 162, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 6, (byte) 163, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 2, (byte) 164, 0 };
    }

    public byte[] getData4() {
        return new byte[] { 48, 2, (byte) 165, 0 };
    }

    public byte[] getData5() {
        return new byte[] { 48, 6, (byte) 167, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
    }

    public byte[] getData6() {
        return new byte[] { 48, 6, (byte) 168, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
    }

    public byte[] getData7() {
        return new byte[] { 48, 15, (byte) 169, 13, (byte) 159, 50, 0, (byte) 159, 52, 7, 3, (byte) 144, 33, 114, 16, (byte) 144, 0 };
    }

    public byte[] getData8() {
        return new byte[] { 48, 2, (byte) 170, 0 };
    }

    public byte[] getData9() {
        return new byte[] { 48, 6, (byte) 172, 4, (byte) 128, 2, (byte) 132, (byte) 144 };
    }

    public byte[] getData10() {
        return new byte[] { 48, 5, (byte) 181, 3, (byte) 159, 50, 0 };
    }

    public byte[] getData11() {
        return new byte[] { 48, 11, (byte) 166, 9, (byte) 161, 7, (byte) 131, 5, 99, 1, 2, 3, 4 };
    }

    public byte[] getData12() {
        return new byte[] { 48, 11, (byte) 171, 9, (byte) 161, 7, (byte) 131, 5, 99, 1, 2, 3, 4 };
    }

    public byte[] getData13() {
        return new byte[] { 48, 9, (byte) 173, 7, (byte) 191, 50, 4, 2, 2, 0, (byte) 135 };
    }

    public byte[] getData14() {
        return new byte[] { 48, 9, (byte) 180, 7, (byte) 191, 50, 4, 2, 2, 0, (byte) 135 };
    }

    public byte[] getData15() {
        return new byte[] { 48, 10, (byte) 191, 50, 7, (byte) 191, 50, 4, 2, 2, 0, (byte) 135 };
    }

    public byte[] getData16() {
        return new byte[] { 48, 10, (byte) 191, 51, 7, (byte) 191, 50, 4, 2, 2, 0, (byte) 135 };
    }

    public byte[] getData17() {
        return new byte[] { 48, 10, (byte) 191, 52, 7, (byte) 160, 5, (byte) 160, 3, (byte) 130, 1, 38 };
    }

    public byte[] getDigitsData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventSpecificInformationBCSMWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        EventSpecificInformationBCSMWrapperImpl elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult();        
        CauseIndicators ci = elem.getEventSpecificInformationBCSM().getRouteSelectFailureSpecificInfo().getFailureCause().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 16);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        ci = elem.getEventSpecificInformationBCSM().getOCalledPartyBusySpecificInfo().getBusyCause().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 16);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        assertNotNull(elem.getEventSpecificInformationBCSM().getONoAnswerSpecificInfo());

        rawData = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        assertNotNull(elem.getEventSpecificInformationBCSM().getOAnswerSpecificInfo());

        rawData = this.getData5();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        ci = elem.getEventSpecificInformationBCSM().getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 16);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);

        rawData = this.getData6();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        ci = elem.getEventSpecificInformationBCSM().getTBusySpecificInfo().getBusyCause().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 16);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);
        assertFalse(elem.getEventSpecificInformationBCSM().getTBusySpecificInfo().getCallForwarded());
        assertFalse(elem.getEventSpecificInformationBCSM().getTBusySpecificInfo().getRouteNotPermitted());
        assertNull(elem.getEventSpecificInformationBCSM().getTBusySpecificInfo().getForwardingDestinationNumber());

        rawData = this.getData7();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        assertTrue(elem.getEventSpecificInformationBCSM().getTNoAnswerSpecificInfo().getCallForwarded());
        CalledPartyNumber cpn = elem.getEventSpecificInformationBCSM().getTNoAnswerSpecificInfo().getForwardingDestinationNumber().getCalledPartyNumber();
        assertFalse(cpn.isOddFlag());
        assertEquals(cpn.getNumberingPlanIndicator(), 1);
        assertEquals(cpn.getInternalNetworkNumberIndicator(), 1);
        assertEquals(cpn.getNatureOfAddressIndicator(), 3);
        assertTrue(cpn.getAddress().equals("1227010900"));

        rawData = this.getData8();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        assertNotNull(elem.getEventSpecificInformationBCSM().getTAnswerSpecificInfo());

        rawData = this.getData9();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        ci = elem.getEventSpecificInformationBCSM().getTDisconnectSpecificInfo().getReleaseCause().getCauseIndicators();
        assertEquals(ci.getCauseValue(), 16);
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);

        rawData = this.getData10();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        assertTrue(elem.getEventSpecificInformationBCSM().getOAbandonSpecificInfo().getRouteNotPermitted());

        rawData = this.getData11();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        ByteBuf value=elem.getEventSpecificInformationBCSM().getOMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits();
        assertNotNull(value);
        byte[] data=new byte[value.readableBytes()];
        value.readBytes(data);
        assertEquals(data, getDigitsData());

        rawData = this.getData12();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        value=elem.getEventSpecificInformationBCSM().getTMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits();
        assertNotNull(value);
        data=new byte[value.readableBytes()];
        value.readBytes(data);
        assertEquals(data, getDigitsData());

        rawData = this.getData13();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        assertEquals((int) elem.getEventSpecificInformationBCSM().getOTermSeizedSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), 135);

        rawData = this.getData14();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        assertEquals((int) elem.getEventSpecificInformationBCSM().getCallAcceptedSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), 135);

        rawData = this.getData15();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        assertEquals((int) elem.getEventSpecificInformationBCSM().getOChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), 135);

        rawData = this.getData16();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        assertEquals((int) elem.getEventSpecificInformationBCSM().getTChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), 135);

        rawData = this.getData17();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof EventSpecificInformationBCSMWrapperImpl);
        
        elem = (EventSpecificInformationBCSMWrapperImpl)result.getResult(); 
        assertEquals(elem.getEventSpecificInformationBCSM().getDpSpecificInfoAlt().getOServiceChangeSpecificInfo().getExtBasicServiceCode().getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.padAccessCA_9600bps);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(EventSpecificInformationBCSMWrapperImpl.class);
    	
        CauseIndicators causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseIsupImpl failureCause = new CauseIsupImpl(causeIndicators);
        RouteSelectFailureSpecificInfoImpl routeSelectFailureSpecificInfo = new RouteSelectFailureSpecificInfoImpl(failureCause);
        EventSpecificInformationBCSMImpl elem = new EventSpecificInformationBCSMImpl(routeSelectFailureSpecificInfo);
        EventSpecificInformationBCSMWrapperImpl wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseIsupImpl busyCause = new CauseIsupImpl(causeIndicators);
        OCalledPartyBusySpecificInfoImpl oCalledPartyBusySpecificInfoImpl = new OCalledPartyBusySpecificInfoImpl(busyCause);
        elem = new EventSpecificInformationBCSMImpl(oCalledPartyBusySpecificInfoImpl);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        ONoAnswerSpecificInfoImpl oNoAnswerSpecificInfo = new ONoAnswerSpecificInfoImpl();
        elem = new EventSpecificInformationBCSMImpl(oNoAnswerSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData3();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        OAnswerSpecificInfoImpl oAnswerSpecificInfo = new OAnswerSpecificInfoImpl();
        elem = new EventSpecificInformationBCSMImpl(oAnswerSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData4();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseIsupImpl releaseCause = new CauseIsupImpl(causeIndicators);
        ODisconnectSpecificInfoImpl oDisconnectSpecificInfo = new ODisconnectSpecificInfoImpl(releaseCause);
        elem = new EventSpecificInformationBCSMImpl(oDisconnectSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData5();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        busyCause = new CauseIsupImpl(causeIndicators);
        TBusySpecificInfoImpl tBusySpecificInfo = new TBusySpecificInfoImpl(busyCause, false, false, null);
        elem = new EventSpecificInformationBCSMImpl(tBusySpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData6();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        CalledPartyNumber cpn = new CalledPartyNumberImpl(3, "1227010900", 1, 1);
        // int natureOfAddresIndicator, String address, int
        // numberingPlanIndicator, int internalNetworkNumberIndicator
        CalledPartyNumberIsupImpl cpnc = new CalledPartyNumberIsupImpl(cpn);
        TNoAnswerSpecificInfoImpl tNoAnswerSpecificInfo = new TNoAnswerSpecificInfoImpl(true, cpnc);
        elem = new EventSpecificInformationBCSMImpl(tNoAnswerSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData7();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        TAnswerSpecificInfoImpl tAnswerSpecificInfo = new TAnswerSpecificInfoImpl();
        elem = new EventSpecificInformationBCSMImpl(tAnswerSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData8();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        
        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        releaseCause = new CauseIsupImpl(causeIndicators);
        TDisconnectSpecificInfoImpl tDisconnectSpecificInfo = new TDisconnectSpecificInfoImpl(releaseCause);
        elem = new EventSpecificInformationBCSMImpl(tDisconnectSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData9();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        OAbandonSpecificInfoImpl oAbandonSpecificInfo = new OAbandonSpecificInfoImpl(true);
        elem = new EventSpecificInformationBCSMImpl(oAbandonSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData10();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        GenericDigitsImpl genericDigits = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BINARY, GenericDigits._TOD_BGCI, Unpooled.wrappedBuffer(getDigitsData()));
        DigitsIsupImpl dtmfDigits = new DigitsIsupImpl(genericDigits);
        MidCallEventsImpl midCallEvents = new MidCallEventsImpl(dtmfDigits, true);
        OMidCallSpecificInfoImpl oMidCallSpecificInfo = new OMidCallSpecificInfoImpl(midCallEvents);
        elem = new EventSpecificInformationBCSMImpl(oMidCallSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData11();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        TMidCallSpecificInfoImpl tMidCallSpecificInfo = new TMidCallSpecificInfoImpl(midCallEvents);
        elem = new EventSpecificInformationBCSMImpl(tMidCallSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData12();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        LocationInformationImpl locationInformation = new LocationInformationImpl(135, null, null, null, null, null, null, null, null, false, false, null, null);
        OTermSeizedSpecificInfoImpl oTermSeizedSpecificInfo = new OTermSeizedSpecificInfoImpl(locationInformation);
        elem = new EventSpecificInformationBCSMImpl(oTermSeizedSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData13();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        CallAcceptedSpecificInfoImpl callAcceptedSpecificInfo = new CallAcceptedSpecificInfoImpl(locationInformation);
        elem = new EventSpecificInformationBCSMImpl(callAcceptedSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData14();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        OChangeOfPositionSpecificInfoImpl oChangeOfPositionSpecificInfo = new OChangeOfPositionSpecificInfoImpl(locationInformation, null);
        elem = new EventSpecificInformationBCSMImpl(oChangeOfPositionSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData15();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        TChangeOfPositionSpecificInfoImpl tChangeOfPositionSpecificInfo = new TChangeOfPositionSpecificInfoImpl(locationInformation, null);
        elem = new EventSpecificInformationBCSMImpl(tChangeOfPositionSpecificInfo);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData16();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        ExtBearerServiceCodeImpl extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(extBearerService);
        OServiceChangeSpecificInfoImpl oServiceChangeSpecificInfo = new OServiceChangeSpecificInfoImpl(extBasicServiceCode);
        DpSpecificInfoAltImpl dpSpecificInfoAlt = new DpSpecificInfoAltImpl(oServiceChangeSpecificInfo, null, null);
        elem = new EventSpecificInformationBCSMImpl(dpSpecificInfoAlt);
        wrapper = new EventSpecificInformationBCSMWrapperImpl(elem);
        rawData = this.getData17();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerializaion() throws Exception {
        CauseIndicators causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseCap failureCause = new CauseCapImpl(causeIndicators);
        RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo = new RouteSelectFailureSpecificInfoImpl(failureCause);
        EventSpecificInformationBCSMImpl original = new EventSpecificInformationBCSMImpl(routeSelectFailureSpecificInfo);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        EventSpecificInformationBCSMImpl copy = reader.read("eventSpecificInformationBCSM",
                EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getRouteSelectFailureSpecificInfo().getFailureCause().getCauseIndicators().getCauseValue(), original
                .getRouteSelectFailureSpecificInfo().getFailureCause().getCauseIndicators().getCauseValue());

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseCap busyCause = new CauseCapImpl(causeIndicators);
        OCalledPartyBusySpecificInfoImpl oCalledPartyBusySpecificInfoImpl = new OCalledPartyBusySpecificInfoImpl(busyCause);
        original = new EventSpecificInformationBCSMImpl(oCalledPartyBusySpecificInfoImpl);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getOCalledPartyBusySpecificInfo().getBusyCause().getCauseIndicators().getCauseValue(), original
                .getOCalledPartyBusySpecificInfo().getBusyCause().getCauseIndicators().getCauseValue());

        ONoAnswerSpecificInfoImpl oNoAnswerSpecificInfo = new ONoAnswerSpecificInfoImpl();
        original = new EventSpecificInformationBCSMImpl(oNoAnswerSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertNotNull(copy.getONoAnswerSpecificInfo());

        OAnswerSpecificInfoImpl oAnswerSpecificInfo = new OAnswerSpecificInfoImpl();
        original = new EventSpecificInformationBCSMImpl(oAnswerSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertNotNull(copy.getOAnswerSpecificInfo());

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        CauseCap releaseCause = new CauseCapImpl(causeIndicators);
        ODisconnectSpecificInfoImpl oDisconnectSpecificInfo = new ODisconnectSpecificInfoImpl(releaseCause);
        original = new EventSpecificInformationBCSMImpl(oDisconnectSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getCauseValue(), original
                .getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getCauseValue());

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        busyCause = new CauseCapImpl(causeIndicators);
        TBusySpecificInfoImpl tBusySpecificInfo = new TBusySpecificInfoImpl(busyCause, false, false, null);
        original = new EventSpecificInformationBCSMImpl(tBusySpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getTBusySpecificInfo().getBusyCause().getCauseIndicators().getCauseValue(), original
                .getTBusySpecificInfo().getBusyCause().getCauseIndicators().getCauseValue());

        CalledPartyNumber cpn = new CalledPartyNumberImpl(3, "1227010900", 1, 1);
        // int natureOfAddresIndicator, String address, int
        // numberingPlanIndicator, int internalNetworkNumberIndicator
        CalledPartyNumberCap cpnc = new CalledPartyNumberCapImpl(cpn);
        TNoAnswerSpecificInfoImpl tNoAnswerSpecificInfo = new TNoAnswerSpecificInfoImpl(true, cpnc);
        original = new EventSpecificInformationBCSMImpl(tNoAnswerSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getTNoAnswerSpecificInfo().getForwardingDestinationNumber().getCalledPartyNumber().getAddress(),
                original.getTNoAnswerSpecificInfo().getForwardingDestinationNumber().getCalledPartyNumber().getAddress());

        CalledPartyNumber calledPartyNumber = new CalledPartyNumberImpl();
        calledPartyNumber.setAddress("73645");
        calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
        calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
        calledPartyNumber.setNatureOfAddresIndicator(CalledPartyNumber._NAI_INTERNATIONAL_NUMBER);
        CalledPartyNumberCap destinationAddress = new CalledPartyNumberCapImpl(calledPartyNumber);
        TAnswerSpecificInfoImpl tAnswerSpecificInfo = new TAnswerSpecificInfoImpl(destinationAddress, false, true, null, null,
                null);
        // CalledPartyNumberCap destinationAddress, boolean orCall, boolean forwardedCall,
        // ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode,
        // ExtBasicServiceCode extBasicServiceCode2
        original = new EventSpecificInformationBCSMImpl(tAnswerSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getTAnswerSpecificInfo().getDestinationAddress().getCalledPartyNumber().getAddress(), original
                .getTAnswerSpecificInfo().getDestinationAddress().getCalledPartyNumber().getAddress());
        assertEquals(copy.getTAnswerSpecificInfo().getForwardedCall(), original.getTAnswerSpecificInfo().getForwardedCall());
        assertEquals(copy.getTAnswerSpecificInfo().getOrCall(), original.getTAnswerSpecificInfo().getOrCall());

        causeIndicators = new CauseIndicatorsImpl(0, 4, 0, 16, null);
        releaseCause = new CauseCapImpl(causeIndicators);
        TDisconnectSpecificInfoImpl tDisconnectSpecificInfo = new TDisconnectSpecificInfoImpl(releaseCause);
        original = new EventSpecificInformationBCSMImpl(tDisconnectSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getTDisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getCauseValue(), original
                .getTDisconnectSpecificInfo().getReleaseCause().getCauseIndicators().getCauseValue());

        OAbandonSpecificInfo oAbandonSpecificInfo = new OAbandonSpecificInfoImpl(true);
        original = new EventSpecificInformationBCSMImpl(oAbandonSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getOAbandonSpecificInfo().getRouteNotPermitted(), original.getOAbandonSpecificInfo()
                .getRouteNotPermitted());


        GenericDigits genericDigits = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BINARY, GenericDigits._TOD_BGCI, getDigitsData());
        Digits dtmfDigits = new DigitsImpl(genericDigits);
        MidCallEvents midCallEvents = new MidCallEventsImpl(dtmfDigits, true);
        OMidCallSpecificInfoImpl oMidCallSpecificInfo = new OMidCallSpecificInfoImpl(midCallEvents);
        original = new EventSpecificInformationBCSMImpl(oMidCallSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getOMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits(), original
                .getOMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits());


        TMidCallSpecificInfo tMidCallSpecificInfo = new TMidCallSpecificInfoImpl(midCallEvents);
        original = new EventSpecificInformationBCSMImpl(tMidCallSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getTMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits(), original
                .getTMidCallSpecificInfo().getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits());


        LocationInformation locationInformation = new LocationInformationImpl(135, null, null, null, null, null, null, null, null, false, false, null, null);
        OTermSeizedSpecificInfo oTermSeizedSpecificInfo = new OTermSeizedSpecificInfoImpl(locationInformation);
        original = new EventSpecificInformationBCSMImpl(oTermSeizedSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals((int) copy.getOTermSeizedSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), (int) original
                .getOTermSeizedSpecificInfo().getLocationInformation().getAgeOfLocationInformation());


        CallAcceptedSpecificInfo callAcceptedSpecificInfo = new CallAcceptedSpecificInfoImpl(locationInformation);
        original = new EventSpecificInformationBCSMImpl(callAcceptedSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals((int) copy.getCallAcceptedSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), (int) original
                .getCallAcceptedSpecificInfo().getLocationInformation().getAgeOfLocationInformation());


        OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo = new OChangeOfPositionSpecificInfoImpl(locationInformation, null);
        original = new EventSpecificInformationBCSMImpl(oChangeOfPositionSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals((int) copy.getOChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), (int) original
                .getOChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation());


        TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo = new TChangeOfPositionSpecificInfoImpl(locationInformation, null);
        original = new EventSpecificInformationBCSMImpl(tChangeOfPositionSpecificInfo);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals((int) copy.getTChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation(), (int) original
                .getTChangeOfPositionSpecificInfo().getLocationInformation().getAgeOfLocationInformation());


        ExtBearerServiceCode extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCodeImpl(extBearerService);
        OServiceChangeSpecificInfo oServiceChangeSpecificInfo = new OServiceChangeSpecificInfoImpl(extBasicServiceCode);
        DpSpecificInfoAlt dpSpecificInfoAlt = new DpSpecificInfoAltImpl(oServiceChangeSpecificInfo, null, null);
        original = new EventSpecificInformationBCSMImpl(dpSpecificInfoAlt);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("eventSpecificInformationBCSM", EventSpecificInformationBCSMImpl.class);

        assertEquals(copy.getDpSpecificInfoAlt().getOServiceChangeSpecificInfo().getExtBasicServiceCode().getExtBearerService().getBearerServiceCodeValue(),
                original.getDpSpecificInfoAlt().getOServiceChangeSpecificInfo().getExtBasicServiceCode().getExtBearerService().getBearerServiceCodeValue());

    }*/
}
