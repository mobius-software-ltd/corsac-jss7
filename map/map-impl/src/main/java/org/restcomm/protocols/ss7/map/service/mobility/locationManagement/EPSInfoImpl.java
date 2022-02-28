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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISRInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PDNGWUpdate;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class EPSInfoImpl implements EPSInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1, defaultImplementation = PDNGWUpdateImpl.class)
    private PDNGWUpdate pndGwUpdate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = ISRInformationImpl.class)
    private ISRInformation isrInformation;

    public EPSInfoImpl() {
        super();
    }

    public EPSInfoImpl(PDNGWUpdate pndGwUpdate) {
        this.pndGwUpdate = pndGwUpdate;
        this.isrInformation = null;
    }

    public EPSInfoImpl(ISRInformation isrInformation) {
        this.pndGwUpdate = null;
        this.isrInformation = isrInformation;
    }

    public PDNGWUpdate getPndGwUpdate() {
        return this.pndGwUpdate;
    }

    public ISRInformation getIsrInformation() {
        return this.isrInformation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EPSInfo [");

        if (this.pndGwUpdate != null) {
            sb.append("pndGwUpdate=");
            sb.append(this.pndGwUpdate.toString());
        }

        if (this.isrInformation != null) {
            sb.append("isrInformation=");
            sb.append(this.isrInformation.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(pndGwUpdate==null && isrInformation==null)
			throw new ASNParsingComponentException("either pdn gw update or isr information should be set for eps info", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
