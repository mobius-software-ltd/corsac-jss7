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
package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
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
public class SGSNCapabilityTest {

    public byte[] getData() {
        return new byte[] { 48, 85, 5, 0, -95, 45, -96, 36, 48, 12, 6, 3, 42, 3, 4, 4, 5, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 13, 6, 3, 42, 3, 5, 4, 6, 21, 22, 23, 24, 25, 26, -95, 5, 4, 3, 31, 32, 33, -94, 2, -128, 0, -125, 0, -124, 2, 4, -16, -123, 2, 3, -8, -122, 2, 1, -2, -121, 0, -120, 2, 3, -8, -119, 5, 6, -1, -1, -1, -64, -118, 0, -117, 1, -1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SGSNCapabilityImpl.class);
    	
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SGSNCapabilityImpl);
        SGSNCapabilityImpl prim = (SGSNCapabilityImpl)result.getResult();
        
        assertTrue(prim.getSolsaSupportIndicator());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        assertTrue(prim.getSuperChargerSupportedInServingNetworkEntity().getSendSubscriberData());
        assertTrue(prim.getGprsEnhancementsSupportIndicator());
        assertTrue(prim.getSupportedCamelPhases().getPhase1Supported());
        assertTrue(prim.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());
        assertTrue(prim.getOfferedCamel4CSIs().getDCsi());
        assertTrue(prim.getSmsCallBarringSupportIndicator());
        assertTrue(prim.getSupportedRATTypesIndicator().getEUtran());
        assertTrue(prim.getSupportedFeatures().getBaoc());

        assertTrue(prim.getTAdsDataRetrieval());
        assertTrue(prim.getHomogeneousSupportOfIMSVoiceOverPSSessions());
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SGSNCapabilityImpl.class);
    	
        boolean solsaSupportIndicator = true;
        MAPExtensionContainerImpl extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        SuperChargerInfoImpl superChargerSupportedInServingNetworkEntity = new SuperChargerInfoImpl(true);

        boolean gprsEnhancementsSupportIndicator = true;
        SupportedCamelPhasesImpl supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, true);
        SupportedLCSCapabilitySetsImpl supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true, true, true, true);
        OfferedCamel4CSIsImpl offeredCamel4CSIs = new OfferedCamel4CSIsImpl(true, true, true, true, true, true, true);
        boolean smsCallBarringSupportIndicator = true;
        SupportedRATTypesImpl supportedRATTypesIndicator = new SupportedRATTypesImpl(true, true, true, true, true);
        SupportedFeaturesImpl supportedFeatures = new SupportedFeaturesImpl(true, true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
        boolean tAdsDataRetrieval = true;
        Boolean homogeneousSupportOfIMSVoiceOverPSSessions = Boolean.TRUE;

        SGSNCapabilityImpl prim = new SGSNCapabilityImpl(solsaSupportIndicator, extensionContainer,
                superChargerSupportedInServingNetworkEntity, gprsEnhancementsSupportIndicator, supportedCamelPhases,
                supportedLCSCapabilitySets, offeredCamel4CSIs, smsCallBarringSupportIndicator, supportedRATTypesIndicator,
                supportedFeatures, tAdsDataRetrieval, homogeneousSupportOfIMSVoiceOverPSSessions);
        
        byte[] data=this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}