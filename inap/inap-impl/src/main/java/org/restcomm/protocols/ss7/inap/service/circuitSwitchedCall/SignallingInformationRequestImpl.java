/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SignallingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardGVNS;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardSuppressionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ForwardSuppressionIndicators;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.BackwardSuppressionIndicatorsImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ForwardSuppressionIndicatorsmpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.BackwardGVNSImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class SignallingInformationRequestImpl extends CircuitSwitchedCallMessageImpl implements SignallingInformationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = BackwardSuppressionIndicatorsImpl.class)
    private BackwardSuppressionIndicators backwardSuppressionIndicators;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1, defaultImplementation = CalledPartyNumberIsupImpl.class)
    private CalledPartyNumberIsup connectedNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1, defaultImplementation = ForwardSuppressionIndicatorsmpl.class)
    private ForwardSuppressionIndicators forwardSuppressionIndicators;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1, defaultImplementation = BackwardGVNSImpl.class)
	private BackwardGVNS backwardGVNSIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
	/**
     * This constructor is only for deserialisation purpose
     */
    public SignallingInformationRequestImpl() {
    }

    public SignallingInformationRequestImpl(BackwardSuppressionIndicators backwardSuppressionIndicators, CalledPartyNumberIsup connectedNumber,
    		ForwardSuppressionIndicators forwardSuppressionIndicators,BackwardGVNS backwardGVNSIndicator,
    		CAPINAPExtensions extensions) {
    	
    	this.backwardSuppressionIndicators=backwardSuppressionIndicators;
    	this.connectedNumber=connectedNumber;
    	this.forwardSuppressionIndicators=forwardSuppressionIndicators;
    	this.backwardGVNSIndicator=backwardGVNSIndicator;
    	this.extensions=extensions;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.signallingInformation_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.signallingInformation;
    }

    @Override
    public BackwardSuppressionIndicators getBackwardSuppressionIndicators() {
		return backwardSuppressionIndicators;
	}

    @Override
    public CalledPartyNumberIsup getConnectedNumber() {
		return connectedNumber;
	}

    @Override
    public ForwardSuppressionIndicators getForwardSuppressionIndicators() {
		return forwardSuppressionIndicators;
	}

    @Override
    public BackwardGVNS getBackwardGVNSIndicator() {
		return backwardGVNSIndicator;
	}

    @Override
    public CAPINAPExtensions getExtensions() {
		return extensions;
	}

	@Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
        sb.append("SignallingInformationRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.backwardSuppressionIndicators != null) {
            sb.append(", backwardSuppressionIndicators=");
            sb.append(backwardSuppressionIndicators.toString());
        }
        if (this.connectedNumber != null) {
            sb.append(", connectedNumber=");
            sb.append(connectedNumber.toString());
        }
        if (this.forwardSuppressionIndicators != null) {
            sb.append(", forwardSuppressionIndicators=");
            sb.append(forwardSuppressionIndicators.toString());
        }
        if (this.backwardGVNSIndicator != null) {
            sb.append(", backwardGVNSIndicator=");
            sb.append(backwardGVNSIndicator.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        
        sb.append("]");

        return sb.toString();
    }
}