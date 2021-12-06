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
package org.restcomm.protocols.ss7.cap.service.sms;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.SMSAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class ConnectSMSRequestTest {

    public byte[] getData() {
        return new byte[] { 48, 48, -128, 9, -111, 33, 67, 101, -121, 25, 50, 84, 118, -127, 7, -111, 20, -121, 8, 80,
                64, -9, -126, 6, -111, 34, 112, 87, 0, -128, -86, 18, 48, 5, 2, 1, 2, -127, 0, 48, 9, 2, 1, 3, 10, 1,
                1, -127, 1, -1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ConnectSMSRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ConnectSMSRequestImpl);
        
        ConnectSMSRequestImpl prim = (ConnectSMSRequestImpl)result.getResult();        
        CalledPartyBCDNumber destinationSubscriberNumber = prim.getDestinationSubscriberNumber();
        SMSAddressString callingPartyNumber = prim.getCallingPartysNumber();

        assertNotNull(destinationSubscriberNumber);
        assertTrue(destinationSubscriberNumber.getAddress().equals("41788005047"));

        assertNotNull(callingPartyNumber);
        assertTrue(callingPartyNumber.getAddress().equals("1234567891234567"));

        ISDNAddressString smscAddress = prim.getSMSCAddress();
        assertNotNull(smscAddress);
        assertTrue(smscAddress.getAddress().equals("2207750008"));

        // extensions
        CAPExtensionsTest.checkTestCAPExtensions(prim.getExtensions());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ConnectSMSRequestImpl.class);
    	
        SMSAddressStringImpl callingPartysNumber = new SMSAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "1234567891234567");
        CalledPartyBCDNumberImpl destinationSubscriberNumber = new CalledPartyBCDNumberImpl(
                AddressNature.international_number, NumberingPlan.ISDN, "41788005047");
        ISDNAddressStringImpl smscAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "2207750008");
        CAPExtensions extensions = CAPExtensionsTest.createTestCAPExtensions();

        ConnectSMSRequestImpl prim = new ConnectSMSRequestImpl(callingPartysNumber, destinationSubscriberNumber,
                smscAddress, extensions);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
