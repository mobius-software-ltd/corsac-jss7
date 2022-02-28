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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AudibleIndicator;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author alerant appngin
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TimeDurationChargingImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger maxCallPeriodDuration;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = ReleaseIfDurationExceededImpl.class)
    private ReleaseIfDurationExceededImpl releaseIfDurationExceeded;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNBoolean releaseIfdurationExceededB;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNInteger tariffSwitchInterval;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private ASNBoolean tone;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private AudibleIndicatorWrapperImpl audibleIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public TimeDurationChargingImpl() {
    }

    //V2
    public TimeDurationChargingImpl(long maxCallPeriodDuration, Boolean tone, CAPINAPExtensions extensions,
            Long tariffSwitchInterval) {
        this.maxCallPeriodDuration = new ASNInteger(maxCallPeriodDuration,"MaxCallPeriodDuration",1L,864000L,false);

        if(tone!=null || extensions!=null)
        	this.releaseIfDurationExceeded = new ReleaseIfDurationExceededImpl(tone, extensions);
        
        if(tariffSwitchInterval!=null)
        	this.tariffSwitchInterval = new ASNInteger(tariffSwitchInterval,"TariffSwitchInterval",1L,86400L,false);        	
    }
    
    //V3
    public TimeDurationChargingImpl(long maxCallPeriodDuration, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, Boolean tone, CAPINAPExtensions extensions) {
    	this.maxCallPeriodDuration = new ASNInteger(maxCallPeriodDuration,"MaxCallPeriodDuration",1L,864000L,false);
        
        if(releaseIfdurationExceeded)
        	this.releaseIfdurationExceededB = new ASNBoolean(releaseIfdurationExceeded,"ReleaseIfdurationExceeded",true,false);
        	
        if(tone!=null)
        	this.tone = new ASNBoolean(tone,"Tone",true,false);
            
        if(tariffSwitchInterval!=null)
        	this.tariffSwitchInterval = new ASNInteger(tariffSwitchInterval,"TariffSwitchInterval",1L,86400L,false);
        	
        this.extensions = extensions;        
    }
    
    //V4
    public TimeDurationChargingImpl(long maxCallPeriodDuration, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, AudibleIndicator audibleIndicator, CAPINAPExtensions extensions) {
    	this.maxCallPeriodDuration = new ASNInteger(maxCallPeriodDuration,"MaxCallPeriodDuration",1L,864000L,false);
        
        if(releaseIfdurationExceeded)
        	this.releaseIfdurationExceededB = new ASNBoolean(releaseIfdurationExceeded,"ReleaseIfdurationExceeded",true,false);
        	
        if(tariffSwitchInterval!=null)
        	this.tariffSwitchInterval = new ASNInteger(tariffSwitchInterval,"TariffSwitchInterval",1L,86400L,false);
        	
        if(audibleIndicator!=null)
        	this.audibleIndicator = new AudibleIndicatorWrapperImpl(audibleIndicator);
        
        this.extensions = extensions;
    }

    public long getMaxCallPeriodDuration() {
    	if(maxCallPeriodDuration==null || maxCallPeriodDuration.getValue()==null)
    		return 0;
    	
        return maxCallPeriodDuration.getValue();
    }

    public boolean getReleaseIfdurationExceeded() {
    	if(releaseIfDurationExceeded!=null)
    		return true;
    	
    	if(releaseIfdurationExceededB==null || releaseIfdurationExceededB.getValue()==null)
    		return false;
    	
        return releaseIfdurationExceededB.getValue();
    }

    public Long getTariffSwitchInterval() {
    	if(tariffSwitchInterval==null)
    		return null;
    	
        return tariffSwitchInterval.getValue();
    }

    public AudibleIndicator getAudibleIndicator() {
    	if(audibleIndicator!=null && audibleIndicator.getAudibleIndicator()!=null)
    		return audibleIndicator.getAudibleIndicator();
    	else if(tone!=null && tone.getValue()!=null)
    		return new AudibleIndicatorImpl(tone.getValue());
    	else if(releaseIfDurationExceeded!=null && releaseIfDurationExceeded.getTone()!=null)
    		return new AudibleIndicatorImpl(releaseIfDurationExceeded.getTone());
    	
    	return null;
    }

    public CAPINAPExtensions getExtensions() {
    	if(releaseIfDurationExceeded!=null && releaseIfDurationExceeded.getExtensions()!=null)
    		return releaseIfDurationExceeded.getExtensions();
    	
        return extensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CAMELAChBillingChargingCharacteristics [");

        if(this.maxCallPeriodDuration!=null && this.maxCallPeriodDuration.getValue()!=null) {
        	sb.append(", maxCallPeriodDuration=");
        	sb.append(this.maxCallPeriodDuration.getValue());
        }
        
        if (this.releaseIfDurationExceeded != null) {
            sb.append(", releaseIfDurationExceeded=");
            sb.append(releaseIfDurationExceeded);
        }
        if (this.releaseIfdurationExceededB!=null && this.releaseIfdurationExceededB.getValue()!=null) {
            sb.append(", releaseIfdurationExceeded = " + this.releaseIfdurationExceededB.getValue());
        }
        if (this.tariffSwitchInterval != null && this.tariffSwitchInterval.getValue()!=null) {
            sb.append(", tariffSwitchInterval=");
            sb.append(tariffSwitchInterval.getValue());
        }
        if (this.audibleIndicator != null && this.audibleIndicator.getAudibleIndicator()!=null) {
            sb.append(", audibleIndicator=");
            sb.append(audibleIndicator.getAudibleIndicator());
        }
        if (this.tone!=null && this.tone.getValue()!=null) {
            sb.append(", tone = " + this.tone.getValue());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(maxCallPeriodDuration==null)
			throw new ASNParsingComponentException("max call perion duration should be set for time duration charging", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
