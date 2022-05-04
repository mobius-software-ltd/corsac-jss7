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

package org.restcomm.protocols.ss7.sccp.impl;

import org.restcomm.protocols.ss7.sccp.ConcernedSignalingPointCode;
import org.restcomm.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.restcomm.protocols.ss7.sccp.RemoteSubSystem;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * @author gennadiy dubina
 * @author yulianoifa
 *
 */
public class SccpResourceProhibitedTest {

    private SccpResourceImpl resource = null;

    public SccpResourceProhibitedTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @AfterMethod
    public void tearDown() {
        resource.removeAllResourses();
        resource.stop();
    }

    @Test(groups = { "sccpresource", "functional.sccp" })
    public void testProhibitedTrue() throws Exception {
        resource = new SccpResourceImpl("SccpResourceProhibitedTest", true);
        resource.start();
        resource.removeAllResourses();

        resource.addRemoteSpc(1, 6034, 0, 0);
        resource.addRemoteSpc(2, 6045, 0, 0);

        assertTrue(resource.getRemoteSpc(1).isRemoteSpcProhibited());
        assertTrue(resource.getRemoteSpc(1).isRemoteSccpProhibited());

        assertTrue(resource.getRemoteSpc(2).isRemoteSpcProhibited());
        assertTrue(resource.getRemoteSpc(2).isRemoteSccpProhibited());
    }

    @Test(groups = { "sccpresource", "functional.sccp" })
    public void testProhibitedFalse() throws Exception {
        resource = new SccpResourceImpl("SccpResourceProhibitedTest", false);
        resource.start();
        resource.removeAllResourses();

        resource.addRemoteSpc(1, 6034, 0, 0);
        resource.addRemoteSpc(2, 6045, 0, 0);

        assertFalse(resource.getRemoteSpc(1).isRemoteSpcProhibited());
        assertFalse(resource.getRemoteSpc(1).isRemoteSccpProhibited());

        assertFalse(resource.getRemoteSpc(2).isRemoteSpcProhibited());
        assertFalse(resource.getRemoteSpc(2).isRemoteSccpProhibited());
    }

    @Test(groups = { "sccpresource", "functional.encode" })
    public void testSerialization() throws Exception {

        resource = new SccpResourceImpl("SccpResourceProhibitedTest", true);
        resource.start();
        resource.removeAllResourses();

        resource.addRemoteSpc(1, 6034, 0, 0);
        resource.addRemoteSpc(2, 6045, 0, 0);

        resource.addRemoteSsn(1, 6034, 8, 0, false);
        resource.addRemoteSsn(2, 6045, 8, 0, false);

        resource.addConcernedSpc(1, 603);
        resource.addConcernedSpc(2, 604);

        assertEquals(resource.getRemoteSpcs().size(), 2);
        RemoteSignalingPointCode rsp1Temp = resource.getRemoteSpc(1);
        assertNotNull(rsp1Temp);
        assertEquals(rsp1Temp.getRemoteSpc(), 6034);

        assertTrue(resource.getRemoteSpc(1).isRemoteSpcProhibited());
        assertTrue(resource.getRemoteSpc(1).isRemoteSccpProhibited());

        assertTrue(resource.getRemoteSpc(2).isRemoteSpcProhibited());
        assertTrue(resource.getRemoteSpc(2).isRemoteSccpProhibited());

        assertEquals(resource.getRemoteSsns().size(), 2);
        RemoteSubSystem rss1Temp = resource.getRemoteSsn(1);
        assertEquals(rss1Temp.getRemoteSsn(), 8);

        assertEquals(resource.getConcernedSpcs().size(), 2);
        ConcernedSignalingPointCode cspc1Temp = resource.getConcernedSpc(1);
        assertEquals(cspc1Temp.getRemoteSpc(), 603);
    }

}
