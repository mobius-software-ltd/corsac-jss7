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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
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
        if(callSessionUnrelated!=null) {
        	this.callSessionUnrelated = new ASNPrivacyCheckRelatedAction();
        	this.callSessionUnrelated.setType(callSessionUnrelated);
        }
        
        if(callSessionRelated!=null) {
        	this.callSessionRelated = new ASNPrivacyCheckRelatedAction();
        	this.callSessionRelated.setType(callSessionRelated);
        }
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
}
