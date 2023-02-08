/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AreaEventInfoImpl implements AreaEventInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=0, defaultImplementation = AreaDefinitionImpl.class)
    private AreaDefinition areaDefinition;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNOccurenceInfo occurrenceInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNInteger intervalTime;

    /**
     *
     */
    public AreaEventInfoImpl() {
    }

    /**
     * @param areaDefinition
     * @param occurrenceInfo
     * @param intervalTime
     */
    public AreaEventInfoImpl(AreaDefinition areaDefinition, OccurrenceInfo occurrenceInfo, Integer intervalTime) {
        this.areaDefinition = areaDefinition;
        
        if(occurrenceInfo!=null)
        	this.occurrenceInfo = new ASNOccurenceInfo(occurrenceInfo);
        	
        if(intervalTime!=null)
        	this.intervalTime = new ASNInteger(intervalTime,"IntervalTime",1,32767,false);        	
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo# getAreaDefinition()
     */
    public AreaDefinition getAreaDefinition() {
        return this.areaDefinition;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo# getOccurrenceInfo()
     */
    public OccurrenceInfo getOccurrenceInfo() {
    	if(this.occurrenceInfo==null)
    		return null;
    	
        return this.occurrenceInfo.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo#getIntervalTime ()
     */
    public Integer getIntervalTime() {
    	if(this.intervalTime==null)
    		return null;
    	
        return this.intervalTime.getIntValue();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((areaDefinition == null) ? 0 : areaDefinition.hashCode());
        result = prime * result + ((occurrenceInfo == null || occurrenceInfo.getType()==null) ? 0 : occurrenceInfo.getType().hashCode());
        result = prime * result + ((intervalTime == null || intervalTime.getValue()==null) ? 0 : (int) intervalTime.getValue().intValue());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AreaEventInfoImpl other = (AreaEventInfoImpl) obj;

        if (this.areaDefinition == null) {
            if (other.areaDefinition != null)
                return false;
        } else {
            if (!this.areaDefinition.equals(other.areaDefinition))
                return false;
        }
        if (this.occurrenceInfo == null) {
            if (other.occurrenceInfo != null)
                return false;
        } else {
            if (this.occurrenceInfo != other.occurrenceInfo)
                return false;
        }
        if (this.intervalTime == null || this.intervalTime.getValue()==null) {
            if (other.intervalTime != null && other.intervalTime.getValue()!=null)
                return false;
        } else {
            if ((long) this.intervalTime.getValue() != (long) other.intervalTime.getValue())
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AreaEventInfo [");

        if (this.areaDefinition != null) {
            sb.append("areaDefinition=");
            sb.append(this.areaDefinition);
        }
        if (this.occurrenceInfo != null) {
            sb.append(", occurrenceInfo=");
            sb.append(this.occurrenceInfo.getType());
        }
        if (this.intervalTime != null) {
            sb.append(", intervalTime=");
            sb.append(this.intervalTime.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(areaDefinition==null)
			throw new ASNParsingComponentException("area definition should be set for area event info", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
