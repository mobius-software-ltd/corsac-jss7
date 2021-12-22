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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall;

/**
 *
 Cancel ::= OPERATION
ARGUMENT CancelArg
ERRORS {
	CancelFailed
}
-- Direction: SCF -> SRF or SCF -> SSF, Timer: Tcan -- This generic operation cancels the correlated previous operation or all previous requests. The
-- following operations can be cancelled:
-- PlayAnnouncement and PromptAndCollectUserInformation.

CancelArg ::= CHOICE {
	invokeID [0] InvokeID,
	allRequests [1] NULL
}
-- The InvokeID has the same value as that which was used for the operation to be cancelled.
 *
 * @author yulian.oifa
 *
 */
public interface CancelRequest extends CircuitSwitchedCallMessage {

    Integer getInvokeID();

    boolean getAllRequests();
}