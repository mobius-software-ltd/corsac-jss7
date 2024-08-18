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

import org.junit.Test;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingRedirectReason;
import org.restcomm.protocols.ss7.isup.message.parameter.PerformingRedirectIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectForwardInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectReason;
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

public class RedirectForwardInformationTest {
//This one does not use harness, since this param has multiple levels of nesting ....
    public RedirectForwardInformationTest() {
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
        RedirectForwardInformationImpl rfi = new RedirectForwardInformationImpl(getBody1());
        ReturnToInvokingExchangePossible[] rtiep = rfi.getReturnToInvokingExchangePossible();
        assertNotNull(rtiep);
        assertEquals(rtiep.length,2);
        assertNotNull(rtiep[0]);
        assertNotNull(rtiep[1]);

        ReturnToInvokingExchangeCallIdentifier[] callIds = rfi.getReturnToInvokingExchangeCallIdentifier();
        assertNotNull(callIds);
        assertEquals(callIds.length,1);
        ReturnToInvokingExchangeCallIdentifier id = callIds[0];
        assertNotNull(id);
        assertEquals(id.getCallIdentity(), 0xAA00AA);
        assertEquals(id.getSignalingPointCode(), 0x1555);
        
        PerformingRedirectIndicator[] pris = rfi.getPerformingRedirectIndicator();
        assertNotNull(pris);
        assertEquals(pris.length,1);
        PerformingRedirectIndicator ri = pris[0];
        assertNotNull(ri);
        RedirectReason[] rrs = ri.getReason();
        assertNotNull(rrs);
        assertEquals(rrs.length,4);
        assertNotNull(rrs[0]);
        assertNotNull(rrs[1]);
        assertNotNull(rrs[2]);
        assertNotNull(rrs[3]);

        assertEquals(rrs[0].getRedirectReason(), 18);
        assertEquals(rrs[0].getRedirectPossibleAtPerformingExchange(), 0);

        assertEquals(rrs[1].getRedirectReason(), 18);
        assertEquals(rrs[1].getRedirectPossibleAtPerformingExchange(), 5);

        assertEquals(rrs[2].getRedirectReason(), 17);
        assertEquals(rrs[2].getRedirectPossibleAtPerformingExchange(), 4);

        assertEquals(rrs[3].getRedirectReason(), 2);
        assertEquals(rrs[3].getRedirectPossibleAtPerformingExchange(), 0);
        
        InvokingRedirectReason[] inrs = rfi.getInvokingRedirectReason();
        assertNotNull(inrs);
        assertEquals(inrs.length,1);
        assertNotNull(inrs[0]);
        InvokingRedirectReason inr = inrs[0];
        RedirectReason[] rrs2 = inr.getReason();
        assertNotNull(rrs2);
        assertEquals(rrs2.length,3);
        assertNotNull(rrs2[0]);
        assertEquals(rrs2[0].getRedirectReason(), 1);
        assertNotNull(rrs2[1]);
        assertEquals(rrs2[1].getRedirectReason(), 2);
        assertNotNull(rrs2[2]);
        assertEquals(rrs2[2].getRedirectReason(), 3);
        
    }

    
    @Test
    public void testSetAndGet() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        RedirectForwardInformationImpl rfi = new RedirectForwardInformationImpl();

        rfi.setReturnToInvokingExchangePossible(new ReturnToInvokingExchangePossibleImpl());

        ReturnToInvokingExchangeCallIdentifierImpl callId = new ReturnToInvokingExchangeCallIdentifierImpl();
        callId.setCallIdentity(0XBB00BC);
        ReturnToInvokingExchangeCallIdentifierImpl callId2 = new ReturnToInvokingExchangeCallIdentifierImpl();
        callId2.setCallIdentity(0XCBF0BC);
        callId2.setSignalingPointCode(1);
        rfi.setReturnToInvokingExchangeCallIdentifier(callId,callId2);

        PerformingRedirectIndicatorImpl pri = new PerformingRedirectIndicatorImpl();
        RedirectReasonImpl rr1 = new RedirectReasonImpl();
        rr1.setRedirectReason((byte) 1);
        RedirectReasonImpl rr2 = new RedirectReasonImpl();
        rr2.setRedirectReason((byte) 1);
        rr2.setRedirectPossibleAtPerformingExchange((byte) 2);
        pri.setReason(rr1,rr2);
        rfi.setPerformingRedirectIndicator(pri);
        
        InvokingRedirectReasonImpl irr = new InvokingRedirectReasonImpl();
        //this differs across some params...
        irr.setTag(RedirectForwardInformation.INFORMATION_INVOKING_REDIRECT_REASON);
        irr.setReason(rr1,rr2);
        rfi.setInvokingRedirectReason(irr);

        ByteBuf data = Unpooled.buffer();
        rfi.encode(data);        
        rfi = new RedirectForwardInformationImpl();        
        rfi.decode(data);


        assertNotNull(rfi.getReturnToInvokingExchangePossible());
        assertEquals(rfi.getReturnToInvokingExchangePossible().length,1);

        assertNotNull(rfi.getReturnToInvokingExchangeCallIdentifier());
        assertEquals(rfi.getReturnToInvokingExchangeCallIdentifier().length,2);
        assertNotNull(rfi.getReturnToInvokingExchangeCallIdentifier()[0]);
        assertNotNull(rfi.getReturnToInvokingExchangeCallIdentifier()[1]);
        
        assertEquals(rfi.getReturnToInvokingExchangeCallIdentifier()[0].getCallIdentity(),0XBB00BC);
        assertEquals(rfi.getReturnToInvokingExchangeCallIdentifier()[1].getCallIdentity(),0XCBF0BC);
        assertEquals(rfi.getReturnToInvokingExchangeCallIdentifier()[1].getSignalingPointCode(),1);
     
        assertNotNull(rfi.getPerformingRedirectIndicator());
        assertEquals(rfi.getPerformingRedirectIndicator().length,1);

        assertNotNull(rfi.getPerformingRedirectIndicator()[0].getReason());
        assertEquals(rfi.getPerformingRedirectIndicator()[0].getReason().length,2);
        assertNotNull(rfi.getPerformingRedirectIndicator()[0].getReason()[0]);
        assertNotNull(rfi.getPerformingRedirectIndicator()[0].getReason()[1]);
        assertEquals(rfi.getPerformingRedirectIndicator()[0].getReason()[0].getRedirectReason(),1);
        assertEquals(rfi.getPerformingRedirectIndicator()[0].getReason()[1].getRedirectReason(),1);
        assertEquals(rfi.getPerformingRedirectIndicator()[0].getReason()[1].getRedirectPossibleAtPerformingExchange(),2);

        assertNotNull(rfi.getInvokingRedirectReason()[0].getReason());
        assertEquals(rfi.getInvokingRedirectReason()[0].getReason().length,2);
        assertNotNull(rfi.getInvokingRedirectReason()[0].getReason()[0]);
        assertNotNull(rfi.getInvokingRedirectReason()[0].getReason()[1]);
        assertEquals(rfi.getInvokingRedirectReason()[0].getReason()[0].getRedirectReason(),1);
        assertEquals(rfi.getInvokingRedirectReason()[0].getReason()[1].getRedirectReason(),1);
        //0 casuse this one does not have it.
        assertEquals(rfi.getInvokingRedirectReason()[0].getReason()[1].getRedirectPossibleAtPerformingExchange(),0);
    }

}
