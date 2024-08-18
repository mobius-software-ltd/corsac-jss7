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
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class ODBHPLMNDataTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 2, 4, 80 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 3, 2, 4, -96 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ODBHPLMNDataImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ODBHPLMNDataImpl);
        ODBHPLMNDataImpl imp = (ODBHPLMNDataImpl)result.getResult();
        
        assertTrue(!imp.getPlmnSpecificBarringType1());
        assertTrue(imp.getPlmnSpecificBarringType2());
        assertTrue(!imp.getPlmnSpecificBarringType3());
        assertTrue(imp.getPlmnSpecificBarringType4());

        rawData = getEncodedData1();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ODBHPLMNDataImpl);
        imp = (ODBHPLMNDataImpl)result.getResult();;

        assertTrue(imp.getPlmnSpecificBarringType1());
        assertTrue(!imp.getPlmnSpecificBarringType2());
        assertTrue(imp.getPlmnSpecificBarringType3());
        assertTrue(!imp.getPlmnSpecificBarringType4());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ODBHPLMNDataImpl.class);

        ODBHPLMNDataImpl imp = new ODBHPLMNDataImpl(false, true, false, true);
        ByteBuf buffer=parser.encode(imp);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = this.getEncodedData();
        assertTrue(Arrays.equals(encodedData, rawData));

        imp = new ODBHPLMNDataImpl(true, false, true, false);
        buffer=parser.encode(imp);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getEncodedData1();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}
