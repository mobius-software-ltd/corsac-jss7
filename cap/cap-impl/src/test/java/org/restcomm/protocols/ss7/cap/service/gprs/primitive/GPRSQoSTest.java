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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_DelayClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_MeanThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PeakThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PrecedenceClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_ReliabilityClass;
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

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
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
        assertEquals(prim.getGPRSQoS().getShortQoSFormat().getReliabilityClass(),
        		QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData);
        assertEquals(prim.getGPRSQoS().getShortQoSFormat().getDelayClass(),
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved);
        assertEquals(prim.getGPRSQoS().getShortQoSFormat().getPrecedenceClass(),
        		QoSSubscribed_PrecedenceClass.reserved);
        assertEquals(prim.getGPRSQoS().getShortQoSFormat().getPeakThroughput(),
        		QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved);
        assertEquals(prim.getGPRSQoS().getShortQoSFormat().getMeanThroughput(),
        		QoSSubscribed_MeanThroughput._10000_octetH);
        
        assertNull(prim.getGPRSQoS().getLongQoSFormat());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GPRSQoSWrapperImpl.class);
    	
    	// Option 1
        QoSSubscribedImpl qosSubscribed = new QoSSubscribedImpl(QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData,
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved,QoSSubscribed_PrecedenceClass.reserved,QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved,
        		QoSSubscribed_MeanThroughput._10000_octetH);
        GPRSQoSImpl prim = new GPRSQoSImpl(qosSubscribed);
        GPRSQoSWrapperImpl wrapper = new GPRSQoSWrapperImpl(prim);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
