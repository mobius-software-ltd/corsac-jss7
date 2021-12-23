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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

/**
 *
<code>
TriggerType ::= ENUMERATED {
	featureActivation(0),
	verticalServiceCode(1),
	customizedAccess(2),
	customizedIntercom(3),
	emergencyService(12),
	aFR(13),
	sharedIOTrunk(14),
	offHookDelay(17),
	channelSetupPRI(18),
	tNoAnswer(25),
	tBusy(26),
	oCalledPartyBusy(27),
	oNoAnswer(29),
	originationAttemptAuthorized(30),
	oAnswer(31),
	oDisconnect(32),
	termAttemptAuthorized(33),
	tAnswer(34),
	tDisconnect(35)
	-- Private (ffs)
}
-- The type of trigger which caused call suspension
-- 4-11: Reserved; 15,16: Reserved; 19-24: Reserved
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum TriggerType {
	featureActivation(0),
	verticalServiceCode(1),
	customizedAccess(2),
	customizedIntercom(3),
	emergencyService(12),
	aFR(13),
	sharedIOTrunk(14),
	offHookDelay(17),
	channelSetupPRI(18),
	tNoAnswer(25),
	tBusy(26),
	oCalledPartyBusy(27),
	oNoAnswer(29),
	originationAttemptAuthorized(30),
	oAnswer(31),
	oDisconnect(32),
	termAttemptAuthorized(33),
	tAnswer(34),
	tDisconnect(35);

    private int code;

    private TriggerType(int code) {
        this.code = code;
    }

    public static TriggerType getInstance(int code) {
        switch (code) {
            case 0:
                return TriggerType.featureActivation;
            case 1:
                return TriggerType.verticalServiceCode;
            case 2:
                return TriggerType.customizedAccess;
            case 3:
                return TriggerType.customizedIntercom;
            case 12:
                return TriggerType.emergencyService;
            case 13:
                return TriggerType.aFR;
            case 14:
                return TriggerType.sharedIOTrunk;
            case 17:
                return TriggerType.offHookDelay;
            case 18:
                return TriggerType.channelSetupPRI;
            case 25:
                return TriggerType.tNoAnswer;
            case 26:
                return TriggerType.tBusy;
            case 27:
                return TriggerType.oCalledPartyBusy;
            case 29:
                return TriggerType.oNoAnswer;
            case 30:
                return TriggerType.originationAttemptAuthorized;
            case 31:
                return TriggerType.oAnswer;
            case 32:
                return TriggerType.oDisconnect;
            case 33:
                return TriggerType.termAttemptAuthorized;
            case 34:
                return TriggerType.tAnswer;
            case 35:
                return TriggerType.tDisconnect;
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
