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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.FQDN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SpecificAPNInfoTest {

	public byte[] getData() {
        return new byte[] { 48, 111, -128, 3, 2, 11, 12, -95, 63, -128, 3, 5, 6, 7, -127, 3, 5, 6, 7, -126, 10, 4, 1, 6, 8, 3, 2,
                5, 6, 1, 7, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
                42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getPDPAddressData() {
        return new byte[] { 5, 6, 7 };
    };

    public byte[] getFQDNData() {
        return new byte[] { 4, 1, 6, 8, 3, 2, 5, 6, 1, 7 };
    };

    public byte[] getDataAPN() {
        return new byte[] { 11, 12 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SpecificAPNInfoImpl.class);
    	
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SpecificAPNInfoImpl);
        SpecificAPNInfoImpl prim = (SpecificAPNInfoImpl)result.getResult();
        
        PDNGWIdentity pdnGWIdentity = prim.getPdnGwIdentity();
        PDPAddress pdnGwIpv4Address = pdnGWIdentity.getPdnGwIpv4Address();
        assertNotNull(pdnGwIpv4Address);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getPDPAddressData()), pdnGwIpv4Address.getValue()));
        PDPAddress pdnGwIpv6Address = pdnGWIdentity.getPdnGwIpv6Address();
        assertNotNull(pdnGwIpv6Address);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getPDPAddressData()), pdnGwIpv6Address.getValue()));
        FQDN pdnGwName = pdnGWIdentity.getPdnGwName();
        assertNotNull(pdnGwName);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(this.getFQDNData()), pdnGwName.getValue()));
        assertNotNull(pdnGWIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdnGWIdentity.getExtensionContainer()));

        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

        assertEquals(prim.getAPN().getApn(), new String(this.getDataAPN()));

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SpecificAPNInfoImpl.class);
    	
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        APNImpl apn = new APNImpl(new String(this.getDataAPN()));
        PDPAddressImpl pdnGwIpv4Address = new PDPAddressImpl(Unpooled.wrappedBuffer(this.getPDPAddressData()));
        PDPAddressImpl pdnGwIpv6Address = new PDPAddressImpl(Unpooled.wrappedBuffer(this.getPDPAddressData()));
        FQDNImpl pdnGwName = new FQDNImpl(Unpooled.wrappedBuffer(this.getFQDNData()));
        PDNGWIdentityImpl pdnGwIdentity = new PDNGWIdentityImpl(pdnGwIpv4Address, pdnGwIpv6Address, pdnGwName,
                extensionContainer);
        SpecificAPNInfoImpl prim = new SpecificAPNInfoImpl(apn, pdnGwIdentity, extensionContainer);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = this.getData();
        assertEquals(encodedData, rawData);
    }
}
