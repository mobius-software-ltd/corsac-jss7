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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_DelayClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_MeanThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PeakThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PrecedenceClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_ReliabilityClass;
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
public class QoSSubscribedTest {

    public byte[] getData1() {
        return new byte[] { 4, 3, 27, (byte) 147, 31 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 3, 35, 98, 31 };
    };

    public byte[] getData3() {
        return new byte[] { 4, 3, 21, 33, 5 };
    };

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(QoSSubscribedImpl.class);
    	
        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof QoSSubscribedImpl);
        QoSSubscribedImpl prim = (QoSSubscribedImpl)result.getResult();
        
        assertEquals(prim.getReliabilityClass(), QoSSubscribed_ReliabilityClass.unacknowledgedGtpAndLlc_AcknowledgedRlc_ProtectedData);
        assertEquals(prim.getDelayClass(), QoSSubscribed_DelayClass.delay_Class_3);
        assertEquals(prim.getPrecedenceClass(), QoSSubscribed_PrecedenceClass.lowPriority);
        assertEquals(prim.getPeakThroughput(), QoSSubscribed_PeakThroughput.upTo_256000_octetS);
        assertEquals(prim.getMeanThroughput(), QoSSubscribed_MeanThroughput.bestEffort);

        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof QoSSubscribedImpl);
        prim = (QoSSubscribedImpl)result.getResult();
        
        assertEquals(prim.getReliabilityClass(), QoSSubscribed_ReliabilityClass.unacknowledgedGtpAndLlc_AcknowledgedRlc_ProtectedData);
        assertEquals(prim.getDelayClass(), QoSSubscribed_DelayClass.delay_Class_4_bestEffort);
        assertEquals(prim.getPrecedenceClass(), QoSSubscribed_PrecedenceClass.normalPriority);
        assertEquals(prim.getPeakThroughput(), QoSSubscribed_PeakThroughput.upTo_32000_octetS);
        assertEquals(prim.getMeanThroughput(), QoSSubscribed_MeanThroughput.bestEffort);

        data = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof QoSSubscribedImpl);
        prim = (QoSSubscribedImpl)result.getResult();
        
        assertEquals(prim.getReliabilityClass(), QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_UnprotectedData);
        assertEquals(prim.getDelayClass(), QoSSubscribed_DelayClass.delay_Class_2);
        assertEquals(prim.getPrecedenceClass(), QoSSubscribed_PrecedenceClass.highPriority);
        assertEquals(prim.getPeakThroughput(), QoSSubscribed_PeakThroughput.upTo_2000_octetS);
        assertEquals(prim.getMeanThroughput(), QoSSubscribed_MeanThroughput._2000_octetH);
    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(QoSSubscribedImpl.class);
    	
        QoSSubscribedImpl prim = new QoSSubscribedImpl(QoSSubscribed_ReliabilityClass.unacknowledgedGtpAndLlc_AcknowledgedRlc_ProtectedData,
                QoSSubscribed_DelayClass.delay_Class_3, QoSSubscribed_PrecedenceClass.lowPriority, QoSSubscribed_PeakThroughput.upTo_256000_octetS,
                QoSSubscribed_MeanThroughput.bestEffort);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = this.getData1();
        assertEquals(encodedData, rawData);


        prim = new QoSSubscribedImpl(QoSSubscribed_ReliabilityClass.unacknowledgedGtpAndLlc_AcknowledgedRlc_ProtectedData,
                QoSSubscribed_DelayClass.delay_Class_4_bestEffort, QoSSubscribed_PrecedenceClass.normalPriority, QoSSubscribed_PeakThroughput.upTo_32000_octetS,
                QoSSubscribed_MeanThroughput.bestEffort);

        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getData2();
        assertEquals(encodedData, rawData);


        prim = new QoSSubscribedImpl(QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_UnprotectedData,
                QoSSubscribed_DelayClass.delay_Class_2, QoSSubscribed_PrecedenceClass.highPriority, QoSSubscribed_PeakThroughput.upTo_2000_octetS,
                QoSSubscribed_MeanThroughput._2000_octetH);

        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = this.getData3();
        assertEquals(encodedData, rawData);        
    }
}