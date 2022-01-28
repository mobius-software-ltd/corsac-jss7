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
package org.restcomm.protocols.ss7.inap.api.errors;

import io.netty.buffer.ByteBuf;

/**
 * The factory of INAP ReturnError messages
 *
 * @author yulian.oifa
 *
 */
public interface INAPErrorMessageFactory {
	/**
     * Generate the empty message depends of the error code (for incoming messages)
     * For improperCallerResponseParameter CS1+ flavour please use createINAPErrorMessageImproperCallerResponseCS1Plus
     * Since its impossible to differentiate between ImproperCallerResponse and executionError only by errorCode
     * For standard errors please use this method with isCS1Plus=false , even if you create the error for CS1+
     * @param errorCode
     * @return
     */
    INAPErrorMessage createMessageFromErrorCode(Long errorCode,Boolean isCS1Plus);

    INAPErrorMessageParameterless createINAPErrorMessageParameterless(Long errorCode);

    INAPErrorMessageCancelFailed createINAPErrorMessageCancelFailed(CancelProblem cancelProblem);

    INAPErrorMessageRequestedInfoError createINAPErrorMessageRequestedInfoError(
            RequestedInfoErrorParameter requestedInfoErrorParameter);

    INAPErrorMessageSystemFailure createINAPErrorMessageSystemFailure(UnavailableNetworkResource unavailableNetworkResource);

    INAPErrorMessageTaskRefused createINAPErrorMessageTaskRefused(TaskRefusedParameter taskRefusedParameter);
    
    INAPErrorMessageImproperCallerResponseCS1Plus createINAPErrorMessageImproperCallerResponseCS1Plus(ImproperCallerResponseParameter improperCallerResponseParameter);
    
    INAPErrorMessageOctetString createINAPErrorMessageOctetString(Long errorCode,ByteBuf parameter);
}
