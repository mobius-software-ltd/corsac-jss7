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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

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
public class LSAIdentityTest {

    public byte[] getData() {
        return new byte[] { 4, 3, 12, 10, 1 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 3, 4, 1, 0 };
    };

    public byte[] getLSAIdentityData() {
        return new byte[] { 12, 10, 1 };
    };

    public byte[] getLSAIdentityData2() {
        return new byte[] { 4, 1, 0 };
    };

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LSAIdentityImpl.class);
    	
        // option 1
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LSAIdentityImpl);
        LSAIdentityImpl prim = (LSAIdentityImpl)result.getResult();
        
        assertTrue(prim.isPlmnSignificantLSA());

        // option 2
        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LSAIdentityImpl);
        prim = (LSAIdentityImpl)result.getResult();
        
        assertFalse(prim.isPlmnSignificantLSA());
    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LSAIdentityImpl.class);
    	
        // option 1
        LSAIdentityImpl prim = new LSAIdentityImpl(Unpooled.wrappedBuffer(this.getLSAIdentityData()));
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        assertTrue(Arrays.equals(encodedData, this.getData()));

        // option 2
        prim = new LSAIdentityImpl(Unpooled.wrappedBuffer(this.getLSAIdentityData2()));
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        assertTrue(Arrays.equals(encodedData, this.getData2()));
    }
}