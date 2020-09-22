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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.ASNAbsentSubscriberDiagnosticSMImpl;
import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberDiagnosticSM;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriberSM;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageAbsentSubscriberSMImpl extends MAPErrorMessageImpl implements MAPErrorMessageAbsentSubscriberSM {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=-1)
    private ASNAbsentSubscriberDiagnosticSMImpl absentSubscriberDiagnosticSM;
    
	private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNAbsentSubscriberDiagnosticSMImpl additionalAbsentSubscriberDiagnosticSM;

    public MAPErrorMessageAbsentSubscriberSMImpl(AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM,
    		MAPExtensionContainerImpl extensionContainer, AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM) {
        super((long) MAPErrorCode.absentSubscriberSM);

        if(absentSubscriberDiagnosticSM!=null){
        	this.absentSubscriberDiagnosticSM = new ASNAbsentSubscriberDiagnosticSMImpl();
        	this.absentSubscriberDiagnosticSM.setType(absentSubscriberDiagnosticSM);
        }
        
        this.extensionContainer = extensionContainer;
        
        if(additionalAbsentSubscriberDiagnosticSM!=null) {
        	this.additionalAbsentSubscriberDiagnosticSM = new ASNAbsentSubscriberDiagnosticSMImpl();
        	this.additionalAbsentSubscriberDiagnosticSM.setType(additionalAbsentSubscriberDiagnosticSM);
        }
    }

    public MAPErrorMessageAbsentSubscriberSMImpl() {
        super((long) MAPErrorCode.absentSubscriberSM);
    }

    public AbsentSubscriberDiagnosticSM getAbsentSubscriberDiagnosticSM() {
    	if(this.absentSubscriberDiagnosticSM==null)
    		return null;
    	
        return this.absentSubscriberDiagnosticSM.getType();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public AbsentSubscriberDiagnosticSM getAdditionalAbsentSubscriberDiagnosticSM() {
    	if(this.additionalAbsentSubscriberDiagnosticSM==null)
    		return null;
    	
        return this.additionalAbsentSubscriberDiagnosticSM.getType();
    }

    public void setAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM) {
    	if(absentSubscriberDiagnosticSM==null)
    		this.absentSubscriberDiagnosticSM=null;
    	else {
    		this.absentSubscriberDiagnosticSM = new ASNAbsentSubscriberDiagnosticSMImpl();
    		this.absentSubscriberDiagnosticSM.setType(absentSubscriberDiagnosticSM);
    	}
    }

    public void setExtensionContainer(MAPExtensionContainerImpl extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setAdditionalAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM) {
    	if(additionalAbsentSubscriberDiagnosticSM==null)
    		this.additionalAbsentSubscriberDiagnosticSM=null;
    	else {
    		this.additionalAbsentSubscriberDiagnosticSM = new ASNAbsentSubscriberDiagnosticSMImpl();
    		this.additionalAbsentSubscriberDiagnosticSM.setType(additionalAbsentSubscriberDiagnosticSM);
    	}
    }

    public boolean isEmAbsentSubscriberSM() {
        return true;
    }

    public MAPErrorMessageAbsentSubscriberSM getEmAbsentSubscriberSM() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageAbsentSubscriberSM [");
        if (this.absentSubscriberDiagnosticSM != null)
            sb.append("absentSubscriberDiagnosticSM=" + this.absentSubscriberDiagnosticSM.toString());
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        if (this.additionalAbsentSubscriberDiagnosticSM != null)
            sb.append(", additionalAbsentSubscriberDiagnosticSM=" + this.additionalAbsentSubscriberDiagnosticSM.toString());
        sb.append("]");

        return sb.toString();
    }
}
