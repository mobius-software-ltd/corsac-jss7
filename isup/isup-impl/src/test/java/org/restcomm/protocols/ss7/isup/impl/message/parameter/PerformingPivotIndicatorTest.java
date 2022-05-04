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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.isup.message.parameter.PivotReason;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author baranowb
 * @author yulianoifa
 * 
 */
public class PerformingPivotIndicatorTest {

    /**
     * 
     */
    public PerformingPivotIndicatorTest() {
        // TODO Auto-generated constructor stub
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testSetEncodeDecodeGet() throws Exception {
        PerformingPivotIndicatorImpl ppi = new PerformingPivotIndicatorImpl();
        PivotReasonImpl pr1 = new PivotReasonImpl();
        pr1.setPivotReason((byte) 1);
        pr1.setPivotPossibleAtPerformingExchange((byte) 5);
        PivotReasonImpl pr2 = new PivotReasonImpl();
        pr2.setPivotReason((byte) 21);
        pr2.setPivotPossibleAtPerformingExchange((byte) 3);
        ppi.setReason(Arrays.asList(new PivotReason [] {pr1, pr2}));
        ByteBuf b = Unpooled.buffer();
        ppi.encode(b);
        ppi = new PerformingPivotIndicatorImpl();
        ppi.decode(b);
        List<PivotReason> reasons = ppi.getReason();
        Assert.assertNotNull(reasons);
        Assert.assertEquals(reasons.size(), 2);
        Assert.assertNotNull(reasons.get(0));
        Assert.assertNotNull(reasons.get(1));
        Assert.assertEquals(reasons.get(0).getPivotReason(), (byte) 1);
        Assert.assertEquals(reasons.get(0).getPivotPossibleAtPerformingExchange(), (byte) 5);
        Assert.assertEquals(reasons.get(1).getPivotReason(), (byte) 21);
        Assert.assertEquals(reasons.get(1).getPivotPossibleAtPerformingExchange(), (byte) 3);
    }
}