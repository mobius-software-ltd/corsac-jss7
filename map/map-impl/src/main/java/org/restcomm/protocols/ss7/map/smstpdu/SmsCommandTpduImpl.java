/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.smstpdu;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.CommandData;
import org.restcomm.protocols.ss7.map.api.smstpdu.CommandType;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsCommandTpdu;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpduType;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SmsCommandTpduImpl extends SmsTpduImpl implements SmsCommandTpdu {
	private boolean userDataHeaderIndicator;
    private boolean statusReportRequest;
    private int messageReference;
    private ProtocolIdentifier protocolIdentifier;
    private CommandType commandType;
    private int messageNumber;
    private AddressField destinationAddress;
    private int commandDataLength;
    private CommandData commandData;

    private SmsCommandTpduImpl() {
        this.tpduType = SmsTpduType.SMS_COMMAND;
        this.mobileOriginatedMessage = true;
    }

    public SmsCommandTpduImpl(boolean statusReportRequest, int messageReference, ProtocolIdentifier protocolIdentifier,
            CommandType commandType, int messageNumber, AddressField destinationAddress, CommandData commandData) {
        this();

        this.statusReportRequest = statusReportRequest;
        this.messageReference = messageReference;
        this.protocolIdentifier = protocolIdentifier;
        this.commandType = commandType;
        this.messageNumber = messageNumber;
        this.destinationAddress = destinationAddress;
        this.commandData = commandData;
    }

    public SmsCommandTpduImpl(ByteBuf buf) throws MAPException {
        this();

        if (buf == null)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: data is empty");
        if (buf.readableBytes() < 1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: data length is equal zero");

        int bt = buf.readByte() & 0x0FF;
        if ((bt & _MASK_TP_UDHI) != 0)
            this.userDataHeaderIndicator = true;
        if ((bt & _MASK_TP_SRR) != 0)
            this.statusReportRequest = true;

        this.messageReference = buf.readByte() & 0x0FF;
        if (this.messageReference == -1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: messageReference field has not been found");

        bt = buf.readByte() & 0x0FF;
        if (bt == -1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: protocolIdentifier field has not been found");
        this.protocolIdentifier = new ProtocolIdentifierImpl(bt);

        bt = buf.readByte() & 0x0FF;
        if (bt == -1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: commandType field has not been found");
        this.commandType = new CommandTypeImpl(bt);

        this.messageNumber = buf.readByte() & 0x0FF;
        if (this.messageNumber == -1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: messageNumber field has not been found");

        this.destinationAddress = AddressFieldImpl.createMessage(buf);

        this.commandDataLength = buf.readByte() & 0x0FF;
        if (this.commandDataLength == -1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: commandDataLength field has not been found");

        int avail = this.commandDataLength;
        commandData = new CommandDataImpl(buf.readSlice(avail));
    }

    public boolean getUserDataHeaderIndicator() {
        return this.userDataHeaderIndicator;
    }

    public boolean getStatusReportRequest() {
        return this.statusReportRequest;
    }

    public int getMessageReference() {
        return messageReference;
    }

    public ProtocolIdentifier getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public AddressField getDestinationAddress() {
        return destinationAddress;
    }

    public int getCommandDataLength() {
        return commandDataLength;
    }

    public CommandData getCommandData() {
        return commandData;
    }

    public void encodeData(ByteBuf buf) throws MAPException {

        if (this.protocolIdentifier == null || this.commandData == null || commandType == null || destinationAddress == null)
            throw new MAPException(
                    "Error encoding a SmsCommandTpdu: protocolIdentifier, messageNumber, destinationAddress and commandData must not be null");

        // byte 0
        buf.writeByte(SmsTpduType.SMS_COMMAND.getEncodedValue() | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0)
                | (this.statusReportRequest ? _MASK_TP_SRR : 0));

        this.commandData.encode();
        this.commandDataLength = this.commandData.getEncodedData().readableBytes();

        if (this.commandDataLength > _CommandDataLimit) {
        	throw new MAPException("Command data field length may not increase " + _CommandDataLimit);
        }
        
        buf.writeByte(this.messageReference);
        buf.writeByte(this.protocolIdentifier.getCode());
        buf.writeByte(this.commandType.getCode());
        buf.writeByte(this.messageNumber);
        this.destinationAddress.encodeData(buf);

        buf.writeByte(this.commandDataLength);
        buf.writeBytes(this.commandData.getEncodedData());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SMS-COMMAND tpdu [");

        boolean started = false;
        if (this.userDataHeaderIndicator) {
            sb.append("userDataHeaderIndicator");
            started = true;
        }
        if (this.statusReportRequest) {
            if (started)
                sb.append(", ");
            sb.append("statusReportRequest");
            started = true;
        }

        if (started)
            sb.append(", ");
        sb.append("messageReference=");
        sb.append(this.messageReference);
        if (this.protocolIdentifier != null) {
            sb.append(", ");
            sb.append(this.protocolIdentifier.toString());
        }
        if (this.commandType != null) {
            sb.append(", ");
            sb.append(this.commandType.toString());
        }
        sb.append(", messageNumber=");
        sb.append(this.messageNumber);
        if (this.destinationAddress != null) {
            sb.append(", destinationAddress [");
            sb.append(this.destinationAddress.toString());
            sb.append("]");
        }
        if (this.commandData != null) {
            sb.append("\nCOMMAND [");
            sb.append(this.commandData.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
