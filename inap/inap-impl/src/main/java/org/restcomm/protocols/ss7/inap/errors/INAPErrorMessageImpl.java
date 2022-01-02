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

package org.restcomm.protocols.ss7.inap.errors;

import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessage;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageCancelFailed;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageImproperCallerResponseCS1Plus;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageOctetString;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageParameterless;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageRequestedInfoError;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageTaskRefused;

/**
 * Base class of INAP ReturnError messages
 *
 * @author yulian.oifa
 *
 */
public abstract class INAPErrorMessageImpl implements INAPErrorMessage {
    protected Long errorCode;

    protected INAPErrorMessageImpl(Long errorCode) {
        this.errorCode = errorCode;
    }

    public INAPErrorMessageImpl() {
    }

    @Override
    public Long getErrorCode() {
        return errorCode;
    }

    @Override
    public boolean isEmParameterless() {
        return false;
    }

    @Override
    public boolean isEmCancelFailed() {
        return false;
    }

    @Override
    public boolean isEmRequestedInfoError() {
        return false;
    }

    @Override
    public boolean isEmSystemFailure() {
        return false;
    }

    @Override
    public boolean isEmTaskRefused() {
        return false;
    }

    @Override
    public boolean isImproperCallerResponseCs1Plus() {
    	return false;
    }

    @Override
    public boolean isEmOctetString() {
    	return false;
    }
    
    @Override
    public INAPErrorMessageParameterless getEmParameterless() {
        return null;
    }

    @Override
    public INAPErrorMessageCancelFailed getEmCancelFailed() {
        return null;
    }

    @Override
    public INAPErrorMessageRequestedInfoError getEmRequestedInfoError() {
        return null;
    }

    @Override
    public INAPErrorMessageSystemFailure getEmSystemFailure() {
        return null;
    }

    @Override
    public INAPErrorMessageTaskRefused getEmTaskRefused() {
        return null;
    }
    
    @Override
    public INAPErrorMessageImproperCallerResponseCS1Plus getImproperCallerResponseCS1Plus() {
    	return null;
    }

    @Override
    public INAPErrorMessageOctetString getEmOctetString() {
    	return null;
    }
}