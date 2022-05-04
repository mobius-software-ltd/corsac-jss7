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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Amit Bhayani
 * @author yulianoifa
 *
 */
public class OCalledPartyBusySpecificInfoTest {

    public byte[] getData() {
        return new byte[] { 48, 4, (byte) 128, 2, (byte) 195, (byte) 128 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(OCalledPartyBusySpecificInfoImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OCalledPartyBusySpecificInfoImpl);
        
        OCalledPartyBusySpecificInfoImpl elem = (OCalledPartyBusySpecificInfoImpl)result.getResult();        
        assertEquals(elem.getBusyCause().getCauseIndicators().getCodingStandard(), CauseIndicators._CODING_STANDARD_NATIONAL);
        assertEquals(elem.getBusyCause().getCauseIndicators().getLocation(), CauseIndicators._LOCATION_TRANSIT_NETWORK);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(OCalledPartyBusySpecificInfoImpl.class);
    	
    	CauseIndicators causeIndicators = new CauseIndicatorsImpl(CauseIndicators._CODING_STANDARD_NATIONAL, CauseIndicators._LOCATION_TRANSIT_NETWORK, 0, 0,
                null);
//        int codingStandard, int location, int recommendation, int causeValue, byte[] diagnostics
        CauseIsupImpl cause = new CauseIsupImpl(causeIndicators);
        OCalledPartyBusySpecificInfoImpl elem = new OCalledPartyBusySpecificInfoImpl(cause);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

    }
}
