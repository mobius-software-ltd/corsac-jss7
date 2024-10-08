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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeature;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.junit.Test;

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
public class InterrogateSSResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { (byte) 128, 1, 1 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { (byte) 162, 3, (byte) 131, 1, 96 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { (byte) 163, 5, 48, 3, (byte) 132, 1, 1 };
    }

    private byte[] getEncodedData4() {
        return new byte[] { (byte) 164, 3, 4, 1, 1 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(InterrogateSSResponseImpl.class);
    	        
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InterrogateSSResponseImpl);
        InterrogateSSResponseImpl impl = (InterrogateSSResponseImpl)result.getResult();
        
        assertTrue(impl.getSsStatus().getABit());
        assertFalse(impl.getSsStatus().getPBit());
        assertFalse(impl.getSsStatus().getQBit());
        assertFalse(impl.getSsStatus().getRBit());

        assertNull(impl.getBasicServiceGroupList());
        assertNull(impl.getForwardingFeatureList());
        assertNull(impl.getGenericServiceInfo());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InterrogateSSResponseImpl);
        impl = (InterrogateSSResponseImpl)result.getResult();

        assertNull(impl.getSsStatus());

        assertEquals(impl.getBasicServiceGroupList().size(), 1);
        assertEquals(impl.getBasicServiceGroupList().get(0).getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allFacsimileTransmissionServices);

        assertNull(impl.getForwardingFeatureList());
        assertNull(impl.getGenericServiceInfo());


        rawData = getEncodedData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InterrogateSSResponseImpl);
        impl = (InterrogateSSResponseImpl)result.getResult();

        assertNull(impl.getSsStatus());
        assertNull(impl.getBasicServiceGroupList());

        assertEquals(impl.getForwardingFeatureList().size(), 1);
        assertTrue(impl.getForwardingFeatureList().get(0).getSsStatus().getABit());
        assertFalse(impl.getForwardingFeatureList().get(0).getSsStatus().getPBit());

        assertNull(impl.getGenericServiceInfo());


        rawData = getEncodedData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InterrogateSSResponseImpl);
        impl = (InterrogateSSResponseImpl)result.getResult();

        assertNull(impl.getSsStatus());
        assertNull(impl.getBasicServiceGroupList());
        assertNull(impl.getForwardingFeatureList());

        assertTrue(impl.getGenericServiceInfo().getSsStatus().getABit());
        assertFalse(impl.getGenericServiceInfo().getSsStatus().getPBit());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(InterrogateSSResponseImpl.class);
    	                
        SSStatusImpl ssStatus = new SSStatusImpl(false, false, false, true);
        InterrogateSSResponseImpl impl = new InterrogateSSResponseImpl(ssStatus);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        List<BasicServiceCode> basicServiceGroupList = new ArrayList<BasicServiceCode>();
        TeleserviceCodeImpl teleservice = new TeleserviceCodeImpl(TeleserviceCodeValue.allFacsimileTransmissionServices);
        BasicServiceCodeImpl item = new BasicServiceCodeImpl(teleservice);
        basicServiceGroupList.add(item);
        impl = new InterrogateSSResponseImpl(basicServiceGroupList, false);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));


        List<ForwardingFeature> forwardingFeatureList = new ArrayList<ForwardingFeature>();
        ForwardingFeatureImpl item2 = new ForwardingFeatureImpl(null, ssStatus, null, null, null, null, null);
        forwardingFeatureList.add(item2);
        impl = new InterrogateSSResponseImpl(forwardingFeatureList);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));


        GenericServiceInfoImpl genericServiceInfo = new GenericServiceInfoImpl(ssStatus, null, null, null, null, null, null, null);
        impl = new InterrogateSSResponseImpl(genericServiceInfo);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData4();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}