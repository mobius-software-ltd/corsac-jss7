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

package org.restcomm.protocols.ss7.map.service.oam;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.oam.JobType;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepth;
import org.testng.annotations.Test;

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
public class ActivateTraceModeRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 15, (byte) 128, 7, 51, 51, 35, 34, 34, 0, 17, (byte) 129, 1, 11, (byte) 130, 1, 55 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 105, (byte) 128, 7, 51, 51, 35, 34, 34, 0, 17, (byte) 129, 1, 11, (byte) 130, 1, 55, (byte) 131, 6, (byte) 145, 17, 17, 49, 51,
                51, (byte) 164, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, (byte) 161, 3, 31, 32, 33, (byte) 133, 3, 12, 13, 14, (byte) 166, 6, (byte) 128, 1, 2, (byte) 129, 1, 0, (byte) 135, 2, 4, 16,
                (byte) 168, 4, (byte) 128, 2, 6, 64, (byte) 169, 4, (byte) 128, 2, 7, (byte) 128, (byte) 138, 5, 4, (byte) 192, (byte) 168, 4, 1,
                (byte) 171, 3, 10, 1, 3 };
    }

    private byte[] getTraceReferenceData() {
        return new byte[] { 11 };
    }

    private byte[] getTraceReference2Data() {
        return new byte[] { 12, 13, 14 };
    }

    private byte[] getTraceCollectionEntityData() {
        return new byte[] { (byte) 192, (byte) 168, 4, 1 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ActivateTraceModeRequestImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ActivateTraceModeRequestImpl);
        ActivateTraceModeRequestImpl asc = (ActivateTraceModeRequestImpl)result.getResult();
        
        assertEquals(asc.getImsi().getData(), "33333222220011");
        assertEquals(asc.getTraceReference().getValue(), Unpooled.wrappedBuffer(getTraceReferenceData()));
        assertEquals(asc.getTraceType().getData(), 55);

        assertNull(asc.getOmcId());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getTraceReference2());
        assertNull(asc.getTraceDepthList());
        assertNull(asc.getTraceNeTypeList());
        assertNull(asc.getTraceInterfaceList());
        assertNull(asc.getTraceEventList());
        assertNull(asc.getTraceCollectionEntity());
        assertNull(asc.getMdtConfiguration());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ActivateTraceModeRequestImpl);
        asc = (ActivateTraceModeRequestImpl)result.getResult();
        
        assertEquals(asc.getImsi().getData(), "33333222220011");
        assertTrue(ByteBufUtil.equals(asc.getTraceReference().getValue(),Unpooled.wrappedBuffer(getTraceReferenceData())));
        assertEquals(asc.getTraceType().getData(), 55);

        assertEquals(asc.getOmcId().getAddress(), "1111133333");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));
        assertTrue(ByteBufUtil.equals(asc.getTraceReference2().getValue(),Unpooled.wrappedBuffer(getTraceReference2Data())));
        assertEquals(asc.getTraceDepthList().getMscSTraceDepth(), TraceDepth.maximum);
        assertEquals(asc.getTraceDepthList().getMgwTraceDepth(), TraceDepth.minimum);
        assertTrue(asc.getTraceNeTypeList().getGgsn());
        assertFalse(asc.getTraceNeTypeList().getSgsn());
        assertTrue(asc.getTraceInterfaceList().getMscSList().getIu());
        assertFalse(asc.getTraceInterfaceList().getMscSList().getA());
        assertTrue(asc.getTraceEventList().getMscSList().getMoMtCall());
        assertFalse(asc.getTraceEventList().getMscSList().getMoMtSms());
        assertEquals(asc.getTraceCollectionEntity().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(asc.getTraceCollectionEntity().getGSNAddressData(),Unpooled.wrappedBuffer(getTraceCollectionEntityData())));
        assertEquals(asc.getMdtConfiguration().getJobType(), JobType.immediateMdtAndTrace);
    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ActivateTraceModeRequestImpl.class);
    	
        IMSIImpl imsi = new IMSIImpl("33333222220011");
        TraceReferenceImpl traceReference = new TraceReferenceImpl(Unpooled.wrappedBuffer(getTraceReferenceData()));
        TraceTypeImpl traceType = new TraceTypeImpl(55);
        ActivateTraceModeRequestImpl asc = new ActivateTraceModeRequestImpl(imsi, traceReference, traceType, null, null, null, null, null, null, null, null,
                null);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        AddressStringImpl omcId = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1111133333");
        TraceReference2Impl traceReference2 = new TraceReference2Impl(Unpooled.wrappedBuffer(getTraceReference2Data()));
        TraceDepthListImpl traceDepthList = new TraceDepthListImpl(TraceDepth.maximum, TraceDepth.minimum, null, null, null, null, null, null, null, null);
        TraceNETypeListImpl traceNeTypeList = new TraceNETypeListImpl(false, false, false, true, false, false, false, false, false, false);
        MSCSInterfaceListImpl mscSList = new MSCSInterfaceListImpl(false, true, false, false, false, false, false, false, false, false);
        TraceInterfaceListImpl traceInterfaceList = new TraceInterfaceListImpl(mscSList, null, null, null, null, null, null, null, null, null);
        MSCSEventListImpl mscSList2 = new MSCSEventListImpl(true, false, false, false, false);
        TraceEventListImpl traceEventList = new TraceEventListImpl(mscSList2, null, null, null, null, null, null, null);
        GSNAddressImpl traceCollectionEntity = new GSNAddressImpl(GSNAddressAddressType.IPv4,Unpooled.wrappedBuffer(getTraceCollectionEntityData()));
        MDTConfigurationImpl mdtConfiguration = new MDTConfigurationImpl(JobType.immediateMdtAndTrace, null, null, null, null, null, null, null, null, null, null);
        asc = new ActivateTraceModeRequestImpl(imsi, traceReference, traceType, omcId, MAPExtensionContainerTest.GetTestExtensionContainer(), traceReference2,
                traceDepthList, traceNeTypeList, traceInterfaceList, traceEventList, traceCollectionEntity, mdtConfiguration);

        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}