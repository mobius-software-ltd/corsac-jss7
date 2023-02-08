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

import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.TerminationCause;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class DeferredmtlrDataImpl implements DeferredmtlrData {
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,index=0,defaultImplementation = DeferredLocationEventTypeImpl.class)
    private DeferredLocationEventType deferredLocationEventType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNTerminateCause terminationCause;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = LCSLocationInfoImpl.class)
    private LCSLocationInfo lcsLocationInfo;

    /**
     *
     */
    public DeferredmtlrDataImpl() {
    }

    /**
     * @param deferredLocationEventType
     * @param terminationCause
     * @param lcsLocationInfo
     */
    public DeferredmtlrDataImpl(DeferredLocationEventType deferredLocationEventType, TerminationCause terminationCause,
    		LCSLocationInfo lcsLocationInfo) {
        this.deferredLocationEventType = deferredLocationEventType;
        
        if(terminationCause!=null)
        	this.terminationCause = new ASNTerminateCause(terminationCause);
        	
        this.lcsLocationInfo = lcsLocationInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData# getDeferredLocationEventType()
     */
    public DeferredLocationEventType getDeferredLocationEventType() {
        return this.deferredLocationEventType;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData# getTerminationCause()
     */
    public TerminationCause getTerminationCause() {
    	if(this.terminationCause==null)
    		return null;
    	
        return this.terminationCause.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData# getLCSLocationInfo()
     */
    public LCSLocationInfo getLCSLocationInfo() {
        return this.lcsLocationInfo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deferredLocationEventType == null) ? 0 : deferredLocationEventType.hashCode());
        result = prime * result + ((lcsLocationInfo == null) ? 0 : lcsLocationInfo.hashCode());
        result = prime * result + ((terminationCause == null) ? 0 : terminationCause.hashCode());
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
        DeferredmtlrDataImpl other = (DeferredmtlrDataImpl) obj;
        if (deferredLocationEventType == null) {
            if (other.deferredLocationEventType != null)
                return false;
        } else if (!deferredLocationEventType.equals(other.deferredLocationEventType))
            return false;
        if (lcsLocationInfo == null) {
            if (other.lcsLocationInfo != null)
                return false;
        } else if (!lcsLocationInfo.equals(other.lcsLocationInfo))
            return false;
        if (terminationCause != other.terminationCause)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeferredmtlrData [");

        if (this.deferredLocationEventType != null) {
            sb.append("deferredLocationEventType=");
            sb.append(this.deferredLocationEventType);
        }
        if (this.terminationCause != null) {
            sb.append(", terminationCause=");
            sb.append(this.terminationCause.getType());
        }
        if (this.lcsLocationInfo != null) {
            sb.append(", lcsLocationInfo=");
            sb.append(this.lcsLocationInfo.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(deferredLocationEventType==null)
			throw new ASNParsingComponentException("deferred location event type should be set for deferred mtlr data", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
