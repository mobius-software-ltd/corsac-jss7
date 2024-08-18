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

package org.restcomm.protocols.ss7.isup.impl.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.restcomm.protocols.ss7.isup.message.ApplicationTransportMessage;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.ApplicationTransport;
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageCompatibilityInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageCompatibilityInstructionIndicator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: restcomm-isup-stack<br>
 * Test for ACM
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class APTTest extends MessageHarness {

    @Test
    public void testTwo_Params() throws Exception {

        ByteBuf message = getDefaultBody();


        ApplicationTransportMessage msg = super.messageFactory.createAPT();
        ((AbstractISUPMessage) msg).decode(message, messageFactory,parameterFactory);

        assertNotNull(msg.getApplicationTransport());
        ApplicationTransport at = msg.getApplicationTransport();
        assertEquals(at.getApplicationContextIdentifier(),new Byte((byte)5));
        assertEquals(at.isSendNotificationIndicator(),Boolean.TRUE);
        assertEquals(at.isReleaseCallIndicator(),Boolean.FALSE);
        assertEquals(at.getAPMSegmentationIndicator(),new Byte((byte)5));

        assertNotNull(msg.getMessageCompatibilityInformation());
        MessageCompatibilityInformation mcis = msg.getMessageCompatibilityInformation();
        assertNotNull(mcis.getMessageCompatibilityInstructionIndicators());
        assertEquals(mcis.getMessageCompatibilityInstructionIndicators().size(),2);
        assertNotNull(mcis.getMessageCompatibilityInstructionIndicators().get(0));
        assertNotNull(mcis.getMessageCompatibilityInstructionIndicators().get(1));
        assertEquals(mcis.getMessageCompatibilityInstructionIndicators().get(0).getBandInterworkingIndicator(),2);
        assertEquals(mcis.getMessageCompatibilityInstructionIndicators().get(1).getBandInterworkingIndicator(),0);
        
        MessageCompatibilityInstructionIndicator mic = mcis.getMessageCompatibilityInstructionIndicators().get(0);
        assertEquals(mic.getBandInterworkingIndicator(), 2);
        mic = mcis.getMessageCompatibilityInstructionIndicators().get(1);
        assertEquals(mic.getBandInterworkingIndicator(), 0);

        CircuitIdentificationCode cic = msg.getCircuitIdentificationCode();
        assertNotNull(cic);
        assertEquals(getDefaultCIC(), cic.getCIC());

    }

    protected ByteBuf getDefaultBody() {
        byte[] message = {
                // CIC
                0x0C, (byte) 0x0B,
                ApplicationTransportMessage.MESSAGE_CODE, 
                //pointer
                0x01,
                //MCI
                MessageCompatibilityInformation._PARAMETER_CODE,
                    //len
                    0x02,
                    0x42, 
                    (byte) 0x81,
                //AT
                ApplicationTransport._PARAMETER_CODE,
                    //len
                    0x04,
                    //ACI
                    5,
                    //SNI,RCI (1 0)
                    0x02,
                    //SI,APM indicator (1 00 01 01)
                    0x45,
                    //Segmentation reference
                    (byte)(0x80 | 6),
               0x00
                
        };
        return Unpooled.wrappedBuffer(message);
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createAPT();
    }
}
