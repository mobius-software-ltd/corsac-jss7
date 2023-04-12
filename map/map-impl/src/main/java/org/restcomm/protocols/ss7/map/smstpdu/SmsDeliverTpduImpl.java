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
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsDeliverTpdu;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserData;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SmsDeliverTpduImpl extends SmsTpduImpl implements SmsDeliverTpdu {
	private boolean moreMessagesToSend;
    private boolean forwardedOrSpawned;
    private boolean replyPathExists;
    private boolean userDataHeaderIndicator;
    private boolean statusReportIndication;
    private AddressField originatingAddress;
    private ProtocolIdentifier protocolIdentifier;
    private DataCodingScheme dataCodingScheme;
    private AbsoluteTimeStamp serviceCentreTimeStamp;
    private int userDataLength;
    private UserData userData;

    private SmsDeliverTpduImpl() {
        this.tpduType = SmsTpduType.SMS_DELIVER;
        this.mobileOriginatedMessage = false;
    }

    public SmsDeliverTpduImpl(boolean moreMessagesToSend, boolean forwardedOrSpawned, boolean replyPathExists,
            boolean statusReportIndication, AddressField originatingAddress, ProtocolIdentifier protocolIdentifier,
            AbsoluteTimeStamp serviceCentreTimeStamp, UserData userData) {
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

        try {
        	this.serviceCentreTimeStamp = AbsoluteTimeStampImpl.createMessage(stm);
        }
    	catch(ASNParsingException ex) {
    		throw new MAPException(ex.getMessage(),ex.getCause());
    	}
        
        this.userDataLength = stm.readByte() & 0x0FF;
        if (this.userDataLength == -1)
            throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: userDataLength field has not been found");

        userData = new UserDataImpl(stm.readSlice(stm.readableBytes()), dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
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

    public AddressField getOriginatingAddress() {
        return originatingAddress;
    }

    public ProtocolIdentifier getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public DataCodingScheme getDataCodingScheme() {
        return dataCodingScheme;
    }

    public AbsoluteTimeStamp getServiceCentreTimeStamp() {
        return serviceCentreTimeStamp;
    }

    public int getUserDataLength() {
        return userDataLength;
    }

    public UserData getUserData() {
        return userData;
    }

    public void encodeData(ByteBuf buf) throws MAPException {

        if (this.originatingAddress == null || this.protocolIdentifier == null || this.serviceCentreTimeStamp == null
                || this.userData == null)
            throw new MAPException(
                    "Parameters originatingAddress, protocolIdentifier, serviceCentreTimeStamp and userData must not be null");

        this.userData.encode();
        this.userDataHeaderIndicator = this.userData.getEncodedUserDataHeaderIndicator();
        this.userDataLength = this.userData.getUserDataLength();
        this.dataCodingScheme = this.userData.getDataCodingScheme();

        if (this.userData.getEncodedData().readableBytes() > _UserDataLimit)
            throw new MAPException("User data field length may not increase " + _UserDataLimit);

        // byte 0
        buf.writeByte(SmsTpduType.SMS_DELIVER.getEncodedValue() | (!this.moreMessagesToSend ? _MASK_TP_MMS : 0)
                | (this.forwardedOrSpawned ? _MASK_TP_LP : 0) | (this.replyPathExists ? _MASK_TP_RP : 0)
                | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0) | (this.statusReportIndication ? _MASK_TP_SRI : 0));

        this.originatingAddress.encodeData(buf);
        buf.writeByte(this.protocolIdentifier.getCode());
        buf.writeByte(this.dataCodingScheme.getCode());
        try {
        	this.serviceCentreTimeStamp.encodeData(buf);
        }
    	catch(ASNParsingException ex) {
    		throw new MAPException(ex.getMessage(),ex.getCause());
    	}
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
