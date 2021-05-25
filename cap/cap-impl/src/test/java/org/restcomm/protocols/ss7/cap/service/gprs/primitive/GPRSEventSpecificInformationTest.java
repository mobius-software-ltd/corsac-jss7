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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.EsiGprs.DetachSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.DisconnectSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PdpContextChangeOfPositionSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddressImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformationWrapperImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSExtensionImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.InitiatingEntity;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPAddressImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationValue;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfServiceImpl;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribedImpl;
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
public class GPRSEventSpecificInformationTest {

    public byte[] getData() {
        return new byte[] { 48, 59, -96, 57, -96, 7, -127, 5, 82, -16, 16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 31, 32,
                33, 34, 35, 36, 37, 38, -125, 4, -111, 86, 52, 18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 2, 3, 4, 5, 6, 7,
                8, 9, 10, -120, 0, -119, 1, 13 };
    };

    public byte[] getData2() {
        return new byte[] { 48, -127, -116, -95, -127, -119, -128, 3, 52, 20, 30, -127, 4, 41, 42, 43, 44, -94, 57, -96, 7, -127, 5, 82, -16,
                16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 31, 32, 33, 34, 35, 36, 37, 38, -125, 4, -111, 86, 52,
                18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13, -93, 11, -128,
                1, -15, -127, 1, 1, -126, 3, 4, 7, 7, -92, 35, -96, 5, -128, 3, 4, 7, 7, -95, 4, -127, 2, 1, 7, -94, 5, -128,
                3, 4, 7, 7, -93, 3, -128, 1, 52, -92, 3, -128, 1, 53, -91, 3, -128, 1, 54, -123, 8, 2, 17, 33, 3, 1, 112, -127,
                35, -122, 5, 1, 1, 1, 1, 1 };
    };

    public byte[] getData3() {
        return new byte[] { 48, 7, -94, 5, -128, 1, 2, -127, 0 };
    };

    public byte[] getData4() {
        return new byte[] { 48, 7, -93, 5, -128, 1, 2, -127, 0 };
    };

    public byte[] getData5() {
        return new byte[] { 48, -127, -124, -92, -127, -127, -128, 3, 52, 20, 30, -95, 11, -128, 1, -15, -127, 1, 1, -126, 3, 4, 7, 7, -94, 35,
                -96, 5, -128, 3, 4, 7, 7, -95, 4, -127, 2, 1, 7, -94, 5, -128, 3, 4, 7, 7, -93, 3, -128, 1, 52, -92, 3, -128,
                1, 53, -91, 3, -128, 1, 54, -93, 57, -96, 7, -127, 5, 82, -16, 16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16,
                -126, 8, 31, 32, 33, 34, 35, 36, 37, 38, -125, 4, -111, 86, 52, 18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1,
                2, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13, -124, 8, 2, 17, 33, 3, 1, 112, -127, 35, -123, 1, 1, -122, 0 };
    };

    public byte[] getData6() {
        return new byte[] { 48, -127, -116, -91, -127, -119, -128, 3, 52, 20, 30, -127, 4, 41, 42, 43, 44, -94, 57, -96, 7, -127, 5, 82, -16,
                16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 31, 32, 33, 34, 35, 36, 37, 38, -125, 4, -111, 86, 52,
                18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13, -93, 11, -128,
                1, -15, -127, 1, 1, -126, 3, 4, 7, 7, -92, 35, -96, 5, -128, 3, 4, 7, 7, -95, 4, -127, 2, 1, 7, -94, 5, -128,
                3, 4, 7, 7, -93, 3, -128, 1, 52, -92, 3, -128, 1, 53, -91, 3, -128, 1, 54, -123, 8, 2, 17, 33, 3, 1, 112, -127,
                35, -122, 5, 1, 1, 1, 1, 1 };
    };

    private byte[] getEncodedDataRAIdentity() {
        return new byte[] { 11, 12, 13, 14, 15, 16 };
    }

