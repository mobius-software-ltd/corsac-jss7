/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;

/**
 * Base class of MAP ReturnError messages
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 *
 */
@ASNWrappedTag
public abstract class BooleanMAPErrorMessage1Impl extends MAPErrorMessageImpl {
	protected Integer errorCode;

	private ASNBoolean value;
		
    protected BooleanMAPErrorMessage1Impl(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public BooleanMAPErrorMessage1Impl() {
    }

    protected void setValue(Boolean value) {
    	if(value==null)
    		this.value=null;
    	else
    		this.value=new ASNBoolean(value,"Error",true,true);    	
    }
    
    protected Boolean getValue() {
    	if(this.value==null)
    		return null;
    	
    	return value.getValue();
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
}
