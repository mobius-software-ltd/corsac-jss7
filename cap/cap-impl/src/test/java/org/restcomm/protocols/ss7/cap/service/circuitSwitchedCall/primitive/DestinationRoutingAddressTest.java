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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class DestinationRoutingAddressTest {

    public byte[] getData1() {
        return new byte[] { 48, 7, 4, 5, 2, 16, 121, 34, 16 };
    }

    public byte[] getIntData1() {
        return new byte[] { 2, 16, 121, 34, 16 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DestinationRoutingAddressImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DestinationRoutingAddressImpl);
        
        DestinationRoutingAddressImpl elem = (DestinationRoutingAddressImpl)result.getResult();                
        assertNotNull(elem.getCalledPartyNumber());
        assertEquals(elem.getCalledPartyNumber().size(), 1);
        assertTrue(ByteBufUtil.equals(CalledPartyNumberIsupImpl.translate(elem.getCalledPartyNumber().get(0).getCalledPartyNumber()),Unpooled.wrappedBuffer(this.getIntData1())));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DestinationRoutingAddressImpl.class);
    	
    	List<CalledPartyNumberIsup> cpnl = new ArrayList<CalledPartyNumberIsup>();
        CalledPartyNumberIsupImpl cpn = new CalledPartyNumberIsupImpl(new CalledPartyNumberImpl(Unpooled.wrappedBuffer(getIntData1())));
        cpnl.add(cpn);
        DestinationRoutingAddressImpl elem = new DestinationRoutingAddressImpl(cpnl);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator
    }
}
