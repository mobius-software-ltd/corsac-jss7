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
import org.restcomm.protocols.ss7.isup.message.InformationMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.CallReference;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.ConnectionRequest;
import org.restcomm.protocols.ss7.isup.message.parameter.InformationIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageName;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageType;
import org.restcomm.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.restcomm.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;

import io.netty.buffer.ByteBuf;

/**
 * Start time:23:59:59 2009-09-06<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class InformationMessageImpl extends ISUPMessageImpl implements InformationMessage {
	public static final MessageType _MESSAGE_TYPE = new MessageTypeImpl(MessageName.Information);
    static final int _INDEX_F_MessageType = 0;
    static final int _INDEX_F_InformationIndicators = 1;
    static final int _INDEX_O_CallingPartyCategory = 0;
    static final int _INDEX_O_CallingPartyNumber = 1;
    static final int _INDEX_O_CallReference = 2;
    static final int _INDEX_O_ConnectionRequest = 3;
    static final int _INDEX_O_ParameterCompatibilityInformation = 4;
    static final int _INDEX_O_NetworkSpecificFacility = 5;
    static final int _INDEX_O_EndOfOptionalParameters = 6;

    /**
     *
     * @param source
     * @throws ParameterException
     */
    public InformationMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
            Map<Integer, Integer> mandatoryCode2Index, Map<Integer, Integer> mandatoryVariableCode2Index,
            Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index,
                optionalCode2Index);

        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
    }

    public InformationIndicators getInformationIndicators() {
        return (InformationIndicators) super.f_Parameters.get(_INDEX_F_InformationIndicators);
    }

    public void setInformationIndicators(InformationIndicators v) {
        super.f_Parameters.put(_INDEX_F_InformationIndicators, v);
    }

    public CallingPartyCategory getCallingPartyCategory() {
        return (CallingPartyCategory) super.o_Parameters.get(_INDEX_O_CallingPartyCategory);
    }

    public void setCallingPartyCategory(CallingPartyCategory v) {
        super.o_Parameters.put(_INDEX_O_CallingPartyCategory, v);
    }

    public CallingPartyNumber getCallingPartyNumber() {
        return (CallingPartyNumber) super.o_Parameters.get(_INDEX_O_CallingPartyNumber);
    }

    public void setCallingPartyNumber(CallingPartyNumber v) {
        super.o_Parameters.put(_INDEX_O_CallingPartyNumber, v);
    }

    public CallReference getCallReference() {
        return (CallReference) super.o_Parameters.get(_INDEX_O_CallReference);
    }

    public void setCallReference(CallReference v) {
        super.o_Parameters.put(_INDEX_O_CallReference, v);
    }

    public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
        return (ParameterCompatibilityInformation) super.o_Parameters.get(_INDEX_O_ParameterCompatibilityInformation);
    }

    public void setParameterCompatibilityInformation(ParameterCompatibilityInformation v) {
        super.o_Parameters.put(_INDEX_O_ParameterCompatibilityInformation, v);
    }

    public ConnectionRequest getConnectionRequest() {
        return (ConnectionRequest) super.o_Parameters.get(_INDEX_O_ConnectionRequest);
    }

    public void setConnectionRequest(ConnectionRequest v) {
        super.o_Parameters.put(_INDEX_O_ConnectionRequest, v);
    }

    public NetworkSpecificFacility getNetworkSpecificFacility() {
        return (NetworkSpecificFacility) super.o_Parameters.get(_INDEX_O_NetworkSpecificFacility);
    }

    public void setNetworkSpecificFacility(NetworkSpecificFacility v) {
        super.o_Parameters.put(_INDEX_O_NetworkSpecificFacility, v);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryParameters (ByteBuf, int)
     */
    protected void decodeMandatoryParameters(ISUPParameterFactory parameterFactory, ByteBuf b)
            throws ParameterException {
        super.decodeMandatoryParameters(parameterFactory, b);
        if (b.readableBytes() > 1) {

            try {
                ByteBuf informationInd = b.slice(b.readerIndex(), 2);
                InformationIndicators bci = parameterFactory.createInformationIndicators();
                ((AbstractISUPParameter) bci).decode(informationInd);
                this.setInformationIndicators(bci);
                b.skipBytes(2);
            } catch (Exception e) {
                // AIOOBE or IllegalArg
                throw new ParameterException("Failed to parse BackwardCallIndicators due to: ", e);
            }
        } else {
            throw new IllegalArgumentException("buffer must have atleast 2 readable octets");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryVariableBody (ByteBuf, int)
     */

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, ByteBuf parameterBody, int parameterIndex)
            throws ParameterException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#decodeOptionalBody(byte [], byte)
     */

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, ByteBuf parameterBody, byte parameterCode)
            throws ParameterException {
        switch (parameterCode & 0xFF) {

            case CallingPartyCategory._PARAMETER_CODE:
                CallingPartyCategory RS = parameterFactory.createCallingPartyCategory();
                ((AbstractISUPParameter) RS).decode(parameterBody);
                this.setCallingPartyCategory(RS);
                break;
            case CallingPartyNumber._PARAMETER_CODE:
                CallingPartyNumber x = parameterFactory.createCallingPartyNumber();
                ((AbstractISUPParameter) x).decode(parameterBody);
                this.setCallingPartyNumber(x);
                break;
            case CallReference._PARAMETER_CODE:
                CallReference cf = parameterFactory.createCallReference();
                ((AbstractISUPParameter) cf).decode(parameterBody);
                this.setCallReference(cf);
                break;
            case ConnectionRequest._PARAMETER_CODE:
                ConnectionRequest z = parameterFactory.createConnectionRequest();
                ((AbstractISUPParameter) z).decode(parameterBody);
                this.setConnectionRequest(z);
                break;
            case ParameterCompatibilityInformation._PARAMETER_CODE:
                ParameterCompatibilityInformation cc = parameterFactory.createParameterCompatibilityInformation();
                ((AbstractISUPParameter) cc).decode(parameterBody);
                this.setParameterCompatibilityInformation(cc);
                break;
            case NetworkSpecificFacility._PARAMETER_CODE:
                NetworkSpecificFacility v = parameterFactory.createNetworkSpecificFacility();
                ((AbstractISUPParameter) v).decode(parameterBody);
                this.setNetworkSpecificFacility(v);
                break;

            default:
                throw new ParameterException("Unrecognized parameter code for optional part: " + parameterCode);
        }
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
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl# getNumberOfMandatoryVariableLengthParameters()
     */

    protected int getNumberOfMandatoryVariableLengthParameters() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.ISUPMessageImpl#hasAllMandatoryParameters ()
     */

    public boolean hasAllMandatoryParameters() {
        return getInformationIndicators() != null;
    }

    protected boolean optionalPartIsPossible() {

        return true;
    }

}
