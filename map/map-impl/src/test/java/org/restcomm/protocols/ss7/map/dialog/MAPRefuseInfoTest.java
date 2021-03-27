/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.dialog;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.dialog.Reason;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPRefuseInfoTest {

    private byte[] getData() {
        return new byte[] { (byte) 0xA3, 0x03, (byte) 0x0A, 0x01, 0x00 };
    }

    private byte[] getDataFull() {
        return new byte[] { -93, 57, 10, 1, 2, 48, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, 6, 5, 42, 3, 4, 5, 6 };
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPRefuseInfoImpl.class);
    	
    	// The raw data is from last packet of long ussd-abort from msc2.txt
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPRefuseInfoImpl);
        MAPRefuseInfoImpl mapRefuseInfoImpl = (MAPRefuseInfoImpl)result.getResult();
        
        Reason reason = mapRefuseInfoImpl.getReason();

        assertNotNull(reason);

        assertEquals(reason, Reason.noReasonGiven);

        data = this.getDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPRefuseInfoImpl);
        mapRefuseInfoImpl = (MAPRefuseInfoImpl)result.getResult();

        reason = mapRefuseInfoImpl.getReason();
        assertNotNull(reason);
        assertEquals(reason, Reason.invalidOriginatingReference);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mapRefuseInfoImpl.getExtensionContainer()));
        assertNotNull(mapRefuseInfoImpl.getAlternativeAcn());
        assertTrue(Arrays.equals(new Long[] { 1L, 2L, 3L, 4L, 5L, 6L }, mapRefuseInfoImpl.getAlternativeAcn().getValue().toArray()));
    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPRefuseInfoImpl.class);
    	
        MAPRefuseInfoImpl mapRefuseInfoImpl = new MAPRefuseInfoImpl();
        mapRefuseInfoImpl.setReason(Reason.noReasonGiven);
        
        ByteBuf buffer=parser.encode(mapRefuseInfoImpl);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(this.getData(), data));

        mapRefuseInfoImpl = new MAPRefuseInfoImpl();
        mapRefuseInfoImpl.setReason(Reason.invalidOriginatingReference);
        ASNObjectIdentifier identifier=new ASNObjectIdentifier();
        identifier.setValue(Arrays.asList(new Long[] { 1L, 2L, 3L, 4L, 5L, 6L }));
        mapRefuseInfoImpl.setAlternativeAcn(identifier);
        mapRefuseInfoImpl.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
        buffer=parser.encode(mapRefuseInfoImpl);
        data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(this.getDataFull(), data));

    }

}
