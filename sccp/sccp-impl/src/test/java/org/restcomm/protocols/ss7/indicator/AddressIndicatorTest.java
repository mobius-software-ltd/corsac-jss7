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

package org.restcomm.protocols.ss7.indicator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class AddressIndicatorTest {

    /**
	 *
	 */
    public AddressIndicatorTest() {
        // TODO Auto-generated constructor stub
    }

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

    @Test
    public void testDecode() throws Exception {
        byte b = 0x42;
        AddressIndicator ai = new AddressIndicator(b, SccpProtocolVersion.ITU);
        assertFalse(ai.isPCPresent());
        assertTrue(ai.isSSNPresent());
        assertEquals(ai.getGlobalTitleIndicator(), GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED);
        assertEquals(ai.getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
        assertFalse(ai.isReservedForNationalUseBit());

        b = (byte) 0xC2;
        ai = new AddressIndicator(b, SccpProtocolVersion.ITU);
        assertFalse(ai.isPCPresent());
        assertTrue(ai.isSSNPresent());
        assertEquals(ai.getGlobalTitleIndicator(), GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED);
        assertEquals(ai.getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
        assertTrue(ai.isReservedForNationalUseBit());

        b = (byte) 197;
        ai = new AddressIndicator(b, SccpProtocolVersion.ANSI);
        assertFalse(ai.isPCPresent());
        assertTrue(ai.isSSNPresent());
        assertEquals(ai.getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME);
        assertEquals(ai.getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
        assertTrue(ai.isReservedForNationalUseBit());

        b = (byte) 138;
        ai = new AddressIndicator(b, SccpProtocolVersion.ANSI);
        assertTrue(ai.isPCPresent());
        assertFalse(ai.isSSNPresent());
        assertEquals(ai.getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
        assertEquals(ai.getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertTrue(ai.isReservedForNationalUseBit());
    }

    @Test
    public void testEncode() throws Exception {
        AddressIndicator ai = new AddressIndicator(false, true, RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED);
        assertEquals((int) ai.getValue(SccpProtocolVersion.ITU), 66);

        ai = new AddressIndicator(false, true, RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME);
        byte i1 = ai.getValue(SccpProtocolVersion.ANSI);
        byte i2 = (byte)(197);
        assertEquals(i1, i2);

        ai = new AddressIndicator(true, false, RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
        i1 = ai.getValue(SccpProtocolVersion.ANSI);

        i2 = (byte) (138);
        assertEquals(i1, i2);
    }
}
