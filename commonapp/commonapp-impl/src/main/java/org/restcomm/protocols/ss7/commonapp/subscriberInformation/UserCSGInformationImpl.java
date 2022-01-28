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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.UserCSGInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CSGId;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class UserCSGInformationImpl implements UserCSGInformation {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = CSGIdImpl.class)
    private CSGId csgId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNSingleByte accessMode;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNSingleByte cmi;

    public UserCSGInformationImpl() {
    }

    public UserCSGInformationImpl(CSGId csgId, MAPExtensionContainer extensionContainer, Integer accessMode, Integer cmi) {
        this.csgId = csgId;
        this.extensionContainer = extensionContainer;
        
        if(accessMode!=null)
        	this.accessMode = new ASNSingleByte(accessMode);
        	
        if(cmi!=null)
        	this.cmi = new ASNSingleByte(cmi);        	
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. UserCSGInformation#getCSGId()
     */
    public CSGId getCSGId() {
        return this.csgId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. UserCSGInformation#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. UserCSGInformation#getAccessMode()
     */
    public Integer getAccessMode() {
    	if(this.accessMode==null)
    		return null;
    	
        return this.accessMode.getValue().intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. UserCSGInformation#getCmi()
     */
    public Integer getCmi() {
    	if(this.cmi==null)
    		return null;
    	
        return this.cmi.getValue().intValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationInformation [");

        if (this.csgId != null) {
            sb.append("csgId=");
            sb.append(this.csgId);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.accessMode != null) {
            sb.append(", accessMode=");
            sb.append(this.accessMode.getValue());
        }
        if (this.cmi != null) {
            sb.append(", cmi=");
            sb.append(this.cmi.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
}
