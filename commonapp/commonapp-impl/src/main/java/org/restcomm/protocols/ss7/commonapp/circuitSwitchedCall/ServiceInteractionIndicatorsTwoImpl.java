/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BackwardServiceInteractionInd;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConnectedNumberTreatmentInd;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CwTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.EctTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ForwardServiceInteractionInd;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.HoldTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BothwayThroughConnectionInd;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNBothwayThroughConnectionInd;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ServiceInteractionIndicatorsTwoImpl implements ServiceInteractionIndicatorsTwo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1,defaultImplementation = ForwardServiceInteractionIndImpl.class)
    private ForwardServiceInteractionInd forwardServiceInteractionInd;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = BackwardServiceInteractionIndImpl.class)
    private BackwardServiceInteractionInd backwardServiceInteractionInd;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNBothwayThroughConnectionInd bothwayThroughConnectionInd;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
    private ASNConnectedNumberTreatmentIndicatorImpl connectedNumberTreatmentInd;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = false,index = -1)
    private ASNNull nonCUGCall;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1)
    private ASNHoldTreatmentIndicatorImpl holdTreatmentIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = false,index = -1)
    private ASNCwTreatmentIndicatorImpl cwTreatmentIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = false,index = -1)
    private ASNEctTreatmentIndicatorImpl ectTreatmentIndicator;

    public ServiceInteractionIndicatorsTwoImpl() {
    }

    public ServiceInteractionIndicatorsTwoImpl(ForwardServiceInteractionInd forwardServiceInteractionInd,
            BackwardServiceInteractionInd backwardServiceInteractionInd,
            BothwayThroughConnectionInd bothwayThroughConnectionInd, ConnectedNumberTreatmentInd connectedNumberTreatmentInd,
            boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator, CwTreatmentIndicator cwTreatmentIndicator,
            EctTreatmentIndicator ectTreatmentIndicator) {
        
    	this.forwardServiceInteractionInd = forwardServiceInteractionInd;
        this.backwardServiceInteractionInd = backwardServiceInteractionInd;
        
        if(bothwayThroughConnectionInd!=null)
        	this.bothwayThroughConnectionInd = new ASNBothwayThroughConnectionInd(bothwayThroughConnectionInd);
        	
        if(connectedNumberTreatmentInd!=null)
        	this.connectedNumberTreatmentInd = new ASNConnectedNumberTreatmentIndicatorImpl(connectedNumberTreatmentInd);
        	
        if(nonCUGCall)
        	this.nonCUGCall = new ASNNull();
        
        if(holdTreatmentIndicator!=null)
        	this.holdTreatmentIndicator = new ASNHoldTreatmentIndicatorImpl(holdTreatmentIndicator);
        	
        if(cwTreatmentIndicator!=null)
        	this.cwTreatmentIndicator = new ASNCwTreatmentIndicatorImpl(cwTreatmentIndicator);
        	
        if(ectTreatmentIndicator!=null)
        	this.ectTreatmentIndicator = new ASNEctTreatmentIndicatorImpl(ectTreatmentIndicator);        	
    }

    public ForwardServiceInteractionInd getForwardServiceInteractionInd() {
        return forwardServiceInteractionInd;
    }

    public BackwardServiceInteractionInd getBackwardServiceInteractionInd() {
        return backwardServiceInteractionInd;
    }

    public BothwayThroughConnectionInd getBothwayThroughConnectionInd() {
    	if(bothwayThroughConnectionInd==null)
    		return null;
    	
        return bothwayThroughConnectionInd.getType();
    }

    public ConnectedNumberTreatmentInd getConnectedNumberTreatmentInd() {
    	if(connectedNumberTreatmentInd==null)
    		return null;
    	
        return connectedNumberTreatmentInd.getType();
    }

    public boolean getNonCUGCall() {
        return nonCUGCall!=null;
    }

    public HoldTreatmentIndicator getHoldTreatmentIndicator() {
    	if(holdTreatmentIndicator==null)
    		return null;
    	
        return holdTreatmentIndicator.getType();
    }

    public CwTreatmentIndicator getCwTreatmentIndicator() {
    	if(cwTreatmentIndicator==null)
    		return null;
    	
        return cwTreatmentIndicator.getType();
    }

    public EctTreatmentIndicator getEctTreatmentIndicator() {
    	if(ectTreatmentIndicator==null)
    		return null;
    	
        return ectTreatmentIndicator.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ServiceInteractionIndicatorsTwo [");

        if (this.forwardServiceInteractionInd != null) {
            sb.append("forwardServiceInteractionInd=");
            sb.append(forwardServiceInteractionInd.toString());
        }
        if (this.backwardServiceInteractionInd != null) {
            sb.append(", backwardServiceInteractionInd=");
            sb.append(backwardServiceInteractionInd.toString());
        }
        if (this.bothwayThroughConnectionInd != null && this.bothwayThroughConnectionInd.getType()!=null) {
            sb.append(", bothwayThroughConnectionInd=");
            sb.append(bothwayThroughConnectionInd.getType());
        }
        if (this.connectedNumberTreatmentInd != null && this.connectedNumberTreatmentInd.getType()!=null) {
            sb.append(", connectedNumberTreatmentInd=");
            sb.append(connectedNumberTreatmentInd.getType());
        }
        if (this.nonCUGCall!=null) {
            sb.append(", nonCUGCall");
        }
        if (this.holdTreatmentIndicator != null && this.holdTreatmentIndicator.getType()!=null) {
            sb.append(", holdTreatmentIndicator=");
            sb.append(holdTreatmentIndicator.getType());
        }
        if (this.cwTreatmentIndicator != null && this.cwTreatmentIndicator.getType()!=null) {
            sb.append(", cwTreatmentIndicator=");
            sb.append(cwTreatmentIndicator.getType());
        }
        if (this.ectTreatmentIndicator != null && this.ectTreatmentIndicator.getType()!=null) {
            sb.append(", ectTreatmentIndicator=");
            sb.append(ectTreatmentIndicator.getType());
        }

        sb.append("]");

        return sb.toString();
    }
}
