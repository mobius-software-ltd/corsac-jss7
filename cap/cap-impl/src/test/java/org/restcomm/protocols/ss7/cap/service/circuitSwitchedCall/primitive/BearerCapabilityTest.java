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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.isup.BearerIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserServiceInformationImpl;
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
public class BearerCapabilityTest {

    public byte[] getData1() {
        return new byte[] { 48, 5, (byte) 128, 3, (byte) 128, (byte) 144, (byte) 163 };
    }

    public byte[] getIntData1() {
        return new byte[] { (byte) 128, (byte) 144, (byte) 163 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BearerCapabilityWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BearerCapabilityWrapperImpl);
        
        BearerCapabilityWrapperImpl elem = (BearerCapabilityWrapperImpl)result.getResult();        
        assertTrue(ByteBufUtil.equals(BearerIsupImpl.translate(elem.getBearerCapability().getBearerCap().getUserServiceInformation()), Unpooled.wrappedBuffer(this.getIntData1())));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BearerCapabilityWrapperImpl.class);
    	
        BearerIsupImpl bc = new BearerIsupImpl(new UserServiceInformationImpl(Unpooled.wrappedBuffer(this.getIntData1())));
        BearerCapabilityImpl elem = new BearerCapabilityImpl(bc);
        BearerCapabilityWrapperImpl wrapper = new BearerCapabilityWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
