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
* @author sergey vetyutnev
*
*/
public class OfferedCamel4FunctionalitiesTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 4, 4, (byte) 170, 85, (byte) 192 };        
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberManagement" })
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

    @Test(groups = { "functional.encode", "service.mobility.subscriberManagement" })
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