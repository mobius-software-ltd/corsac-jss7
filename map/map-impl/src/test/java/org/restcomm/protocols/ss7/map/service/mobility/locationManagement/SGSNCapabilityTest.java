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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
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
public class SGSNCapabilityTest {

    public byte[] getData() {
        return new byte[] { 48, 79, 5, 0, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6,
                48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 2, -128, 0, -125, 0, -124, 2, 4, -16,
                -123, 2, 3, -8, -122, 2, 1, -2, -121, 0, -120, 2, 3, -8, -119, 5, 6, -1, -1, -1, -64, -118, 0, -117,
                1, -1 };
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
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
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