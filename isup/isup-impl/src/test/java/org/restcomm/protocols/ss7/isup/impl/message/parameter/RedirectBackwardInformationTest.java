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
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectBackwardInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectReason;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeCallIdentifier;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeDuration;

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

public class RedirectBackwardInformationTest {
//This one does not use harness, since this param has multiple levels of nesting ....
    public RedirectBackwardInformationTest() {
        super();

    }

    private ByteBuf getBody1() {

        byte[] body = new byte[] {
          //3.100.1 duration
          0x01,
              //len
              0x02,
              (byte) 0xAA,
              (byte) 0xBB,
          //3.100.2
              0x02,
                  //len
                  0x05,
                  //body
                  (byte) 0xAA,
                  0,
                  (byte) 0xAA,
                  0x55,
                  0x15,
           //3.100.3
              0x03,
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
        RedirectBackwardInformationImpl parameter = new RedirectBackwardInformationImpl(getBody1());
        
        ReturnToInvokingExchangeDuration[] rtied = parameter.getReturnToInvokingExchangeDuration();
        assertNotNull(rtied);
        assertEquals(rtied.length,1);
        assertNotNull(rtied[0]);
        assertEquals(rtied[0].getDuration(),0xBBAA);

        ReturnToInvokingExchangeCallIdentifier[] callIds = parameter.getReturnToInvokingExchangeCallIdentifier();
        assertNotNull(callIds);
        assertEquals(callIds.length,1);
        ReturnToInvokingExchangeCallIdentifier id = callIds[0];
        assertNotNull(id);
        assertEquals(id.getCallIdentity(), 0xAA00AA);
        assertEquals(id.getSignalingPointCode(), 0x1555);

        InvokingRedirectReason[] inrs = parameter.getInvokingRedirectReason();
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
        RedirectBackwardInformationImpl parameter = new RedirectBackwardInformationImpl();

       ReturnToInvokingExchangeDurationImpl duration  = new ReturnToInvokingExchangeDurationImpl();
       duration.setDuration(0xAA0C);
       parameter.setReturnToInvokingExchangeDuration(duration);

        ReturnToInvokingExchangeCallIdentifierImpl callId = new ReturnToInvokingExchangeCallIdentifierImpl();
        callId.setCallIdentity(0XBB00BC);
        ReturnToInvokingExchangeCallIdentifierImpl callId2 = new ReturnToInvokingExchangeCallIdentifierImpl();
        callId2.setCallIdentity(0XCBF0BC);
        callId2.setSignalingPointCode(1);
        parameter.setReturnToInvokingExchangeCallIdentifier(callId,callId2);

        InvokingRedirectReasonImpl irr = new InvokingRedirectReasonImpl();
        //this differs across some params...
        irr.setTag(RedirectBackwardInformation.INFORMATION_INVOKING_REDIRECT_REASON);
        RedirectReasonImpl rr1 = new RedirectReasonImpl();
        rr1.setRedirectReason((byte) 1);
        RedirectReasonImpl rr2 = new RedirectReasonImpl();
        rr2.setRedirectReason((byte) 1);
        rr2.setRedirectPossibleAtPerformingExchange((byte) 2);
        irr.setReason(rr1,rr2);
        parameter.setInvokingRedirectReason(irr);

        ByteBuf data = Unpooled.buffer();
        parameter.encode(data);
        parameter = new RedirectBackwardInformationImpl();
        parameter.decode(data);

        assertNotNull(parameter.getReturnToInvokingExchangeDuration());
        assertEquals(parameter.getReturnToInvokingExchangeDuration().length,1);
        assertNotNull(parameter.getReturnToInvokingExchangeDuration()[0]);
        
        assertEquals(parameter.getReturnToInvokingExchangeDuration()[0].getDuration(),0xAA0C);

        assertNotNull(parameter.getReturnToInvokingExchangeCallIdentifier());
        assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier().length,2);
        assertNotNull(parameter.getReturnToInvokingExchangeCallIdentifier()[0]);
        assertNotNull(parameter.getReturnToInvokingExchangeCallIdentifier()[1]);
        
        assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier()[0].getCallIdentity(),0XBB00BC);
        assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier()[1].getCallIdentity(),0XCBF0BC);
        assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier()[1].getSignalingPointCode(),1);


        assertNotNull(parameter.getInvokingRedirectReason()[0].getReason());
        assertEquals(parameter.getInvokingRedirectReason()[0].getReason().length,2);
        assertNotNull(parameter.getInvokingRedirectReason()[0].getReason()[0]);
        assertNotNull(parameter.getInvokingRedirectReason()[0].getReason()[1]);
        assertEquals(parameter.getInvokingRedirectReason()[0].getReason()[0].getRedirectReason(),1);
        assertEquals(parameter.getInvokingRedirectReason()[0].getReason()[1].getRedirectReason(),1);
        //0 casuse this one does not have it.
        assertEquals(parameter.getInvokingRedirectReason()[0].getReason()[1].getRedirectPossibleAtPerformingExchange(),0);
    }

}
