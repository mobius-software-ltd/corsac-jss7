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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
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
public class SSDataTest {

    private byte[] getEncodedData1() {
        return new byte[] { 48, 20, 4, 1, 18, (byte) 132, 1, 10, (byte) 130, 1, 2, 48, 3, (byte) 131, 1, 99, 2, 1, 3, (byte) 133, 1, 6 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SSDataImpl.class);
        
        byte[] rawData = getEncodedData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SSDataImpl);
        SSDataImpl impl = (SSDataImpl)result.getResult();

        assertEquals(impl.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.clir);

        assertTrue(impl.getSsStatus().getQBit());
        assertFalse(impl.getSsStatus().getPBit());
        assertTrue(impl.getSsStatus().getRBit());
        assertFalse(impl.getSsStatus().getABit());
        assertEquals(impl.getSsSubscriptionOption().getCliRestrictionOption(), CliRestrictionOption.temporaryDefaultAllowed);

        assertEquals(impl.getBasicServiceGroupList().size(), 1);
        assertEquals(impl.getBasicServiceGroupList().get(0).getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.facsimileGroup4);

        assertEquals(impl.getDefaultPriority(), EMLPPPriority.priorityLevel3);
        assertEquals((int) impl.getNbrUser(), 6);

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SSDataImpl.class);
                
    	SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.clir);
        SSStatusImpl ssStatus = new SSStatusImpl(true, false, true, false);
        SSSubscriptionOptionImpl ssSubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.temporaryDefaultAllowed);

        List<BasicServiceCode> basicServiceGroupList = new ArrayList<BasicServiceCode>();
        TeleserviceCodeImpl teleserviceCode = new TeleserviceCodeImpl(TeleserviceCodeValue.facsimileGroup4);
        BasicServiceCodeImpl bsc = new BasicServiceCodeImpl(teleserviceCode);
        basicServiceGroupList.add(bsc);

        SSDataImpl impl = new SSDataImpl(ssCode, ssStatus, ssSubscriptionOption, basicServiceGroupList, EMLPPPriority.priorityLevel3, 6);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
