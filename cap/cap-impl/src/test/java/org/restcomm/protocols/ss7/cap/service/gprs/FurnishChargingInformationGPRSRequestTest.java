/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.restcomm.protocols.ss7.cap.service.gprs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.FCIBCCCAMELSequence1GprsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.FreeFormatDataGprsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
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
 *
 */
public class FurnishChargingInformationGPRSRequestTest {

    public byte[] getData() {
        return new byte[] { 4, 20, 48, 18, -96, 16, -128, 8, 48, 6, -128, 1, 5, -127, 1, 2, -127, 1, 2, -126, 1, 1 };
    };

    public byte[] getFreeFormatData() {
        return new byte[] { 48, 6, -128, 1, 5, -127, 1, 2 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FurnishChargingInformationGPRSRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof FurnishChargingInformationGPRSRequestImpl);
        
        FurnishChargingInformationGPRSRequestImpl prim = (FurnishChargingInformationGPRSRequestImpl)result.getResult();        
        assertTrue(ByteBufUtil.equals(prim.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getFreeFormatData().getValue(),
                Unpooled.wrappedBuffer(this.getFreeFormatData())));
        assertEquals(prim.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getPDPID().getId(), 2);
        assertEquals(prim.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getAppendFreeFormatData(),
                AppendFreeFormatData.append);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FurnishChargingInformationGPRSRequestImpl.class);
    	
        FreeFormatDataGprsImpl freeFormatData = new FreeFormatDataGprsImpl(Unpooled.wrappedBuffer(this.getFreeFormatData()));
        PDPIDImpl pdpID = new PDPIDImpl(2);
        FCIBCCCAMELSequence1GprsImpl fcIBCCCAMELsequence1 = new FCIBCCCAMELSequence1GprsImpl(freeFormatData, pdpID,
                AppendFreeFormatData.append);

        CAMELFCIGPRSBillingChargingCharacteristicsImpl fciGPRSBillingChargingCharacteristics = new CAMELFCIGPRSBillingChargingCharacteristicsImpl(
                fcIBCCCAMELsequence1);

        FurnishChargingInformationGPRSRequestImpl prim = new FurnishChargingInformationGPRSRequestImpl(
                fciGPRSBillingChargingCharacteristics);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}