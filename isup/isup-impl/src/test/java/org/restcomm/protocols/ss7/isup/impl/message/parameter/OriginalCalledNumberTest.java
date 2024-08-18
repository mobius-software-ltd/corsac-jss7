/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private ByteBuf getData() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 131, 68, 0x21, 0x43, 0x05 });
    }

    private ByteBuf getData2() {
        return Unpooled.wrappedBuffer(new byte[] { 0, 8 });
    }

    @Test
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

    @Test
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
