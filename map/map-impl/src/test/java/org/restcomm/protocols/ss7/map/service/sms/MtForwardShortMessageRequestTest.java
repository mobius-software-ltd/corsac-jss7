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

package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.smstpdu.SmsTpduImpl;
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
public class MtForwardShortMessageRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 60, -128, 8, 16, 33, 34, 34, 17, -126, 21, -12, -124, 7, -111, -127, 33, 105, 0, -112, -10, 4, 
        		39, -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3, -21, 2, 1, -112, 101, 
        		54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 102, -128, 8, 1, -128, 56, 67, 84, 101, 118, -9, -124, 6, -111, 17, 17, 33, 34, 34, 4, 39, -28, 
        		10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3, -21, 2, 1, -112, 101, 54, -5, -51, 
        		2, -35, -33, 114, 54, 25, 20, 10, -123, 0, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 
        		5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MtForwardShortMessageRequestImpl.class);
                        
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MtForwardShortMessageRequestImpl);
        MtForwardShortMessageRequestImpl ind = (MtForwardShortMessageRequestImpl)result.getResult();   
        
        SM_RP_DA da = ind.getSM_RP_DA();
        SM_RP_OA oa = ind.getSM_RP_OA();
        SmsSignalInfo ui = ind.getSM_RP_UI();
        // assertEquals( (long) da.getIMSI().getMCC(),11);
        // assertEquals( (long) da.getIMSI().getMNC(),22);
        assertEquals(da.getIMSI().getData(), "011222221128514");
        assertEquals(oa.getServiceCentreAddressOA().getAddressNature(), AddressNature.international_number);
        assertEquals(oa.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oa.getServiceCentreAddressOA().getAddress(), "18129600096");
        
        ByteBuf buffer=Unpooled.buffer();
        ui.decodeTpdu(false).encodeData(buffer);
        assertTrue(ByteBufUtil.equals(buffer,Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3, -21, 2, 1,
                -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 })));

        rawData = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MtForwardShortMessageRequestImpl);
        ind = (MtForwardShortMessageRequestImpl)result.getResult();   

        da = ind.getSM_RP_DA();
        oa = ind.getSM_RP_OA();
        ui = ind.getSM_RP_UI();
        Boolean moreMesToSend = ind.getMoreMessagesToSend();
        // assertEquals( (long) da.getIMSI().getMCC(),100);
        // assertEquals( (long) da.getIMSI().getMNC(),88);
        assertEquals(da.getIMSI().getData(), "100883344556677");
        assertEquals(oa.getServiceCentreAddressOA().getAddressNature(), AddressNature.international_number);
        assertEquals(oa.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oa.getServiceCentreAddressOA().getAddress(), "1111122222");
        
        buffer=Unpooled.buffer();
        ui.decodeTpdu(false).encodeData(buffer);
        assertTrue(ByteBufUtil.equals(buffer,Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3, -21, 2, 1,
                -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 })));
        
        assertTrue(moreMesToSend);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MtForwardShortMessageRequestImpl.class);
                
        IMSIImpl imsi = new IMSIImpl("011222221128514");
        SM_RP_DAImpl sm_RP_DA = new SM_RP_DAImpl(imsi);
        AddressStringImpl sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "18129600096");
        SM_RP_OAImpl sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(sca);
        SmsSignalInfoImpl sm_RP_UI = new SmsSignalInfoImpl(SmsTpduImpl.createInstance(Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3, -21, 2, 1,
                -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 }), false, null), null);
        MtForwardShortMessageRequestImpl ind = new MtForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, false, null);

        ByteBuf buffer=parser.encode(ind);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("100883344556677");
        sm_RP_DA = new SM_RP_DAImpl(imsi);
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1111122222");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(sca);
        sm_RP_UI = new SmsSignalInfoImpl(SmsTpduImpl.createInstance(Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3, -21, 2, 1,
                -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 }), false, null), null);
        ind = new MtForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, true,
                MAPExtensionContainerTest.GetTestExtensionContainer());

        buffer=parser.encode(ind);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
