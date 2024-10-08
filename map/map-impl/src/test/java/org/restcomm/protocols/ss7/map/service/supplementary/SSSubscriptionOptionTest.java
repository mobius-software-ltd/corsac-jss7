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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;

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
public class SSSubscriptionOptionTest {

    private byte[] getData1() {
        return new byte[] { 48, 3, -126, 1, 0 };
    }

    private byte[] getData2() {
        return new byte[] { 48, 3, -127, 1, 1 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SSSubscriptionOptionImpl.class);
    	
        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SSSubscriptionOptionImpl);
        SSSubscriptionOptionImpl prim = (SSSubscriptionOptionImpl)result.getResult();

        assertNotNull(prim.getCliRestrictionOption());
        assertNull(prim.getOverrideCategory());
        assertTrue(prim.getCliRestrictionOption().getCode() == CliRestrictionOption.permanent.getCode());

        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SSSubscriptionOptionImpl);
        prim = (SSSubscriptionOptionImpl)result.getResult();

        assertNull(prim.getCliRestrictionOption());
        assertNotNull(prim.getOverrideCategory());
        assertTrue(prim.getOverrideCategory().getCode() == OverrideCategory.overrideDisabled.getCode());

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SSSubscriptionOptionImpl.class);
    	        
        SSSubscriptionOptionImpl impl = new SSSubscriptionOptionImpl(CliRestrictionOption.permanent);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertArrayEquals(encodedData, this.getData1());

        impl = new SSSubscriptionOptionImpl(OverrideCategory.overrideDisabled);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertArrayEquals(encodedData, this.getData2());
    }
}