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

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class EventInformationTest extends ParameterHarness {

    public EventInformationTest() {
        super();
        super.goodBodies.add(Unpooled.wrappedBuffer(new byte[] { 67 }));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[2]));
    }

    private ByteBuf getBody(int _EI, boolean _RI) {
        byte[] b = new byte[1];
        int v = _EI;
        if (_RI)
            v |= 0x01 << 7;

        b[0] = (byte) v;

        return Unpooled.wrappedBuffer(b);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws ParameterException {
        EventInformationImpl eci = new EventInformationImpl(getBody(EventInformationImpl._EVENT_INDICATOR_CFONNR,
                EventInformationImpl._EVENT_PRESENTATION_IPR));

        String[] methodNames = { "getEventIndicator", "isEventPresentationRestrictedIndicator" };
        Object[] expectedValues = { EventInformationImpl._EVENT_INDICATOR_CFONNR, EventInformationImpl._EVENT_PRESENTATION_IPR };
        super.testValues(eci, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new EventInformationImpl(Unpooled.wrappedBuffer(new byte[1]));
    }

}
