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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictionsValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
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
public class CUGFeatureTest {

	public byte[] getData() {
        return new byte[] { 48, 50, -126, 1, 38, 2, 1, 1, 4, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData1() {
        return new byte[] { 48, 50, -125, 1, 17, 2, 1, 1, 4, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CUGFeatureImpl.class);
    	
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CUGFeatureImpl);
        CUGFeatureImpl prim = (CUGFeatureImpl)result.getResult();   
        
        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
    	assertEquals(prim.getBasicService().getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
    	assertNull(prim.getBasicService().getExtTeleservice());
    	assertEquals((int) prim.getPreferentialCugIndicator(), 1);
    	assertEquals(prim.getInterCugRestrictions().getInterCUGRestrictionsValue(), InterCUGRestrictionsValue.CUGOnlyFacilities);
    	assertNotNull(extensionContainer);
    	assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        
    	data = this.getData1();
    	result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CUGFeatureImpl);
        prim = (CUGFeatureImpl)result.getResult();
        
    	extensionContainer = prim.getExtensionContainer();
    	assertEquals(prim.getBasicService().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.telephony);
    	assertNull(prim.getBasicService().getExtBearerService());
    	assertEquals((int) prim.getPreferentialCugIndicator(), 1);
    	assertTrue(prim.getInterCugRestrictions().getInterCUGRestrictionsValue().equals(InterCUGRestrictionsValue.CUGOnlyFacilities));
    	assertNotNull(extensionContainer);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(CUGFeatureImpl.class);
    	
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
    	ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
    	Integer preferentialCugIndicator = 1;
        InterCUGRestrictionsImpl interCugRestrictions = new InterCUGRestrictionsImpl(InterCUGRestrictionsValue.CUGOnlyFacilities);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        CUGFeatureImpl prim = new CUGFeatureImpl(basicService, preferentialCugIndicator, interCugRestrictions, extensionContainer);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        assertTrue(Arrays.equals(encodedData, this.getData()));

        ExtTeleserviceCodeImpl t = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.telephony);
        basicService = new ExtBasicServiceCodeImpl(t);

        preferentialCugIndicator = 1;
        interCugRestrictions = new InterCUGRestrictionsImpl(0);
        extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        prim = new CUGFeatureImpl(basicService, preferentialCugIndicator, interCugRestrictions, extensionContainer);

        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        assertTrue(Arrays.equals(encodedData, this.getData1()));        
    }
}