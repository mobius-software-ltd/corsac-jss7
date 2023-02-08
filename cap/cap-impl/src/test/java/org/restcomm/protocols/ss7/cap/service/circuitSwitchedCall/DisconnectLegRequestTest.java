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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
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
public class DisconnectLegRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 29, (byte) 160, 3, (byte) 129, 1, 6, (byte) 129, 2, (byte) 128, (byte) 134, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48,
                9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DisconnectLegRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DisconnectLegRequestImpl);
        
        DisconnectLegRequestImpl elem = (DisconnectLegRequestImpl)result.getResult();        
        assertEquals(elem.getLegToBeReleased().getReceivingSideID(), LegType.leg6);
        CauseIndicators ci = elem.getReleaseCause().getCauseIndicators();
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getCauseValue(), 6);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DisconnectLegRequestImpl.class);
    	
        LegIDImpl legToBeReleased = new LegIDImpl(LegType.leg6,null);
        CauseIndicatorsImpl causeIndicators = new CauseIndicatorsImpl(0, 0, 0, 6, null);
        CauseIsupImpl releaseCause = new CauseIsupImpl(causeIndicators);
        DisconnectLegRequestImpl elem = new DisconnectLegRequestImpl(legToBeReleased, releaseCause, CAPExtensionsTest.createTestCAPExtensions());

        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
