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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityType;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class ProvideSubscriberLocationResponseTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public byte[] getEncodedData() {
        return new byte[] { 48, 13, 4, 8, 16, 92, 113, -57, -106, 11, 97, 7, -128, 1, 15 };
    }

    public byte[] getEncodedDataFull() {
        return new byte[] { 48, 73, 4, 8, 16, 92, 113, -57, -106, 11, 97, 7, -128, 1, 15, -126, 8, 16, 92, 113, -57, -106, 11, 97, 7, -125, 0, -124, 2, 11, 
        		12, -123, 3, 15, 16, 17, -90, 7, -127, 5, 33, -15, 16, 8, -82, -121, 0, -120, 1, 0, -119, 4, 0, 90, 0, 59, -118, 
        		0, -117, 2, 25, 26, -116, 1, 29, -83, 8, -128, 6, -111, 68, 100, 102, -120, -8 };
    }

    public byte[] getPositioningDataInformation() {
        return new byte[] { 11, 12 };
    }

    public byte[] getUtranPositioningDataInfo() {
        return new byte[] { 15, 16, 17 };
    }

    public byte[] getGeranGANSSpositioningData() {
        return new byte[] { 25, 26 };
    }

    public byte[] getUtranGANSSpositioningData() {
        return new byte[] { 29 };
    }

    @Test
    public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProvideSubscriberLocationResponseImpl.class);
    	
        byte[] data = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideSubscriberLocationResponseImpl);
        ProvideSubscriberLocationResponseImpl impl = (ProvideSubscriberLocationResponseImpl)result.getResult();

        assertEquals(impl.getLocationEstimate().getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLocationEstimate().getLatitude() - 65) < 0.01);
        assertTrue(Math.abs(impl.getLocationEstimate().getLongitude() - (-149)) < 0.01);  // -31
        assertTrue(Math.abs(impl.getLocationEstimate().getUncertainty() - 9.48) < 0.01);
        
        assertEquals((int) impl.getAgeOfLocationEstimate(), 15);

        assertNull(impl.getExtensionContainer());
        assertNull(impl.getAdditionalLocationEstimate());
        assertFalse(impl.getDeferredMTLRResponseIndicator());
        assertNull(impl.getGeranPositioningData());
        assertNull(impl.getUtranPositioningData());
        assertNull(impl.getCellIdOrSai());
        assertFalse(impl.getSaiPresent());
        assertNull(impl.getAccuracyFulfilmentIndicator());
        assertNull(impl.getVelocityEstimate());
        assertFalse(impl.getMoLrShortCircuitIndicator());
        assertNull(impl.getGeranGANSSpositioningData());
        assertNull(impl.getUtranGANSSpositioningData());
        assertNull(impl.getTargetServingNodeForHandover());

        data = getEncodedDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProvideSubscriberLocationResponseImpl);
        impl = (ProvideSubscriberLocationResponseImpl)result.getResult();

        assertEquals(impl.getLocationEstimate().getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLocationEstimate().getLatitude() - 65) < 0.01);
        assertTrue(Math.abs(impl.getLocationEstimate().getLongitude() - (-149)) < 0.01);  // -31
        assertTrue(Math.abs(impl.getLocationEstimate().getUncertainty() - 9.48) < 0.01);
        
        assertEquals((int) impl.getAgeOfLocationEstimate(), 15);

        assertNull(impl.getExtensionContainer());

        assertEquals(impl.getAdditionalLocationEstimate().getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getAdditionalLocationEstimate().getLatitude() - 65) < 0.01);
        assertTrue(Math.abs(impl.getAdditionalLocationEstimate().getLongitude() - (-149)) < 0.01); // -31
        assertTrue(Math.abs(impl.getAdditionalLocationEstimate().getUncertainty() - 9.48) < 0.01);
        
        assertTrue(impl.getDeferredMTLRResponseIndicator());
        assertTrue(ByteBufUtil.equals(impl.getGeranPositioningData().getValue(),Unpooled.wrappedBuffer(getPositioningDataInformation())));
        assertTrue(ByteBufUtil.equals(impl.getUtranPositioningData().getValue(),Unpooled.wrappedBuffer(getUtranPositioningDataInfo())));
        assertEquals(impl.getCellIdOrSai().getLAIFixedLength().getMCC(), 121);
        assertEquals(impl.getCellIdOrSai().getLAIFixedLength().getMNC(), 1);
        assertEquals(impl.getCellIdOrSai().getLAIFixedLength().getLac(), 2222);
        assertTrue(impl.getSaiPresent());
        assertEquals(impl.getAccuracyFulfilmentIndicator(), AccuracyFulfilmentIndicator.requestedAccuracyFulfilled);
        
        assertEquals(impl.getVelocityEstimate().getVelocityType(), VelocityType.HorizontalVelocity);
        assertEquals(impl.getVelocityEstimate().getHorizontalSpeed(), 59);
        assertEquals(impl.getVelocityEstimate().getBearing(), 90);
        
        assertTrue(impl.getMoLrShortCircuitIndicator());
        assertTrue(ByteBufUtil.equals(impl.getGeranGANSSpositioningData().getValue(), Unpooled.wrappedBuffer(getGeranGANSSpositioningData())));
        assertTrue(ByteBufUtil.equals(impl.getUtranGANSSpositioningData().getValue(), Unpooled.wrappedBuffer(getUtranGANSSpositioningData())));
        assertTrue(impl.getTargetServingNodeForHandover().getMscNumber().getAddress().equals("444666888"));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProvideSubscriberLocationResponseImpl.class);
    	
        ExtGeographicalInformationImpl egeo = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, 65, -149, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        ProvideSubscriberLocationResponseImpl reqInd = new ProvideSubscriberLocationResponseImpl(egeo, null, null, 15, null,
                null, false, null, false, null, null, false, null, null, null);

        byte[] data=getEncodedData();
        ByteBuf buffer=parser.encode(reqInd);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        PositioningDataInformationImpl geranPositioningData = new PositioningDataInformationImpl(Unpooled.wrappedBuffer(
                getPositioningDataInformation()));
        UtranPositioningDataInfoImpl utranPositioningData = new UtranPositioningDataInfoImpl(Unpooled.wrappedBuffer(getUtranPositioningDataInfo()));
        AddGeographicalInformationImpl additionalLocationEstimate = new AddGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, 65, -149, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        LAIFixedLengthImpl laiFixedLength = new LAIFixedLengthImpl(121, 1, 2222);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(
                laiFixedLength);
        VelocityEstimateImpl velocityEstimate = new VelocityEstimateImpl(VelocityType.HorizontalVelocity, 59, 90, 0, 0, 0);;
        GeranGANSSpositioningDataImpl geranGANSSpositioningData = new GeranGANSSpositioningDataImpl(Unpooled.wrappedBuffer(
                getGeranGANSSpositioningData()));
        UtranGANSSpositioningDataImpl utranGANSSpositioningData = new UtranGANSSpositioningDataImpl(Unpooled.wrappedBuffer(
                getUtranGANSSpositioningData()));
        ISDNAddressStringImpl isdnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "444666888");
        ServingNodeAddressImpl targetServingNodeForHandover = new ServingNodeAddressImpl(isdnNumber, true);

        reqInd = new ProvideSubscriberLocationResponseImpl(egeo, geranPositioningData, utranPositioningData, 15,
                additionalLocationEstimate, null, true, cellGlobalIdOrServiceAreaIdOrLAI, true,
                AccuracyFulfilmentIndicator.requestedAccuracyFulfilled, velocityEstimate, true, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover);
        
        data=getEncodedDataFull();
        buffer=parser.encode(reqInd);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }

}