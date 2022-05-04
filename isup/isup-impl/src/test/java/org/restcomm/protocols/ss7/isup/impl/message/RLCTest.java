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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Start time:15:07:07 2009-07-17<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class RLCTest extends MessageHarness {

    protected ByteBuf getDefaultBody() {
        // FIXME: for now we strip MTP part
        byte[] message = {

        0x0C, (byte) 0x0B, 0x10, 0x00

        };

        return Unpooled.wrappedBuffer(message);
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createRLC();
    }

    @Test(groups = { "functional.encode", "functional.decode", "message" })
    public void testCauseIndicators() throws Exception {
        final CauseIndicators causeIndicators = super.parameterFactory.createCauseIndicators();
        causeIndicators.setCauseValue(CauseIndicators._CV_ALL_CLEAR);
        final ReleaseCompleteMessage releaseCompleteMessage = super.messageFactory.createRLC(1);
        releaseCompleteMessage.setCauseIndicators(causeIndicators);
        releaseCompleteMessage.setSls(2);
        final ByteBuf encoded = Unpooled.buffer();
        ((AbstractISUPMessage) releaseCompleteMessage).encode(encoded);
        final AbstractISUPMessage msg = (AbstractISUPMessage) getDefaultMessage();
        msg.decode(encoded, messageFactory,parameterFactory);
        final ReleaseCompleteMessage decodedRLC = (ReleaseCompleteMessage) msg;
        Assert.assertNotNull(decodedRLC.getCauseIndicators(), "No Cause Indicators present!");
        Assert.assertEquals(decodedRLC.getCauseIndicators().getCauseValue(), CauseIndicators._CV_ALL_CLEAR, "Wrong CauseValue");
    }
}
