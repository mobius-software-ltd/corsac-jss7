package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author vadim subbotin
 * @author yulianoifa
 */
public class ClirDataTest {
    private byte[] data = {48, 8, -127, 1, 10, -126, 1, 0, -125, 0};

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ClirDataImpl.class);
    	        
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ClirDataImpl);
        ClirDataImpl clirData = (ClirDataImpl)result.getResult();

        assertNotNull(clirData.getSsStatus());
        assertTrue(clirData.getSsStatus().getBitQ());
        assertFalse(clirData.getSsStatus().getBitP());
        assertTrue(clirData.getSsStatus().getBitR());
        assertFalse(clirData.getSsStatus().getBitA());
        assertEquals(clirData.getCliRestrictionOption(), CliRestrictionOption.permanent);
        assertTrue(clirData.getNotificationToCSE());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ClirDataImpl.class);
    	        
    	ClirDataImpl clirData = new ClirDataImpl(new ExtSSStatusImpl(true, false, true, false), CliRestrictionOption.permanent, true);

    	ByteBuf buffer=parser.encode(clirData);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, data));
    }
}
