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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberDiagnosticSM;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriberSM;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageAbsentSubscriberSMImpl extends MAPErrorMessageImpl implements MAPErrorMessageAbsentSubscriberSM {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=-1)
    private ASNAbsentSubscriberDiagnosticSMImpl absentSubscriberDiagnosticSM;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNAbsentSubscriberDiagnosticSMImpl additionalAbsentSubscriberDiagnosticSM;

    public MAPErrorMessageAbsentSubscriberSMImpl(AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM,
    		MAPExtensionContainer extensionContainer, AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM) {
        super(MAPErrorCode.absentSubscriberSM);

        if(absentSubscriberDiagnosticSM!=null)
        	this.absentSubscriberDiagnosticSM = new ASNAbsentSubscriberDiagnosticSMImpl(absentSubscriberDiagnosticSM);
        	
        this.extensionContainer = extensionContainer;
        
        if(additionalAbsentSubscriberDiagnosticSM!=null)
        	this.additionalAbsentSubscriberDiagnosticSM = new ASNAbsentSubscriberDiagnosticSMImpl(additionalAbsentSubscriberDiagnosticSM);        	
    }

    public MAPErrorMessageAbsentSubscriberSMImpl() {
        super(MAPErrorCode.absentSubscriberSM);
    }

    public AbsentSubscriberDiagnosticSM getAbsentSubscriberDiagnosticSM() {
    	if(this.absentSubscriberDiagnosticSM==null)
    		return null;
    	
        return this.absentSubscriberDiagnosticSM.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
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
    	else 
    		this.absentSubscriberDiagnosticSM = new ASNAbsentSubscriberDiagnosticSMImpl(absentSubscriberDiagnosticSM);    		
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setAdditionalAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM) {
    	if(additionalAbsentSubscriberDiagnosticSM==null)
    		this.additionalAbsentSubscriberDiagnosticSM=null;
    	else
    		this.additionalAbsentSubscriberDiagnosticSM = new ASNAbsentSubscriberDiagnosticSMImpl(additionalAbsentSubscriberDiagnosticSM);    		
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
