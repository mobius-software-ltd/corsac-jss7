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
import org.restcomm.protocols.ss7.isup.message.CallProgressMessage;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.ConnectedNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.EventInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.TransmissionMediumUsed;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Start time:15:07:07 2009-07-17<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CPGTest extends MessageHarness {

    @Test
    public void testTwo_Parameters() throws Exception {
        ByteBuf message = getDefaultBody();

        // CallProgressMessage cpg=new CallProgressMessageImpl(this,message);
        CallProgressMessage cpg = super.messageFactory.createCPG(0);
        ((AbstractISUPMessage) cpg).decode(message, messageFactory,parameterFactory);
        assertNotNull(cpg.getParameter(EventInformation._PARAMETER_CODE));
        assertNotNull(cpg.getParameter(BackwardCallIndicators._PARAMETER_CODE));
        assertNotNull(cpg.getParameter(TransmissionMediumUsed._PARAMETER_CODE));
        assertNotNull(cpg.getParameter(ConnectedNumber._PARAMETER_CODE));

        EventInformation ei = (EventInformation) cpg.getParameter(EventInformation._PARAMETER_CODE);
        assertEquals(ei.getEventIndicator(), 0x02);

        BackwardCallIndicators bci = (BackwardCallIndicators) cpg.getParameter(BackwardCallIndicators._PARAMETER_CODE);

        assertEquals(bci.getChargeIndicator(), BackwardCallIndicators._CHARGE_INDICATOR_CHARGE);
        assertEquals(bci.getCalledPartysStatusIndicator(), BackwardCallIndicators._CPSI_SUBSCRIBER_FREE);
        assertEquals(bci.getCalledPartysCategoryIndicator(), BackwardCallIndicators._CPCI_PAYPHONE);
        assertEquals(bci.getEndToEndMethodIndicator(), BackwardCallIndicators._ETEMI_SCCP);
        assertEquals(bci.isInterworkingIndicator(), BackwardCallIndicators._II_IE);
        assertEquals(bci.isEndToEndInformationIndicator(), BackwardCallIndicators._ETEII_NO_IA);
        assertEquals(bci.isIsdnUserPartIndicator(), BackwardCallIndicators._ISDN_UPI_UATW);
        assertEquals(bci.isHoldingIndicator(), BackwardCallIndicators._HI_REQUESTED);
        assertEquals(bci.isIsdnAccessIndicator(), BackwardCallIndicators._ISDN_AI_TA_ISDN);
        assertEquals(bci.isEchoControlDeviceIndicator(), BackwardCallIndicators._ECDI_IECD_NOT_INCLUDED);
        assertEquals(bci.getSccpMethodIndicator(), BackwardCallIndicators._SCCP_MI_CONNECTION_ORIENTED);

        TransmissionMediumUsed tmu = (TransmissionMediumUsed) cpg.getParameter(TransmissionMediumUsed._PARAMETER_CODE);
        assertEquals(tmu.getTransimissionMediumUsed(), 0x03);

        ConnectedNumber cn = (ConnectedNumber) cpg.getParameter(ConnectedNumber._PARAMETER_CODE);

        // XXX: note this can fail, once we decide when APRI is done

        assertEquals(cn.getNatureOfAddressIndicator(), ConnectedNumber._NAI_SUBSCRIBER_NUMBER);
        assertEquals(cn.getNumberingPlanIndicator(), ConnectedNumber._NPI_TELEX);
        assertEquals(cn.getAddressRepresentationRestrictedIndicator(), ConnectedNumber._APRI_ALLOWED);
        assertEquals(ConnectedNumber._SI_NETWORK_PROVIDED, cn.getScreeningIndicator(), ConnectedNumber._SI_NETWORK_PROVIDED);
        assertEquals(cn.getAddress(), "380683");
    }

    protected ByteBuf getDefaultBody() {
        // FIXME: for now we strip MTP part
        byte[] message = { 0x0C, (byte) 0x0B, CallProgressMessage.MESSAGE_CODE
                // EventInformation
                , 0x02
                // no mandatory varialbe part, no pointer
                // pointer to optioanl part
                , 0x01

                // backward call idnicators
                , BackwardCallIndicators._PARAMETER_CODE, 0x02, (byte) 0xA6, (byte) 0x9D

                // TransmissionMedium Used
                , TransmissionMediumUsed._PARAMETER_CODE, 0x01, 0x03

                // Connected Number
                , ConnectedNumber._PARAMETER_CODE, 0x05, 0x01, 0x43, (byte) (0x83 & 0xFF), 0x60, 0x38

                // End of Opt Part
                , 0x00

        };
        return Unpooled.wrappedBuffer(message);
    }

    protected ISUPMessage getDefaultMessage() {
        return super.messageFactory.createCPG(0);
    }
}
