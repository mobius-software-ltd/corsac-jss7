/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.inap.isup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.isup.impl.message.parameter.CallingPartyCategoryImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyCategory;
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
public class CallingPartysCategoryInapTest {

    public byte[] getData() {
        return new byte[] { (byte) 133, 1, 10 };
    }

    public byte[] getIntData() {
        return new byte[] { 10 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(CallingPartysCategoryInapImpl.class);
        ByteBuf data = Unpooled.wrappedBuffer(this.getData());
        ASNDecodeResult output=parser.decode(data);
        assertTrue(output.getResult() instanceof CallingPartysCategoryInapImpl);
        CallingPartysCategoryInapImpl elem = (CallingPartysCategoryInapImpl)output.getResult();
        CallingPartyCategory cpc = elem.getCallingPartyCategory();
        assertEquals(cpc.getCallingPartyCategory(), 10);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(CallingPartysCategoryInapImpl.class);
        
        CallingPartysCategoryInapImpl elem = new CallingPartysCategoryInapImpl(new CallingPartyCategoryImpl((byte)10));
        ByteBuf data=parser.encode(elem);
        byte[] encodedData=new byte[data.readableBytes()];
        data.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, this.getData()));
    }
}
