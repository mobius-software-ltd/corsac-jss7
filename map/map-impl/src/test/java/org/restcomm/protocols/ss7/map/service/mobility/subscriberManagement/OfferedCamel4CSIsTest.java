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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class OfferedCamel4CSIsTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 2, 1, (byte) 148 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(OfferedCamel4CSIsImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OfferedCamel4CSIsImpl);
        OfferedCamel4CSIsImpl imp = (OfferedCamel4CSIsImpl)result.getResult();
        
        assertTrue(imp.getOCsi());
        assertFalse(imp.getDCsi());
        assertFalse(imp.getVtCsi());
        assertTrue(imp.getTCsi());
        assertFalse(imp.getMtSmsCsi());
        assertTrue(imp.getMgCsi());
        assertFalse(imp.getPsiEnhancements());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(OfferedCamel4CSIsImpl.class);
    	
        OfferedCamel4CSIsImpl imp = new OfferedCamel4CSIsImpl(true, false, false, true, false, true, false);
        ByteBuf buffer=parser.encode(imp);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(getEncodedData(), encodedData));
    }
}