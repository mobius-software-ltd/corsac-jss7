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
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAMELAChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author alerant appngin
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public class CAMELAChBillingChargingCharacteristicsImpl implements CAMELAChBillingChargingCharacteristics {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private TimeDurationChargingImpl timeDurationCharging;

    public CAMELAChBillingChargingCharacteristicsImpl() {
    }

    //V2
    public CAMELAChBillingChargingCharacteristicsImpl(long maxCallPeriodDuration, Boolean tone, CAPINAPExtensions extensions,
            Long tariffSwitchInterval) {
    	this.timeDurationCharging = new TimeDurationChargingImpl(maxCallPeriodDuration,tone,extensions,tariffSwitchInterval);    	
    }
    
    //V3
    public CAMELAChBillingChargingCharacteristicsImpl(long maxCallPeriodDuration, Boolean tone, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, CAPINAPExtensions extensions) {
    	this.timeDurationCharging = new TimeDurationChargingImpl(maxCallPeriodDuration,releaseIfdurationExceeded,tariffSwitchInterval,tone,extensions);
    }
    
    //V4
    public CAMELAChBillingChargingCharacteristicsImpl(long maxCallPeriodDuration, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, AudibleIndicator audibleIndicator, CAPINAPExtensions extensions) {
        this.timeDurationCharging = new TimeDurationChargingImpl(maxCallPeriodDuration, releaseIfdurationExceeded, tariffSwitchInterval, audibleIndicator, extensions);
    }

    public long getMaxCallPeriodDuration() {
    	if(this.timeDurationCharging==null)
    		return 0;
    	
        return this.timeDurationCharging.getMaxCallPeriodDuration();
    }

    public boolean getReleaseIfdurationExceeded() {
    	if(this.timeDurationCharging==null)
    		return false;
    	
        return this.timeDurationCharging.getReleaseIfdurationExceeded();
    }

    public Long getTariffSwitchInterval() {
    	if(this.timeDurationCharging==null)
    		return null;
    	
    	return this.timeDurationCharging.getTariffSwitchInterval();
    }

    public AudibleIndicator getAudibleIndicator() {
    	if(this.timeDurationCharging==null)
    		return null;
    	
        return this.timeDurationCharging.getAudibleIndicator();
    }

    public CAPINAPExtensions getExtensions() {
    	if(this.timeDurationCharging==null)
    		return null;
    	
        return this.timeDurationCharging.getExtensions();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CAMELAChBillingChargingCharacteristics [");

        if (this.timeDurationCharging != null) {
            sb.append(", timeDurationCharging=");
            sb.append(timeDurationCharging);
        }

        sb.append("]");

        return sb.toString();
    }
}
