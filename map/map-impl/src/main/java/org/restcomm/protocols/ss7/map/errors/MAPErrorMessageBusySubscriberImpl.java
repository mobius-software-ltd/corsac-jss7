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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageBusySubscriber;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageBusySubscriberImpl extends MAPErrorMessageImpl implements MAPErrorMessageBusySubscriber {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull ccbsPossible;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull ccbsBusy;

    public MAPErrorMessageBusySubscriberImpl(MAPExtensionContainer extensionContainer, boolean ccbsPossible, boolean ccbsBusy) {
        super(MAPErrorCode.busySubscriber);

        this.extensionContainer = extensionContainer;
        if(ccbsPossible)
        	this.ccbsPossible = new ASNNull();
        
        if(ccbsBusy)
        	this.ccbsBusy = new ASNNull();
    }

    public MAPErrorMessageBusySubscriberImpl() {
        super(MAPErrorCode.busySubscriber);
    }

    public boolean isEmBusySubscriber() {
        return true;
    }

    public MAPErrorMessageBusySubscriber getEmBusySubscriber() {
        return this;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public boolean getCcbsPossible() {
    	return ccbsPossible!=null;
    }

    @Override
    public boolean getCcbsBusy() {
        return ccbsBusy!=null;
    }

    @Override
    public void setExtensionContainer(MAPExtensionContainer val) {
        this.extensionContainer = val;
    }

    @Override
    public void setCcbsPossible(boolean val) {
    	if(val)
    		this.ccbsPossible = new ASNNull();
    	else
    		this.ccbsPossible = null;
    }

    @Override
    public void setCcbsBusy(boolean val) {
    	if(val)
    		this.ccbsBusy = new ASNNull();
    	else
    		this.ccbsBusy = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageBusySubscriber [");

        if (this.extensionContainer != null)
            sb.append("extensionContainer=" + this.extensionContainer.toString());
        if (this.ccbsPossible!=null)
            sb.append(", ccbsPossible");
        if (this.ccbsBusy!=null)
            sb.append(", ccbsBusy");
        sb.append("]");

        return sb.toString();
    }
}
