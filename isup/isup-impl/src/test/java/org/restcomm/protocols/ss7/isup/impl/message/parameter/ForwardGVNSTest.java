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

import java.io.IOException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.GVNSUserGroup;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginatingParticipatingServiceProvider;
import org.restcomm.protocols.ss7.isup.message.parameter.TerminatingNetworkRoutingNumber;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class ForwardGVNSTest extends ParameterHarness {

    /**
     * @throws IOException
     * @throws ParameterException
     */
    public ForwardGVNSTest() throws IOException, ParameterException {

        super.goodBodies.add(getBody(getSixDigitsString(), getSixDigitsString(), getFiveDigitsString()));

    }

    private ByteBuf getBody(String bs, String bs2, String bs3) throws ParameterException, IOException {
        // those are tested in other junits.
        OriginatingParticipatingServiceProviderImpl ops = new OriginatingParticipatingServiceProviderImpl();
        ops.setAddress(bs);
        GVNSUserGroupImpl gvns = new GVNSUserGroupImpl();
        gvns.setAddress(bs2);
        TerminatingNetworkRoutingNumberImpl tnrn = new TerminatingNetworkRoutingNumberImpl();
        tnrn.setAddress(bs3);
        tnrn.setNumberingPlanIndicator(TerminatingNetworkRoutingNumberImpl._NPI_TELEX);
        tnrn.setNatureOfAddressIndicator(TerminatingNetworkRoutingNumberImpl._NAI_NATIONAL_SN);
        return getBody(ops, gvns, tnrn);
    }

    private ByteBuf getBody(OriginatingParticipatingServiceProvider ops, GVNSUserGroup gnvs, TerminatingNetworkRoutingNumber tnrn)
            throws ParameterException, IOException {
    	ByteBuf bos = Unpooled.buffer();
        // bit weird tests...
    	((AbstractISUPParameter) ops).encode(bos);        
        ((AbstractISUPParameter) gnvs).encode(bos);
        ((AbstractISUPParameter) tnrn).encode(bos);
        return bos;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() {
        return new ForwardGVNSImpl();
    }

}
