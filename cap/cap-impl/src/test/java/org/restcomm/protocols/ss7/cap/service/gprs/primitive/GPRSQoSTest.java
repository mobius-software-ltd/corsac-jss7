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
package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.QoSSubscribedImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class GPRSQoSTest {

    public byte[] getData() {
        return new byte[] { 48, 5, -128, 3, 4, 7, 7 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 4, -127, 2, 1, 7 };
    };

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GPRSQoSWrapperImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSQoSWrapperImpl);
        
        GPRSQoSWrapperImpl prim = (GPRSQoSWrapperImpl)result.getResult();        
        assertTrue(Arrays.equals(prim.getGPRSQoS().getShortQoSFormat().getData(), this.getQoSSubscribedData()));
        assertNull(prim.getGPRSQoS().getLongQoSFormat());

        // Option 2
        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSQoSWrapperImpl);
        
        prim = (GPRSQoSWrapperImpl)result.getResult();  
        assertTrue(Arrays.equals(prim.getGPRSQoS().getLongQoSFormat().getData(), this.getExtQoSSubscribedData()));
        assertNull(prim.getGPRSQoS().getShortQoSFormat());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GPRSQoSWrapperImpl.class);
    	
    	// Option 1
        QoSSubscribedImpl qosSubscribed = new QoSSubscribedImpl(this.getQoSSubscribedData());
        GPRSQoSImpl prim = new GPRSQoSImpl(qosSubscribed);
        GPRSQoSWrapperImpl wrapper = new GPRSQoSWrapperImpl(prim);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // Option 2
        ExtQoSSubscribedImpl extQoSSubscribed = new ExtQoSSubscribedImpl(this.getExtQoSSubscribedData());
        prim = new GPRSQoSImpl(extQoSSubscribed);
        wrapper = new GPRSQoSWrapperImpl(prim);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
