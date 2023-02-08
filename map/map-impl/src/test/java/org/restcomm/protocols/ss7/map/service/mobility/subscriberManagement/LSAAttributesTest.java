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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentificationPriorityValue;
import org.testng.annotations.Test;

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
public class LSAAttributesTest {

    public byte[] getData() {
        return new byte[] { 4, 1, 57 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 1, 15 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LSAAttributesImpl.class);
    	
        // option 1
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LSAAttributesImpl);
        LSAAttributesImpl prim = (LSAAttributesImpl)result.getResult();
        
        assertTrue(prim.isActiveModeSupportAvailable());
        assertTrue(prim.isPreferentialAccessAvailable());
        assertEquals(prim.getLSAIdentificationPriority(), LSAIdentificationPriorityValue.Priority_10);

        // option 2
        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LSAAttributesImpl);
        prim = (LSAAttributesImpl)result.getResult();
        
        assertFalse(prim.isActiveModeSupportAvailable());
        assertFalse(prim.isPreferentialAccessAvailable());
        assertEquals(prim.getLSAIdentificationPriority(), LSAIdentificationPriorityValue.Priority_16);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LSAAttributesImpl.class);
    	
        // option 1
        LSAAttributesImpl prim = new LSAAttributesImpl(LSAIdentificationPriorityValue.Priority_10, true, true);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        byte[] rawData=this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 2
        prim = new LSAAttributesImpl(LSAIdentificationPriorityValue.Priority_16, false, false);
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        rawData=this.getData2();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}