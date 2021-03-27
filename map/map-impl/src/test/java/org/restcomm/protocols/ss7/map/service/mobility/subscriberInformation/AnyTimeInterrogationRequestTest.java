/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfoImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author abhayani
 *
 */
public class AnyTimeInterrogationRequestTest {
    // Real Trace
    byte[] data = new byte[] { 0x30, 0x1a, (byte) 0xa0, 0x09, (byte) 0x81, 0x07, (byte) 0x91, 0x55, 0x43, (byte) 0x99, 0x77,
            0x15, 0x09, (byte) 0xa1, 0x04, (byte) 0x80, 0x00, (byte) 0x81, 0x00, (byte) 0x83, 0x07, (byte) 0x91, 0x55, 0x43,
            0x69, 0x26, (byte) 0x99, 0x34 };

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AnyTimeInterrogationRequestImpl.class);
    	
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AnyTimeInterrogationRequestImpl);
        AnyTimeInterrogationRequestImpl anyTimeInt = (AnyTimeInterrogationRequestImpl)result.getResult();

        SubscriberIdentityImpl subsId = anyTimeInt.getSubscriberIdentity();
        ISDNAddressStringImpl isdnAddress = subsId.getMSISDN();
        assertEquals(isdnAddress.getAddress(), "553499775190");

        ISDNAddressStringImpl gscmSCFAddress = anyTimeInt.getGsmSCFAddress();
        assertEquals(gscmSCFAddress.getAddress(), "553496629943");

        RequestedInfoImpl requestedInfo = anyTimeInt.getRequestedInfo();
        assertTrue(requestedInfo.getLocationInformation());
        assertTrue(requestedInfo.getSubscriberState());
        DomainType domainType = requestedInfo.getRequestedDomain();
        assertNull(domainType);

    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
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