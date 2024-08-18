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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class VoiceBroadcastDataTest {

	public byte[] getData() {
        return new byte[] { 48, 54, 4, 3, -1, -1, -1, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5,
                6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 4, 33, 67, 101, -121 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 48, 4, 3, 33, 67, 101, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
                5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData3() {
        return new byte[] { 48, 54, 4, 3, -1, -1, -1, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5,
                6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 4, 33, 67, 101, -121 };
    };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(VoiceBroadcastDataImpl.class);
    	// Option 1
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VoiceBroadcastDataImpl);
        VoiceBroadcastDataImpl prim = (VoiceBroadcastDataImpl)result.getResult();
        
        assertTrue(prim.getGroupId().getGroupId().equals(""));
        assertTrue(prim.getLongGroupId().getLongGroupId().equals("12345678"));
        assertTrue(prim.getBroadcastInitEntitlement());
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

        // Option 2
        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VoiceBroadcastDataImpl);
        prim = (VoiceBroadcastDataImpl)result.getResult();
        
        assertTrue(prim.getGroupId().getGroupId().equals("123456"));
        assertNull(prim.getLongGroupId());
        assertTrue(prim.getBroadcastInitEntitlement());
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

        // Option 3
        data = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VoiceBroadcastDataImpl);
        prim = (VoiceBroadcastDataImpl)result.getResult();

        assertTrue(prim.getGroupId().getGroupId().equals(""));
        assertTrue(prim.getLongGroupId().getLongGroupId().equals("12345678"));
        assertTrue(prim.getBroadcastInitEntitlement());
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(VoiceBroadcastDataImpl.class);
    	// option 1
        GroupIdImpl groupId = new GroupIdImpl("123456");
        boolean broadcastInitEntitlement = true;
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        LongGroupIdImpl longGroupId = new LongGroupIdImpl("12345678");

        VoiceBroadcastDataImpl prim = new VoiceBroadcastDataImpl(groupId, broadcastInitEntitlement, extensionContainer,
                longGroupId);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = this.getData();
        assertArrayEquals(encodedData, rawData);
        
        // option 2
        prim = new VoiceBroadcastDataImpl(groupId, broadcastInitEntitlement, extensionContainer, null);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getData2();
        assertArrayEquals(encodedData, rawData);

        // option 3
        prim = new VoiceBroadcastDataImpl(null, broadcastInitEntitlement, extensionContainer, longGroupId);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getData3();
        assertArrayEquals(encodedData, rawData);
    }
}