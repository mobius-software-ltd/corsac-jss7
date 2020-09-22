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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

/**
 * MAP V3:
 *
 * provideSubscriberLocation OPERATION ::= { --Timer ml ARGUMENT ProvideSubscriberLocation-Arg RESULT
 * ProvideSubscriberLocation-Res ERRORS { systemFailure | dataMissing | unexpectedDataValue | facilityNotSupported |
 * unidentifiedSubscriber | illegalSubscriber | illegalEquipment | absentSubscriber | unauthorizedRequestingNetwork |
 * unauthorizedLCSClient | positionMethodFailure } CODE local:83 }
 *
 * ProvideSubscriberLocation-Arg ::= SEQUENCE { locationType LocationType, mlc-Number ISDN-AddressStringImpl, lcs-ClientID [0]
 * LCS-ClientID OPTIONAL, privacyOverride [1] NULL OPTIONAL, imsi [2] IMSI OPTIONAL, msisdn [3] ISDN-AddressStringImpl OPTIONAL,
 * lmsi [4] LMSI OPTIONAL, imei [5] IMEI OPTIONAL, lcs-Priority [6] LCS-Priority OPTIONAL, lcs-QoS [7] LCS-QoS OPTIONAL,
 * extensionContainer [8] ExtensionContainer OPTIONAL, ... , supportedGADShapes [9] SupportedGADShapes OPTIONAL,
 * lcs-ReferenceNumber [10] LCS-ReferenceNumber OPTIONAL, lcsServiceTypeID [11] LCSServiceTypeID OPTIONAL, lcsCodeword [12]
 * LCSCodeword OPTIONAL, lcs-PrivacyCheck [13] LCS-PrivacyCheck OPTIONAL, areaEventInfo [14] AreaEventInfo OPTIONAL,
 * h-gmlc-Address [15] GSN-Address OPTIONAL, mo-lrShortCircuitIndicator [16] NULL OPTIONAL, periodicLDRInfo [17] PeriodicLDRInfo
 * OPTIONAL, reportingPLMNList [18] ReportingPLMNList OPTIONAL }
 *
 * -- one of imsi or msisdn is mandatory -- If a location estimate type indicates activate deferred location or cancel deferred
 * -- location, a lcs-Reference number shall be included.
 *
 *
 * @author amit bhayani
 *
 */
public interface ProvideSubscriberLocationRequest extends LsmMessage {

    /**
     *
     * @return
     */
    LocationTypeImpl getLocationType();

    ISDNAddressStringImpl getMlcNumber();

    LCSClientIDImpl getLCSClientID();

    boolean getPrivacyOverride();

    IMSIImpl getIMSI();

    ISDNAddressStringImpl getMSISDN();

    LMSIImpl getLMSI();

    /**
     * LCS-Priority ::= OCTET STRING (SIZE (1)) -- 0 = highest priority -- 1 = normal priority -- all other values treated as 1
     *
     * @return
     */
    LCSPriority getLCSPriority();

    LCSQoSImpl getLCSQoS();

    IMEIImpl getIMEI();

    MAPExtensionContainerImpl getExtensionContainer();

    /**
     * SupportedGADShapes ::= BIT STRING { ellipsoidPoint (0), ellipsoidPointWithUncertaintyCircle (1),
     * ellipsoidPointWithUncertaintyEllipse (2), polygon (3), ellipsoidPointWithAltitude (4),
     * ellipsoidPointWithAltitudeAndUncertaintyElipsoid (5), ellipsoidArc (6) } (SIZE (7..16)) -- A node shall mark in the BIT
     * STRING all Shapes defined in 3GPP TS 23.032 it supports. -- exception handling: bits 7 to 15 shall be ignored if
     * received.
     *
     * @return
     */
    SupportedGADShapesImpl getSupportedGADShapes();

    /**
     * LCS-ReferenceNumber::= OCTET STRING (SIZE(1))
     *
     * @return
     */
    Integer getLCSReferenceNumber();

    LCSCodewordImpl getLCSCodeword();

    /**
     * LCSServiceTypeID ::= INTEGER (0..127) -- the integer values 0-63 are reserved for Standard LCS service types -- the
     * integer values 64-127 are reserved for Non Standard LCS service types
     *
     * @return
     */
    Integer getLCSServiceTypeID();

    LCSPrivacyCheckImpl getLCSPrivacyCheck();

    AreaEventInfoImpl getAreaEventInfo();

    /**
     * GSN-Address ::= OCTET STRING (SIZE (5..17)) -- Octets are coded according to TS 3GPP TS 23.003 [17]
     *
     * @return
     */
    GSNAddressImpl getHGMLCAddress();

    boolean getMoLrShortCircuitIndicator();

    PeriodicLDRInfoImpl getPeriodicLDRInfo();

    ReportingPLMNListImpl getReportingPLMNList();

}
