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

package org.restcomm.protocols.ss7.tcapAnsi.api.asn;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ConfidentialityImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=25,constructed=false,lengthIndefinite=false)
public class DialogPortionImpl {
	private ProtocolVersionImpl protocolVersion;
    private IntegerApplicationContextNameImpl intApplicationContext;
    private ObjectApplicationContextNameImpl objApplicationContext;
    private UserInformationImpl userInformation;
    private IntegerSecurityContextImpl intSecurityContext;
    private ObjectSecurityContextImpl objSecurityContext;
    private ConfidentialityImpl confidentiality;

    public DialogPortionImpl() {
        super();
    }

    public ProtocolVersionImpl getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(ProtocolVersionImpl val) {
        protocolVersion = val;
    }

    public ApplicationContext getApplicationContext() {
    	if(intApplicationContext!=null)
    		return intApplicationContext;
    	
        return objApplicationContext;
    }

    public void setApplicationContext(ApplicationContext val) {
    	if(val instanceof IntegerApplicationContextNameImpl) {
    		this.intApplicationContext=(IntegerApplicationContextNameImpl)val;
    		this.objApplicationContext=null;
    	} else if(val instanceof ObjectApplicationContextNameImpl) {
    		this.intApplicationContext=null;
    		this.objApplicationContext=(ObjectApplicationContextNameImpl)val;
    	} else
    		throw new IllegalArgumentException("Unsupported Application Context");
    }

    public UserInformationImpl getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformationImpl val) {
        userInformation = val;
    }

    public SecurityContext getSecurityContext() {
    	if(intSecurityContext!=null)
    		return intSecurityContext;
    	
        return objSecurityContext;
    }

    public void setSecurityContext(SecurityContext val) {
        if(val instanceof IntegerSecurityContextImpl) {
    		this.intSecurityContext=(IntegerSecurityContextImpl)val;
    		this.objSecurityContext=null;
    	} else if(val instanceof ObjectSecurityContextImpl) {
    		this.intSecurityContext=null;
    		this.objSecurityContext=(ObjectSecurityContextImpl)val;
    	} else
    		throw new IllegalArgumentException("Unsupported Security Context");
    }

    public ConfidentialityImpl getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(ConfidentialityImpl val) {
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
