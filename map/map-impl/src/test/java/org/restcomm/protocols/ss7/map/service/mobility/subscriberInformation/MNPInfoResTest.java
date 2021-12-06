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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
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
public class MNPInfoResTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 23, -128, 3, -112, 120, -10, -127, 6, 82, 48, 3, 33, 67, -11, -126, 5, -111, 17, 34, 51, 68,
                -125, 1, 5 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MNPInfoResImpl.class);
        
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MNPInfoResImpl);
        MNPInfoResImpl impl = (MNPInfoResImpl)result.getResult();
        
        assertTrue(impl.getRouteingNumber().getRouteingNumber().equals("09876"));
        assertTrue(impl.getIMSI().getData().equals("25033012345"));
        assertTrue(impl.getMSISDN().getAddress().equals("11223344"));
        assertEquals(impl.getMSISDN().getAddressNature(), AddressNature.international_number);
        assertEquals(impl.getMSISDN().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(impl.getNumberPortabilityStatus(), NumberPortabilityStatus.foreignNumberPortedIn);
        assertNull(impl.getExtensionContainer());

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MNPInfoResImpl.class);
        
        RouteingNumberImpl rn = new RouteingNumberImpl("09876");
        IMSIImpl imsi = new IMSIImpl("25033012345");
        ISDNAddressStringImpl isdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "11223344");

        MNPInfoResImpl impl = new MNPInfoResImpl(rn, imsi, isdn, NumberPortabilityStatus.foreignNumberPortedIn, null);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, getEncodedData()));
    }
}
