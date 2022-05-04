package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;
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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtTeleserviceCodeImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 * @author yulianoifa
 */
public class MSISDNBSTest {
    private byte[] data = {48, 54, 4, 6, -111, 17, 33, 34, 51, -13, -96, 3, -125, 1, 96, -95, 39, -96, 32, 48, 10, 6,
            3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95,
            3, 31, 32, 33};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MSISDNBSImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MSISDNBSImpl);
        MSISDNBSImpl msisdnbs = (MSISDNBSImpl)result.getResult();

        assertNotNull(msisdnbs.getMsisdn());
        assertNotNull(msisdnbs.getBasicServiceList());
        assertEquals(msisdnbs.getBasicServiceList().size(), 1);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(msisdnbs.getExtensionContainer()));

        ExtBasicServiceCode extBasicServiceCode = msisdnbs.getBasicServiceList().get(0);
        assertEquals(extBasicServiceCode.getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allFacsimileTransmissionServices);

        ISDNAddressString msisdn = msisdnbs.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "111222333");
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MSISDNBSImpl.class);
    	        
    	ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        final ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(
                new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allFacsimileTransmissionServices));
        
        List<ExtBasicServiceCode> extBasicServiceCodeList=new ArrayList<ExtBasicServiceCode>();
        extBasicServiceCodeList.add(extBasicServiceCode);
        MSISDNBSImpl msisdnbs = new MSISDNBSImpl(msisdn, extBasicServiceCodeList,MAPExtensionContainerTest.GetTestExtensionContainer());

        ByteBuf buffer=parser.encode(msisdnbs);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}
