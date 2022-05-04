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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPPrivateExtension;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPPrivateExtensionImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class VLRCapabilityTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 11, -128, 2, 6, -64, -127, 1, 1, -123, 2, 4, -16 };
    }

    private byte[] getEncodedDataEC() {
        return new byte[] { 48, 26, -128, 2, 5, -32, 48, 20, -96, 18, 48, 16, 6, 8, 42, -122, 8, 8, 8, 8, 8, 1, 48, 4, -123, 2, 6, 64 };
    }

    private Long[] getECOid() {
        return new Long[] { 1L, 2L, 776L, 8L, 8L, 8L, 8L, 1L };
    }

    private byte[] getEncodedDataSuperChargerInfo() {
        return new byte[] { 48, 4, (byte) 163, 2, (byte) 128, 0 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 16, -126, 0, -124, 0, -122, 2, 1, 14, -121, 2, 4, 80, -120, 0, -119, 0 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(VLRCapabilityImpl.class);
    	
    	ASNParser extensionsParser=new ASNParser();
    	extensionsParser.replaceClass(SGSNCapabilityImpl.class);
    	
    	List<Long> ecOIDs=Arrays.asList(getECOid());
    	parser.registerLocalMapping(MAPPrivateExtensionImpl.class, ecOIDs, SGSNCapabilityImpl.class);
    	parser.registerAlternativeClassMapping(SGSNCapabilityImpl.class, SGSNCapabilityImpl.class);
    	
        byte[] data = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VLRCapabilityImpl);
        VLRCapabilityImpl asc = (VLRCapabilityImpl)result.getResult();
        
        SupportedCamelPhases scph = asc.getSupportedCamelPhases();
        assertTrue(scph.getPhase1Supported());
        assertTrue(scph.getPhase2Supported());
        assertFalse(scph.getPhase3Supported());
        assertFalse(scph.getPhase4Supported());

        assertNull(asc.getExtensionContainer());

        assertFalse(asc.getSolsaSupportIndicator());

        assertEquals(asc.getIstSupportIndicator(), ISTSupportIndicator.istCommandSupported);

        assertNull(asc.getSuperChargerSupportedInServingNetworkEntity());
        assertFalse(asc.getLongFtnSupported());

        SupportedLCSCapabilitySets slcs = asc.getSupportedLCSCapabilitySets();
        assertTrue(slcs.getCapabilitySetRelease98_99());
        assertTrue(slcs.getCapabilitySetRelease4());
        assertTrue(slcs.getCapabilitySetRelease5());
        assertTrue(slcs.getCapabilitySetRelease6());
        assertFalse(slcs.getCapabilitySetRelease7());

        assertNull(asc.getOfferedCamel4CSIs());
        assertNull(asc.getSupportedRATTypesIndicator());
        assertFalse(asc.getLongGroupIDSupported());
        assertFalse(asc.getMtRoamingForwardingSupported());

        data = getEncodedDataEC();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VLRCapabilityImpl);
        asc = (VLRCapabilityImpl)result.getResult();

        scph = asc.getSupportedCamelPhases();
        assertTrue(scph.getPhase1Supported());
        assertTrue(scph.getPhase2Supported());
        assertTrue(scph.getPhase3Supported());
        assertFalse(scph.getPhase4Supported());

        MAPExtensionContainer ext = asc.getExtensionContainer();
        Long[] oids=new Long[ext.getPrivateExtensionList().get(0).getOId().size()];
        oids=ext.getPrivateExtensionList().get(0).getOId().toArray(oids);
        assertTrue(Arrays.equals(oids, getECOid()));
        
        ASNDecodeResult asnResult=extensionsParser.decode(ext.getPrivateExtensionList().get(0).getData());
        assertFalse(asnResult.getHadErrors());
        assertTrue(asnResult.getResult() instanceof SGSNCapabilityImpl);
        
        assertFalse(asc.getSolsaSupportIndicator());

        assertNull(asc.getIstSupportIndicator());

        assertNull(asc.getSuperChargerSupportedInServingNetworkEntity());
        assertFalse(asc.getLongFtnSupported());

        assertNull(asc.getSupportedLCSCapabilitySets());

        assertNull(asc.getOfferedCamel4CSIs());
        assertNull(asc.getSupportedRATTypesIndicator());
        assertFalse(asc.getLongGroupIDSupported());
        assertFalse(asc.getMtRoamingForwardingSupported());

        data = getEncodedDataSuperChargerInfo();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VLRCapabilityImpl);
        asc = (VLRCapabilityImpl)result.getResult();

        assertNull(asc.getSupportedCamelPhases());

        assertNull(asc.getExtensionContainer());
        assertFalse(asc.getSolsaSupportIndicator());

        assertNull(asc.getIstSupportIndicator());

        assertTrue(asc.getSuperChargerSupportedInServingNetworkEntity().getSendSubscriberData());
        assertFalse(asc.getLongFtnSupported());

        assertNull(asc.getSupportedLCSCapabilitySets());

        assertNull(asc.getOfferedCamel4CSIs());
        assertNull(asc.getSupportedRATTypesIndicator());
        assertFalse(asc.getLongGroupIDSupported());
        assertFalse(asc.getMtRoamingForwardingSupported());

        data = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VLRCapabilityImpl);
        asc = (VLRCapabilityImpl)result.getResult();

        assertNull(asc.getSupportedCamelPhases());
        assertNull(asc.getExtensionContainer());
        assertTrue(asc.getSolsaSupportIndicator());

        assertNull(asc.getIstSupportIndicator());
        assertNull(asc.getSuperChargerSupportedInServingNetworkEntity());
        assertTrue(asc.getLongFtnSupported());

        assertNull(asc.getSupportedLCSCapabilitySets());

        OfferedCamel4CSIs offeredCamel4CSIs = asc.getOfferedCamel4CSIs();
        // boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSMSCsi, boolean mgCsi, boolean psiEnhancements
        assertFalse(offeredCamel4CSIs.getOCsi());
        assertFalse(offeredCamel4CSIs.getDCsi());
        assertFalse(offeredCamel4CSIs.getVtCsi());
        assertFalse(offeredCamel4CSIs.getTCsi());
        assertTrue(offeredCamel4CSIs.getMtSmsCsi());
        assertTrue(offeredCamel4CSIs.getMgCsi());
        assertTrue(offeredCamel4CSIs.getPsiEnhancements());

        SupportedRATTypes rat = asc.getSupportedRATTypesIndicator();
        // boolean utran, boolean geran, boolean gan, boolean i_hspa_evolution, boolean e_utran
        assertFalse(rat.getUtran());
        assertTrue(rat.getGeran());
        assertFalse(rat.getGan());
        assertTrue(rat.getIHspaEvolution());
        assertFalse(rat.getEUtran());

        assertTrue(asc.getLongGroupIDSupported());
        assertTrue(asc.getMtRoamingForwardingSupported());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(VLRCapabilityImpl.class);
    	
    	ASNParser extensionsParser=new ASNParser();
    	extensionsParser.replaceClass(SGSNCapabilityImpl.class);
    	
        SupportedCamelPhasesImpl scp = new SupportedCamelPhasesImpl(true, true, false, false);
        SupportedLCSCapabilitySetsImpl slcs = new SupportedLCSCapabilitySetsImpl(true, true, true, true, false);
        VLRCapabilityImpl asc = new VLRCapabilityImpl(scp, null, false, ISTSupportIndicator.istCommandSupported, null, false,
                slcs, null, null, false, false);
        
        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        scp = new SupportedCamelPhasesImpl(true, true, true, false);
        List<MAPPrivateExtension> privateExtensionList = new ArrayList<MAPPrivateExtension>();
        
        SupportedLCSCapabilitySetsImpl slcsInner = new SupportedLCSCapabilitySetsImpl(false, true, false, false, false);
        SGSNCapabilityImpl wrappedExtension=new SGSNCapabilityImpl(false, null, null, false, null, slcsInner, null, false, null, null, false, null);        
        List<Long> ecOIDs=Arrays.asList(getECOid());
        MAPPrivateExtension pe = new MAPPrivateExtensionImpl(ecOIDs, extensionsParser.encode(wrappedExtension));
        privateExtensionList.add(pe);
        MAPExtensionContainer ext = new MAPExtensionContainerImpl(privateExtensionList, null);
        asc = new VLRCapabilityImpl(scp, ext, false, null, null, false, null, null, null, false, false);
        data=getEncodedDataEC();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        SuperChargerInfoImpl sci = new SuperChargerInfoImpl(true);
        asc = new VLRCapabilityImpl(null, null, false, null, sci, false, null, null, null, false, false);
        data=getEncodedDataSuperChargerInfo();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        OfferedCamel4CSIsImpl offeredCamel4CSIs = new OfferedCamel4CSIsImpl(false, false, false, false, true, true, true);
        SupportedRATTypesImpl rat = new SupportedRATTypesImpl(false, true, false, true, false);
        asc = new VLRCapabilityImpl(null, null, true, null, null, true, null, offeredCamel4CSIs, rat, true, true);
        data=getEncodedDataFull();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}
