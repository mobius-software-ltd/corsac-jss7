/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.sms.MWStatusImpl;
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
public class InformServiceCentreRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 4, 3, 2, 6, 64 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 67, 4, 6, -111, 17, 33, 34, 51, -13, 3, 2, 4, 80, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 2, 2, 2, 43, -128, 2, 1, -68 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(InformServiceCentreRequestImpl.class);
    	                
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InformServiceCentreRequestImpl);
        InformServiceCentreRequestImpl isc = (InformServiceCentreRequestImpl)result.getResult();   
        
        MWStatusImpl mwStatus = isc.getMwStatus();
        assertNotNull(mwStatus);
        assertFalse(mwStatus.getScAddressNotIncluded());
        assertTrue(mwStatus.getMnrfSet());
        assertFalse(mwStatus.getMcefSet());
        assertFalse(mwStatus.getMnrgSet());

        rawData = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InformServiceCentreRequestImpl);
        isc = (InformServiceCentreRequestImpl)result.getResult();   

        MAPExtensionContainerImpl extensionContainer = isc.getExtensionContainer();
        ISDNAddressStringImpl storedMSISDN = isc.getStoredMSISDN();
        mwStatus = isc.getMwStatus();
        int absentSubscriberDiagnosticSM = isc.getAbsentSubscriberDiagnosticSM();
        int additionalAbsentSubscriberDiagnosticSM = isc.getAdditionalAbsentSubscriberDiagnosticSM();

        assertNotNull(storedMSISDN);
        assertEquals(AddressNature.international_number, storedMSISDN.getAddressNature());
        assertEquals(NumberingPlan.ISDN, storedMSISDN.getNumberingPlan());
        assertEquals("111222333", storedMSISDN.getAddress());
        assertNotNull(mwStatus);
        assertFalse(mwStatus.getScAddressNotIncluded());
        assertTrue(mwStatus.getMnrfSet());
        assertFalse(mwStatus.getMcefSet());
        assertTrue(mwStatus.getMnrgSet());
        assertNotNull(absentSubscriberDiagnosticSM);
        assertEquals(555, (int) absentSubscriberDiagnosticSM);
        assertNotNull(additionalAbsentSubscriberDiagnosticSM);
        assertEquals(444, (int) additionalAbsentSubscriberDiagnosticSM);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(InformServiceCentreRequestImpl.class);

        MWStatusImpl mwStatus = new MWStatusImpl(false, true, false, false);
        InformServiceCentreRequestImpl isc = new InformServiceCentreRequestImpl(null, mwStatus, null, null, null);

        ByteBuf buffer=parser.encode(isc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        ISDNAddressStringImpl storedMSISDN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "111222333");
        mwStatus = new MWStatusImpl(false, true, false, true);
        Integer absentSubscriberDiagnosticSM = 555;
        Integer additionalAbsentSubscriberDiagnosticSM = 444;
        isc = new InformServiceCentreRequestImpl(storedMSISDN, mwStatus, MAPExtensionContainerTest.GetTestExtensionContainer(),
                absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM);

        buffer=parser.encode(isc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
