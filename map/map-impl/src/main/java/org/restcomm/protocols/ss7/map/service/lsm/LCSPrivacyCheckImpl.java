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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LCSPrivacyCheckImpl implements LCSPrivacyCheck {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ASNPrivacyCheckRelatedAction callSessionUnrelated;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNPrivacyCheckRelatedAction callSessionRelated;

    /**
     *
     */
    public LCSPrivacyCheckImpl() {
    }

    /**
     * @param callSessionUnrelated
     * @param callSessionRelated
     */
    public LCSPrivacyCheckImpl(PrivacyCheckRelatedAction callSessionUnrelated, PrivacyCheckRelatedAction callSessionRelated) {
        if(callSessionUnrelated!=null)
        	this.callSessionUnrelated = new ASNPrivacyCheckRelatedAction(callSessionUnrelated);
        	
        if(callSessionRelated!=null)
        	this.callSessionRelated = new ASNPrivacyCheckRelatedAction(callSessionRelated);        	
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck# getCallSessionUnrelated()
     */
    public PrivacyCheckRelatedAction getCallSessionUnrelated() {
    	if(this.callSessionUnrelated==null)
    		return null;
    	
        return this.callSessionUnrelated.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck# getCallSessionRelated()
     */
    public PrivacyCheckRelatedAction getCallSessionRelated() {
    	if(this.callSessionRelated==null)
    		return null;
    	
        return this.callSessionRelated.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LCSPrivacyCheck [");

        if (this.callSessionUnrelated != null) {
            sb.append("callSessionUnrelated=");
            sb.append(this.callSessionUnrelated.getType());
        }
        if (this.callSessionRelated != null) {
            sb.append(", callSessionRelated=");
            sb.append(this.callSessionRelated.getType());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(callSessionUnrelated==null)
			throw new ASNParsingComponentException("call session unrelated should be set for lcs privacy check", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
