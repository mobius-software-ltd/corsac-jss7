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
 * @author yulianoifa
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