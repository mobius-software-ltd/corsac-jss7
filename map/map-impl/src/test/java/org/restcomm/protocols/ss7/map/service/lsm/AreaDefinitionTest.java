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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.service.lsm.Area;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

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
public class AreaDefinitionTest {
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
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 29, -96, 27, 48, 12, -128, 1, 5, -127, 7, 2, -15, 113, 13, 62, -41, -114, 48, 11, -128, 1, 3,
                -127, 6, 18, 112, 113, 3, -24, 100 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AreaDefinitionImpl.class);
    	
        byte[] data = getEncodedData();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AreaDefinitionImpl);
        AreaDefinitionImpl areaDef = (AreaDefinitionImpl)result.getResult();

        List<Area> areaList = areaDef.getAreaList();

        assertNotNull(areaList);
        assertEquals(areaList.size(), 2);

        assertEquals(areaList.get(0).getAreaType(), AreaType.utranCellId);
        assertEquals(areaList.get(1).getAreaType(), AreaType.routingAreaId);
        assertEquals(areaList.get(0).getAreaIdentification().getMCC(), 201);
        assertEquals(areaList.get(0).getAreaIdentification().getMNC(), 17);
        assertEquals(areaList.get(0).getAreaIdentification().getUtranCellId(), 222222222);
        assertEquals(areaList.get(1).getAreaIdentification().getMCC(), 210);
        assertEquals(areaList.get(1).getAreaIdentification().getMNC(), 177);
        assertEquals(areaList.get(1).getAreaIdentification().getLac(), 1000);
        assertEquals(areaList.get(1).getAreaIdentification().getRac(), 100);

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AreaDefinitionImpl.class);
    	
        AreaIdentificationImpl ai1 = new AreaIdentificationImpl(AreaType.utranCellId, 201, 17, 0, 222222222);
        AreaIdentificationImpl ai2 = new AreaIdentificationImpl(AreaType.routingAreaId, 210, 177, 1000, 100);
        AreaImpl area1 = new AreaImpl(AreaType.utranCellId, ai1);
        AreaImpl area2 = new AreaImpl(AreaType.routingAreaId, ai2);

        List<Area> areaList = new ArrayList<Area>();
        areaList.add(area1);
        areaList.add(area2);
        AreaDefinitionImpl areaDef = new AreaDefinitionImpl(areaList);

        byte[] data = getEncodedData();
        ByteBuf buffer=parser.encode(areaDef);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}