    private byte[] getGeographicalInformation() {
        return new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 };
    }

    private byte[] getEncodedDataLSAIdentity() {
        return new byte[] { 91, 92, 93 };
    }

    private byte[] getGeodeticInformation() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    private byte[] getAccessPointNameData() {
        return new byte[] { 52, 20, 30 };
    }

    private byte[] getEncodedchargingId() {
        return new byte[] { 41, 42, 43, 44 };
    }

    public byte[] getPDPAddressData() {
        return new byte[] { 4, 7, 7 };
    };

    private byte[] getEncodedqos2Subscribed1() {
        return new byte[] { 52 };
    }

    private byte[] getEncodedqos2Subscribed2() {
        return new byte[] { 53 };
    }

    private byte[] getEncodedqos2Subscribed3() {
        return new byte[] { 54 };
    }

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    private byte[] getGSNAddressData() {
        return new byte[] { 1, 1, 1, 1, 1 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GPRSEventSpecificInformationWrapperImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSEventSpecificInformationWrapperImpl);
        
        GPRSEventSpecificInformationWrapperImpl prim = (GPRSEventSpecificInformationWrapperImpl)result.getResult();        
        assertEquals(prim.getGPRSEventSpecificInformation().getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
        assertEquals(prim.getGPRSEventSpecificInformation().getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
        assertEquals(prim.getGPRSEventSpecificInformation().getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);

        // Option 2
        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSEventSpecificInformationWrapperImpl);
        
        prim = (GPRSEventSpecificInformationWrapperImpl)result.getResult();
        assertTrue(Arrays.equals(prim.getGPRSEventSpecificInformation().getPdpContextchangeOfPositionSpecificInformation().getAccessPointName().getData(),
                this.getAccessPointNameData()));
        assertTrue(Arrays.equals(prim.getGPRSEventSpecificInformation().getPdpContextchangeOfPositionSpecificInformation().getChargingID().getData(),
                this.getEncodedchargingId()));
        assertEquals(prim.getGPRSEventSpecificInformation().getPdpContextchangeOfPositionSpecificInformation().getLocationInformationGPRS()
                .getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
        assertEquals(prim.getGPRSEventSpecificInformation().getPdpContextchangeOfPositionSpecificInformation().getLocationInformationGPRS()
                .getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
        assertEquals(prim.getGPRSEventSpecificInformation().getPdpContextchangeOfPositionSpecificInformation().getLocationInformationGPRS()
                .getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);

        // Option 3
        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSEventSpecificInformationWrapperImpl);
        
        prim = (GPRSEventSpecificInformationWrapperImpl)result.getResult();
        assertEquals(prim.getGPRSEventSpecificInformation().getDetachSpecificInformation().getInitiatingEntity(), InitiatingEntity.hlr);
        assertTrue(prim.getGPRSEventSpecificInformation().getDetachSpecificInformation().getRouteingAreaUpdate());

        // Option 4
        rawData = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSEventSpecificInformationWrapperImpl);
        
        prim = (GPRSEventSpecificInformationWrapperImpl)result.getResult();
        assertEquals(prim.getGPRSEventSpecificInformation().getDisconnectSpecificInformation().getInitiatingEntity(), InitiatingEntity.hlr);
        assertTrue(prim.getGPRSEventSpecificInformation().getDisconnectSpecificInformation().getRouteingAreaUpdate());

        // Option 5
        rawData = this.getData5();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSEventSpecificInformationWrapperImpl);
        
        prim = (GPRSEventSpecificInformationWrapperImpl)result.getResult();
        assertTrue(Arrays.equals(prim.getGPRSEventSpecificInformation().getPDPContextEstablishmentSpecificInformation().getAccessPointName().getData(),
                this.getAccessPointNameData()));
        assertEquals(prim.getGPRSEventSpecificInformation().getPDPContextEstablishmentSpecificInformation().getLocationInformationGPRS()
                .getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
        assertEquals(prim.getGPRSEventSpecificInformation().getPDPContextEstablishmentSpecificInformation().getLocationInformationGPRS()
                .getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
        assertEquals(prim.getGPRSEventSpecificInformation().getPDPContextEstablishmentSpecificInformation().getLocationInformationGPRS()
                .getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);

        // Option 6
        rawData = this.getData6();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSEventSpecificInformationWrapperImpl);
        
        prim = (GPRSEventSpecificInformationWrapperImpl)result.getResult();
        assertTrue(Arrays.equals(prim.getGPRSEventSpecificInformation().getPDPContextEstablishmentAcknowledgementSpecificInformation().getAccessPointName()
                .getData(), this.getAccessPointNameData()));
        assertTrue(Arrays.equals(prim.getGPRSEventSpecificInformation().getPDPContextEstablishmentAcknowledgementSpecificInformation().getChargingID().getData(),
                this.getEncodedchargingId()));
        assertEquals(prim.getGPRSEventSpecificInformation().getPDPContextEstablishmentAcknowledgementSpecificInformation().getLocationInformationGPRS()
                .getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
        assertEquals(prim.getGPRSEventSpecificInformation().getPDPContextEstablishmentAcknowledgementSpecificInformation().getLocationInformationGPRS()
                .getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
        assertEquals(prim.getGPRSEventSpecificInformation().getPDPContextEstablishmentAcknowledgementSpecificInformation().getLocationInformationGPRS()
                .getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GPRSEventSpecificInformationWrapperImpl.class);
    	
        // locationInformationGPRS - Option 1
        LAIFixedLengthImpl lai = new LAIFixedLengthImpl(250, 1, 4444);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
        RAIdentityImpl ra = new RAIdentityImpl(this.getEncodedDataRAIdentity());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(this.getGeographicalInformation());
        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "654321");
        LSAIdentityImpl lsa = new LSAIdentityImpl(this.getEncodedDataLSAIdentity());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(this.getGeodeticInformation());
        LocationInformationGPRSImpl locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn, lsa, null, true,
                gdi, true, 13);

        // pdpContextchangeOfPositionSpecificInformation - Option 2
        AccessPointNameImpl accessPointName = new AccessPointNameImpl(this.getAccessPointNameData());
        GPRSChargingIDImpl chargingID = new GPRSChargingIDImpl(getEncodedchargingId());
        PDPAddressImpl pdpAddress = new PDPAddressImpl(getPDPAddressData());
        PDPTypeNumberImpl pdpTypeNumber = new PDPTypeNumberImpl(PDPTypeNumberValue.PPP);
        PDPTypeOrganizationImpl pdpTypeOrganization = new PDPTypeOrganizationImpl(PDPTypeOrganizationValue.ETSI);
        EndUserAddressImpl endUserAddress = new EndUserAddressImpl(pdpTypeOrganization, pdpTypeNumber, pdpAddress);
        QoSSubscribedImpl qosSubscribed = new QoSSubscribedImpl(this.getQoSSubscribedData());
        GPRSQoSImpl requestedQoS = new GPRSQoSImpl(qosSubscribed);
        ExtQoSSubscribedImpl extQoSSubscribed = new ExtQoSSubscribedImpl(this.getExtQoSSubscribedData());
        GPRSQoSImpl subscribedQoS = new GPRSQoSImpl(extQoSSubscribed);
        GPRSQoSImpl negotiatedQoS = new GPRSQoSImpl(qosSubscribed);
        Ext2QoSSubscribedImpl qos2Subscribed1 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed1());
        GPRSQoSExtensionImpl requestedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed1);
        Ext2QoSSubscribedImpl qos2Subscribed2 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed2());
        GPRSQoSExtensionImpl subscribedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed2);
        Ext2QoSSubscribedImpl qos2Subscribed3 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed3());
        GPRSQoSExtensionImpl negotiatedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed3);
        QualityOfServiceImpl qualityOfService = new QualityOfServiceImpl(requestedQoS, subscribedQoS, negotiatedQoS,
                requestedQoSExtension, subscribedQoSExtension, negotiatedQoSExtension);
        TimeAndTimezoneImpl timeAndTimezone = new TimeAndTimezoneImpl(2011, 12, 30, 10, 7, 18, 32);
        GSNAddressImpl gsnAddress = new GSNAddressImpl(getGSNAddressData());
        PdpContextChangeOfPositionSpecificInformationImpl pdpContextchangeOfPositionSpecificInformation = new PdpContextChangeOfPositionSpecificInformationImpl(
                accessPointName, chargingID, locationInformationGPRS, endUserAddress, qualityOfService, timeAndTimezone,
                gsnAddress);

        // detachSpecificInformation - Option 3
        DetachSpecificInformationImpl detachSpecificInformation = new DetachSpecificInformationImpl(InitiatingEntity.hlr, true);

        // disconnectSpecificInformation - Option 4
        DisconnectSpecificInformationImpl disconnectSpecificInformation = new DisconnectSpecificInformationImpl(
                InitiatingEntity.hlr, true);

        // pdpContextEstablishmentSpecificInformation - Option 5
        PDPContextEstablishmentSpecificInformationImpl pdpContextEstablishmentSpecificInformation = new PDPContextEstablishmentSpecificInformationImpl(
                accessPointName, endUserAddress, qualityOfService, locationInformationGPRS, timeAndTimezone,
                PDPInitiationType.networkInitiated, true);

        // pdpContextEstablishmentAcknowledgementSpecificInformation - Option 6
        PDPContextEstablishmentAcknowledgementSpecificInformationImpl pdpContextEstablishmentAcknowledgementSpecificInformation = new PDPContextEstablishmentAcknowledgementSpecificInformationImpl(
                accessPointName, chargingID, locationInformationGPRS, endUserAddress, qualityOfService, timeAndTimezone,
                gsnAddress);

        // option 1
        GPRSEventSpecificInformationImpl prim = new GPRSEventSpecificInformationImpl(locationInformationGPRS);
        GPRSEventSpecificInformationWrapperImpl wrapper = new GPRSEventSpecificInformationWrapperImpl(prim);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // option 2
        prim = new GPRSEventSpecificInformationImpl(pdpContextchangeOfPositionSpecificInformation);
        wrapper = new GPRSEventSpecificInformationWrapperImpl(prim);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // option 3
        prim = new GPRSEventSpecificInformationImpl(detachSpecificInformation);
        wrapper = new GPRSEventSpecificInformationWrapperImpl(prim);
        rawData = this.getData3();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // option 4
        prim = new GPRSEventSpecificInformationImpl(disconnectSpecificInformation);
        wrapper = new GPRSEventSpecificInformationWrapperImpl(prim);
        rawData = this.getData4();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // option 5
        prim = new GPRSEventSpecificInformationImpl(pdpContextEstablishmentSpecificInformation);
        wrapper = new GPRSEventSpecificInformationWrapperImpl(prim);
        rawData = this.getData5();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // option 6
        prim = new GPRSEventSpecificInformationImpl(pdpContextEstablishmentAcknowledgementSpecificInformation);
        wrapper = new GPRSEventSpecificInformationWrapperImpl(prim);
        rawData = this.getData6();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}