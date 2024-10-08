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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class FCIBCCCAMELSequence1GprsTest {

    public byte[] getData() {
        return new byte[] { -96, 16, -128, 8, 48, 6, -128, 1, 5, -127, 1, 2, -127, 1, 2, -126, 1, 1 };
    };

    public byte[] getFreeFormatData() {
        return new byte[] { 48, 6, -128, 1, 5, -127, 1, 2 };
    };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FCIBCCCAMELSequence1GprsImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof FCIBCCCAMELSequence1GprsImpl);
        
        FCIBCCCAMELSequence1GprsImpl prim = (FCIBCCCAMELSequence1GprsImpl)result.getResult();        
        assertTrue(ByteBufUtil.equals(prim.getFreeFormatData().getValue(), Unpooled.wrappedBuffer(this.getFreeFormatData())));
        assertEquals(prim.getPDPID().getId(), 2);
        assertEquals(prim.getAppendFreeFormatData(), AppendFreeFormatData.append);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(FCIBCCCAMELSequence1GprsImpl.class);
    	
        FreeFormatDataGprsImpl freeFormatData = new FreeFormatDataGprsImpl(Unpooled.wrappedBuffer(this.getFreeFormatData()));
        PDPIDImpl pdpID = new PDPIDImpl(2);
        FCIBCCCAMELSequence1GprsImpl prim = new FCIBCCCAMELSequence1GprsImpl(freeFormatData, pdpID, AppendFreeFormatData.append);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
