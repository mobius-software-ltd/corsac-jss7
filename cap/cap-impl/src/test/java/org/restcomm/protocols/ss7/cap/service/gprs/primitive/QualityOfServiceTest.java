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

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed_SourceStatisticsDescriptor;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRate;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRateExtended;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_DeliveryOfErroneousSdus;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_DeliveryOrder;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_MaximumSduSize;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_ResidualBER;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_SduErrorRatio;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TrafficClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TrafficHandlingPriority;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TransferDelay;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_DelayClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_MeanThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PeakThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PrecedenceClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_ReliabilityClass;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.Ext2QoSSubscribedImpl;
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
public class QualityOfServiceTest {

    public byte[] getData() {
        return new byte[] { 48, 48, -96, 5, -128, 3, 4, 7, 7, -95, 11, -127, 9, 1, 0, 0, 0, 0, 0, 0, 0, 0, -94, 5, 
        		-128, 3, 4, 7, 7, -93, 5, -128, 3, 0, 0, 0, -92, 5, -128, 3, 16, 0, 0, -91, 5, -128, 3, 17, 0, 0 };
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
    	parser.replaceClass(QualityOfServiceImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof QualityOfServiceImpl);
        
        QualityOfServiceImpl prim = (QualityOfServiceImpl)result.getResult();        
        assertEquals(prim.getRequestedQoS().getShortQoSFormat().getReliabilityClass(),
        		QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData);
        assertEquals(prim.getRequestedQoS().getShortQoSFormat().getDelayClass(),
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved);
        assertEquals(prim.getRequestedQoS().getShortQoSFormat().getPrecedenceClass(),
        		QoSSubscribed_PrecedenceClass.reserved);
        assertEquals(prim.getRequestedQoS().getShortQoSFormat().getPeakThroughput(),
        		QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved);
        assertEquals(prim.getRequestedQoS().getShortQoSFormat().getMeanThroughput(),
        		QoSSubscribed_MeanThroughput._10000_octetH);
        
        
        assertNull(prim.getRequestedQoS().getLongQoSFormat());

        assertNull(prim.getSubscribedQoS().getShortQoSFormat());

        assertEquals(prim.getNegotiatedQoS().getShortQoSFormat().getReliabilityClass(),
        		QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData);
        assertEquals(prim.getNegotiatedQoS().getShortQoSFormat().getDelayClass(),
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved);
        assertEquals(prim.getNegotiatedQoS().getShortQoSFormat().getPrecedenceClass(),
        		QoSSubscribed_PrecedenceClass.reserved);
        assertEquals(prim.getNegotiatedQoS().getShortQoSFormat().getPeakThroughput(),
        		QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved);
        assertEquals(prim.getNegotiatedQoS().getShortQoSFormat().getMeanThroughput(),
        		QoSSubscribed_MeanThroughput._10000_octetH);
        
        assertNull(prim.getNegotiatedQoS().getLongQoSFormat());

        assertEquals(prim.getRequestedQoSExtension().getSupplementToLongQoSFormat().getSourceStatisticsDescriptor(),
        		Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown);
        assertEquals(prim.getRequestedQoSExtension().getSupplementToLongQoSFormat().isOptimisedForSignallingTraffic(),false);
        assertEquals(prim.getRequestedQoSExtension().getSupplementToLongQoSFormat().getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(prim.getRequestedQoSExtension().getSupplementToLongQoSFormat().getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        assertEquals(prim.getSubscribedQoSExtension().getSupplementToLongQoSFormat().getSourceStatisticsDescriptor(),
        		Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown);
        assertEquals(prim.getSubscribedQoSExtension().getSupplementToLongQoSFormat().isOptimisedForSignallingTraffic(),true);
        assertEquals(prim.getSubscribedQoSExtension().getSupplementToLongQoSFormat().getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(prim.getSubscribedQoSExtension().getSupplementToLongQoSFormat().getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        assertEquals(prim.getNegotiatedQoSExtension().getSupplementToLongQoSFormat().getSourceStatisticsDescriptor(),
        		Ext2QoSSubscribed_SourceStatisticsDescriptor.speech);
        assertEquals(prim.getNegotiatedQoSExtension().getSupplementToLongQoSFormat().isOptimisedForSignallingTraffic(),true);
        assertEquals(prim.getNegotiatedQoSExtension().getSupplementToLongQoSFormat().getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(prim.getNegotiatedQoSExtension().getSupplementToLongQoSFormat().getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(QualityOfServiceImpl.class);
    	
        QoSSubscribedImpl qosSubscribed = new QoSSubscribedImpl(QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData,
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved,QoSSubscribed_PrecedenceClass.reserved,QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved,
        		QoSSubscribed_MeanThroughput._10000_octetH);
        GPRSQoSImpl requestedQoS = new GPRSQoSImpl(qosSubscribed);

        ExtQoSSubscribedImpl extQoSSubscribed = new ExtQoSSubscribedImpl(1,ExtQoSSubscribed_DeliveryOfErroneousSdus.subscribedDeliveryOfErroneousSdus_Reserved,
                ExtQoSSubscribed_DeliveryOrder.subscribeddeliveryOrder_Reserved, ExtQoSSubscribed_TrafficClass.subscribedTrafficClass_Reserved, new ExtQoSSubscribed_MaximumSduSize(0,true),
                new ExtQoSSubscribed_BitRate(0,true), new ExtQoSSubscribed_BitRate(0,true), ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved,
                ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved, ExtQoSSubscribed_TrafficHandlingPriority.subscribedTrafficHandlingPriority_Reserved,
                new ExtQoSSubscribed_TransferDelay(0,true), new ExtQoSSubscribed_BitRate(0,true),new ExtQoSSubscribed_BitRate(0,true));
        
        GPRSQoSImpl subscribedQoS = new GPRSQoSImpl(extQoSSubscribed);

        GPRSQoSImpl negotiatedQoS = new GPRSQoSImpl(qosSubscribed);

        Ext2QoSSubscribedImpl qos2Subscribed1 = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown,false,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        GPRSQoSExtensionImpl requestedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed1);
        Ext2QoSSubscribedImpl qos2Subscribed2 = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown,true,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        GPRSQoSExtensionImpl subscribedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed2);
        Ext2QoSSubscribedImpl qos2Subscribed3 = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.speech,true,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        GPRSQoSExtensionImpl negotiatedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed3);
        
        QualityOfServiceImpl prim = new QualityOfServiceImpl(requestedQoS, subscribedQoS, negotiatedQoS, requestedQoSExtension,
                subscribedQoSExtension, negotiatedQoSExtension);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
