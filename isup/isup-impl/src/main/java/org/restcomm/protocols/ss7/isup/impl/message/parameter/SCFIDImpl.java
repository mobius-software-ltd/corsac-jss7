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

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.SCFID;

/**
 * Start time:12:48:19 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 *
 */
public class SCFIDImpl extends NetworkRoutingNumberImpl implements SCFID {
	// FIXME: Q.1218 - oleg is this correct? :
    // http://www.itu.int/ITU-T/asn1/database/itu-t/q/q1238.2/2000/IN-CS3-SSF-SCF-datatypes.html
    public SCFIDImpl() {
        super();

    }

    public SCFIDImpl(ByteBuf representation) throws ParameterException {
        super(representation);
    }

    public SCFIDImpl(String address, int numberingPlanIndicator, int natureOfAddressIndicator) {
        super(address, numberingPlanIndicator, natureOfAddressIndicator);

    }

    public int getCode() {
        return SCFID._PARAMETER_CODE;
    }
}