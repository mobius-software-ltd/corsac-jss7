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

import org.restcomm.protocols.ss7.map.api.errors.ASNCUGRejectCauseImpl;
import org.restcomm.protocols.ss7.map.api.errors.CUGRejectCause;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageCUGReject;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageCUGRejectImpl extends MAPErrorMessageImpl implements MAPErrorMessageCUGReject {
	private ASNCUGRejectCauseImpl cugRejectCause;
    private MAPExtensionContainerImpl extensionContainer;

    public MAPErrorMessageCUGRejectImpl(CUGRejectCause cugRejectCause, MAPExtensionContainerImpl extensionContainer) {
        super((long) MAPErrorCode.cugReject);

        if(cugRejectCause!=null){
        	this.cugRejectCause = new ASNCUGRejectCauseImpl();
        	this.cugRejectCause.setType(cugRejectCause);
        }
        
        this.extensionContainer = extensionContainer;
    }

    public MAPErrorMessageCUGRejectImpl() {
        super((long) MAPErrorCode.cugReject);
    }

    public boolean isEmCUGReject() {
        return true;
    }

    public MAPErrorMessageCUGReject getEmCUGReject() {
        return this;
    }

    @Override
    public CUGRejectCause getCUGRejectCause() {
    	if(this.cugRejectCause==null)
    		return null;
    	
        return cugRejectCause.getType();
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public void setCUGRejectCause(CUGRejectCause val) {
    	if(val==null)
    		cugRejectCause=null;
    	else {
    		cugRejectCause = new ASNCUGRejectCauseImpl();
    		cugRejectCause.setType(val);
    	}
    }

    @Override
    public void setExtensionContainer(MAPExtensionContainerImpl val) {
        extensionContainer = val;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageCUGReject [");

        if (this.cugRejectCause != null)
            sb.append("cugRejectCause=" + this.cugRejectCause.toString());
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        sb.append("]");

        return sb.toString();
    }
}
