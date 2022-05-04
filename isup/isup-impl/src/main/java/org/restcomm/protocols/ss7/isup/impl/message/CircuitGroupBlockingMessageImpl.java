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

import java.util.Map;
import java.util.Set;

import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.restcomm.protocols.ss7.isup.message.CircuitGroupBlockingMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitGroupSuperVisionMessageType;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageName;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageType;
import org.restcomm.protocols.ss7.isup.message.parameter.RangeAndStatus;

import io.netty.buffer.ByteBuf;

/**
 * Start time:00:07:25 2009-09-07<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CircuitGroupBlockingMessageImpl extends ISUPMessageImpl implements CircuitGroupBlockingMessage {
	public static final MessageType _MESSAGE_TYPE = new MessageTypeImpl(MessageName.CircuitGroupBlocking);
    private static final int _MANDATORY_VAR_COUNT = 1;

    static final int _INDEX_F_MessageType = 0;
    static final int _INDEX_F_CircuitGroupSuperVisionMessageType = 1;

    static final int _INDEX_V_RangeAndStatus = 0;

    CircuitGroupBlockingMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes,
            Set<Integer> optionalCodes, Map<Integer, Integer> mandatoryCode2Index,
            Map<Integer, Integer> mandatoryVariableCode2Index, Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index,
                optionalCode2Index);

        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());

    }

    public void setSupervisionType(CircuitGroupSuperVisionMessageType ras) {
        super.f_Parameters.put(_INDEX_F_CircuitGroupSuperVisionMessageType, ras);
    }

    public CircuitGroupSuperVisionMessageType getSupervisionType() {
        return (CircuitGroupSuperVisionMessageType) super.f_Parameters.get(_INDEX_F_CircuitGroupSuperVisionMessageType);
    }

    public void setRangeAndStatus(RangeAndStatus ras) {
        super.v_Parameters.put(_INDEX_V_RangeAndStatus, ras);
    }

    public RangeAndStatus getRangeAndStatus() {
        return (RangeAndStatus) super.v_Parameters.get(_INDEX_V_RangeAndStatus);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryParameters(ByteBuf, int)
     */

    protected void decodeMandatoryParameters(ISUPParameterFactory parameterFactory, ByteBuf b)
            throws ParameterException {
        super.decodeMandatoryParameters(parameterFactory, b);
        if (b.readableBytes()>0) {
            CircuitGroupSuperVisionMessageType cgsvmt = parameterFactory.createCircuitGroupSuperVisionMessageType();
            ((AbstractISUPParameter) cgsvmt).decode(b.slice(b.readerIndex(), 1));
            this.setSupervisionType(cgsvmt);
            b.skipBytes(1);
        } else {
            throw new IllegalArgumentException("buffer must have atleast four readable octets");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryVariableBody(byte [], int)
     */

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, ByteBuf parameterBody, int parameterIndex)
            throws ParameterException {
        switch (parameterIndex) {
            case _INDEX_V_RangeAndStatus:
                RangeAndStatus ras = parameterFactory.createRangeAndStatus();
                ((AbstractISUPParameter) ras).decode(parameterBody);
                this.setRangeAndStatus(ras);
                break;
            default:
                throw new ParameterException("Unrecognized parameter index for mandatory variable part, index: "
                        + parameterIndex);

        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#decodeOptionalBody(ByteBuf, byte)
     */

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, ByteBuf parameterBody, byte parameterCode)
            throws ParameterException {
        throw new ParameterException("This message does not support optional parameters");

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#getMessageType()
     */

    public MessageType getMessageType() {
        return _MESSAGE_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.isup.ISUPMessageImpl# getNumberOfMandatoryVariableLengthParameters()
     */

    protected int getNumberOfMandatoryVariableLengthParameters() {

        return _MANDATORY_VAR_COUNT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#hasAllMandatoryParameters()
     */

    public boolean hasAllMandatoryParameters() {
        return super.f_Parameters.get(_INDEX_F_CircuitGroupSuperVisionMessageType) != null
                && super.v_Parameters.get(_INDEX_V_RangeAndStatus) != null;
    }

    protected boolean optionalPartIsPossible() {

        return false;
    }

}
