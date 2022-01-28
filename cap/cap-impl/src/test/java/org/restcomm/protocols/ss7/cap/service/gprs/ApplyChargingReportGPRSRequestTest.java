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
package org.restcomm.protocols.ss7.cap.service.gprs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ChargingResultImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ChargingRollOverImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ElapsedTimeImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ElapsedTimeRollOverImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSQoSExtensionImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSQoSImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.TransferredVolumeImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.TransferredVolumeRollOverImpl;
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
public class ApplyChargingReportGPRSRequestTest {

    public byte[] getData() {
        return new byte[] { 48, 70, -96, 5, -95, 3, -128, 1, 24, -95, 48, -96, 5, -128, 3, 4, 7, 7, -95, 11, -127, 9, 1, 0, 0, 
        		0, 0, 0, 0, 0, 0, -94, 5, -128, 3, 4, 7, 7, -93, 5, -128, 3, 0, 0, 0, -92, 5, -128, 3, 16, 0, 0, -91, 5, -128, 
        		3, 17, 0, 0, -126, 1, -1, -125, 1, 2, -92, 5, -128, 3, -128, 1, 25 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 70, -96, 5, -96, 3, -128, 1, 25, -95, 48, -96, 5, -128, 3, 4, 7, 7, -95, 11, -127, 9, 1, 
        		0, 0, 0, 0, 0, 0, 0, 0, -94, 5, -128, 3, 4, 7, 7, -93, 5, -128, 3, 0, 0, 0, -92, 5, -128, 3, 16, 0, 0, 
        		-91, 5, -128, 3, 17, 0, 0, -126, 1, -1, -125, 1, 2, -92, 5, -127, 3, -128, 1, 24 };
    };

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    public byte[] getDataLiveTraceOption1() {
        return new byte[] { 0x30, 0x0b, (byte) 0xa0, 0x06, (byte) 0xa1, 0x04, (byte) 0x80, 0x02, 0x14, (byte) 0xc8,
                (byte) 0x82, 0x01, (byte) 0xff };
    };

    public byte[] getDataLiveTraceOption2() {
        return new byte[] { 0x30, 0x0d, (byte) 0xa0, 0x08, (byte) 0xa0, 0x06, (byte) 0x80, 0x04, 0x6a, 0x50, 0x00, 0x00,
                (byte) 0x82, 0x01, (byte) 0xff };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ApplyChargingReportGPRSRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ApplyChargingReportGPRSRequestImpl);
        
