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
public class SupportedFeaturesTest {

    public byte[] getData() {
        return new byte[] { 3, 5, 6, 85, 85, 85, 64 };
    };

    public byte[] getData1() {
        return new byte[] { 3, 5, 6, -86, -86, -86, -128 };
    };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SupportedFeaturesImpl.class);
    	
        // test one
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SupportedFeaturesImpl);
        SupportedFeaturesImpl prim = (SupportedFeaturesImpl)result.getResult();
        
        assertTrue(!prim.getOdbAllApn());
        assertTrue(prim.getOdbHPLMNApn());
        assertTrue(!prim.getOdbVPLMNApn());
        assertTrue(prim.getOdbAllOg());
        assertTrue(!prim.getOdbAllInternationalOg());
        assertTrue(prim.getOdbAllIntOgNotToHPLMNCountry());
        assertTrue(!prim.getOdbAllInterzonalOg());
        assertTrue(prim.getOdbAllInterzonalOgNotToHPLMNCountry());
        assertTrue(!prim.getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertTrue(prim.getRegSub());
        assertTrue(!prim.getTrace());
        assertTrue(prim.getLcsAllPrivExcep());
        assertTrue(!prim.getLcsUniversal());
        assertTrue(prim.getLcsCallSessionRelated());
        assertTrue(!prim.getLcsCallSessionUnrelated());
        assertTrue(prim.getLcsPLMNOperator());
        assertTrue(!prim.getLcsServiceType());
        assertTrue(prim.getLcsAllMOLRSS());
        assertTrue(!prim.getLcsBasicSelfLocation());
        assertTrue(prim.getLcsAutonomousSelfLocation());
        assertTrue(!prim.getLcsTransferToThirdParty());
        assertTrue(prim.getSmMoPp());
        assertTrue(!prim.getBarringOutgoingCalls());
        assertTrue(prim.getBaoc());
        assertTrue(!prim.getBoic());
        assertTrue(prim.getBoicExHC());

        // test two
        data = this.getData1();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SupportedFeaturesImpl);
        prim = (SupportedFeaturesImpl)result.getResult();

        assertTrue(prim.getOdbAllApn());
        assertTrue(!prim.getOdbHPLMNApn());
        assertTrue(prim.getOdbVPLMNApn());
        assertTrue(!prim.getOdbAllOg());
        assertTrue(prim.getOdbAllInternationalOg());
        assertTrue(!prim.getOdbAllIntOgNotToHPLMNCountry());
        assertTrue(prim.getOdbAllInterzonalOg());
        assertTrue(!prim.getOdbAllInterzonalOgNotToHPLMNCountry());
        assertTrue(prim.getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertTrue(!prim.getRegSub());
        assertTrue(prim.getTrace());
        assertTrue(!prim.getLcsAllPrivExcep());
        assertTrue(prim.getLcsUniversal());
        assertTrue(!prim.getLcsCallSessionRelated());
        assertTrue(prim.getLcsCallSessionUnrelated());
        assertTrue(!prim.getLcsPLMNOperator());
        assertTrue(prim.getLcsServiceType());
        assertTrue(!prim.getLcsAllMOLRSS());
        assertTrue(prim.getLcsBasicSelfLocation());
        assertTrue(!prim.getLcsAutonomousSelfLocation());
        assertTrue(prim.getLcsTransferToThirdParty());
        assertTrue(!prim.getSmMoPp());
        assertTrue(prim.getBarringOutgoingCalls());
        assertTrue(!prim.getBaoc());
        assertTrue(prim.getBoic());
        assertTrue(!prim.getBoicExHC());

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SupportedFeaturesImpl.class);
    	
        // Test one
        SupportedFeaturesImpl prim = new SupportedFeaturesImpl(false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true);

        byte[] data=this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // Tes two
        prim = new SupportedFeaturesImpl(true, false, true, false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false);
        data=this.getData1();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}