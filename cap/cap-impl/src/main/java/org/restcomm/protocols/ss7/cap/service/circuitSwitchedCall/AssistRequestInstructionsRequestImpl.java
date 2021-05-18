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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.isup.DigitsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilitiesImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AssistRequestInstructionsRequestImpl extends CircuitSwitchedCallMessageImpl implements
        AssistRequestInstructionsRequest {
	private static final long serialVersionUID = 1L;

	private DigitsImpl correlationID;
    private IPSSPCapabilitiesImpl iPSSPCapabilities;
    private CAPExtensionsImpl extensions;

    public AssistRequestInstructionsRequestImpl() {
    }

    public AssistRequestInstructionsRequestImpl(DigitsImpl correlationID, IPSSPCapabilitiesImpl ipSSPCapabilities,
            CAPExtensionsImpl extensions) {
        this.correlationID = correlationID;
        this.iPSSPCapabilities = ipSSPCapabilities;
        this.extensions = extensions;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.assistRequestInstructions_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.assistRequestInstructions;
    }

    @Override
    public DigitsImpl getCorrelationID() {
        return correlationID;
    }

    @Override
    public IPSSPCapabilitiesImpl getIPSSPCapabilities() {
        return iPSSPCapabilities;
    }

    @Override
    public CAPExtensionsImpl getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AssistRequestInstructionsRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.correlationID != null) {
            sb.append(", correlationID=");
            sb.append(correlationID.toString());
        }
        if (this.iPSSPCapabilities != null) {
            sb.append(", iPSSPCapabilities=");
            sb.append(iPSSPCapabilities.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
