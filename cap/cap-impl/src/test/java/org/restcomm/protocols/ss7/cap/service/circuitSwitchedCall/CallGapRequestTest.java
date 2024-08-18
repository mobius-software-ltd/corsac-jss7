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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CriticalityType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ExtensionField;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InbandInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InformationToSendImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MessageIDImpl;
import org.restcomm.protocols.ss7.commonapp.gap.BasicGapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CalledAddressAndServiceImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CompoundCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapIndicatorsImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapTreatmentImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ExtensionFieldImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ScfIDImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 * @author yulianoifa
 *
 */
public class CallGapRequestTest {

    public static final int SERVICE_KEY = 821;
    public static final int DURATION = 60;
    public static final int GAP_INTERVAL = 1;

    public static final int ELEMENTARY_MESSAGE_ID = 1;
    public static final int NUMBER_OF_REPETITIONS = 5;
    public static final int DURATION_IF = 5;
    public static final int INTERVAL = 5;

    // Max parameters
    public byte[] getData() {
        return new byte[] { 48, 71, (byte) 160, 22, 48, 20, (byte) 160, 12, (byte) 189, 10, (byte) 128, 4, 48, 69, 91, 84,
                (byte) 129, 2, 3, 53, (byte) 129, 4, 12, 32, 23, 56, (byte) 161, 6, (byte) 128, 1, 60, (byte) 129,
                1, (byte) 255, (byte) 130, 1, 0, (byte) 163, 18, (byte) 160, 16, (byte) 160, 14, (byte) 160, 3, (byte) 128, 1, 1,
                (byte) 129, 1, 5, (byte) 130, 1, 5, (byte) 131, 1, 5, (byte) 164, 14, 48, 12, 2, 4, (byte) 128,
                0, 0, 0, (byte) 129, 4, 13, 14, 15, 16};
    }

    public byte[] getData2() {
        return new byte[] { 48, 22, (byte) 160, 12, (byte) 189, 10, (byte) 128, 4, 48, 69, 91, 84, (byte) 129, 2, 3, 53,
                (byte) 161, 6, (byte) 128, 1, 60, (byte) 129, 1, (byte) 255 };
    }

    // Digits for CalledAddressAndService
    public byte[] getDigitsData() {
        return new byte[] {48, 69, 91, 84};
    }

    // Digits for ScfID
    public byte[] getDigitsData1() {
        return new byte[] {12, 32, 23, 56};
    }

    // Digits for ExtensionField
    public byte[] getDigitsData2() {
        return new byte[] {13, 14, 15, 16};
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallGapRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallGapRequestImpl);
        
        CallGapRequestImpl elem = (CallGapRequestImpl)result.getResult();
        assertEquals(elem.getGapCriteria().getCompoundGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), SERVICE_KEY);
        assertTrue(ByteBufUtil.equals(DigitsIsupImpl.translate(elem.getGapCriteria().getCompoundGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getCalledAddressDigits().getGenericDigits()),Unpooled.wrappedBuffer(getDigitsData())));
        assertTrue(ByteBufUtil.equals(elem.getGapCriteria().getCompoundGapCriteria().getScfID().getValue(), Unpooled.wrappedBuffer(getDigitsData1())));

        assertEquals(elem.getGapIndicators().getDuration(), DURATION);
        assertEquals(elem.getGapIndicators().getGapInterval(), -GAP_INTERVAL);

        assertEquals(elem.getControlType(), ControlType.sCPOverloaded);

        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getMessageID().getElementaryMessageID().intValue(), ELEMENTARY_MESSAGE_ID);
        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getNumberOfRepetitions().intValue(), NUMBER_OF_REPETITIONS);
        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getDuration().intValue(), DURATION_IF);
        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getInterval().intValue(), INTERVAL);

        assertEquals(elem.getExtensions().getExtensionFields().get(0).getLocalCode().intValue(), Integer.MIN_VALUE);
        assertEquals(elem.getExtensions().getExtensionFields().get(0).getCriticalityType(), CriticalityType.typeIgnore);
        assertTrue(ByteBufUtil.equals(elem.getExtensions().getExtensionFields().get(0).getValue(),Unpooled.wrappedBuffer(getDigitsData2())));

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallGapRequestImpl);
        
        elem = (CallGapRequestImpl)result.getResult();
        assertEquals(elem.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), SERVICE_KEY);
        assertTrue(ByteBufUtil.equals(DigitsIsupImpl.translate(elem.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getCalledAddressDigits().getGenericDigits()), Unpooled.wrappedBuffer(getDigitsData())));

        assertEquals(elem.getGapIndicators().getDuration(), DURATION);
        assertEquals(elem.getGapIndicators().getGapInterval(), -GAP_INTERVAL);

        assertNull(elem.getControlType());
        assertNull(elem.getGapTreatment());
        assertNull(elem.getExtensions());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallGapRequestImpl.class);
    	
        DigitsIsupImpl digits = new DigitsIsupImpl(new GenericDigitsImpl(Unpooled.wrappedBuffer(getDigitsData())));
        CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits, SERVICE_KEY);
        BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
        ScfIDImpl scfId = new ScfIDImpl(Unpooled.wrappedBuffer(getDigitsData1()));
        CompoundCriteriaImpl compoundCriteria = new CompoundCriteriaImpl(basicGapCriteria, scfId);
        GapCriteriaImpl gapCriteria = new GapCriteriaImpl(compoundCriteria);

        GapIndicatorsImpl gapIndicators = new GapIndicatorsImpl(DURATION, -GAP_INTERVAL);

        MessageIDImpl messageID = new MessageIDImpl(ELEMENTARY_MESSAGE_ID);
        InbandInfoImpl inbandInfo = new InbandInfoImpl(messageID, NUMBER_OF_REPETITIONS, DURATION_IF, INTERVAL);
        InformationToSendImpl informationToSend = new InformationToSendImpl(inbandInfo);
        GapTreatmentImpl gapTreatment = new GapTreatmentImpl(informationToSend);

        List<ExtensionField> fieldsList = new ArrayList<>();
        ExtensionFieldImpl extensionField = new ExtensionFieldImpl(Integer.MIN_VALUE, CriticalityType.typeIgnore, Unpooled.wrappedBuffer(getDigitsData2()), false);
        fieldsList.add(extensionField);
        CAPINAPExtensions cAPExtensions = new CAPINAPExtensionsImpl(fieldsList);

        CallGapRequestImpl elem = new CallGapRequestImpl(gapCriteria, gapIndicators, ControlType.sCPOverloaded, gapTreatment, cAPExtensions);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        gapCriteria = new GapCriteriaImpl(basicGapCriteria);
        elem = new CallGapRequestImpl(gapCriteria, gapIndicators, null, null, null);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
