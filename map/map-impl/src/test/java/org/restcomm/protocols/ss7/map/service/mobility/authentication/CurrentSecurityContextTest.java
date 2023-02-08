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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.GSMSecurityContextData;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.UMTSSecurityContextData;
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
public class CurrentSecurityContextTest {

    public byte[] getData1() {
        return new byte[] { 48, 15, -96, 13, 4, 8, 4, 4, 1, 2, 3, 4, 4, 4, 4, 1, 4 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 41, -95, 39, 4, 16, 4, 4, 1, 2, 3, 4, 4, 4, 1, 2, 3, 4, 4, 4, 1, 2, 4, 16, 2, 5, 27, 6, 23, 23, 34, 56,
                34, 76, 34, 4, 4, 1, 2, 3, 4, 1, 2 };
    };

    public byte[] getDataKc() {
        return new byte[] { 4, 4, 1, 2, 3, 4, 4, 4 };
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
    	parser.replaceClass(CurrentSecurityContextImpl.class);

        // option 1
        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CurrentSecurityContextImpl);
        CurrentSecurityContextImpl prim = (CurrentSecurityContextImpl)result.getResult();
        
        GSMSecurityContextData gsm = prim.getGSMSecurityContextData();
        UMTSSecurityContextData umts = prim.getUMTSSecurityContextData();
        assertNull(umts);
        assertTrue(ByteBufUtil.equals(gsm.getKc().getValue(), Unpooled.wrappedBuffer(getDataKc())));
        assertEquals(gsm.getCksn().getData(), 4);

        // option 2
        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CurrentSecurityContextImpl);
        prim = (CurrentSecurityContextImpl)result.getResult();

        gsm = prim.getGSMSecurityContextData();
        umts = prim.getUMTSSecurityContextData();
        assertNull(gsm);

        assertTrue(ByteBufUtil.equals(umts.getCK().getValue(), Unpooled.wrappedBuffer(getDataCk())));
        assertTrue(ByteBufUtil.equals(umts.getIK().getValue(), Unpooled.wrappedBuffer(getDataIk())));
        assertEquals(umts.getKSI().getData(), 2);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CurrentSecurityContextImpl.class);

        // option 1
        KcImpl kc = new KcImpl(Unpooled.wrappedBuffer(getDataKc()));
        CksnImpl cksn = new CksnImpl(4);

        GSMSecurityContextDataImpl gsm = new GSMSecurityContextDataImpl(kc, cksn);
        CurrentSecurityContextImpl prim = new CurrentSecurityContextImpl(gsm);

        byte[] data=getData1();        		
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // option 2
        CKImpl ck = new CKImpl(Unpooled.wrappedBuffer(getDataCk()));
        IKImpl ik = new IKImpl(Unpooled.wrappedBuffer(getDataIk()));
        KSIImpl ksi = new KSIImpl(2);
        UMTSSecurityContextDataImpl umts = new UMTSSecurityContextDataImpl(ck, ik, ksi);
        prim = new CurrentSecurityContextImpl(umts);
        
        data=getData2();        		
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}