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

package org.restcomm.protocols.ss7.cap.gap;

import org.restcomm.protocols.ss7.cap.api.gap.*;
import org.restcomm.protocols.ss7.cap.api.isup.DigitsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.ScfIDImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 *
 */
public class CompoundCriteriaTest {

    public static final int SERVICE_KEY = 821;

    public byte[] getData() {
        return new byte[] {48, 20, (byte) 160, 12, (byte) 189, 10, (byte) 128, 4, 48, 69, 91, 11, (byte) 129, 2, 3, 53, (byte) 129, 4, 12, 32, 23, 56};
    }

    public byte[] getDigitsData() {
        return new byte[] {48, 69, 91, 11};
    }

    public byte[] getDigitsData1() {
        return new byte[] {12, 32, 23, 56};
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode_CalledAddressAndService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CompoundCriteriaImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CompoundCriteriaImpl);
        
        CompoundCriteriaImpl elem = (CompoundCriteriaImpl)result.getResult();        
        assertEquals(elem.getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), SERVICE_KEY);
        assertEquals(elem.getBasicGapCriteria().getCalledAddressAndService().getCalledAddressValue().getData(), getDigitsData());
        assertEquals(elem.getScfID().getData(), getDigitsData1());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode_CalledAddressAndService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CompoundCriteriaImpl.class);
    	
    	DigitsImpl digits = new DigitsImpl(getDigitsData());
        CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits, SERVICE_KEY);
        BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);

        ScfIDImpl scfId = new ScfIDImpl(getDigitsData1());

        CompoundCriteriaImpl elem = new CompoundCriteriaImpl(basicGapCriteria, scfId);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "gap" })
    public void testXMLSerialize() throws Exception {

        GenericNumberImpl gn = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        Digits digits = new DigitsImpl(gn);

        CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits, SERVICE_KEY);
        CallingAddressAndServiceImpl callingAddressAndService = new CallingAddressAndServiceImpl(digits, SERVICE_KEY);
        GapOnService gapOnService = new GapOnServiceImpl(SERVICE_KEY);

        BasicGapCriteriaImpl basicGapCriteria;

        int i = 0;
        while(i < 4) {
            switch (i) {
                case 0:
                    basicGapCriteria = new BasicGapCriteriaImpl(digits);
                    test(basicGapCriteria);
                    break;
                case 1:
                    basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
                    test(basicGapCriteria);
                    break;
                case 2:
                    basicGapCriteria = new BasicGapCriteriaImpl(callingAddressAndService);
                    test(basicGapCriteria);
                    break;
                case 3:
                    basicGapCriteria = new BasicGapCriteriaImpl(gapOnService);
                    test(basicGapCriteria);
                    break;
            }
            i++;
        }
    }

    private void test(BasicGapCriteriaImpl basicGapCriteria) throws Exception {

        ScfID scfId = new ScfIDImpl(getDigitsData1());
        CompoundCriteriaImpl original = new CompoundCriteriaImpl(basicGapCriteria, scfId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "compoundCriteriaArg", CompoundCriteriaImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);

        CompoundCriteriaImpl copy = reader.read("compoundCriteriaArg", CompoundCriteriaImpl.class);

        assertTrue(isEqual(original, copy));
    }

    private boolean isEqual(CompoundCriteriaImpl o1, CompoundCriteriaImpl o2) {
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
