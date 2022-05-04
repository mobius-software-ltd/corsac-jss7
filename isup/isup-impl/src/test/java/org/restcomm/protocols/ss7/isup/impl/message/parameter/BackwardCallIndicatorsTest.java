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

import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 * Class to test BCI
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class BackwardCallIndicatorsTest extends ParameterHarness {

    public BackwardCallIndicatorsTest() {
        super();

        super.badBodies.add(Unpooled.wrappedBuffer(new byte[1]));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[3]));

        super.goodBodies.add(Unpooled.wrappedBuffer(getBody1()));
        super.goodBodies.add(Unpooled.wrappedBuffer(getBody2()));
    }

    private ByteBuf getBody1() {

        byte[] body = new byte[2];
        // Chardi IND : 10 - charge
        // Called part status ind: 01 - sub free
        // Called part category ind: 10 - pay phone
        // e2emethod ind: 10 - sccp
        // whole: 10100110
        body[0] = (byte) 0xA6;

        // Interworking : 1 - encoutnered
        // e2einfo ind : 0 - no info
        // ISUP ind : 1 - all the way
        // hold ind : 1 - requested
        // ISDN acc ind : 1 - terminating acc isdn
        // echo ctrl dev: 0 - not included
        // SCCP m ind : 10 - connection oriented only
        // whole : 10011101
        body[1] = (byte) 0x9D;
        return Unpooled.wrappedBuffer(body);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        BackwardCallIndicatorsImpl bci = new BackwardCallIndicatorsImpl(getBody1());

        String[] methodNames = { "getChargeIndicator", "getCalledPartysStatusIndicator", "getCalledPartysCategoryIndicator",
                "getEndToEndMethodIndicator", "isInterworkingIndicator", "isEndToEndInformationIndicator",
                "isIsdnUserPartIndicator", "isHoldingIndicator", "isIsdnAccessIndicator", "isEchoControlDeviceIndicator",
                "getSccpMethodIndicator" };
        Object[] expectedValues = { BackwardCallIndicatorsImpl._CHARGE_INDICATOR_CHARGE, BackwardCallIndicatorsImpl._CPSI_SUBSCRIBER_FREE, BackwardCallIndicatorsImpl._CPCI_PAYPHONE,
        		BackwardCallIndicatorsImpl._ETEMI_SCCP, BackwardCallIndicatorsImpl._II_IE, BackwardCallIndicatorsImpl._ETEII_NO_IA, BackwardCallIndicatorsImpl._ISDN_UPI_UATW, BackwardCallIndicatorsImpl._HI_REQUESTED, BackwardCallIndicatorsImpl._ISDN_AI_TA_ISDN,
        		BackwardCallIndicatorsImpl._ECDI_IECD_NOT_INCLUDED, BackwardCallIndicatorsImpl._SCCP_MI_CONNECTION_ORIENTED };
        super.testValues(bci, methodNames, expectedValues);
    }

    private ByteBuf getBody2() {
        byte[] body = new byte[2];
        // Chardi IND : 01 - no charge
        // Called part status ind: 10 - conn when free
        // Called part category ind: 10 - pay phone
        // e2emethod ind: 11 - pass alond and sccp
        // whole: 11101001
        body[0] = (byte) 0xE9;

        // Interworking : 1 - encoutnered
        // e2einfo ind : 0 - no info
        // ISUP ind : 1 - all the way
        // hold ind : 1 - requested
        // ISDN acc ind : 1 - terminating acc isdn
        // echo ctrl dev: 1 - not included
        // SCCP m ind : 10 - connection oriented only
        // whole : 10111101
        body[1] = (byte) 0xBD;
        return Unpooled.wrappedBuffer(body);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        BackwardCallIndicatorsImpl bci = new BackwardCallIndicatorsImpl(getBody2());

        String[] methodNames = { "getChargeIndicator", "getCalledPartysStatusIndicator", "getCalledPartysCategoryIndicator",
                "getEndToEndMethodIndicator", "isInterworkingIndicator", "isEndToEndInformationIndicator",
                "isIsdnUserPartIndicator", "isHoldingIndicator", "isIsdnAccessIndicator", "isEchoControlDeviceIndicator",
                "getSccpMethodIndicator" };
        Object[] expectedValues = { BackwardCallIndicatorsImpl._CHARGE_INDICATOR_NOCHARGE, BackwardCallIndicatorsImpl._CPSI_CONNECT_WHEN_FREE, BackwardCallIndicatorsImpl._CPCI_PAYPHONE,
        		BackwardCallIndicatorsImpl._ETEMI_SCCP_AND_PASSALONG, BackwardCallIndicatorsImpl._II_IE, BackwardCallIndicatorsImpl._ETEII_NO_IA, BackwardCallIndicatorsImpl._ISDN_UPI_UATW, BackwardCallIndicatorsImpl._HI_REQUESTED,
        		BackwardCallIndicatorsImpl._ISDN_AI_TA_ISDN, BackwardCallIndicatorsImpl._ECDI_IECD_INCLUDED, BackwardCallIndicatorsImpl._SCCP_MI_CONNECTION_ORIENTED };
        super.testValues(bci, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new org.restcomm.protocols.ss7.isup.impl.message.parameter.BackwardCallIndicatorsImpl(Unpooled.wrappedBuffer(new byte[2]));
    }

}
