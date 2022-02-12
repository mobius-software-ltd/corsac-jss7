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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberReason;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageAbsentSubscriberImpl extends MAPErrorMessageImpl implements MAPErrorMessageAbsentSubscriber {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNAbsentSubscriberReasonImpl absentSubscriberReason;

    /**
     * For MAP V2-3
     *
     * @param extensionContainer
     * @param absentSubscriberReason
     */
    public MAPErrorMessageAbsentSubscriberImpl(MAPExtensionContainer extensionContainer,
            AbsentSubscriberReason absentSubscriberReason) {
        super(MAPErrorCode.absentSubscriber);

        this.extensionContainer = extensionContainer;
        if(absentSubscriberReason!=null)
        	this.absentSubscriberReason = new ASNAbsentSubscriberReasonImpl(absentSubscriberReason);        	
    }

    public MAPErrorMessageAbsentSubscriberImpl() {
        super(MAPErrorCode.absentSubscriber);
    }

    public boolean isEmAbsentSubscriber() {
        return true;
    }

    public MAPErrorMessageAbsentSubscriber getEmAbsentSubscriber() {
        return this;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public AbsentSubscriberReason getAbsentSubscriberReason() {
    	if(this.absentSubscriberReason==null)
    		return null;
    	
        return this.absentSubscriberReason.getType();
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setAbsentSubscriberReason(AbsentSubscriberReason absentSubscriberReason) {
    	if(absentSubscriberReason==null)
    		this.absentSubscriberReason=null;
    	else
    		this.absentSubscriberReason = new ASNAbsentSubscriberReasonImpl(absentSubscriberReason);    		
    }

    @Override
    public Boolean getMwdSet() {
        return null;
    }

    @Override
    public void setMwdSet(Boolean val) {
        
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageAbsentSubscriber [");
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        if (this.absentSubscriberReason != null)
            sb.append(", absentSubscriberReason=" + this.absentSubscriberReason.toString());
        sb.append("]");

        return sb.toString();
    }
}
