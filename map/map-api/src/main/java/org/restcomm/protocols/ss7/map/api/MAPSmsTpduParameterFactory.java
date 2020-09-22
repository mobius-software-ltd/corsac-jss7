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

package org.restcomm.protocols.ss7.map.api;

import java.nio.charset.Charset;

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
 * @author sergey vetyutnev
 *
 */
public interface MAPSmsTpduParameterFactory {

    SmsCommandTpduImpl createSmsCommandTpdu(boolean statusReportRequest, int messageReference,
            ProtocolIdentifierImpl protocolIdentifier, CommandTypeImpl commandType, int messageNumber, AddressFieldImpl destinationAddress,
            CommandDataImpl commandData);

    SmsDeliverReportTpduImpl createSmsDeliverReportTpdu(FailureCauseImpl failureCause, ProtocolIdentifierImpl protocolIdentifier,
            UserDataImpl userData);

    SmsDeliverTpduImpl createSmsDeliverTpdu(boolean moreMessagesToSend, boolean forwardedOrSpawned, boolean replyPathExists,
            boolean statusReportIndication, AddressFieldImpl originatingAddress, ProtocolIdentifierImpl protocolIdentifier,
            AbsoluteTimeStampImpl serviceCentreTimeStamp, UserDataImpl userData);

    SmsStatusReportTpduImpl createSmsStatusReportTpdu(boolean moreMessagesToSend, boolean forwardedOrSpawned,
            StatusReportQualifier statusReportQualifier, int messageReference, AddressFieldImpl recipientAddress,
            AbsoluteTimeStampImpl serviceCentreTimeStamp, AbsoluteTimeStampImpl dischargeTime, StatusImpl status,
            ProtocolIdentifierImpl protocolIdentifier, UserDataImpl userData);

    SmsSubmitReportTpduImpl createSmsSubmitReportTpdu(FailureCauseImpl failureCause, AbsoluteTimeStampImpl serviceCentreTimeStamp,
            ProtocolIdentifierImpl protocolIdentifier, UserDataImpl userData);

    SmsSubmitTpduImpl createSmsSubmitTpdu(boolean rejectDuplicates, boolean replyPathExists, boolean statusReportRequest,
            int messageReference, AddressFieldImpl destinationAddress, ProtocolIdentifierImpl protocolIdentifier,
            ValidityPeriodImpl validityPeriod, UserDataImpl userData);

    AbsoluteTimeStampImpl createAbsoluteTimeStamp(int year, int month, int day, int hour, int minute, int second,
            int timeZone);

    AddressFieldImpl createAddressField(TypeOfNumber typeOfNumber, NumberingPlanIdentification numberingPlanIdentification,
            String addressValue);

    CommandTypeImpl createCommandType(int code);

    CommandTypeImpl createCommandType(CommandTypeValue value);

    DataCodingSchemeImpl createDataCodingScheme(int code);

    DataCodingSchemeImpl createDataCodingScheme(DataCodingGroup dataCodingGroup, DataCodingSchemaMessageClass messageClass,
            DataCodingSchemaIndicationType dataCodingSchemaIndicationType, Boolean setIndicationActive,
            CharacterSet characterSet, boolean isCompressed);

    FailureCauseImpl createFailureCause(int code);

    ParameterIndicator createParameterIndicator(boolean TP_UDLPresence, boolean getTP_DCSPresence,
            boolean getTP_PIDPresence);

    ProtocolIdentifierImpl createProtocolIdentifier(int code);

    StatusImpl createStatus(int code);

    ValidityEnhancedFormatDataImpl createValidityEnhancedFormatData(byte[] data);

    ValidityPeriodImpl createValidityPeriod(int relativeFormatValue);

    ValidityPeriodImpl createValidityPeriod(AbsoluteTimeStampImpl absoluteFormatValue);

    ValidityPeriodImpl createValidityPeriod(ValidityEnhancedFormatDataImpl enhancedFormatValue);

    UserDataHeaderImpl createUserDataHeader();

    UserDataHeaderImpl createUserDataHeader(byte[] encodedData);

    UserDataImpl createUserData(byte[] encodedData, DataCodingSchemeImpl dataCodingScheme, int encodedUserDataLength,
            boolean encodedUserDataHeaderIndicator, Charset gsm8Charset);

    UserDataImpl createUserData(String decodedMessage, DataCodingSchemeImpl dataCodingScheme,
            UserDataHeaderImpl decodedUserDataHeader, Charset gsm8Charset);

    CommandDataImpl createCommandData(byte[] data);

    CommandDataImpl createCommandData(String decodedMessage);

    ConcatenatedShortMessagesIdentifierImpl createConcatenatedShortMessagesIdentifier(boolean referenceIs16bit,
            int reference, int mesageSegmentCount, int mesageSegmentNumber);

    NationalLanguageLockingShiftIdentifierImpl createNationalLanguageLockingShiftIdentifier(
            NationalLanguageIdentifier nationalLanguageCode);

    NationalLanguageSingleShiftIdentifierImpl createNationalLanguageSingleShiftIdentifier(
            NationalLanguageIdentifier nationalLanguageCode);
}
