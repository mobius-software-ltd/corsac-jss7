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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GenericServiceInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatusImpl;
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

    @Test(groups = { "functional.decode", "service.supplementary" })
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

    @Test(groups = { "functional.encode", "service.supplementary" })
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


        List<BasicServiceCodeImpl> basicServiceGroupList = new ArrayList<BasicServiceCodeImpl>();
        TeleserviceCodeImpl teleservice = new TeleserviceCodeImpl(TeleserviceCodeValue.allFacsimileTransmissionServices);
        BasicServiceCodeImpl item = new BasicServiceCodeImpl(teleservice);
        basicServiceGroupList.add(item);
        impl = new InterrogateSSResponseImpl(basicServiceGroupList, false);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));


        List<ForwardingFeatureImpl> forwardingFeatureList = new ArrayList<ForwardingFeatureImpl>();
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