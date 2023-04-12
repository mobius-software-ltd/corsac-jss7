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

import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.commonapp.api.smstpdu.AbsoluteTimeStamp;
import org.restcomm.protocols.ss7.commonapp.smstpu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.restcomm.protocols.ss7.map.api.smstpdu.ParameterIndicator;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsStatusReportTpdu;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.restcomm.protocols.ss7.map.api.smstpdu.Status;
import org.restcomm.protocols.ss7.map.api.smstpdu.StatusReportQualifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserData;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SmsStatusReportTpduImpl extends SmsTpduImpl implements SmsStatusReportTpdu {
	private boolean userDataHeaderIndicator;
    private boolean moreMessagesToSend;
    private boolean forwardedOrSpawned;
    private StatusReportQualifier statusReportQualifier;
    private int messageReference;
    private AddressField recipientAddress;
    private AbsoluteTimeStamp serviceCentreTimeStamp;
    private AbsoluteTimeStamp dischargeTime;
    private Status status;
    private ParameterIndicator parameterIndicator;
    private ProtocolIdentifier protocolIdentifier;
    private DataCodingScheme dataCodingScheme;
    private int userDataLength;
    private UserData userData;

    private SmsStatusReportTpduImpl() {
        this.tpduType = SmsTpduType.SMS_STATUS_REPORT;
        this.mobileOriginatedMessage = false;
    }

    public SmsStatusReportTpduImpl(boolean moreMessagesToSend, boolean forwardedOrSpawned,
            StatusReportQualifier statusReportQualifier, int messageReference, AddressField recipientAddress,
            AbsoluteTimeStamp serviceCentreTimeStamp, AbsoluteTimeStamp dischargeTime, Status status,
            ProtocolIdentifier protocolIdentifier, UserData userData) {
        this();

        this.moreMessagesToSend = moreMessagesToSend;
        this.forwardedOrSpawned = forwardedOrSpawned;
        this.statusReportQualifier = statusReportQualifier;
        this.messageReference = messageReference;
        this.recipientAddress = recipientAddress;
        this.serviceCentreTimeStamp = serviceCentreTimeStamp;
        this.dischargeTime = dischargeTime;
        this.status = status;
        this.protocolIdentifier = protocolIdentifier;
        this.userData = userData;
    }

    public SmsStatusReportTpduImpl(ByteBuf stm, Charset gsm8Charset) throws MAPException {
        this();

        if (stm == null)
            throw new MAPException("Error creating a new SmsStatusReport instance: data is empty");
        if (stm.readableBytes() < 1)
            throw new MAPException("Error creating a new SmsStatusReport instance: data length is equal zero");

        int bt = stm.readByte() & 0x0FF;
        if ((bt & _MASK_TP_UDHI) != 0)
            this.userDataHeaderIndicator = true;
        if ((bt & _MASK_TP_MMS) == 0)
            this.moreMessagesToSend = true;
        if ((bt & _MASK_TP_LP) != 0)
            this.forwardedOrSpawned = true;
        int code = (bt & _MASK_TP_SRQ) >> 5;
        this.statusReportQualifier = StatusReportQualifier.getInstance(code);

        this.messageReference = stm.readByte() & 0x0FF;
        if (this.messageReference == -1)
            throw new MAPException("Error creating a new SmsStatusReport instance: messageReference field has not been found");

        this.recipientAddress = AddressFieldImpl.createMessage(stm);
        try {
        	this.serviceCentreTimeStamp = AbsoluteTimeStampImpl.createMessage(stm);
        	this.dischargeTime = AbsoluteTimeStampImpl.createMessage(stm);
        }
    	catch(ASNParsingException ex) {
    		throw new MAPException(ex.getMessage(),ex.getCause());
    	}

        bt = stm.readByte() & 0x0FF;
        if (bt == -1)
            throw new MAPException("Error creating a new SmsStatusReport instance: Status field has not been found");
        this.status = new StatusImpl(bt);

        bt = -1;
        if(stm.readableBytes()>0)
        	bt = stm.readByte() & 0x0FF;
        
        if (bt == -1)
            this.parameterIndicator = new ParameterIndicatorImpl(0);
        else
            this.parameterIndicator = new ParameterIndicatorImpl(bt);

        if (this.parameterIndicator.getTP_PIDPresence()) {
            bt = stm.readByte() & 0x0FF;
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsStatusReport instance: protocolIdentifier field has not been found");
            this.protocolIdentifier = new ProtocolIdentifierImpl(bt);
        }

        if (this.parameterIndicator.getTP_DCSPresence()) {
            bt = stm.readByte() & 0x0FF;
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsStatusReport instance: dataCodingScheme field has not been found");
            this.dataCodingScheme = new DataCodingSchemeImpl(bt);
        }

        if (this.parameterIndicator.getTP_UDLPresence()) {
            this.userDataLength = stm.readByte() & 0x0FF;
            if (this.userDataLength == -1)
                throw new MAPException("Error creating a new SmsStatusReport instance: userDataLength field has not been found");

            userData = new UserDataImpl(stm.readSlice(stm.readableBytes()), dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
        }
    }

    public boolean getUserDataHeaderIndicator() {
        return this.userDataHeaderIndicator;
    }

    public boolean getMoreMessagesToSend() {
        return this.moreMessagesToSend;
    }

    public boolean getForwardedOrSpawned() {
        return this.forwardedOrSpawned;
    }

    public StatusReportQualifier getStatusReportQualifier() {
        return this.statusReportQualifier;
    }

    public int getMessageReference() {
        return messageReference;
    }

    public AddressField getRecipientAddress() {
        return recipientAddress;
    }

    public AbsoluteTimeStamp getServiceCentreTimeStamp() {
        return serviceCentreTimeStamp;
    }

    public AbsoluteTimeStamp getDischargeTime() {
        return dischargeTime;
    }

    public Status getStatus() {
        return status;
    }

    public ParameterIndicator getParameterIndicator() {
        return parameterIndicator;
    }

    public ProtocolIdentifier getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public DataCodingScheme getDataCodingScheme() {
        return dataCodingScheme;
    }

    public int getUserDataLength() {
        return userDataLength;
    }

    public UserData getUserData() {
        return userData;
    }

    public void encodeData(ByteBuf buf) throws MAPException {

        if (statusReportQualifier == null || this.recipientAddress == null || this.serviceCentreTimeStamp == null
                || this.dischargeTime == null || this.status == null)
            throw new MAPException(
                    "Error encoding a SmsStatusReportTpdu: statusReportQualifier, recipientAddress, serviceCentreTimeStamp, dischargeTime and status must no be null");

        if (this.userData != null) {
            this.userData.encode();
            this.userDataHeaderIndicator = this.userData.getEncodedUserDataHeaderIndicator();
            this.userDataLength = this.userData.getUserDataLength();
            this.dataCodingScheme = this.userData.getDataCodingScheme();
            if (this.userData.getEncodedData().readableBytes() > _UserDataStatusReportLimit)
                throw new MAPException("User data field length may not increase " + _UserDataStatusReportLimit);
        }

        // byte 0
        buf.writeByte(SmsTpduType.SMS_COMMAND.getEncodedValue() | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0)
                | (!this.moreMessagesToSend ? _MASK_TP_MMS : 0) | (this.forwardedOrSpawned ? _MASK_TP_LP : 0)
                | (this.statusReportQualifier.getCode() << 5));

        buf.writeByte(this.messageReference);
        this.recipientAddress.encodeData(buf);
        
        try {
        	this.serviceCentreTimeStamp.encodeData(buf);
        	this.dischargeTime.encodeData(buf);
        }
    	catch(ASNParsingException ex) {
    		throw new MAPException(ex.getMessage(),ex.getCause());
    	}
        
        buf.writeByte(this.status.getCode());

        this.parameterIndicator = new ParameterIndicatorImpl(this.userData != null, this.dataCodingScheme != null,
                this.protocolIdentifier != null);

        if (this.parameterIndicator.getCode() != 0) {
        	buf.writeByte(this.parameterIndicator.getCode());
        }
        if (this.protocolIdentifier != null) {
        	buf.writeByte(this.protocolIdentifier.getCode());
        }
        if (this.dataCodingScheme != null) {
        	buf.writeByte(this.dataCodingScheme.getCode());
        }

        if (this.userData != null) {
        	buf.writeByte(this.userDataLength);
        	buf.writeBytes(this.userData.getEncodedData());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SMS-STATUS-REPORT tpdu [");

        boolean started = false;
        if (this.userDataHeaderIndicator) {
            sb.append("userDataHeaderIndicator");
            started = true;
        }
        if (this.moreMessagesToSend) {
            if (started)
                sb.append(", ");
            sb.append("moreMessagesToSend");
            started = true;
        }
        if (this.forwardedOrSpawned) {
            if (started)
                sb.append(", ");
            sb.append("forwardedOrSpawned");
            started = true;
        }
        if (this.statusReportQualifier != null) {
            if (started)
                sb.append(", ");
            sb.append("statusReportQualifier=");
            sb.append(this.statusReportQualifier);
        }

        sb.append(", messageReference=");
        sb.append(this.messageReference);
        if (this.recipientAddress != null) {
            sb.append(", recipientAddress [");
            sb.append(this.recipientAddress.toString());
            sb.append("]");
        }
        if (this.serviceCentreTimeStamp != null) {
            sb.append(", serviceCentreTimeStamp [");
            sb.append(this.serviceCentreTimeStamp.toString());
            sb.append("]");
        }
        if (this.dischargeTime != null) {
            sb.append(", dischargeTime [");
            sb.append(this.dischargeTime.toString());
            sb.append("]");
        }
        if (this.status != null) {
            sb.append(", ");
            sb.append(this.status.toString());
        }
        if (this.parameterIndicator != null) {
            sb.append(", ");
            sb.append(this.parameterIndicator.toString());
        }
        if (this.protocolIdentifier != null) {
            sb.append(", ");
            sb.append(this.protocolIdentifier.toString());
        }
        if (this.userData != null) {
            sb.append("\nMSG [");
            sb.append(this.userData.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
