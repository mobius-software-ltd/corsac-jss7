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
package org.restcomm.protocols.ss7.cap.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMS;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.FCIBCCCAMELSequence1SMSImpl;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.FreeFormatDataSMSImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * 
 * @author Lasith Waruna Perera
 * @author yulianoifa
 * 
 */
public class FurnishChargingInformationSMSRequestTest {

    public byte[] getData() {
        return new byte[] { 4, 15, -96, 13, -128, 8, 48, 6, -128, 1, 3, -118, 1, 1, -127, 1, 1 };
//        return new byte[] { 4, 17, 48, 15, -96, 13, -128, 8, 48, 6, -128, 1, 3, -118, 1, 1, -127, 1, 1 };
    };

    public byte[] getFreeFormatData() {
        return new byte[] { 48, 6, -128, 1, 3, -118, 1, 1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FurnishChargingInformationSMSRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof FurnishChargingInformationSMSRequestImpl);
        
        FurnishChargingInformationSMSRequestImpl prim = (FurnishChargingInformationSMSRequestImpl)result.getResult();        
        FCIBCCCAMELSequence1SMS fcIBCCCAMELsequence1 = prim.getFCIBCCCAMELsequence1();
        assertNotNull(fcIBCCCAMELsequence1);
        assertTrue(ByteBufUtil.equals(fcIBCCCAMELsequence1.getFreeFormatData().getValue(),Unpooled.wrappedBuffer(this.getFreeFormatData())));
        assertEquals(fcIBCCCAMELsequence1.getAppendFreeFormatData(), AppendFreeFormatData.append);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FurnishChargingInformationSMSRequestImpl.class);
    	    	
        FreeFormatDataSMSImpl freeFormatData = new FreeFormatDataSMSImpl(Unpooled.wrappedBuffer(getFreeFormatData()));
        FCIBCCCAMELSequence1SMSImpl fcIBCCCAMELsequence1 = new FCIBCCCAMELSequence1SMSImpl(freeFormatData,
                AppendFreeFormatData.append);

        FurnishChargingInformationSMSRequestImpl prim = new FurnishChargingInformationSMSRequestImpl(fcIBCCCAMELsequence1);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}