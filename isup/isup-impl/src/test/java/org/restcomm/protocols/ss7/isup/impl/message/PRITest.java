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

package org.restcomm.protocols.ss7.isup.impl.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.isup.message.PreReleaseInformationMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.ApplicationTransport;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageCompatibilityInformation;
import org.testng.annotations.Test;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: restcomm-isup-stack<br>
 * Test for ACM
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class PRITest extends MessageHarness {

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testTwo_Params() throws Exception {

        ByteBuf message = getDefaultBody();


        PreReleaseInformationMessage msg =  super.messageFactory.createPRI();
        ((AbstractISUPMessage) msg).decode(message, messageFactory,parameterFactory);

       

        assertNotNull(msg.getMessageCompatibilityInformation());
        MessageCompatibilityInformation mcis = msg.getMessageCompatibilityInformation();
        assertNotNull(mcis.getMessageCompatibilityInstructionIndicators());
        assertEquals(mcis.getMessageCompatibilityInstructionIndicators().size(),2);
        assertNotNull(mcis.getMessageCompatibilityInstructionIndicators().get(0));
        assertNotNull(mcis.getMessageCompatibilityInstructionIndicators().get(1));
        assertEquals(mcis.getMessageCompatibilityInstructionIndicators().get(0).getBandInterworkingIndicator(),2);
        assertEquals(mcis.getMessageCompatibilityInstructionIndicators().get(1).getBandInterworkingIndicator(),0);
        
        assertNotNull(msg.getApplicationTransport());
        ApplicationTransport at = msg.getApplicationTransport();
        assertEquals(at.getApplicationContextIdentifier(),new Byte((byte)5));
        assertEquals(at.isSendNotificationIndicator(),Boolean.TRUE);
        assertEquals(at.isReleaseCallIndicator(),Boolean.FALSE);
        assertEquals(at.getAPMSegmentationIndicator(),new Byte((byte)5));
    }

    protected ByteBuf getDefaultBody() {
        byte[] message = {
                // CIC
                0x0C, (byte) 0x0B,
                PreReleaseInformationMessage.MESSAGE_CODE, 
                    //pointer
                    0x01,
                    //MCI
                    MessageCompatibilityInformation._PARAMETER_CODE,
                        //len
                         0x02,
                            0x42, 
                            (byte) 0x81,
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
        return super.messageFactory.createPRI();
    }
}
