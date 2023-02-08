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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriberSM;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageBusySubscriber;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageCUGReject;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageExtensionContainer;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageFacilityNotSup;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageParameterless;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessagePositionMethodFailure;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessagePwRegistrationFailure;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageRoamingNotAllowed;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSsErrorStatus;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSsIncompatibility;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSubscriberBusyForMtSms;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageUnauthorizedLCSClient;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageUnknownSubscriber;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNEnumerated;

/**
 * Base class of MAP ReturnError messages
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNWrappedTag
public abstract class EnumeratedMAPErrorMessage1Impl extends MAPErrorMessageImpl {
	protected Integer errorCode;

	private ASNEnumerated value;
	private String name;
	private Integer minValue;
	private Integer maxValue;
	
    protected EnumeratedMAPErrorMessage1Impl(Integer errorCode,String name,Integer minValue,Integer maxValue) {
        this.errorCode = errorCode;
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public EnumeratedMAPErrorMessage1Impl() {
    }

    protected Integer getValue() {
    	if(this.value==null)
    		return null;
    	
		return value.getIntValue();
	}

    protected void setValue(Integer value) {
    	if(this.value==null)
    		this.value=new ASNEnumerated(value,name,minValue,maxValue,true);    	
	}
	
    public Integer getErrorCode() {
        return errorCode;
    }

    public boolean isEmParameterless() {
        return false;
    }

    public boolean isEmExtensionContainer() {
        return false;
    }

    public boolean isEmFacilityNotSup() {
        return false;
    }

    public boolean isEmSMDeliveryFailure() {
        return false;
    }

    public boolean isEmSystemFailure() {
        return false;
    }

    public boolean isEmUnknownSubscriber() {
        return false;
    }

    public boolean isEmAbsentSubscriberSM() {
        return false;
    }

    public boolean isEmAbsentSubscriber() {
        return false;
    }

    public boolean isEmSubscriberBusyForMtSms() {
        return false;
    }

    public boolean isEmCallBarred() {
        return false;
    }

    public boolean isEmUnauthorizedLCSClient() {
        return false;
    }

    public boolean isEmPositionMethodFailure() {
        return false;
    }

    public boolean isEmBusySubscriber() {
        return false;
    }

    public boolean isEmCUGReject() {
        return false;
    }

    public boolean isEmRoamingNotAllowed() {
        return false;
    }

    public boolean isEmSsErrorStatus() {
        return false;
    }

    public boolean isEmSsIncompatibility() {
        return false;
    }

    public boolean isEmPwRegistrationFailure() {
        return false;
    }

    public MAPErrorMessageParameterless getEmParameterless() {
        return null;
    }

    public MAPErrorMessageExtensionContainer getEmExtensionContainer() {
        return null;
    }

    public MAPErrorMessageFacilityNotSup getEmFacilityNotSup() {
        return null;
    }

    public MAPErrorMessageSMDeliveryFailure getEmSMDeliveryFailure() {
        return null;
    }

    public MAPErrorMessageSystemFailure getEmSystemFailure() {
        return null;
    }

    public MAPErrorMessageUnknownSubscriber getEmUnknownSubscriber() {
        return null;
    }

    public MAPErrorMessageAbsentSubscriberSM getEmAbsentSubscriberSM() {
        return null;
    }

    public MAPErrorMessageAbsentSubscriber getEmAbsentSubscriber() {
        return null;
    }

    public MAPErrorMessageSubscriberBusyForMtSms getEmSubscriberBusyForMtSms() {
        return null;
    }

    public MAPErrorMessageCallBarred getEmCallBarred() {
        return null;
    }

    public MAPErrorMessageUnauthorizedLCSClient getEmUnauthorizedLCSClient() {
        return null;
    }

    public MAPErrorMessagePositionMethodFailure getEmPositionMethodFailure() {
        return null;
    }

    public MAPErrorMessageBusySubscriber getEmBusySubscriber() {
        return null;
    }

    public MAPErrorMessageCUGReject getEmCUGReject() {
        return null;
    }

    public MAPErrorMessageRoamingNotAllowed getEmRoamingNotAllowed() {
        return null;
    }

    public MAPErrorMessageSsErrorStatus getEmSsErrorStatus() {
        return null;
    }

    public MAPErrorMessageSsIncompatibility getEmSsIncompatibility() {
        return null;
    }

    public MAPErrorMessagePwRegistrationFailure getEmPwRegistrationFailure() {
        return null;
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(value==null)
			throw new ASNParsingComponentException("value not set for " + name, ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
