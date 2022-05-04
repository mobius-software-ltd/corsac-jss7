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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ForwardingInfoTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 7, 48, 5, 48, 3, (byte) 131, 1, (byte) 144 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 10, 4, 1, 33, 48, 5, 48, 3, (byte) 131, 1, (byte) 144 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ForwardingInfoImpl.class);
    	        
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ForwardingInfoImpl);
        ForwardingInfoImpl impl = (ForwardingInfoImpl)result.getResult();

        assertNull(impl.getSsCode());

        assertEquals(impl.getForwardingFeatureList().size(), 1);
        assertEquals(impl.getForwardingFeatureList().get(0).getBasicService().getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allVoiceGroupCallServices);

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ForwardingInfoImpl);
        impl = (ForwardingInfoImpl)result.getResult();

        assertEquals(impl.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);

        assertEquals(impl.getForwardingFeatureList().size(), 1);
        assertEquals(impl.getForwardingFeatureList().get(0).getBasicService().getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allVoiceGroupCallServices);
    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ForwardingInfoImpl.class);
        
        TeleserviceCodeImpl teleservice = new TeleserviceCodeImpl(TeleserviceCodeValue.allVoiceGroupCallServices);
        BasicServiceCodeImpl basicService = new BasicServiceCodeImpl(teleservice);
        List<ForwardingFeature> forwardingFeatureList = new ArrayList<ForwardingFeature>();
        ForwardingFeatureImpl forwardingFeature = new ForwardingFeatureImpl(basicService, null, null, null, null, null, null);
        forwardingFeatureList.add(forwardingFeature);
        ForwardingInfo impl = new ForwardingInfoImpl(null, forwardingFeatureList);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        impl = new ForwardingInfoImpl(ssCode, forwardingFeatureList);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}