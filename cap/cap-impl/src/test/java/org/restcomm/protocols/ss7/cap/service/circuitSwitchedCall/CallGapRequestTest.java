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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

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
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
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

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallGapRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallGapRequestImpl);
        
        CallGapRequestImpl elem = (CallGapRequestImpl)result.getResult();
        assertEquals(elem.getGapCriteria().getCompoundGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), SERVICE_KEY);
        assertEquals(elem.getGapCriteria().getCompoundGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getCalledAddressValue().getData(), getDigitsData());
        assertEquals(elem.getGapCriteria().getCompoundGapCriteria().getScfID().getData(), getDigitsData1());

        assertEquals(elem.getGapIndicators().getDuration(), DURATION);
        assertEquals(elem.getGapIndicators().getGapInterval(), -GAP_INTERVAL);

        assertEquals(elem.getControlType(), ControlType.sCPOverloaded);

        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getMessageID().getElementaryMessageID().intValue(), ELEMENTARY_MESSAGE_ID);
        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getNumberOfRepetitions().intValue(), NUMBER_OF_REPETITIONS);
        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getDuration().intValue(), DURATION_IF);
        assertEquals(elem.getGapTreatment().getInformationToSend().getInbandInfo().getInterval().intValue(), INTERVAL);

        assertEquals(elem.getExtensions().getExtensionFields().get(0).getLocalCode().intValue(), Integer.MIN_VALUE);
        assertEquals(elem.getExtensions().getExtensionFields().get(0).getCriticalityType(), CriticalityType.typeIgnore);
        assertEquals(elem.getExtensions().getExtensionFields().get(0).getData(), getDigitsData2());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallGapRequestImpl);
        
        elem = (CallGapRequestImpl)result.getResult();
        assertEquals(elem.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), SERVICE_KEY);
        assertEquals(elem.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getCalledAddressValue().getData(), getDigitsData());

        assertEquals(elem.getGapIndicators().getDuration(), DURATION);
        assertEquals(elem.getGapIndicators().getGapInterval(), -GAP_INTERVAL);

        assertNull(elem.getControlType());
        assertNull(elem.getGapTreatment());
        assertNull(elem.getExtensions());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallGapRequestImpl.class);
    	
        DigitsIsupImpl digits = new DigitsIsupImpl(getDigitsData());
        CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits, SERVICE_KEY);
        BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
        ScfIDImpl scfId = new ScfIDImpl(getDigitsData1());
        CompoundCriteriaImpl compoundCriteria = new CompoundCriteriaImpl(basicGapCriteria, scfId);
        GapCriteriaImpl gapCriteria = new GapCriteriaImpl(compoundCriteria);

        GapIndicatorsImpl gapIndicators = new GapIndicatorsImpl(DURATION, -GAP_INTERVAL);

        MessageIDImpl messageID = new MessageIDImpl(ELEMENTARY_MESSAGE_ID);
        InbandInfoImpl inbandInfo = new InbandInfoImpl(messageID, NUMBER_OF_REPETITIONS, DURATION_IF, INTERVAL);
        InformationToSendImpl informationToSend = new InformationToSendImpl(inbandInfo);
        GapTreatmentImpl gapTreatment = new GapTreatmentImpl(informationToSend);

        List<ExtensionField> fieldsList = new ArrayList<>();
        ExtensionFieldImpl extensionField = new ExtensionFieldImpl(Integer.MIN_VALUE, CriticalityType.typeIgnore, getDigitsData2(), false);
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

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        //Min parameters
        GenericNumberImpl gn = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        Digits digits = new DigitsImpl(gn);

        BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(digits);

        GapCriteriaImpl gapCriteria = new GapCriteriaImpl(basicGapCriteria);

        GapIndicatorsImpl gapIndicators = new GapIndicatorsImpl(60, -1);

        CallGapRequestImpl original = new CallGapRequestImpl(gapCriteria, gapIndicators, null, null, null);
        original.setInvokeId(24);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "callGapArg", CallGapRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);

        CallGapRequestImpl copy = reader.read("callGapArg", CallGapRequestImpl.class);

        assertTrue(isEqual(original, copy));


        //Max paramters
        gn = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        digits = new DigitsImpl(gn);

        CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits, SERVICE_KEY);
//        GapOnService gapOnService = new GapOnServiceImpl(SERVICE_KEY);
        basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
        byte[] data2 = new byte[]{12, 32, 23, 56};
        ScfIDImpl scfId = new ScfIDImpl(data2);
        CompoundCriteriaImpl compoundCriteria = new CompoundCriteriaImpl(basicGapCriteria, null);
        gapCriteria = new GapCriteriaImpl(compoundCriteria);

        gapIndicators = new GapIndicatorsImpl(60, -1);

        ArrayList<VariablePart> aL = new ArrayList<VariablePart>();
        aL.add(new VariablePartImpl(new VariablePartDateImpl(2015, 6, 27)));
        aL.add(new VariablePartImpl(new VariablePartTimeImpl(15, 10)));
        aL.add(new VariablePartImpl(new Integer(145)));
        VariableMessageImpl vm = new VariableMessageImpl(145, aL);
        MessageIDImpl mi = new MessageIDImpl(vm);
        InbandInfoImpl inbandInfo = new InbandInfoImpl(mi, new Integer(5), new Integer(8), new Integer(2));
        InformationToSendImpl informationToSend = new InformationToSendImpl(inbandInfo);
        GapTreatmentImpl gapTreatment = new GapTreatmentImpl(informationToSend);

        ArrayList<ExtensionField> fieldsList = new ArrayList<>();
        byte[] data3 = new byte[]{13, 14, 15, 16};
        ExtensionFieldImpl extensionField = new ExtensionFieldImpl(Integer.MIN_VALUE, CriticalityType.typeIgnore, data3);
        fieldsList.add(extensionField);
        CAPExtensionsImpl cAPExtensions = new CAPExtensionsImpl(fieldsList);

        original = new CallGapRequestImpl(gapCriteria, gapIndicators, ControlType.sCPOverloaded, gapTreatment, cAPExtensions);
        original.setInvokeId(24);

        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "callGapArg", CallGapRequestImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);

        copy = reader.read("callGapArg", CallGapRequestImpl.class);

        assertTrue(isEqual(original, copy));
    }

    private boolean isEqual(CallGapRequestImpl o1, CallGapRequestImpl o2) {
        if (o1 == o2)
            return true;
        if (o1 == null && o2 != null || o1 != null && o2 == null)
            return false;
        if (o1 == null && o2 == null)
            return true;
        if (!o1.toString().equals(o2.toString()))
            return false;
        return true;
    }*/
}
