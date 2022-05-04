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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.FCIBCCCAMELSequence1Impl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.FreeFormatDataImpl;
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
public class FurnishChargingInformationTest {

    public byte[] getData1() {
        return new byte[] { 4, 16, (byte) 160, 14, (byte) 128, 4, 4, 5, 6, 7, (byte) 161, 3, (byte) 128, 1, 2, (byte) 130, 1, 1 };
    }

    public byte[] getDataFFD() {
        return new byte[] { 4, 5, 6, 7 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FurnishChargingInformationRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof FurnishChargingInformationRequestImpl);
        
        FurnishChargingInformationRequestImpl elem = (FurnishChargingInformationRequestImpl)result.getResult();        
        assertTrue(ByteBufUtil.equals(elem.getFCIBCCCAMELsequence1().getFreeFormatData().getValue(), Unpooled.wrappedBuffer(this.getDataFFD())));
        assertEquals(elem.getFCIBCCCAMELsequence1().getPartyToCharge(), LegType.leg2);
        assertEquals(elem.getFCIBCCCAMELsequence1().getAppendFreeFormatData(), AppendFreeFormatData.append);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FurnishChargingInformationRequestImpl.class);
    	
        FreeFormatDataImpl ffd = new FreeFormatDataImpl(Unpooled.wrappedBuffer(getDataFFD()));
        FCIBCCCAMELSequence1Impl fci = new FCIBCCCAMELSequence1Impl(ffd, LegType.leg2, AppendFreeFormatData.append);
        FurnishChargingInformationRequestImpl elem = new FurnishChargingInformationRequestImpl(fci);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
