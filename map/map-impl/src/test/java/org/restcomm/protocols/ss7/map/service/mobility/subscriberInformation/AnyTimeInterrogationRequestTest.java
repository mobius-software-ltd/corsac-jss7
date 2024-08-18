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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.restcomm.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author abhayani
 * @author yulianoifa
 *
 */
public class AnyTimeInterrogationRequestTest {
    // Real Trace
    byte[] data = new byte[] { 0x30, 0x1a, (byte) 0xa0, 0x09, (byte) 0x81, 0x07, (byte) 0x91, 0x55, 0x43, (byte) 0x99, 0x77,
            0x15, 0x09, (byte) 0xa1, 0x04, (byte) 0x80, 0x00, (byte) 0x81, 0x00, (byte) 0x83, 0x07, (byte) 0x91, 0x55, 0x43,
            0x69, 0x26, (byte) 0x99, 0x34 };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AnyTimeInterrogationRequestImpl.class);
    	
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AnyTimeInterrogationRequestImpl);
        AnyTimeInterrogationRequestImpl anyTimeInt = (AnyTimeInterrogationRequestImpl)result.getResult();

        SubscriberIdentity subsId = anyTimeInt.getSubscriberIdentity();
        ISDNAddressString isdnAddress = subsId.getMSISDN();
        assertEquals(isdnAddress.getAddress(), "553499775190");

        ISDNAddressString gscmSCFAddress = anyTimeInt.getGsmSCFAddress();
        assertEquals(gscmSCFAddress.getAddress(), "553496629943");

        RequestedInfo requestedInfo = anyTimeInt.getRequestedInfo();
        assertTrue(requestedInfo.getLocationInformation());
        assertTrue(requestedInfo.getSubscriberState());
        DomainType domainType = requestedInfo.getRequestedDomain();
        assertNull(domainType);

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AnyTimeInterrogationRequestImpl.class);
    	
        ISDNAddressStringImpl isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553499775190");
        SubscriberIdentityImpl subsId = new SubscriberIdentityImpl(isdnAdd);
        RequestedInfoImpl requestedInfo = new RequestedInfoImpl(true, true, null, false, null, false, false, false);
        ISDNAddressStringImpl gscmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553496629943");

        AnyTimeInterrogationRequestImpl anyTimeInt = new AnyTimeInterrogationRequestImpl(subsId, requestedInfo, gscmSCFAddress,
                null);

        ByteBuf buffer=parser.encode(anyTimeInt);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}