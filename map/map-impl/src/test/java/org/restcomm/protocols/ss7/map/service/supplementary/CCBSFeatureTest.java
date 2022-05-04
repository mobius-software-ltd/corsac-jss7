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

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
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
public class CCBSFeatureTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 3, (byte) 128, 1, 1 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 20, (byte) 128, 1, 1, (byte) 129, 4, (byte) 145, 51, 51, 16, (byte) 130, 4, (byte) 145, 51, 51, 32, (byte) 163, 3, (byte) 131,
                1, 0 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CCBSFeatureImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CCBSFeatureImpl);
        CCBSFeatureImpl impl = (CCBSFeatureImpl)result.getResult();
        
        assertEquals((int)impl.getCcbsIndex(), 1);
        assertNull(impl.getBSubscriberNumber());
        assertNull(impl.getBSubscriberSubaddress());
        assertNull(impl.getBasicServiceCode());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CCBSFeatureImpl);
        impl = (CCBSFeatureImpl)result.getResult();
        
        assertEquals((int) impl.getCcbsIndex(), 1);
        assertEquals(impl.getBSubscriberNumber().getAddress(), "333301");
        assertEquals(impl.getBSubscriberSubaddress().getAddress(), "333302");
        assertEquals(impl.getBasicServiceCode().getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);

    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CCBSFeatureImpl.class);
    	
        CCBSFeatureImpl impl = new CCBSFeatureImpl(1, null, null, null);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        ISDNAddressStringImpl bSubscriberNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "333301");
        ISDNAddressStringImpl bSubscriberSubaddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "333302");
        TeleserviceCodeImpl teleservice = new TeleserviceCodeImpl(TeleserviceCodeValue.allTeleservices);
        BasicServiceCodeImpl basicServiceCode = new BasicServiceCodeImpl(teleservice);
        impl = new CCBSFeatureImpl(1, bSubscriberNumber, bSubscriberSubaddress, basicServiceCode);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}