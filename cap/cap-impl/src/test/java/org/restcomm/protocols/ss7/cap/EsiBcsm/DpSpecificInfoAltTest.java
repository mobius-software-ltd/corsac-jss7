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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
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
public class DpSpecificInfoAltTest {

    public byte[] getData1() {
        return new byte[] { 48, 25, (byte) 160, 5, (byte) 160, 3, (byte) 130, 1, 38, (byte) 161, 5, (byte) 160, 3, (byte) 131, 1, 98, (byte) 162, 9,
                (byte) 128, 7, 0, 0, 51, 51, 19, 17, 17 };
    }

    @Test(groups = { "functional.decode", "EsiBcsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DpSpecificInfoAltImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DpSpecificInfoAltImpl);
        
        DpSpecificInfoAltImpl elem = (DpSpecificInfoAltImpl)result.getResult();        
        assertEquals(elem.getOServiceChangeSpecificInfo().getExtBasicServiceCode().getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.padAccessCA_9600bps);
        assertEquals(elem.getCollectedInfoSpecificInfo().getCalledPartyNumber().getCalledPartyNumber().getAddress(), "3333311111");
        assertEquals(elem.getTServiceChangeSpecificInfo().getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.automaticFacsimileGroup3);
    }

    @Test(groups = { "functional.encode", "EsiBcsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DpSpecificInfoAltImpl.class);
    	
        ExtBearerServiceCodeImpl extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(extBearerService);
        OServiceChangeSpecificInfoImpl oServiceChangeSpecificInfo = new OServiceChangeSpecificInfoImpl(extBasicServiceCode);
        CalledPartyNumber calledPartyNumber = new CalledPartyNumberImpl();
        calledPartyNumber.setAddress("3333311111");
        CalledPartyNumberIsupImpl calledPartyNumberCap = new CalledPartyNumberIsupImpl(calledPartyNumber);
        CollectedInfoSpecificInfoImpl collectedInfoSpecificInfo = new CollectedInfoSpecificInfoImpl(calledPartyNumberCap);
        ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.automaticFacsimileGroup3);
        ExtBasicServiceCodeImpl extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleservice);
        TServiceChangeSpecificInfoImpl tServiceChangeSpecificInfo = new TServiceChangeSpecificInfoImpl(extBasicServiceCode2);
        DpSpecificInfoAltImpl elem = new DpSpecificInfoAltImpl(oServiceChangeSpecificInfo, collectedInfoSpecificInfo, tServiceChangeSpecificInfo);
//        OServiceChangeSpecificInfo oServiceChangeSpecificInfo, CollectedInfoSpecificInfo collectedInfoSpecificInfo,
//        TServiceChangeSpecificInfo tServiceChangeSpecificInfo
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
