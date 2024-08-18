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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictionsValue;
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
public class CUGFeatureTest {

	public byte[] getData() {
        return new byte[] { 48, 50, -126, 1, 38, 2, 1, 1, 4, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData1() {
        return new byte[] { 48, 50, -125, 1, 17, 2, 1, 1, 4, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    @Test
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

    @Test
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