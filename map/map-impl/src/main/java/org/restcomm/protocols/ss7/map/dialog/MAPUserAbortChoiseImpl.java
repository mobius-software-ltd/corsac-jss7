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

package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.restcomm.protocols.ss7.map.api.dialog.ResourceUnavailableReason;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * MAP-UserAbortInfo ::= SEQUENCE { map-UserAbortChoice MAP-UserAbortChoice, ... extensionContainer ExtensionContainer OPTIONAL
 * }
 *
 * MAP-UserAbortChoice ::= CHOICE { userSpecificReason [0] NULL, userResourceLimitation [1] NULL, resourceUnavailable [2]
 * ResourceUnavailableReason, applicationProcedureCancellation [3] ProcedureCancellationReason}
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x04,constructed=true,lengthIndefinite=false)
public class MAPUserAbortChoiseImpl implements MAPUserAbortChoice {
	private ASNProcedureCancellationReason procedureCancellationReason;
	private ASNResourceUnavailableReason resourceUnavailableReason;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=0)
	private ASNNull userResourceLimitation;
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
	private ASNNull userSpecificReason;
	
	public ProcedureCancellationReason getProcedureCancellationReason() {
    	if(this.procedureCancellationReason==null)
    		return null;
    	
        return this.procedureCancellationReason.getType();
    }

    public ResourceUnavailableReason getResourceUnavailableReason() {
    	if(this.resourceUnavailableReason==null)
    		return null;
    	
        return this.resourceUnavailableReason.getType();
    }

    public boolean isProcedureCancellationReason() {
        return this.procedureCancellationReason!=null;
    }

    public boolean isResourceUnavailableReason() {
        return this.resourceUnavailableReason!=null;
    }

    public boolean isUserResourceLimitation() {
        return this.userResourceLimitation!=null;
    }

    public boolean isUserSpecificReason() {
        return this.userSpecificReason!=null;
    }

    public void setProcedureCancellationReason(ProcedureCancellationReason procCanReasn) {
        this.procedureCancellationReason =new ASNProcedureCancellationReason(procCanReasn);
        this.resourceUnavailableReason=null;
        this.userResourceLimitation=null;
        this.userSpecificReason=null;
    }

    public void setResourceUnavailableReason(ResourceUnavailableReason resUnaReas) {
        this.resourceUnavailableReason = new ASNResourceUnavailableReason(resUnaReas);
        this.procedureCancellationReason=null;
        this.userResourceLimitation=null;
        this.userSpecificReason=null;
    }

    public void setUserResourceLimitation() {
        this.userResourceLimitation=new ASNNull();
        this.procedureCancellationReason=null;
        this.resourceUnavailableReason=null;
        this.userSpecificReason=null;
    }

    public void setUserSpecificReason() {
        this.userSpecificReason=new ASNNull();
        this.userResourceLimitation=null;
        this.procedureCancellationReason=null;
        this.resourceUnavailableReason=null;        
    }
}
