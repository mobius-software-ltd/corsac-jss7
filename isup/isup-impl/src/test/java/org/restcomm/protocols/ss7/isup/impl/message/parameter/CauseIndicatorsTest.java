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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CauseIndicatorsTest {
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

    private ByteBuf getData() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 133, (byte) 149 });
    }

    private ByteBuf getData2() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 133, (byte) 149, 1, 2, (byte) 0xFF });
    }

    private ByteBuf getDiagnosticsData() {
        return Unpooled.wrappedBuffer(new byte[] { 1, 2, (byte) 0xFF });
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        CauseIndicatorsImpl prim = new CauseIndicatorsImpl();
        prim.decode(getData());

        assertEquals(prim.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
        assertEquals(prim.getLocation(), CauseIndicators._LOCATION_PRIVATE_NSRU);
        assertEquals(prim.getRecommendation(), 0);
        assertEquals(prim.getCauseValue(), CauseIndicators._CV_CALL_REJECTED);
        assertNull(prim.getDiagnostics());

        prim = new CauseIndicatorsImpl();
        prim.decode(getData2());

        assertEquals(prim.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
        assertEquals(prim.getLocation(), CauseIndicators._LOCATION_PRIVATE_NSRU);
        assertEquals(prim.getRecommendation(), 0);
        assertEquals(prim.getCauseValue(), CauseIndicators._CV_CALL_REJECTED);
        assertEquals(prim.getDiagnostics(), getDiagnosticsData());

        // TODO: add an encoding/decoding unittest for CodingStandard!=CauseIndicators._CODING_STANDARD_ITUT and
        // recomendations!=null (extra Recommendation byte)
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        CauseIndicatorsImpl prim = new CauseIndicatorsImpl(CauseIndicators._CODING_STANDARD_ITUT,
                CauseIndicators._LOCATION_PRIVATE_NSRU, 0, CauseIndicators._CV_CALL_REJECTED, null);

        ByteBuf data = getData();
        ByteBuf encodedData=Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

        prim = new CauseIndicatorsImpl(CauseIndicators._CODING_STANDARD_ITUT, CauseIndicators._LOCATION_PRIVATE_NSRU, 0,
                CauseIndicators._CV_CALL_REJECTED, getDiagnosticsData());

        data = getData2();
        encodedData=Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

        // TODO: add an encoding/decoding unittest for CodingStandard!=CauseIndicators._CODING_STANDARD_ITUT and
        // recomendations!=null (extra Recommendation byte)
    }
}
