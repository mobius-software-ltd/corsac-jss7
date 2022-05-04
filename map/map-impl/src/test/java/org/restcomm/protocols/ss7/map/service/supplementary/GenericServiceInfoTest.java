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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CCBSFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class GenericServiceInfoTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 3, 4, 1, 2 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 28, 4, 1, 2, 10, 1, 2, (byte) 128, 1, 3, (byte) 129, 1, 4, (byte) 162, 5, 48, 3, (byte) 128, 1, 2, (byte) 131, 1, 11,
                (byte) 132, 1, 12, (byte) 133, 1, 13 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(GenericServiceInfoImpl.class);
    	                
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GenericServiceInfoImpl);
        GenericServiceInfoImpl impl = (GenericServiceInfoImpl)result.getResult();

        assertFalse(impl.getSsStatus().getABit());
        assertFalse(impl.getSsStatus().getPBit());
        assertFalse(impl.getSsStatus().getQBit());
        assertTrue(impl.getSsStatus().getRBit());

        assertNull(impl.getCliRestrictionOption());
        assertNull(impl.getMaximumEntitledPriority());
        assertNull(impl.getDefaultPriority());
        assertNull(impl.getCcbsFeatureList());
        assertNull(impl.getNbrSB());
        assertNull(impl.getNbrUser());
        assertNull(impl.getNbrSN());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GenericServiceInfoImpl);
        impl = (GenericServiceInfoImpl)result.getResult();

        assertFalse(impl.getSsStatus().getABit());
        assertFalse(impl.getSsStatus().getPBit());
        assertFalse(impl.getSsStatus().getQBit());
        assertTrue(impl.getSsStatus().getRBit());

        assertEquals(impl.getCliRestrictionOption(), CliRestrictionOption.temporaryDefaultAllowed);
        assertEquals(impl.getMaximumEntitledPriority(), EMLPPPriority.priorityLevel3);
        assertEquals(impl.getDefaultPriority(), EMLPPPriority.priorityLevel4);
        assertEquals(impl.getCcbsFeatureList().size(), 1);
        assertEquals((int) impl.getCcbsFeatureList().get(0).getCcbsIndex(), 2);
        assertEquals((int) impl.getNbrSB(), 11);
        assertEquals((int) impl.getNbrUser(), 12);
        assertEquals((int) impl.getNbrSN(), 13);
    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(GenericServiceInfoImpl.class);
    	
        SSStatusImpl ssStatus = new SSStatusImpl(false, false, true, false);
        GenericServiceInfoImpl impl = new GenericServiceInfoImpl(ssStatus, null, null, null, null, null, null, null);

        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);

        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        List<CCBSFeature> ccbsFeatureList = new ArrayList<CCBSFeature>();
        CCBSFeatureImpl ccbs = new CCBSFeatureImpl(2, null, null, null);
        ccbsFeatureList.add(ccbs);
        impl = new GenericServiceInfoImpl(ssStatus, CliRestrictionOption.temporaryDefaultAllowed, EMLPPPriority.priorityLevel3, EMLPPPriority.priorityLevel4,ccbsFeatureList, 11, 12, 13);

        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedData2();
        assertEquals(rawData, encodedData);
    }
}