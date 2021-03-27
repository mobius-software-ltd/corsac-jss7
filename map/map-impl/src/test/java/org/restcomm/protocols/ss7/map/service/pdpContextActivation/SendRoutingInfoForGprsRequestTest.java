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

package org.restcomm.protocols.ss7.map.service.pdpContextActivation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
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
public class SendRoutingInfoForGprsRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 16, (byte) 128, 7, 17, 17, 33, 34, 34, 51, (byte) 243, (byte) 130, 5, (byte) 145, (byte) 136, (byte) 136, 0, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 70, -128, 7, 17, 17, 33, 34, 34, 51, -13, -127, 5, 4, -64, -88, 4, 22, -126, 5, -111, -120, -120, 0, 0, -93, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33 };
    }

    private byte[] getAddressData() {
        return new byte[] { (byte) 192, (byte) 168, 4, 22 };
    }

    @Test(groups = { "functional.decode", "service.pdpContextActivation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInfoForGprsRequestImpl.class);
    	
        byte[] rawData = getEncodedData();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForGprsRequestImpl);
        SendRoutingInfoForGprsRequestImpl impl = (SendRoutingInfoForGprsRequestImpl)result.getResult();
        
        assertEquals(impl.getImsi().getData(), "1111122222333");
        assertNull(impl.getGgsnAddress());
        assertEquals(impl.getGgsnNumber().getAddress(), "88880000");
        assertNull(impl.getExtensionContainer());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForGprsRequestImpl);
        impl = (SendRoutingInfoForGprsRequestImpl)result.getResult();

        assertEquals(impl.getImsi().getData(), "1111122222333");
        assertEquals(impl.getGgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(impl.getGgsnAddress().getGSNAddressData(), getAddressData());
        assertEquals(impl.getGgsnNumber().getAddress(), "88880000");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(impl.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.pdpContextActivation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInfoForGprsRequestImpl.class);
    	
        IMSIImpl imsi = new IMSIImpl("1111122222333");
        ISDNAddressStringImpl ggsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "88880000");
        SendRoutingInfoForGprsRequestImpl impl = new SendRoutingInfoForGprsRequestImpl(imsi, null, ggsnNumber, null);

        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        GSNAddressImpl ggsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, getAddressData());
        impl = new SendRoutingInfoForGprsRequestImpl(imsi, ggsnAddress, ggsnNumber, MAPExtensionContainerTest.GetTestExtensionContainer());

        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}