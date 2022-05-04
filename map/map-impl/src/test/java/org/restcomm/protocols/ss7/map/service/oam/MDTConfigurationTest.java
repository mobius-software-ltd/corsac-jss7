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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;
import org.restcomm.protocols.ss7.map.api.service.oam.AreaScope;
import org.restcomm.protocols.ss7.map.api.service.oam.JobType;
import org.restcomm.protocols.ss7.map.api.service.oam.LoggingDuration;
import org.restcomm.protocols.ss7.map.api.service.oam.LoggingInterval;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportAmount;
import org.restcomm.protocols.ss7.map.api.service.oam.ReportInterval;
import org.restcomm.protocols.ss7.map.primitives.GlobalCellIdImpl;
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
public class MDTConfigurationTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 3, 10, 1, 2 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 84, 10, 1, 2, 48, 11, (byte) 160, 9, 4, 7, 33, (byte) 240, 16, 7, (byte) 208, 4, 87, 4, 4, 11, 12, 13, 14, (byte) 128, 1, 121,
                10, 1, 20, (byte) 129, 1, 6, 2, 1, 10, (byte) 130, 1, 11, (byte) 131, 1, 4, (byte) 132, 1, 2, (byte) 165, 39, (byte) 160, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    }

    private byte[] getListOfMeasurements() {
        return new byte[] { 11, 12, 13, 14 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MDTConfigurationImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MDTConfigurationImpl);
        MDTConfigurationImpl asc = (MDTConfigurationImpl)result.getResult();
        
        assertEquals(asc.getJobType(), JobType.traceOnly);

        assertNull(asc.getAreaScope());
        assertNull(asc.getListOfMeasurements());
        assertNull(asc.getReportingTrigger());
        assertNull(asc.getReportInterval());
        assertNull(asc.getReportAmount());
        assertNull(asc.getEventThresholdRSRP());
        assertNull(asc.getEventThresholdRSRQ());
        assertNull(asc.getLoggingInterval());
        assertNull(asc.getLoggingDuration());
        assertNull(asc.getExtensionContainer());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MDTConfigurationImpl);
        asc = (MDTConfigurationImpl)result.getResult();
        
        assertEquals(asc.getJobType(), JobType.traceOnly);

        assertEquals(asc.getAreaScope().getCgiList().size(), 1);
        assertEquals(asc.getAreaScope().getCgiList().get(0).getMcc(), 120);
        assertEquals(asc.getAreaScope().getCgiList().get(0).getMnc(), 1);
        assertEquals(asc.getAreaScope().getCgiList().get(0).getLac(), 2000);
        assertEquals(asc.getAreaScope().getCgiList().get(0).getCellId(), 1111);

        assertTrue(ByteBufUtil.equals(asc.getListOfMeasurements().getValue(),Unpooled.wrappedBuffer(getListOfMeasurements())));
        assertEquals(asc.getReportingTrigger().getData(), 121);
        assertEquals(asc.getReportInterval(), ReportInterval.lte2048ms);
        assertEquals(asc.getReportAmount(), ReportAmount.d64);
        assertEquals((int)asc.getEventThresholdRSRP(), 10);
        assertEquals((int)asc.getEventThresholdRSRQ(), 11);
        assertEquals(asc.getLoggingInterval(), LoggingInterval.d20dot48);
        assertEquals(asc.getLoggingDuration(), LoggingDuration.d2400sec);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(MDTConfigurationImpl.class);
    	
        MDTConfigurationImpl asc = new MDTConfigurationImpl(JobType.traceOnly, null, null, null, null, null, null, null, null, null, null);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        List<GlobalCellId> cgiList = new ArrayList<GlobalCellId>();
        GlobalCellIdImpl globalCellId = new GlobalCellIdImpl(120, 1, 2000, 1111); // int mcc, int mnc, int lac, int cellId
        cgiList.add(globalCellId);
        AreaScope areaScope = new AreaScopeImpl(cgiList, null, null, null, null, null);
        ListOfMeasurementsImpl listOfMeasurements = new ListOfMeasurementsImpl(Unpooled.wrappedBuffer(getListOfMeasurements()));
        ReportingTriggerImpl reportingTrigger = new ReportingTriggerImpl(121);
        asc = new MDTConfigurationImpl(JobType.traceOnly, areaScope, listOfMeasurements, reportingTrigger, ReportInterval.lte2048ms, ReportAmount.d64, 10, 11,
                LoggingInterval.d20dot48, LoggingDuration.d2400sec, MAPExtensionContainerTest.GetTestExtensionContainer());

        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}