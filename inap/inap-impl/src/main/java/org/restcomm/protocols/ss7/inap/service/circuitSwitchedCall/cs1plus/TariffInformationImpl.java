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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.IntervalAccuracy;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.TariffInformation;
import org.restcomm.protocols.ss7.inap.primitives.DateAndTimeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TariffInformationImpl implements TariffInformation {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNInteger numberOfStartPulses;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNInteger startInterval;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1)
    private ASNIntervalAccuracy startIntervalAccuracy;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1)
    private ASNInteger numberOfPeriodicPulses;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false, index=-1)
    private ASNInteger periodicInterval;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false, index=-1)
    private ASNIntervalAccuracy periodicIntervalAccuracy;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false, index=-1,defaultImplementation = DateAndTimeImpl.class)
    private DateAndTime activationTime;

    public TariffInformationImpl() {
    }

    public TariffInformationImpl(Integer numberOfStartPulses,Integer startInterval,IntervalAccuracy startIntervalAccuracy,
    		Integer numberOfPeriodicPulses,Integer periodicInterval,IntervalAccuracy periodicIntervalAccuracy,DateAndTime activationTime) {
    	if(numberOfStartPulses!=null)
    		this.numberOfStartPulses=new ASNInteger(numberOfStartPulses,"NumberOfStartPulses",0,255,false);
    		
    	if(startInterval!=null)
    		this.startInterval=new ASNInteger(startInterval,"StartInterval",0,3276,false);
    		
    	if(startIntervalAccuracy!=null)
    		this.startIntervalAccuracy=new ASNIntervalAccuracy(startIntervalAccuracy);
    		
    	if(numberOfPeriodicPulses!=null)
    		this.numberOfPeriodicPulses=new ASNInteger(numberOfPeriodicPulses,"NumberOfPeriodicPulses",0,255,false);
    		
    	if(periodicInterval!=null)
    		this.periodicInterval=new ASNInteger(periodicInterval,"PeriodicInterval",0,3276,false);
    		
    	if(periodicIntervalAccuracy!=null)
    		this.periodicIntervalAccuracy=new ASNIntervalAccuracy(periodicIntervalAccuracy);
    		
    	this.activationTime=activationTime;    	
    }       

    public Integer getNumberOfStartPulses() {
    	if(numberOfStartPulses==null)
    		return null;
    	
    	return numberOfStartPulses.getIntValue();
    }      

    public Integer getStartInterval() {
    	if(startInterval==null)
    		return null;
    	
    	return startInterval.getIntValue();
    } 
    
    public IntervalAccuracy getStartIntervalAccuracy() {
    	if(startIntervalAccuracy==null)
    		return null;
    	
    	return startIntervalAccuracy.getType();
    }       

    public Integer getNumberOfPeriodicPulses() {
    	if(numberOfPeriodicPulses==null)
    		return null;
    	
    	return numberOfPeriodicPulses.getIntValue();
    }      

    public Integer getPeriodicInterval() {
    	if(periodicInterval==null)
    		return null;
    	
    	return periodicInterval.getIntValue();
    }
    
    public IntervalAccuracy getPeriodicIntervalAccuracy() {
    	if(periodicIntervalAccuracy==null)
    		return null;
    	
    	return periodicIntervalAccuracy.getType();
    } 

    public DateAndTime getActivationTime() {
    	return activationTime;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TariffInformation [");

        if (this.numberOfStartPulses != null && this.numberOfStartPulses.getValue()!=null) {
            sb.append(", numberOfStartPulses=");
            sb.append(numberOfStartPulses.getValue());
        }
        
        if (this.startInterval != null && this.startInterval.getValue()!=null) {
            sb.append(", startInterval=");
            sb.append(startInterval.getValue());
        }
        
        if (this.startIntervalAccuracy != null && this.startIntervalAccuracy.getType()!=null) {
            sb.append(", startIntervalAccuracy=");
            sb.append(startIntervalAccuracy.getType());
        }
        
        if (this.numberOfPeriodicPulses != null && this.numberOfPeriodicPulses.getValue()!=null) {
            sb.append(", numberOfPeriodicPulses=");
            sb.append(numberOfPeriodicPulses.getValue());
        }
        
        if (this.periodicInterval != null && this.periodicInterval.getValue()!=null) {
            sb.append(", periodicInterval=");
            sb.append(periodicInterval.getValue());
        }
        
        if (this.periodicIntervalAccuracy != null && this.periodicIntervalAccuracy.getType()!=null) {
            sb.append(", periodicIntervalAccuracy=");
            sb.append(periodicIntervalAccuracy.getType());
        }
        
        if (this.activationTime != null) {
            sb.append(", activationTime=");
            sb.append(activationTime);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(numberOfStartPulses==null)
			throw new ASNParsingComponentException("number of start pulses should be set for tariff information", ASNParsingComponentExceptionReason.MistypedParameter);

		if(startInterval==null)
			throw new ASNParsingComponentException("start interval should be set for tariff information", ASNParsingComponentExceptionReason.MistypedParameter);

		if(startIntervalAccuracy==null)
			throw new ASNParsingComponentException("start interval accuracy should be set for tariff information", ASNParsingComponentExceptionReason.MistypedParameter);

		if(numberOfPeriodicPulses==null)
			throw new ASNParsingComponentException("number of periodic pulses should be set for tariff information", ASNParsingComponentExceptionReason.MistypedParameter);

		if(periodicInterval==null)
			throw new ASNParsingComponentException("periodic interval should be set for tariff information", ASNParsingComponentExceptionReason.MistypedParameter);

		if(periodicIntervalAccuracy==null)
			throw new ASNParsingComponentException("periodic interval accuracy should be set for tariff information", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}