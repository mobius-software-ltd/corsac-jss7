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

package org.restcomm.protocols.ss7.map;

import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.map.api.MAPSmsTpduParameterFactory;
import org.restcomm.protocols.ss7.map.api.datacoding.NationalLanguageIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressFieldImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.CharacterSet;
import org.restcomm.protocols.ss7.map.api.smstpdu.CommandDataImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.CommandTypeImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.CommandTypeValue;
import org.restcomm.protocols.ss7.map.api.smstpdu.ConcatenatedShortMessagesIdentifierImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingGroup;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingSchemaIndicationType;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingSchemaMessageClass;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.FailureCauseImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.NationalLanguageLockingShiftIdentifierImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.NationalLanguageSingleShiftIdentifierImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.ParameterIndicator;
import org.restcomm.protocols.ss7.map.api.smstpdu.ParameterIndicatorImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsCommandTpduImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpduImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsDeliverTpduImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsStatusReportTpduImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsSubmitReportTpduImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsSubmitTpduImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.StatusImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.StatusReportQualifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataHeaderImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.ValidityEnhancedFormatDataImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.ValidityPeriodImpl;

/**
 *
 * @author amit bhayani
 *
 */
public class MAPSmsTpduParameterFactoryImpl implements MAPSmsTpduParameterFactory {

    public SmsCommandTpduImpl createSmsCommandTpdu(boolean statusReportRequest, int messageReference,
            ProtocolIdentifierImpl protocolIdentifier, CommandTypeImpl commandType, int messageNumber, AddressFieldImpl destinationAddress,
            CommandDataImpl commandData) {
        return new SmsCommandTpduImpl(statusReportRequest, messageReference, protocolIdentifier, commandType, messageNumber,
                destinationAddress, commandData);
    }

    public SmsDeliverReportTpduImpl createSmsDeliverReportTpdu(FailureCauseImpl failureCause, ProtocolIdentifierImpl protocolIdentifier,
            UserDataImpl userData) {
        return new SmsDeliverReportTpduImpl(failureCause, protocolIdentifier, userData);
    }

    public SmsDeliverTpduImpl createSmsDeliverTpdu(boolean moreMessagesToSend, boolean forwardedOrSpawned, boolean replyPathExists,
            boolean statusReportIndication, AddressFieldImpl originatingAddress, ProtocolIdentifierImpl protocolIdentifier,
            AbsoluteTimeStampImpl serviceCentreTimeStamp, UserDataImpl userData) {
        return new SmsDeliverTpduImpl(moreMessagesToSend, forwardedOrSpawned, replyPathExists, statusReportIndication,
                originatingAddress, protocolIdentifier, serviceCentreTimeStamp, userData);
    }

    public SmsStatusReportTpduImpl createSmsStatusReportTpdu(boolean moreMessagesToSend, boolean forwardedOrSpawned,
            StatusReportQualifier statusReportQualifier, int messageReference, AddressFieldImpl recipientAddress,
            AbsoluteTimeStampImpl serviceCentreTimeStamp, AbsoluteTimeStampImpl dischargeTime, StatusImpl status,
            ProtocolIdentifierImpl protocolIdentifier, UserDataImpl userData) {
        return new SmsStatusReportTpduImpl(moreMessagesToSend, forwardedOrSpawned, statusReportQualifier, messageReference,
                recipientAddress, serviceCentreTimeStamp, dischargeTime, status, protocolIdentifier, userData);
    }

    public SmsSubmitReportTpduImpl createSmsSubmitReportTpdu(FailureCauseImpl failureCause, AbsoluteTimeStampImpl serviceCentreTimeStamp,
            ProtocolIdentifierImpl protocolIdentifier, UserDataImpl userData) {
        return new SmsSubmitReportTpduImpl(failureCause, serviceCentreTimeStamp, protocolIdentifier, userData);
    }

    public SmsSubmitTpduImpl createSmsSubmitTpdu(boolean rejectDuplicates, boolean replyPathExists, boolean statusReportRequest,
            int messageReference, AddressFieldImpl destinationAddress, ProtocolIdentifierImpl protocolIdentifier,
            ValidityPeriodImpl validityPeriod, UserDataImpl userData) {
        return new SmsSubmitTpduImpl(rejectDuplicates, replyPathExists, statusReportRequest, messageReference,
                destinationAddress, protocolIdentifier, validityPeriod, userData);
    }

    public AbsoluteTimeStampImpl createAbsoluteTimeStamp(int year, int month, int day, int hour, int minute, int second,
            int timeZone) {
        return new AbsoluteTimeStampImpl(year, month, day, hour, minute, second, timeZone);
    }

