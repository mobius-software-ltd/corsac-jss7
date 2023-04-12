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

import org.restcomm.protocols.ss7.commonapp.api.smstpdu.ValidityPeriod;
import org.restcomm.protocols.ss7.commonapp.api.smstpdu.ValidityPeriodFormat;
import org.restcomm.protocols.ss7.commonapp.smstpu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.commonapp.smstpu.ValidityEnhancedFormatDataImpl;
import org.restcomm.protocols.ss7.commonapp.smstpu.ValidityPeriodImpl;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsSubmitTpdu;
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
public class SmsSubmitTpduImpl extends SmsTpduImpl implements SmsSubmitTpdu {
	private boolean rejectDuplicates;
    private ValidityPeriodFormat validityPeriodFormat;
    private boolean replyPathExists;
    private boolean userDataHeaderIndicator;
    private boolean statusReportRequest;
    private int messageReference;
    private AddressField destinationAddress;
    private ProtocolIdentifier protocolIdentifier;
    private DataCodingScheme dataCodingScheme;
    private ValidityPeriod validityPeriod;
    private int userDataLength;
    private UserData userData;

    private SmsSubmitTpduImpl() {
        this.tpduType = SmsTpduType.SMS_SUBMIT;
        this.mobileOriginatedMessage = true;
    }

    public SmsSubmitTpduImpl(boolean rejectDuplicates, boolean replyPathExists, boolean statusReportRequest,
            int messageReference, AddressField destinationAddress, ProtocolIdentifier protocolIdentifier,
            ValidityPeriod validityPeriod, UserData userData) {
        this();

        this.rejectDuplicates = rejectDuplicates;
        this.replyPathExists = replyPathExists;
        this.statusReportRequest = statusReportRequest;
        this.messageReference = messageReference;
        this.destinationAddress = destinationAddress;
        this.protocolIdentifier = protocolIdentifier;
        this.validityPeriod = validityPeriod;
        this.userData = userData;
    }

