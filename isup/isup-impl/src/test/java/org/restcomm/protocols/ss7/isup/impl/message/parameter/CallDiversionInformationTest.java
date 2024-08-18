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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.junit.Test;

/**
 * Start time:13:37:14 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>ca </a>
 * @author yulianoifa
 */
public class CallDiversionInformationTest extends ParameterHarness {

    public CallDiversionInformationTest() {
        super();

        super.badBodies.add(Unpooled.wrappedBuffer(new byte[0]));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[2]));

        super.goodBodies.add(Unpooled.wrappedBuffer(getBody1()));

    }

    private ByteBuf getBody1() {
        // Notif sub options : 010 - presentation allowed
        // redirect reason : 0100 - deflection during alerting
        // whole : 00100010
        return Unpooled.wrappedBuffer(new byte[] { 0x22 });
    }

    @Test
    public void testBody1EncodedValues() throws ParameterException {
        CallDiversionInformationImpl cdi = new CallDiversionInformationImpl(getBody1());
        String[] methodNames = { "getNotificationSubscriptionOptions", "getRedirectingReason" };
        Object[] expectedValues = { CallDiversionInformationImpl._NSO_P_A_WITH_RN, CallDiversionInformationImpl._REDIRECTING_REASON_DDA };
        super.testValues(cdi, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new CallDiversionInformationImpl(Unpooled.wrappedBuffer(new byte[1]));
    }

}
