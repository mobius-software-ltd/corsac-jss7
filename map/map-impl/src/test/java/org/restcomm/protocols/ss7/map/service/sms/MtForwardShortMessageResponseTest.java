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

package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.smstpdu.SmsTpduImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MtForwardShortMessageResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 46, 4, 3, 0, 1, 44, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
                5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MtForwardShortMessageResponseImpl.class);

        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MtForwardShortMessageResponseImpl);
        MtForwardShortMessageResponseImpl ind = (MtForwardShortMessageResponseImpl)result.getResult();  
        
        ByteBuf buffer=Unpooled.buffer();
        ind.getSM_RP_UI().decodeTpdu(true).encodeData(buffer);
        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(new byte[] { 0, 1, 44 }), buffer));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MtForwardShortMessageResponseImpl.class);

        SmsSignalInfoImpl ui = new SmsSignalInfoImpl(SmsTpduImpl.createInstance(Unpooled.wrappedBuffer(new byte[] { 0, 1, 44 }), true, null), null);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        MtForwardShortMessageResponseImpl ind = new MtForwardShortMessageResponseImpl(ui, extensionContainer);

        ByteBuf buffer=parser.encode(ind);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
