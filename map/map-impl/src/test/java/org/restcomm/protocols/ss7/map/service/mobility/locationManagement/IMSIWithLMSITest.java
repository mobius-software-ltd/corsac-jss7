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

import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
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
public class IMSIWithLMSITest {

    public byte[] getData1() {
        return new byte[] { 48, 12, 4, 4, 17, 17, 119, 119, 4, 4, 1, 2, 3, 4 };
    };

    public byte[] getDataLmsi() {
        return new byte[] { 1, 2, 3, 4 };
    };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(IMSIWithLMSIImpl.class);
    	
        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof IMSIWithLMSIImpl);
        IMSIWithLMSIImpl prim = (IMSIWithLMSIImpl)result.getResult();
        
        assertTrue(prim.getImsi().getData().equals("11117777"));
        assertTrue(ByteBufUtil.equals(prim.getLmsi().getValue(),Unpooled.wrappedBuffer(getDataLmsi())));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(IMSIWithLMSIImpl.class);
    	
        IMSIImpl imsi = new IMSIImpl("11117777");
        LMSIImpl lmsi = new LMSIImpl(Unpooled.wrappedBuffer(getDataLmsi()));
        IMSIWithLMSIImpl prim = new IMSIWithLMSIImpl(imsi, lmsi);

        byte[] data=this.getData1();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }

}
