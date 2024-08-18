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

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.supplementary.Password;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtCallBarringFeatureImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.PasswordImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author vadim subbotin
 * @author yulianoifa
 *
 */
public class CallBarringDataTest {
    private byte[] data = {48, 62, 48, 8, 48, 6, -126, 1, 0, -124, 1, 8, 18, 4, 48, 48, 48, 48, 2, 1, 3, 5, 0, 48, 39,
            -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
            23, 24, 25, 26, -95, 3, 31, 32, 33};

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallBarringDataImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallBarringDataImpl);
        CallBarringDataImpl callBarringData = (CallBarringDataImpl)result.getResult();

        assertEquals(callBarringData.getWrongPasswordAttemptsCounter().intValue(), 3);
        assertTrue(callBarringData.getNotificationToCSE());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(callBarringData.getExtensionContainer()));

        List<ExtCallBarringFeature> extCallBarringFeatures = callBarringData.getCallBarringFeatureList();
        assertNotNull(extCallBarringFeatures);
        assertEquals(extCallBarringFeatures.size(), 1);

        ExtCallBarringFeature extCallBarringFeature = extCallBarringFeatures.get(0);
        ExtBasicServiceCode extBasicServiceCode = extCallBarringFeature.getBasicService();
        ExtSSStatus extSSStatus = extCallBarringFeature.getSsStatus();
        assertEquals(extBasicServiceCode.getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.allBearerServices);
        assertTrue(extSSStatus.getBitQ());
        assertFalse(extSSStatus.getBitP());
        assertFalse(extSSStatus.getBitR());
        assertFalse(extSSStatus.getBitA());

        Password password = callBarringData.getPassword();
        assertNotNull(password);
        assertEquals(password.getData(), "0000");
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CallBarringDataImpl.class);
    	        
        ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allBearerServices));
        final ExtCallBarringFeatureImpl extCallBarringFeature = new ExtCallBarringFeatureImpl(extBasicServiceCode,
                new ExtSSStatusImpl(true, false, false, false), null);
        
        List<ExtCallBarringFeature> extCallBarringFeatureList=new ArrayList<ExtCallBarringFeature>();
        extCallBarringFeatureList.add(extCallBarringFeature);
        CallBarringDataImpl callBarringData = new CallBarringDataImpl(extCallBarringFeatureList,
                new PasswordImpl("0000"), 3, true, MAPExtensionContainerTest.GetTestExtensionContainer());

        ByteBuf buffer=parser.encode(callBarringData);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}
