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
public class SmsSubmitReportTpduImpl extends SmsTpduImpl {
	private boolean userDataHeaderIndicator;
    private FailureCauseImpl failureCause;
    private ParameterIndicator parameterIndicator;
    private AbsoluteTimeStampImpl serviceCentreTimeStamp;
    private ProtocolIdentifierImpl protocolIdentifier;
    private DataCodingSchemeImpl dataCodingScheme;
    private int userDataLength;
    private UserDataImpl userData;

    private SmsSubmitReportTpduImpl() {
        this.tpduType = SmsTpduType.SMS_SUBMIT_REPORT;
        this.mobileOriginatedMessage = false;
    }

    public SmsSubmitReportTpduImpl(FailureCauseImpl failureCause, AbsoluteTimeStampImpl serviceCentreTimeStamp,
            ProtocolIdentifierImpl protocolIdentifier, UserDataImpl userData) {
        this();

        this.failureCause = failureCause;
        this.serviceCentreTimeStamp = serviceCentreTimeStamp;
        this.protocolIdentifier = protocolIdentifier;
        this.userData = userData;
    }

    public SmsSubmitReportTpduImpl(ByteBuf stm, Charset gsm8Charset) throws MAPException {
        this();

        if (stm == null)
            throw new MAPException("Error creating a new SmsSubmitReportTpdu instance: data is empty");
        if (stm.readableBytes() < 1)
            throw new MAPException("Error creating a new SmsSubmitReportTpdu instance: data length is equal zero");

        int bt = stm.readByte() & 0x0FF;
        if ((bt & _MASK_TP_UDHI) != 0)
            this.userDataHeaderIndicator = true;

        bt = stm.readByte() & 0x0FF;
        if (bt == -1)
            throw new MAPException(
                    "Error creating a new SmsDeliverReportTpdu instance: Failure-Cause and Parameter-Indicator fields have not been found");
        if ((bt & 0x80) != 0) {
            // Failure-Cause exists
            this.failureCause = new FailureCauseImpl(bt);

            bt = stm.readByte() & 0x0FF;
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverReportTpdu instance: Parameter-Indicator field has not been found");
        }

        this.parameterIndicator = new ParameterIndicatorImpl(bt);

        this.serviceCentreTimeStamp = AbsoluteTimeStampImpl.createMessage(stm);

        if (this.parameterIndicator.getTP_PIDPresence()) {
            bt = stm.readByte() & 0x0FF;
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverTpduImpl instance: protocolIdentifier field has not been found");
            this.protocolIdentifier = new ProtocolIdentifierImpl(bt);
        }

        if (this.parameterIndicator.getTP_DCSPresence()) {
            bt = stm.readByte() & 0x0FF;
            if (bt == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverTpduImpl instance: dataCodingScheme field has not been found");
            this.dataCodingScheme = new DataCodingSchemeImpl(bt);
        }

        if (this.parameterIndicator.getTP_UDLPresence()) {
            this.userDataLength = stm.readByte() & 0x0FF;
            if (this.userDataLength == -1)
                throw new MAPException(
                        "Error creating a new SmsDeliverTpduImpl instance: userDataLength field has not been found");

            byte[] buf = new byte[stm.readableBytes()];
            stm.readBytes(buf);
            userData = new UserDataImpl(buf, dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
        }
    }

    public boolean getUserDataHeaderIndicator() {
        return this.userDataHeaderIndicator;
    }

    public FailureCauseImpl getFailureCause() {
        return failureCause;
    }

    public ParameterIndicator getParameterIndicator() {
        return parameterIndicator;
    }

    public AbsoluteTimeStampImpl getServiceCentreTimeStamp() {
        return serviceCentreTimeStamp;
    }

    public ProtocolIdentifierImpl getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public DataCodingSchemeImpl getDataCodingScheme() {
        return dataCodingScheme;
    }

    public int getUserDataLength() {
        return userDataLength;
    }

    public UserDataImpl getUserData() {
        return userData;
    }

    public void encodeData(ByteBuf buf) throws MAPException {

        if (this.serviceCentreTimeStamp == null)
            throw new MAPException("Parameter serviceCentreTimeStamp must not be null");

        if (this.userData != null) {
            this.userData.encode();
            this.userDataHeaderIndicator = this.userData.getEncodedUserDataHeaderIndicator();
            this.userDataLength = this.userData.getEncodedUserDataLength();
            this.dataCodingScheme = this.userData.getDataCodingScheme();

            if (this.userData.getEncodedData().length > _UserDataSubmitReportLimit)
                throw new MAPException("User data field length may not increase " + _UserDataSubmitReportLimit);
        }

        // byte 0
        buf.writeByte(SmsTpduType.SMS_SUBMIT_REPORT.getEncodedValue() | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0));

        if (this.failureCause != null)
        	buf.writeByte(this.failureCause.getCode());

        this.parameterIndicator = new ParameterIndicatorImpl(this.userData != null, this.dataCodingScheme != null,
                this.protocolIdentifier != null);
        buf.writeByte(this.parameterIndicator.getCode());
        this.serviceCentreTimeStamp.encodeData(buf);

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

        sb.append("SMS-SUBMIT-REPORT tpdu [");

        boolean started = false;
        if (this.userDataHeaderIndicator) {
            sb.append("userDataHeaderIndicator");
            started = true;
        }

        if (this.failureCause != null) {
            if (started)
                sb.append(", ");
            sb.append("failureCause=");
            sb.append(this.failureCause.toString());
            started = true;
        }
        if (this.parameterIndicator != null) {
            if (started)
                sb.append(", ");
            sb.append(this.parameterIndicator.toString());
            started = true;
        }
        if (this.serviceCentreTimeStamp != null) {
            if (started)
                sb.append(", ");
            sb.append("serviceCentreTimeStamp [");
            sb.append(this.serviceCentreTimeStamp.toString());
            sb.append("]");
            started = true;
        }
        if (this.protocolIdentifier != null) {
            if (started)
                sb.append(", ");
            sb.append(this.protocolIdentifier.toString());
            started = true;
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
