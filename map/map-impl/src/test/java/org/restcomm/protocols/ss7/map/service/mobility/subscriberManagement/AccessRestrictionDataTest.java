/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionDataImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class AccessRestrictionDataTest {

    public byte[] getData() {
        return new byte[] { 3, 2, 2, 84 };
    };

    public byte[] getData1() {
        return new byte[] { 3, 2, 3, -88 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AccessRestrictionDataImpl.class);
    	
        byte[] rawData = getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AccessRestrictionDataImpl);
        AccessRestrictionDataImpl imp = (AccessRestrictionDataImpl)result.getResult();
        
        assertTrue(!imp.getUtranNotAllowed());
        assertTrue(imp.getGeranNotAllowed());
        assertTrue(!imp.getGanNotAllowed());
        assertTrue(imp.getIHspaEvolutionNotAllowed());
        assertTrue(!imp.getEUtranNotAllowed());
        assertTrue(imp.getHoToNon3GPPAccessNotAllowed());

        rawData = getData1();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AccessRestrictionDataImpl);
        imp = (AccessRestrictionDataImpl)result.getResult();

        assertTrue(imp.getUtranNotAllowed());
        assertTrue(!imp.getGeranNotAllowed());
        assertTrue(imp.getGanNotAllowed());
        assertTrue(!imp.getIHspaEvolutionNotAllowed());
        assertTrue(imp.getEUtranNotAllowed());
        assertTrue(!imp.getHoToNon3GPPAccessNotAllowed());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AccessRestrictionDataImpl.class);
    	
        AccessRestrictionDataImpl imp = new AccessRestrictionDataImpl(false, true, false, true, false, true);
        ByteBuf buffer=parser.encode(imp);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(getData(), encodedData));

        imp = new AccessRestrictionDataImpl(true, false, true, false, true, false);
        buffer=parser.encode(imp);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(getData1(), encodedData));
    }
}
