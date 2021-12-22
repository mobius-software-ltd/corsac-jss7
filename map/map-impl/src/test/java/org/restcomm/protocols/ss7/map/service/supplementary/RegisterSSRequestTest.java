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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BearerServiceCodeImpl;
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
public class RegisterSSRequestTest {

    private byte[] getEncodedData1() {
        return new byte[] { 48, 36, 4, 1, 18, -126, 1, 35, -124, 5, -111, 17, 1, 0, -15, -122, 5, -111, 17, 1, 0, -14, -123, 1, 1, -121, 1, 6, -120, 1, 2,
                -119, 5, -111, 17, 1, 0, -13 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RegisterSSRequestImpl.class);
    	        
        byte[] rawData = getEncodedData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RegisterSSRequestImpl);
        RegisterSSRequestImpl impl = (RegisterSSRequestImpl)result.getResult();
        
        assertEquals(impl.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.clir);
        assertEquals(impl.getBasicService().getBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_1200_75bps);
        assertEquals(impl.getForwardedToNumber().getAddress(), "1110001");
        assertEquals(impl.getForwardedToSubaddress().getAddress(), "1110002");
        assertEquals((int)impl.getNoReplyConditionTime(), 1);
        assertEquals(impl.getDefaultPriority(), EMLPPPriority.priorityLevelA);
        assertEquals((int)impl.getNbrUser(), 2);
        assertEquals(impl.getLongFTNSupported().getAddress(), "1110003");

    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RegisterSSRequestImpl.class);
    	                
    	SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.clir);
        BearerServiceCodeImpl bearerService = new BearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_1200_75bps);
        BasicServiceCodeImpl basicService = new BasicServiceCodeImpl(bearerService);
        AddressStringImpl forwardedToNumber = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1110001");
        ISDNAddressStringImpl forwardedToSubaddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1110002");
        ISDNAddressStringImpl longFTNSupported = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1110003");;

        RegisterSSRequestImpl impl = new RegisterSSRequestImpl(ssCode, basicService, forwardedToNumber, forwardedToSubaddress, 1, EMLPPPriority.priorityLevelA, 2, longFTNSupported);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}