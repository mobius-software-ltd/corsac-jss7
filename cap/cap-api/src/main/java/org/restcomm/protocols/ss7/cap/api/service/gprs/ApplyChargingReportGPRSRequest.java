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

package org.restcomm.protocols.ss7.cap.api.service.gprs;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;

/**
 *
 applyChargingReportGPRS OPERATION ::= { ARGUMENT ApplyChargingReportGPRSArg RETURN RESULT TRUE ERRORS {missingParameter |
 * unexpectedComponentSequence | unexpectedParameter | unexpectedDataValue | parameterOutOfRange | systemFailure | taskRefused |
 * unknownPDPID} CODE opcode-applyChargingReportGPRS} -- Direction gprsSSF -> gsmSCF,Timer Tacrg -- The ApplyChargingReportGPRS
 * operation provides the feedback from the gprsSCF to the gsmSCF -- CSE-controlled GPRS session charging mechanism.
 *
 * ApplyChargingReportGPRSArg ::= SEQUENCE { chargingResult [0] ChargingResult, qualityOfService [1] QualityOfService OPTIONAL,
 * active [2] BOOLEAN DEFAULT TRUE, pDPID [3] PDPID OPTIONAL, ..., chargingRollOver [4] ChargingRollOver OPTIONAL }
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ApplyChargingReportGPRSRequest extends GprsMessage {

    ChargingResult getChargingResult();

    QualityOfService getQualityOfService();

    boolean getActive();

    PDPID getPDPID();

    ChargingRollOver getChargingRollOver();
}