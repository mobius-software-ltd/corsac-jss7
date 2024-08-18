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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

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
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class OfferedCamel4FunctionalitiesTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 4, 6, (byte) 170, 85, (byte) 192 };        
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(OfferedCamel4FunctionalitiesImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OfferedCamel4FunctionalitiesImpl);
        OfferedCamel4FunctionalitiesImpl imp = (OfferedCamel4FunctionalitiesImpl)result.getResult();
        
        assertTrue(imp.getInitiateCallAttempt());
        assertFalse(imp.getSplitLeg());
        assertTrue(imp.getMoveLeg());
        assertFalse(imp.getDisconnectLeg());
        assertTrue(imp.getEntityReleased());
        assertFalse(imp.getDfcWithArgument());
        assertTrue(imp.getPlayTone());
        assertFalse(imp.getDtmfMidCall());

        assertFalse(imp.getChargingIndicator());
        assertTrue(imp.getAlertingDP());
        assertFalse(imp.getLocationAtAlerting());
        assertTrue(imp.getChangeOfPositionDP());
        assertFalse(imp.getOrInteractions());
        assertTrue(imp.getWarningToneEnhancements());
        assertFalse(imp.getCfEnhancements());
        assertTrue(imp.getSubscribedEnhancedDialledServices());

        assertTrue(imp.getServingNetworkEnhancedDialledServices());
        assertTrue(imp.getCriteriaForChangeOfPositionDP());
        assertFalse(imp.getServiceChangeDP());
        assertFalse(imp.getCollectInformation());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(OfferedCamel4FunctionalitiesImpl.class);
    	
        OfferedCamel4FunctionalitiesImpl imp = new OfferedCamel4FunctionalitiesImpl(true, false, true, false, true, false, true, false, false, true, false,
                true, false, true, false, true, true, true, false, false);
        ByteBuf buffer=parser.encode(imp);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(getEncodedData(), encodedData));
    }
}