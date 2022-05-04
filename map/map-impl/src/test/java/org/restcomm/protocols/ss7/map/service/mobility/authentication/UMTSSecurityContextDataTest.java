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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class UMTSSecurityContextDataTest {

    public byte[] getData() {
        return new byte[] { 48, 39, 4, 16, 4, 4, 1, 2, 3, 4, 4, 4, 1, 2, 3, 4, 4, 4, 1, 2, 4, 16, 2, 5, 27, 6, 23, 23, 34, 56,
                34, 76, 34, 4, 4, 1, 2, 3, 4, 1, 2 };
    };

    public byte[] getDataCk() {
        return new byte[] { 4, 4, 1, 2, 3, 4, 4, 4, 1, 2, 3, 4, 4, 4, 1, 2 };
    };

    public byte[] getDataIk() {
        return new byte[] { 2, 5, 27, 6, 23, 23, 34, 56, 34, 76, 34, 4, 4, 1, 2, 3 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(UMTSSecurityContextDataImpl.class);

        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof UMTSSecurityContextDataImpl);
        UMTSSecurityContextDataImpl prim = (UMTSSecurityContextDataImpl)result.getResult();
        
        assertTrue(ByteBufUtil.equals(prim.getCK().getValue(), Unpooled.wrappedBuffer(getDataCk())));
        assertTrue(ByteBufUtil.equals(prim.getIK().getValue(), Unpooled.wrappedBuffer(getDataIk())));
        assertEquals(prim.getKSI().getData(), 2);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(UMTSSecurityContextDataImpl.class);

        CKImpl ck = new CKImpl(Unpooled.wrappedBuffer(getDataCk()));
        IKImpl ik = new IKImpl(Unpooled.wrappedBuffer(getDataIk()));
        KSIImpl ksi = new KSIImpl(2);
        UMTSSecurityContextDataImpl prim = new UMTSSecurityContextDataImpl(ck, ik, ksi);

        byte[] data=this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}