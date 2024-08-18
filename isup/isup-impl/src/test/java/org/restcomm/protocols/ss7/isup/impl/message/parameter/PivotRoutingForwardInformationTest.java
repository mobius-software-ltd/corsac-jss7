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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingPivotReason;
import org.restcomm.protocols.ss7.isup.message.parameter.PerformingPivotIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotReason;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotRoutingForwardInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeCallIdentifier;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangePossible;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 * Class to test BCI
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */

public class PivotRoutingForwardInformationTest {
//This one does not use harness, since this param has multiple levels of nesting ....
    public PivotRoutingForwardInformationTest() {
        super();

    }

    private ByteBuf getBody1() {

        byte[] body = new byte[] {
                //3.99.1 ReturnToInvokingExchangePossible
                0x01,
                    //len
                    0x00,
                0x01,
                    //len
                    0x00,
                //3.99.2
                0x02,
                    //len
                    0x05,
                    //body
                    (byte)0xAA,
                    0,
                    (byte)0xAA,
                    0x55,
                    0x15,
                //3.99.3
                0x03,
                    //len
                    0x06,
                    //body
                    //pri1
                    (byte)(0x80|0x12),
                    //pri2
                    0x12,
                    0x05,
                    //pri3
                    0x11,
                    0x04,
                    //pri4
                    (byte)(0x80|0x2),
                //3.99.4
                0x04,
                   //len
                   0x03,
                   //body
                   0x01,
                   0x02,
                   (byte)(0x80| 0x03)
                
        };
        return Unpooled.wrappedBuffer(body);
    }

    @Test
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        PivotRoutingForwardInformationImpl parameter = new PivotRoutingForwardInformationImpl(getBody1());
        ReturnToInvokingExchangePossible[] rtiep = parameter.getReturnToInvokingExchangePossible();
        assertNotNull(rtiep);
        assertEquals(rtiep.length,2);
        assertNotNull(rtiep[0]);
        assertNotNull(rtiep[1]);

        ReturnToInvokingExchangeCallIdentifier[] callIds = parameter.getReturnToInvokingExchangeCallIdentifier();
        assertNotNull(callIds);
        assertEquals(callIds.length,1);
        ReturnToInvokingExchangeCallIdentifier id = callIds[0];
        assertNotNull(id);
        assertEquals(id.getCallIdentity(), 0xAA00AA);
        assertEquals(id.getSignalingPointCode(), 0x1555);
        
        PerformingPivotIndicator[] pris = parameter.getPerformingPivotIndicator();
        assertNotNull(pris);
        assertEquals(pris.length,1);
        PerformingPivotIndicator ri = pris[0];
        assertNotNull(ri);
        List<PivotReason> rrs = ri.getReason();
        assertNotNull(rrs);
        assertEquals(rrs.size(),4);
        assertNotNull(rrs.get(0));
        assertNotNull(rrs.get(1));
        assertNotNull(rrs.get(2));
        assertNotNull(rrs.get(3));

        assertEquals(rrs.get(0).getPivotReason(), 18);
        assertEquals(rrs.get(0).getPivotPossibleAtPerformingExchange(), 0);

        assertEquals(rrs.get(1).getPivotReason(), 18);
        assertEquals(rrs.get(1).getPivotPossibleAtPerformingExchange(), 5);

        assertEquals(rrs.get(2).getPivotReason(), 17);
        assertEquals(rrs.get(2).getPivotPossibleAtPerformingExchange(), 4);

        assertEquals(rrs.get(3).getPivotReason(), 2);
        assertEquals(rrs.get(3).getPivotPossibleAtPerformingExchange(), 0);
        
