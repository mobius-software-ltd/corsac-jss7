/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.api.smstpdu;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.map.api.MAPException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SmsCommandTpduImpl extends SmsTpduImpl {
	private boolean userDataHeaderIndicator;
    private boolean statusReportRequest;
    private int messageReference;
    private ProtocolIdentifierImpl protocolIdentifier;
    private CommandTypeImpl commandType;
    private int messageNumber;
    private AddressFieldImpl destinationAddress;
    private int commandDataLength;
    private CommandDataImpl commandData;

    private SmsCommandTpduImpl() {
        this.tpduType = SmsTpduType.SMS_COMMAND;
        this.mobileOriginatedMessage = true;
    }

    public SmsCommandTpduImpl(boolean statusReportRequest, int messageReference, ProtocolIdentifierImpl protocolIdentifier,
            CommandTypeImpl commandType, int messageNumber, AddressFieldImpl destinationAddress, CommandDataImpl commandData) {
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
        byte[] commandDataArr = new byte[avail];
        buf.readBytes(commandDataArr);
        commandData = new CommandDataImpl(commandDataArr);
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

    public ProtocolIdentifierImpl getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public CommandTypeImpl getCommandType() {
        return commandType;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public AddressFieldImpl getDestinationAddress() {
        return destinationAddress;
    }

    public int getCommandDataLength() {
        return commandDataLength;
    }

    public CommandDataImpl getCommandData() {
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
        this.commandDataLength = this.commandData.getEncodedData().length;

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
