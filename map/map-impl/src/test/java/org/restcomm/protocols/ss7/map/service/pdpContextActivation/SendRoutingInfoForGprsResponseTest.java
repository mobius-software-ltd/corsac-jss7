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

package org.restcomm.protocols.ss7.map.service.pdpContextActivation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class SendRoutingInfoForGprsResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 7, (byte) 128, 5, 4, (byte) 192, (byte) 168, 4, 11 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 58, (byte) 128, 5, 4, (byte) 192, (byte) 168, 4, 11, (byte) 129, 5, 4, (byte) 192, (byte) 168, 4, 22, (byte) 130, 1, 6,
                (byte) 163, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, (byte) 161, 3, 31, 32, 33 };
    }

    private byte[] getAddressData() {
        return new byte[] { (byte) 192, (byte) 168, 4, 11 };
    }

    private byte[] getAddressData2() {
        return new byte[] { (byte) 192, (byte) 168, 4, 22 };
    }

    @Test(groups = { "functional.decode", "service.pdpContextActivation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInfoForGprsResponseImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForGprsResponseImpl);
        SendRoutingInfoForGprsResponseImpl impl = (SendRoutingInfoForGprsResponseImpl)result.getResult();
        

        assertEquals(impl.getSgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(impl.getSgsnAddress().getGSNAddressData(), Unpooled.wrappedBuffer(getAddressData())));
        assertNull(impl.getGgsnAddress());
        assertNull(impl.getMobileNotReachableReason());
        assertNull(impl.getExtensionContainer());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendRoutingInfoForGprsResponseImpl);
        impl = (SendRoutingInfoForGprsResponseImpl)result.getResult();

        assertEquals(impl.getSgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(impl.getSgsnAddress().getGSNAddressData(), Unpooled.wrappedBuffer(getAddressData())));
        assertEquals(impl.getGgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(impl.getGgsnAddress().getGSNAddressData(), Unpooled.wrappedBuffer(getAddressData2())));
        assertEquals((int) impl.getMobileNotReachableReason(), 6);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(impl.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "service.pdpContextActivation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SendRoutingInfoForGprsResponseImpl.class);
    	
        GSNAddressImpl sgsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, Unpooled.wrappedBuffer(getAddressData()));
        SendRoutingInfoForGprsResponseImpl impl = new SendRoutingInfoForGprsResponseImpl(sgsnAddress, null, null, null);

        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        GSNAddressImpl ggsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, Unpooled.wrappedBuffer(getAddressData2()));
        impl = new SendRoutingInfoForGprsResponseImpl(sgsnAddress, ggsnAddress, 6, MAPExtensionContainerTest.GetTestExtensionContainer());
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}