package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 * @author yulianoifa
 */
public class CallWaitingDataTest {
    private byte[] data = {48, 14, -95, 10, 48, 8, -95, 3, -125, 1, 0, -126, 1, 15, -126, 0};

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallWaitingDataImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallWaitingDataImpl);
        CallWaitingDataImpl callWaitingData = (CallWaitingDataImpl)result.getResult();

        assertNotNull(callWaitingData.getCwFeatureList());
        assertEquals(callWaitingData.getCwFeatureList().size(), 1);
        assertTrue(callWaitingData.getNotificationToCSE());

        ExtCwFeature extCwFeature = callWaitingData.getCwFeatureList().get(0);
        assertNotNull(extCwFeature.getSsStatus());
        assertTrue(extCwFeature.getSsStatus().getBitQ());
        assertTrue(extCwFeature.getSsStatus().getBitP());
        assertTrue(extCwFeature.getSsStatus().getBitR());
        assertTrue(extCwFeature.getSsStatus().getBitA());
        assertEquals(extCwFeature.getBasicService().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallWaitingDataImpl.class);
    	        
        ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allTeleservices));
        final ExtCwFeatureImpl extCwFeature = new ExtCwFeatureImpl(extBasicServiceCode, new ExtSSStatusImpl(true, true, true, true));
        
        List<ExtCwFeature> extCwFeatureList=new ArrayList<ExtCwFeature>();
        extCwFeatureList.add(extCwFeature);
        CallWaitingDataImpl callWaitingData = new CallWaitingDataImpl(extCwFeatureList, true);

        ByteBuf buffer=parser.encode(callWaitingData);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}
