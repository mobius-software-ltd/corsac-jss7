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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MCSSInfoImpl implements MCSSInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0,defaultImplementation = SSCodeImpl.class)
    private SSCode ssCode;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=1,defaultImplementation = ExtSSStatusImpl.class)
    private ExtSSStatus ssStatus;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=2)
    private ASNInteger nbrSB;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=3)
    private ASNInteger nbrUser;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public MCSSInfoImpl() {
    }

    public MCSSInfoImpl(SSCode ssCode, ExtSSStatus ssStatus, int nbrSB, int nbrUser, MAPExtensionContainer extensionContainer) {
        this.ssCode = ssCode;
        this.ssStatus = ssStatus;
        this.nbrSB = new ASNInteger(nbrSB);
        this.nbrUser = new ASNInteger(nbrUser);
        this.extensionContainer = extensionContainer;
    }

    public SSCode getSSCode() {
        return this.ssCode;
    }

    public ExtSSStatus getSSStatus() {
        return this.ssStatus;
    }

    public int getNbrSB() {
    	if(this.nbrSB==null || this.nbrSB.getValue()==null)
    		return 0;
    	
        return this.nbrSB.getIntValue();
    }

    public int getNbrUser() {
    	if(this.nbrUser==null || this.nbrUser.getValue()==null)
    		return 0;
    	
        return this.nbrUser.getIntValue();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MCSSInfo [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode.toString());
            sb.append(", ");
        }

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus.toString());
            sb.append(", ");
        }

        if(this.nbrSB!=null) {
	        sb.append("nbrSB=");
	        sb.append(this.nbrSB.getValue());
	        sb.append(", ");
        }
        
        if(this.nbrUser!=null) {
	        sb.append("nbrUser=");
	        sb.append(this.nbrUser.getValue());
	        sb.append(", ");
        }
        
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }
}