        ApplyChargingReportGPRSRequestImpl prim = (ApplyChargingReportGPRSRequestImpl)result.getResult();        
        assertEquals(prim.getChargingResult().getElapsedTime().getTimeGPRSIfNoTariffSwitch().intValue(), 24);
        assertNull(prim.getChargingResult().getTransferredVolume());
        assertNotNull(prim.getQualityOfService());
        assertTrue(prim.getActive());
        assertEquals(prim.getPDPID().getId(), 2);
        assertEquals(prim.getChargingRollOver().getTransferredVolumeRollOver().getROVolumeIfNoTariffSwitch().longValue(), 25);
        assertNull(prim.getChargingRollOver().getElapsedTimeRollOver());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ApplyChargingReportGPRSRequestImpl);
        
        prim = (ApplyChargingReportGPRSRequestImpl)result.getResult();        
        assertEquals(prim.getChargingResult().getTransferredVolume().getVolumeIfNoTariffSwitch().longValue(), 25);
        assertNull(prim.getChargingResult().getElapsedTime());
        assertNotNull(prim.getQualityOfService());
        assertTrue(prim.getActive());
        assertEquals(prim.getPDPID().getId(), 2);
        assertEquals(prim.getChargingRollOver().getElapsedTimeRollOver().getROTimeGPRSIfNoTariffSwitch().intValue(), 24);
        assertNull(prim.getChargingRollOver().getTransferredVolumeRollOver());
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ApplyChargingReportGPRSRequestImpl.class);
    	
    	byte[] rawData = this.getDataLiveTraceOption1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ApplyChargingReportGPRSRequestImpl);
        
        ApplyChargingReportGPRSRequestImpl prim = (ApplyChargingReportGPRSRequestImpl)result.getResult();        
        assertEquals(prim.getChargingResult().getElapsedTime().getTimeGPRSIfNoTariffSwitch().intValue(), 5320);
        assertNull(prim.getChargingResult().getTransferredVolume());
        assertNull(prim.getQualityOfService());
        assertTrue(prim.getActive());
        assertNull(prim.getPDPID());
        assertNull(prim.getChargingRollOver());

        rawData = this.getDataLiveTraceOption2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ApplyChargingReportGPRSRequestImpl);
        
        prim = (ApplyChargingReportGPRSRequestImpl)result.getResult();        
        assertEquals(prim.getChargingResult().getTransferredVolume().getVolumeIfNoTariffSwitch().longValue(), 1783627776);
        assertNull(prim.getChargingResult().getElapsedTime());
        assertNull(prim.getQualityOfService());
        assertTrue(prim.getActive());
        assertNull(prim.getPDPID());
        assertNull(prim.getChargingRollOver());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ApplyChargingGPRSRequestImpl.class);
    	
    	// =Option 1
        // chargingResult
        ElapsedTimeImpl elapsedTime = new ElapsedTimeImpl(new Integer(24));
        ChargingResultImpl chargingResult = new ChargingResultImpl(elapsedTime);
        // qualityOfService
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
        QualityOfServiceImpl qualityOfService = new QualityOfServiceImpl(requestedQoS, subscribedQoS, negotiatedQoS,
                requestedQoSExtension, subscribedQoSExtension, negotiatedQoSExtension);
        // active
        boolean active = true;
        // pdpID
        PDPIDImpl pdpID = new PDPIDImpl(2);
        // chargingRollOver
        TransferredVolumeRollOverImpl transferredVolumeRollOver = new TransferredVolumeRollOverImpl(new Integer(25));
        ChargingRollOverImpl chargingRollOver = new ChargingRollOverImpl(transferredVolumeRollOver);
        ApplyChargingReportGPRSRequestImpl prim = new ApplyChargingReportGPRSRequestImpl(chargingResult, qualityOfService,
                active, pdpID, chargingRollOver);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // =Option 2
        // chargingResult
        TransferredVolumeImpl transferredVolume = new TransferredVolumeImpl(new Long(25));
        chargingResult = new ChargingResultImpl(transferredVolume);
        // chargingRollOver
        ElapsedTimeRollOverImpl elapsedTimeRollOver = new ElapsedTimeRollOverImpl(new Integer(24));
        chargingRollOver = new ChargingRollOverImpl(elapsedTimeRollOver);
        prim = new ApplyChargingReportGPRSRequestImpl(chargingResult, qualityOfService, active, pdpID, chargingRollOver);
        rawData = this.getData2();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ApplyChargingGPRSRequestImpl.class);
    	
    	// Option 1
        ElapsedTimeImpl elapsedTime = new ElapsedTimeImpl(new Integer(5320));
        ChargingResultImpl chargingResult = new ChargingResultImpl(elapsedTime);
        boolean active = true;
        ApplyChargingReportGPRSRequestImpl prim = new ApplyChargingReportGPRSRequestImpl(chargingResult, null, active, null,
                null);
        byte[] rawData = this.getDataLiveTraceOption1();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        
        // Option 2
        TransferredVolumeImpl transferredVolume = new TransferredVolumeImpl(new Long(1783627776));
        chargingResult = new ChargingResultImpl(transferredVolume);
        active = true;
        prim = new ApplyChargingReportGPRSRequestImpl(chargingResult, null, active, null, null);
        rawData = this.getDataLiveTraceOption2();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}