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

import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.map.api.MAPException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SmsDeliverTpduImpl extends SmsTpduImpl {
	private boolean moreMessagesToSend;
    private boolean forwardedOrSpawned;
    private boolean replyPathExists;
    private boolean userDataHeaderIndicator;
    private boolean statusReportIndication;
    private AddressFieldImpl originatingAddress;
    private ProtocolIdentifierImpl protocolIdentifier;
    private DataCodingSchemeImpl dataCodingScheme;
    private AbsoluteTimeStampImpl serviceCentreTimeStamp;
    private int userDataLength;
    private UserDataImpl userData;

    private SmsDeliverTpduImpl() {
        this.tpduType = SmsTpduType.SMS_DELIVER;
        this.mobileOriginatedMessage = false;
    }

    public SmsDeliverTpduImpl(boolean moreMessagesToSend, boolean forwardedOrSpawned, boolean replyPathExists,
            boolean statusReportIndication, AddressFieldImpl originatingAddress, ProtocolIdentifierImpl protocolIdentifier,
            AbsoluteTimeStampImpl serviceCentreTimeStamp, UserDataImpl userData) {
        this();

        this.moreMessagesToSend = moreMessagesToSend;
        this.forwardedOrSpawned = forwardedOrSpawned;
        this.replyPathExists = replyPathExists;
        this.statusReportIndication = statusReportIndication;
        this.originatingAddress = originatingAddress;
        this.protocolIdentifier = protocolIdentifier;
        this.serviceCentreTimeStamp = serviceCentreTimeStamp;
        this.userData = userData;
    }

    public SmsDeliverTpduImpl(ByteBuf stm, Charset gsm8Charset) throws MAPException {
        this();

        if (stm == null)
            throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: data is empty");
        if (stm.readableBytes() < 1)
            throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: data length is equal zero");

        int bt = stm.readByte() & 0x0FF;
        if ((bt & _MASK_TP_MMS) == 0)
            this.moreMessagesToSend = true;
        if ((bt & _MASK_TP_LP) != 0)
            this.forwardedOrSpawned = true;
        if ((bt & _MASK_TP_RP) != 0)
            this.replyPathExists = true;
        if ((bt & _MASK_TP_UDHI) != 0)
            this.userDataHeaderIndicator = true;
        if ((bt & _MASK_TP_SRI) != 0)
            this.statusReportIndication = true;

        this.originatingAddress = AddressFieldImpl.createMessage(stm);

        bt = stm.readByte() & 0x0FF;
        if (bt == -1)
            throw new MAPException(
                    "Error creating a new SmsDeliverTpduImpl instance: protocolIdentifier field has not been found");
        this.protocolIdentifier = new ProtocolIdentifierImpl(bt);

        bt = stm.readByte() & 0x0FF;
        if (bt == -1)
            throw new MAPException(
                    "Error creating a new SmsDeliverTpduImpl instance: dataCodingScheme field has not been found");
        this.dataCodingScheme = new DataCodingSchemeImpl(bt);

        this.serviceCentreTimeStamp = AbsoluteTimeStampImpl.createMessage(stm);

        this.userDataLength = stm.readByte() & 0x0FF;
        if (this.userDataLength == -1)
            throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: userDataLength field has not been found");

        byte[] buf = new byte[stm.readableBytes()];
        stm.readBytes(buf);
        userData = new UserDataImpl(buf, dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
    }

    public boolean getMoreMessagesToSend() {
        return this.moreMessagesToSend;
    }

    public boolean getForwardedOrSpawned() {
        return this.forwardedOrSpawned;
    }

    public boolean getReplyPathExists() {
        return this.replyPathExists;
    }

    public boolean getUserDataHeaderIndicator() {
        return this.userDataHeaderIndicator;
    }

    public boolean getStatusReportIndication() {
        return this.statusReportIndication;
    }

    public AddressFieldImpl getOriginatingAddress() {
        return originatingAddress;
    }

    public ProtocolIdentifierImpl getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public DataCodingSchemeImpl getDataCodingScheme() {
        return dataCodingScheme;
    }

    public AbsoluteTimeStampImpl getServiceCentreTimeStamp() {
        return serviceCentreTimeStamp;
    }

    public int getUserDataLength() {
        return userDataLength;
    }

    public UserDataImpl getUserData() {
        return userData;
    }

    public void encodeData(ByteBuf buf) throws MAPException {

        if (this.originatingAddress == null || this.protocolIdentifier == null || this.serviceCentreTimeStamp == null
                || this.userData == null)
            throw new MAPException(
                    "Parameters originatingAddress, protocolIdentifier, serviceCentreTimeStamp and userData must not be null");

        this.userData.encode();
        this.userDataHeaderIndicator = this.userData.getEncodedUserDataHeaderIndicator();
        this.userDataLength = this.userData.getEncodedUserDataLength();
        this.dataCodingScheme = this.userData.getDataCodingScheme();

        if (this.userData.getEncodedData().length > _UserDataLimit)
            throw new MAPException("User data field length may not increase " + _UserDataLimit);

        // byte 0
        buf.writeByte(SmsTpduType.SMS_DELIVER.getEncodedValue() | (!this.moreMessagesToSend ? _MASK_TP_MMS : 0)
                | (this.forwardedOrSpawned ? _MASK_TP_LP : 0) | (this.replyPathExists ? _MASK_TP_RP : 0)
                | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0) | (this.statusReportIndication ? _MASK_TP_SRI : 0));

        this.originatingAddress.encodeData(buf);
        buf.writeByte(this.protocolIdentifier.getCode());
        buf.writeByte(this.dataCodingScheme.getCode());
        this.serviceCentreTimeStamp.encodeData(buf);
        buf.writeByte(this.userDataLength);
        buf.writeBytes(this.userData.getEncodedData());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SMS-DELIVER tpdu [");

        boolean started = false;
        if (this.moreMessagesToSend) {
            sb.append("moreMessagesToSend");
            started = true;
        }
        if (this.dataCodingScheme != null) {
            if (started)
                sb.append(", ");
            sb.append("dataCodingScheme [");
            sb.append(dataCodingScheme);
            sb.append("]");
            started = true;
        }
        if (this.forwardedOrSpawned) {
            if (started)
                sb.append(", ");
            sb.append("LoopPrevention");
            started = true;
        }
        if (this.replyPathExists) {
            if (started)
                sb.append(", ");
            sb.append("replyPathExists");
            started = true;
        }
        if (this.userDataHeaderIndicator) {
            if (started)
                sb.append(", ");
            sb.append("userDataHeaderIndicator");
            started = true;
        }
        if (this.statusReportIndication) {
            if (started)
                sb.append(", ");
            sb.append("statusReportIndication");
            started = true;
        }
        if (this.originatingAddress != null) {
            if (started)
                sb.append(", ");
            sb.append("originatingAddress [");
            sb.append(this.originatingAddress.toString());
            sb.append("]");
        }
        if (this.protocolIdentifier != null) {
            sb.append(", ");
            sb.append(this.protocolIdentifier.toString());
        }
        if (this.serviceCentreTimeStamp != null) {
            sb.append(", serviceCentreTimeStamp [");
            sb.append(this.serviceCentreTimeStamp.toString());
            sb.append("]");
        }
        if (this.userData != null) {
            sb.append("\nMSG [");
            try {
                this.userData.decode();
            } catch (MAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            sb.append(this.userData.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
