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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.service.lsm.Area;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;
import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class AreaEventInfoTest {
    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getEncodedData() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 24, -96, 15, -96, 13, 48, 11, -128, 1, 3, -127, 6, 18, 112, 113, 3, -24, 100, -127, 1, 1, -126,
                2, 127, -2 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AreaEventInfoImpl.class);
    	
        byte[] data = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AreaEventInfoImpl);
        AreaEventInfoImpl areaEvtInf = (AreaEventInfoImpl)result.getResult();

        AreaDefinition areaDef = areaEvtInf.getAreaDefinition();
        assertNotNull(areaDef);

        List<Area> areaList = areaDef.getAreaList();

        assertNotNull(areaList);
        assertEquals(areaList.size(), 1);

        assertEquals(areaList.get(0).getAreaType(), AreaType.routingAreaId);
        assertEquals(areaList.get(0).getAreaIdentification().getMCC(), 210);
        assertEquals(areaList.get(0).getAreaIdentification().getMNC(), 177);
        assertEquals(areaList.get(0).getAreaIdentification().getLac(), 1000);
        assertEquals(areaList.get(0).getAreaIdentification().getRac(), 100);

        OccurrenceInfo occInfo = areaEvtInf.getOccurrenceInfo();
        assertNotNull(occInfo);
        assertEquals(occInfo, OccurrenceInfo.multipleTimeEvent);

        int intTime = areaEvtInf.getIntervalTime();
        assertEquals(intTime, 32766);

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AreaEventInfoImpl.class);
    	
        byte[] data = getEncodedData();

        AreaIdentificationImpl ai1 = new AreaIdentificationImpl(AreaType.routingAreaId, 210, 177, 1000, 100);
        AreaImpl area1 = new AreaImpl(AreaType.routingAreaId, ai1);

        List<Area> areaList = new ArrayList<Area>();
        areaList.add(area1);
        AreaDefinitionImpl areaDef = new AreaDefinitionImpl(areaList);

        AreaEventInfoImpl areaEvtInf = new AreaEventInfoImpl(areaDef, OccurrenceInfo.multipleTimeEvent, 32766);
        ByteBuf buffer=parser.encode(areaEvtInf);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}