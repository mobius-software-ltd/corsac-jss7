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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalSubscriptionsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GroupIdImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LongGroupIdImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallDataImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
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
public class VoiceGroupCallDataTest {

    public byte[] getData() {
        return new byte[] { 48, 66, 4, 3, -1, -1, -1, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 3, 2, 5, -96, -128, 2, 7, -128, -127, 4, -11, -1, -1, -1 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 60, 4, 3, -12, -1, -1, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 3, 2, 5, -96, -128, 2, 7, -128 };
    };

    public byte[] getData3() {
        return new byte[] { 48, 66, 4, 3, -1, -1, -1, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 3, 2, 5, -96, -128, 2, 7, -128, -127, 4, -11, -1, -1, -1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(VoiceGroupCallDataImpl.class);
    	// Option 1
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VoiceGroupCallDataImpl);
        VoiceGroupCallDataImpl prim = (VoiceGroupCallDataImpl)result.getResult();
        
        assertTrue(prim.getGroupId().getGroupId().equals(""));
        assertTrue(prim.getLongGroupId().getLongGroupId().equals("5"));
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        assertTrue(prim.getAdditionalSubscriptions().getEmergencyReset());
        assertFalse(prim.getAdditionalSubscriptions().getEmergencyUplinkRequest());
        assertTrue(prim.getAdditionalSubscriptions().getPrivilegedUplinkRequest());
        assertNotNull(prim.getAdditionalInfo());
        assertTrue(prim.getAdditionalInfo().isBitSet(0));

        // Option 2
        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VoiceGroupCallDataImpl);
        prim = (VoiceGroupCallDataImpl)result.getResult();

        assertTrue(prim.getGroupId().getGroupId().equals("4"));
        assertNull(prim.getLongGroupId());
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        assertTrue(prim.getAdditionalSubscriptions().getEmergencyReset());
        assertFalse(prim.getAdditionalSubscriptions().getEmergencyUplinkRequest());
        assertTrue(prim.getAdditionalSubscriptions().getPrivilegedUplinkRequest());
        assertNotNull(prim.getAdditionalInfo());
        assertTrue(prim.getAdditionalInfo().isBitSet(0));

        // Option 3
        data = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VoiceGroupCallDataImpl);
        prim = (VoiceGroupCallDataImpl)result.getResult();

        assertTrue(prim.getGroupId().getGroupId().equals(""));
        assertTrue(prim.getLongGroupId().getLongGroupId().equals("5"));
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        assertTrue(prim.getAdditionalSubscriptions().getEmergencyReset());
        assertFalse(prim.getAdditionalSubscriptions().getEmergencyUplinkRequest());
        assertTrue(prim.getAdditionalSubscriptions().getPrivilegedUplinkRequest());
        assertNotNull(prim.getAdditionalInfo());
        assertTrue(prim.getAdditionalInfo().isBitSet(0));

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(VoiceGroupCallDataImpl.class);
    	// Option 1
        GroupIdImpl groupId = new GroupIdImpl("4");
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        LongGroupIdImpl longGroupId = new LongGroupIdImpl("5");
        AdditionalSubscriptionsImpl additionalSubscriptions = new AdditionalSubscriptionsImpl(true, false, true);
        AdditionalInfoImpl additionalInfo = new AdditionalInfoImpl();
        additionalInfo.setBit(0);

        VoiceGroupCallDataImpl prim = new VoiceGroupCallDataImpl(groupId, extensionContainer, additionalSubscriptions,
                additionalInfo, longGroupId);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = this.getData();
        assertEquals(encodedData, rawData);

        // Option 2
        prim = new VoiceGroupCallDataImpl(groupId, extensionContainer, additionalSubscriptions, additionalInfo, null);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getData2();
        assertEquals(encodedData, rawData);

        // Option 3
        prim = new VoiceGroupCallDataImpl(null, extensionContainer, additionalSubscriptions, additionalInfo, longGroupId);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getData3();
        assertEquals(encodedData, rawData);
    }
}