    public SmsSubmitTpduImpl(ByteBuf stm, Charset gsm8Charset) throws MAPException {
        this();

        if (stm == null)
            throw new MAPException("Error creating a new SmsSubmitTpdu instance: data is empty");
        if (stm.readableBytes() < 1)
            throw new MAPException("Error creating a new SmsSubmitTpdu instance: data length is equal zero");

        int bt = stm.readByte() & 0x0FF;
        if ((bt & _MASK_TP_RD) != 0)
            this.rejectDuplicates = true;
        int code = (bt & _MASK_TP_VPF) >> 3;
        this.validityPeriodFormat = ValidityPeriodFormat.getInstance(code);
        if ((bt & _MASK_TP_RP) != 0)
            this.replyPathExists = true;
        if ((bt & _MASK_TP_UDHI) != 0)
            this.userDataHeaderIndicator = true;
        if ((bt & _MASK_TP_SRR) != 0)
            this.statusReportRequest = true;

        this.messageReference = stm.readByte() & 0x0FF;
        if (this.messageReference == -1)
            throw new MAPException("Error creating a new SmsSubmitTpdu instance: messageReference field has not been found");

        this.destinationAddress = AddressFieldImpl.createMessage(stm);

        bt = stm.readByte() & 0x0FF;
        if (bt == -1)
            throw new MAPException("Error creating a new SmsSubmitTpdu instance: protocolIdentifier field has not been found");
        this.protocolIdentifier = new ProtocolIdentifierImpl(bt);

        bt = stm.readByte() & 0x0FF;
        if (bt == -1)
            throw new MAPException("Error creating a new SmsSubmitTpdu instance: dataCodingScheme field has not been found");
        this.dataCodingScheme = new DataCodingSchemeImpl(bt);

        switch (this.validityPeriodFormat) {
            case fieldPresentRelativeFormat:
                bt = stm.readByte() & 0x0FF;
                if (bt == -1)
                    throw new MAPException(
                            "Error creating a new SmsSubmitTpdu instance: validityPeriodFormat-fieldPresentEnhancedFormat field has not been found");
                this.validityPeriod = new ValidityPeriodImpl(bt);
                break;
            case fieldPresentAbsoluteFormat:
            	try {
            		AbsoluteTimeStampImpl ats = AbsoluteTimeStampImpl.createMessage(stm);
            		this.validityPeriod = new ValidityPeriodImpl(ats);                    
            	}
            	catch(ASNParsingException ex) {
            		throw new MAPException(ex.getMessage(),ex.getCause());
            	}
                break;
            case fieldPresentEnhancedFormat:
                ValidityEnhancedFormatDataImpl vef = new ValidityEnhancedFormatDataImpl(stm.readSlice(7));
                this.validityPeriod = new ValidityPeriodImpl(vef);
                break;
			default:
				break;
        }

        this.userDataLength = stm.readByte() & 0x0FF;
        if (this.userDataLength == -1)
            throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: userDataLength field has not been found");

        userData = new UserDataImpl(stm.readSlice(stm.readableBytes()), dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
    }

    public boolean getRejectDuplicates() {
        return this.rejectDuplicates;
    }

    public ValidityPeriodFormat getValidityPeriodFormat() {
        return this.validityPeriodFormat;
    }

    public boolean getReplyPathExists() {
        return this.replyPathExists;
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

    public AddressField getDestinationAddress() {
        return destinationAddress;
    }

    public ProtocolIdentifier getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public DataCodingScheme getDataCodingScheme() {
        return dataCodingScheme;
    }

    public ValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public int getUserDataLength() {
        return userDataLength;
    }

    public UserData getUserData() {
        return userData;
    }

    public void encodeData(ByteBuf buf) throws MAPException {

        if (this.destinationAddress == null || this.protocolIdentifier == null || this.userData == null)
            throw new MAPException(
                    "Error encoding a SmsSubmitTpdu: destinationAddress, protocolIdentifier and userData must not be null");

        if (this.validityPeriod == null) {
            this.validityPeriodFormat = ValidityPeriodFormat.fieldNotPresent;
        } else if (this.validityPeriod.getRelativeFormatValue() != null) {
            this.validityPeriodFormat = ValidityPeriodFormat.fieldPresentRelativeFormat;
        } else if (this.validityPeriod.getAbsoluteFormatValue() != null) {
            this.validityPeriodFormat = ValidityPeriodFormat.fieldPresentAbsoluteFormat;
        } else if (this.validityPeriod.getEnhancedFormatValue() != null) {
            this.validityPeriodFormat = ValidityPeriodFormat.fieldPresentEnhancedFormat;
        } else {
            this.validityPeriodFormat = ValidityPeriodFormat.fieldNotPresent;
        }

        this.userData.encode();
        this.userDataHeaderIndicator = this.userData.getEncodedUserDataHeaderIndicator();
        this.userDataLength = this.userData.getUserDataLength();
        this.dataCodingScheme = this.userData.getDataCodingScheme();

        if (this.userData.getEncodedData().readableBytes() > _UserDataLimit)
            throw new MAPException("User data field length may not increase " + _UserDataLimit);

        // byte 0
        buf.writeByte(SmsTpduType.SMS_SUBMIT.getEncodedValue() | (this.rejectDuplicates ? _MASK_TP_RD : 0)
                | (this.validityPeriodFormat.getCode() << 3) | (this.replyPathExists ? _MASK_TP_RP : 0)
                | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0) | (this.statusReportRequest ? _MASK_TP_SRR : 0));

        buf.writeByte(this.messageReference);
        this.destinationAddress.encodeData(buf);
        buf.writeByte(this.protocolIdentifier.getCode());
        buf.writeByte(this.dataCodingScheme.getCode());

        switch (this.validityPeriodFormat) {
            case fieldPresentRelativeFormat:
            	buf.writeByte(this.validityPeriod.getRelativeFormatValue());
                break;
            case fieldPresentAbsoluteFormat:
            	try {
            		this.validityPeriod.getAbsoluteFormatValue().encodeData(buf);
            	}
            	catch(ASNParsingException ex) {
            		throw new MAPException(ex.getMessage(),ex.getCause());
            	}
                break;
            case fieldPresentEnhancedFormat:
            	buf.writeBytes(this.validityPeriod.getEnhancedFormatValue().getValue());
                break;
			default:
				break;
        }

        buf.writeByte(this.userDataLength);
        buf.writeBytes(this.userData.getEncodedData());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SMS-SUBMIT tpdu [");

        boolean started = false;
        if (this.rejectDuplicates) {
            sb.append("rejectDuplicates");
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
        if (this.destinationAddress != null) {
            sb.append(", destinationAddress [");
            sb.append(this.destinationAddress.toString());
            sb.append("]");
        }
        if (this.protocolIdentifier != null) {
            sb.append(", ");
            sb.append(this.protocolIdentifier.toString());
        }
        if (this.validityPeriod != null) {
            sb.append(", ");
            sb.append(this.validityPeriod.toString());
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