    public AddressFieldImpl createAddressField(TypeOfNumber typeOfNumber, NumberingPlanIdentification numberingPlanIdentification,
            String addressValue) {
        return new AddressFieldImpl(typeOfNumber, numberingPlanIdentification, addressValue);
    }

    public CommandTypeImpl createCommandType(int code) {
        return new CommandTypeImpl(code);
    }

    public CommandTypeImpl createCommandType(CommandTypeValue value) {
        return new CommandTypeImpl(value);
    }

    public DataCodingSchemeImpl createDataCodingScheme(int code) {
        return new DataCodingSchemeImpl(code);
    }

    public DataCodingSchemeImpl createDataCodingScheme(DataCodingGroup dataCodingGroup, DataCodingSchemaMessageClass messageClass,
            DataCodingSchemaIndicationType dataCodingSchemaIndicationType, Boolean setIndicationActive,
            CharacterSet characterSet, boolean isCompressed) {
        return new DataCodingSchemeImpl(dataCodingGroup, messageClass, dataCodingSchemaIndicationType, setIndicationActive,
                characterSet, isCompressed);
    }

    public FailureCauseImpl createFailureCause(int code) {
        return new FailureCauseImpl(code);
    }

    public ParameterIndicator createParameterIndicator(boolean TP_UDLPresence, boolean getTP_DCSPresence,
            boolean getTP_PIDPresence) {
        return new ParameterIndicatorImpl(TP_UDLPresence, getTP_DCSPresence, getTP_PIDPresence);
    }

    public ProtocolIdentifierImpl createProtocolIdentifier(int code) {
        return new ProtocolIdentifierImpl(code);
    }

    public StatusImpl createStatus(int code) {
        return new StatusImpl(code);
    }

    public ValidityEnhancedFormatDataImpl createValidityEnhancedFormatData(byte[] data) {
        return new ValidityEnhancedFormatDataImpl(data);
    }

    public ValidityPeriodImpl createValidityPeriod(int relativeFormatValue) {
        return new ValidityPeriodImpl(relativeFormatValue);
    }

    public ValidityPeriodImpl createValidityPeriod(AbsoluteTimeStampImpl absoluteFormatValue) {
        return new ValidityPeriodImpl(absoluteFormatValue);
    }

    public ValidityPeriodImpl createValidityPeriod(ValidityEnhancedFormatDataImpl enhancedFormatValue) {
        return new ValidityPeriodImpl(enhancedFormatValue);
    }

    public UserDataHeaderImpl createUserDataHeader() {
        return new UserDataHeaderImpl();
    }

    public UserDataHeaderImpl createUserDataHeader(byte[] encodedData) {
        return new UserDataHeaderImpl(encodedData);
    }

    public UserDataImpl createUserData(byte[] encodedData, DataCodingSchemeImpl dataCodingScheme, int encodedUserDataLength,
            boolean encodedUserDataHeaderIndicator, Charset gsm8Charset) {
        return new UserDataImpl(encodedData, dataCodingScheme, encodedUserDataLength, encodedUserDataHeaderIndicator,
                gsm8Charset);
    }

    public UserDataImpl createUserData(String decodedMessage, DataCodingSchemeImpl dataCodingScheme,
            UserDataHeaderImpl decodedUserDataHeader, Charset gsm8Charset) {
        return new UserDataImpl(decodedMessage, dataCodingScheme, decodedUserDataHeader, gsm8Charset);
    }

    public CommandDataImpl createCommandData(byte[] data) {
        return new CommandDataImpl(data);
    }

    public CommandDataImpl createCommandData(String decodedMessage) {
        return new CommandDataImpl(decodedMessage);
    }

    public ConcatenatedShortMessagesIdentifierImpl createConcatenatedShortMessagesIdentifier(boolean referenceIs16bit,
            int reference, int mesageSegmentCount, int mesageSegmentNumber) {
        return new ConcatenatedShortMessagesIdentifierImpl(referenceIs16bit, reference, mesageSegmentCount, mesageSegmentNumber);
    }

    public NationalLanguageLockingShiftIdentifierImpl createNationalLanguageLockingShiftIdentifier(
            NationalLanguageIdentifier nationalLanguageCode) {
        return new NationalLanguageLockingShiftIdentifierImpl(nationalLanguageCode);
    }

    public NationalLanguageSingleShiftIdentifierImpl createNationalLanguageSingleShiftIdentifier(
            NationalLanguageIdentifier nationalLanguageCode) {
        return new NationalLanguageSingleShiftIdentifierImpl(nationalLanguageCode);
    }
}