        InvokingPivotReason[] inrs = parameter.getInvokingPivotReason();
        assertNotNull(inrs);
        assertEquals(inrs.length,1);
        assertNotNull(inrs[0]);
        InvokingPivotReason inr = inrs[0];
        PivotReason[] rrs2 = inr.getReason();
        assertNotNull(rrs2);
        assertEquals(rrs2.length,3);
        assertNotNull(rrs2[0]);
        assertEquals(rrs2[0].getPivotReason(), 1);
        assertNotNull(rrs2[1]);
        assertEquals(rrs2[1].getPivotReason(), 2);
        assertNotNull(rrs2[2]);
        assertEquals(rrs2[2].getPivotReason(), 3);
        
    }

    
    @Test
    public void testSetAndGet() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        PivotRoutingForwardInformationImpl parameter = new PivotRoutingForwardInformationImpl();

        parameter.setReturnToInvokingExchangePossible(new ReturnToInvokingExchangePossibleImpl());

        ReturnToInvokingExchangeCallIdentifierImpl callId = new ReturnToInvokingExchangeCallIdentifierImpl();
        callId.setCallIdentity(0XBB00BC);
        ReturnToInvokingExchangeCallIdentifierImpl callId2 = new ReturnToInvokingExchangeCallIdentifierImpl();
        callId2.setCallIdentity(0XCBF0BC);
        callId2.setSignalingPointCode(1);
        parameter.setReturnToInvokingExchangeCallIdentifier(callId,callId2);

        PerformingPivotIndicatorImpl pri = new PerformingPivotIndicatorImpl();
        PivotReasonImpl rr1 = new PivotReasonImpl();
        rr1.setPivotReason((byte) 1);
        PivotReasonImpl rr2 = new PivotReasonImpl();
        rr2.setPivotReason((byte) 1);
        rr2.setPivotPossibleAtPerformingExchange((byte) 2);
        pri.setReason(Arrays.asList(new PivotReason [] {rr1,rr2}));
        parameter.setPerformingPivotIndicator(pri);
        
        InvokingPivotReasonImpl irr = new InvokingPivotReasonImpl();
        //this differs across some params...
        irr.setTag(PivotRoutingForwardInformation.INFORMATION_INVOKING_PIVOT_REASON);
        irr.setReason(rr1,rr2);
        parameter.setInvokingPivotReason(irr);

        ByteBuf data = Unpooled.buffer();
        parameter.encode(data);
        parameter = new PivotRoutingForwardInformationImpl();
        parameter.decode(data);


        assertNotNull(parameter.getReturnToInvokingExchangePossible());
        assertEquals(parameter.getReturnToInvokingExchangePossible().length,1);

        assertNotNull(parameter.getReturnToInvokingExchangeCallIdentifier());
        assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier().length,2);
        assertNotNull(parameter.getReturnToInvokingExchangeCallIdentifier()[0]);
        assertNotNull(parameter.getReturnToInvokingExchangeCallIdentifier()[1]);
        
        assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier()[0].getCallIdentity(),0XBB00BC);
        assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier()[1].getCallIdentity(),0XCBF0BC);
        assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier()[1].getSignalingPointCode(),1);
     
        assertNotNull(parameter.getPerformingPivotIndicator());
        assertEquals(parameter.getPerformingPivotIndicator().length,1);

        assertNotNull(parameter.getPerformingPivotIndicator()[0].getReason());
        assertEquals(parameter.getPerformingPivotIndicator()[0].getReason().size(),2);
        assertNotNull(parameter.getPerformingPivotIndicator()[0].getReason().get(0));
        assertNotNull(parameter.getPerformingPivotIndicator()[0].getReason().get(1));
        assertEquals(parameter.getPerformingPivotIndicator()[0].getReason().get(0).getPivotReason(),1);
        assertEquals(parameter.getPerformingPivotIndicator()[0].getReason().get(1).getPivotReason(),1);
        assertEquals(parameter.getPerformingPivotIndicator()[0].getReason().get(1).getPivotPossibleAtPerformingExchange(),2);

        assertNotNull(parameter.getInvokingPivotReason()[0].getReason());
        assertEquals(parameter.getInvokingPivotReason()[0].getReason().length,2);
        assertNotNull(parameter.getInvokingPivotReason()[0].getReason()[0]);
        assertNotNull(parameter.getInvokingPivotReason()[0].getReason()[1]);
        assertEquals(parameter.getInvokingPivotReason()[0].getReason()[0].getPivotReason(),1);
        assertEquals(parameter.getInvokingPivotReason()[0].getReason()[1].getPivotReason(),1);
        //0 casuse this one does not have it.
        assertEquals(parameter.getInvokingPivotReason()[0].getReason()[1].getPivotPossibleAtPerformingExchange(),0);
    }

}
