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

import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.restcomm.protocols.ss7.isup.message.ChargeInformationMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageName;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageType;

import io.netty.buffer.ByteBuf;

/**
 * Start time:00:12:12 2009-09-07<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class ChargeInformationMessageImpl extends ISUPMessageImpl implements ChargeInformationMessage {
	public static final MessageType _MESSAGE_TYPE = new MessageTypeImpl(MessageName.ChargeInformation);

    /**
     *
     * @param source
     * @throws ParameterException
     */
    public ChargeInformationMessageImpl() {

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryVariableBody(ByteBuf, int)
     */

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, ByteBuf parameterBody, int parameterIndex)
            throws ParameterException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#decodeOptionalBody(ByteBuf, byte)
     */

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, ByteBuf parameterBody, byte parameterCode)
            throws ParameterException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#getMessageType()
     */

    public MessageType getMessageType() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#getNumberOfMandatoryVariableLengthParameters()
     */

    protected int getNumberOfMandatoryVariableLengthParameters() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#hasAllMandatoryParameters()
     */

    public boolean hasAllMandatoryParameters() {
        throw new UnsupportedOperationException();
    }

    protected boolean optionalPartIsPossible() {

        throw new UnsupportedOperationException();
    }

}
