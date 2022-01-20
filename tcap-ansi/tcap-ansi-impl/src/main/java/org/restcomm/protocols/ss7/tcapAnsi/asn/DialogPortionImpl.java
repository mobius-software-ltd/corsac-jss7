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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.Confidentiality;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=25,constructed=true,lengthIndefinite=false)
public class DialogPortionImpl implements DialogPortion {
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=26,constructed=false,index=-1,defaultImplementation = ProtocolVersionImpl.class)
	private ProtocolVersion protocolVersion;
	
	@ASNChoise(defaultImplementation = ApplicationContextImpl.class)
    private ApplicationContext applicationContext;
    
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=0x1D,constructed=true,index=-1,defaultImplementation = UserInformationImpl.class)
	private UserInformation userInformation;
    
    @ASNChoise(defaultImplementation = SecurityContextImpl.class)
    private SecurityContext securityContext;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index = -1,defaultImplementation = ConfidentialityImpl.class)
    private Confidentiality confidentiality;

    public DialogPortionImpl() {
        super();
    }

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(ProtocolVersion val) {
        protocolVersion = val;
    }

    public ApplicationContext getApplicationContext() {
    	return applicationContext;
    }

    public void setApplicationContext(ApplicationContext val) {
    	this.applicationContext=val;    	
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation val) {
        userInformation = val;
    }

    public SecurityContext getSecurityContext() {
    	return securityContext;
    }

    public void setSecurityContext(SecurityContext val) {
    	this.securityContext=val;    	
    }

    public Confidentiality getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(Confidentiality val) {
        confidentiality = val;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DialogPortion[");

        if (this.getProtocolVersion() != null) {
            sb.append("ProtocolVersion=");
            sb.append(this.getProtocolVersion());
            sb.append(", ");
        }
        if (this.getApplicationContext() != null) {
            sb.append("ApplicationContext=");
            sb.append(this.getApplicationContext());
            sb.append(", ");
        }
        if (this.getUserInformation() != null) {
            sb.append("UserInformation=[");
            sb.append(this.getUserInformation());
            sb.append(", ");
        }
        if (this.getSecurityContext() != null) {
            sb.append("SecurityContext=");
            sb.append(this.getSecurityContext());
            sb.append(", ");
        }
        if (this.getConfidentiality() != null) {
            sb.append("Confidentiality=");
            sb.append(this.getConfidentiality());
            sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
