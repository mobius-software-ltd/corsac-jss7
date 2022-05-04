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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.Cksn;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.GSMSecurityContextData;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.Kc;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GSMSecurityContextDataImpl implements GSMSecurityContextData {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = KcImpl.class)
	private Kc kc;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1, defaultImplementation = CksnImpl.class)
	private Cksn cksn;
	
    public GSMSecurityContextDataImpl() {
    }

    public GSMSecurityContextDataImpl(Kc kc, Cksn cksn) {
        this.kc = kc;
        this.cksn = cksn;
    }

    public Kc getKc() {
        return this.kc;
    }

    public Cksn getCksn() {
        return this.cksn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GSMSecurityContextData [");

        if (this.kc != null) {
            sb.append("kc=");
            sb.append(this.kc.toString());
            sb.append(", ");
        }

        if (this.cksn != null) {
            sb.append("cksn=");
            sb.append(this.cksn.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();

    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(kc==null)
			throw new ASNParsingComponentException("kc should be set for gsm security context", ASNParsingComponentExceptionReason.MistypedParameter);

		if(cksn==null)
			throw new ASNParsingComponentException("cksn should be set for gsm security context", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
