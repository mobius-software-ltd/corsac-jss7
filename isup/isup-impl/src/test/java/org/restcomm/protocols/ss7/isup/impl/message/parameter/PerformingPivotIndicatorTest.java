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
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotReason;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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

    @Test
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
        assertNotNull(reasons);
        assertEquals(reasons.size(), 2);
        assertNotNull(reasons.get(0));
        assertNotNull(reasons.get(1));
        assertEquals(reasons.get(0).getPivotReason(), (byte) 1);
        assertEquals(reasons.get(0).getPivotPossibleAtPerformingExchange(), (byte) 5);
        assertEquals(reasons.get(1).getPivotReason(), (byte) 21);
        assertEquals(reasons.get(1).getPivotPossibleAtPerformingExchange(), (byte) 3);
    }
}