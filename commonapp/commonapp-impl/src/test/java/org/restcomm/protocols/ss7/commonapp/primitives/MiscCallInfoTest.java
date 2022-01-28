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

package org.restcomm.protocols.ss7.commonapp.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoDpAssignment;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MiscCallInfoTest {

    private byte[] getData1() {
        return new byte[] { (byte) 164, 3, (byte) 128, 1, 1 };
    }

    private byte[] getData2() {
        return new byte[] { (byte) 164, 6, (byte) 128, 1, 0, (byte) 129, 1, 0 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MiscCallInfoImpl.class);
        ByteBuf data = Unpooled.wrappedBuffer(this.getData1());
        ASNDecodeResult result=parser.decode(data);
        MiscCallInfoImpl elem = (MiscCallInfoImpl)result.getResult();
        assertNotNull(elem.getMessageType());
        assertNull(elem.getDpAssignment());
        assertEquals(elem.getMessageType(), MiscCallInfoMessageType.notification);

        data = Unpooled.wrappedBuffer(this.getData2());
        result=parser.decode(data);
        elem = (MiscCallInfoImpl)result.getResult();
        assertNotNull(elem.getMessageType());
        assertNotNull(elem.getDpAssignment());
        assertEquals(elem.getMessageType(), MiscCallInfoMessageType.request);
        assertEquals(elem.getDpAssignment(), MiscCallInfoDpAssignment.individualLine);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MiscCallInfoImpl.class);
        
        MiscCallInfoImpl elem = new MiscCallInfoImpl(MiscCallInfoMessageType.notification, null);
        ByteBuf data=parser.encode(elem);
        byte[] encodedData=new byte[data.readableBytes()];
        data.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, this.getData1()));

        elem = new MiscCallInfoImpl(MiscCallInfoMessageType.request, MiscCallInfoDpAssignment.individualLine);
        data=parser.encode(elem);
        encodedData=new byte[data.readableBytes()];
        data.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, this.getData2()));

    }
}
