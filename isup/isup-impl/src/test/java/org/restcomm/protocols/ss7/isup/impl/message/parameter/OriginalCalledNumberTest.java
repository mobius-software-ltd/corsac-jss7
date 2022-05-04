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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
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
public class OriginalCalledNumberTest {
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
        return Unpooled.wrappedBuffer(new byte[] { (byte) 131, 68, 0x21, 0x43, 0x05 });
    }

    private ByteBuf getData2() {
        return Unpooled.wrappedBuffer(new byte[] { 0, 8 });
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        OriginalCalledNumberImpl prim = new OriginalCalledNumberImpl();
        prim.decode(getData());

        assertEquals(prim.getAddress(), "12345");
        assertEquals(prim.getNatureOfAddressIndicator(), NAINumber._NAI_NATIONAL_SN);
        assertEquals(prim.getNumberingPlanIndicator(), CallingPartyNumber._NPI_TELEX);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), CallingPartyNumber._APRI_RESTRICTED);
        assertTrue(prim.isOddFlag());

        prim = new OriginalCalledNumberImpl();
        prim.decode(getData2());

        assertEquals(prim.getAddress(), "");
        assertEquals(prim.getNatureOfAddressIndicator(), 0);
        assertEquals(prim.getNumberingPlanIndicator(), 0);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), CallingPartyNumber._APRI_NOT_AVAILABLE);
        assertFalse(prim.isOddFlag());

    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        OriginalCalledNumberImpl prim = new OriginalCalledNumberImpl(OriginalCalledNumber._NAI_NATIONAL_SN, "12345",
                OriginalCalledNumber._NPI_TELEX, OriginalCalledNumber._APRI_RESTRICTED);
        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int addressRepresentationRestrictedIndicator

        ByteBuf data = getData();
        ByteBuf encodedData = Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

        prim = new OriginalCalledNumberImpl(0, "", 0, OriginalCalledNumber._APRI_NOT_AVAILABLE);

        data = getData2();
        encodedData = Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));
    }
}
