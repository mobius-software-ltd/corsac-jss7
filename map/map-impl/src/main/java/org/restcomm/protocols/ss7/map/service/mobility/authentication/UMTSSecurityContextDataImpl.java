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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.CK;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.IK;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.KSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.UMTSSecurityContextData;

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
public class UMTSSecurityContextDataImpl implements UMTSSecurityContextData {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = CKImpl.class)
	private CK ck;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1, defaultImplementation = IKImpl.class)
	private IK ik;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2, defaultImplementation = KSIImpl.class)
	private KSI ksi;

    public UMTSSecurityContextDataImpl() {
    }

    public UMTSSecurityContextDataImpl(CK ck, IK ik, KSI ksi) {
        this.ck = ck;
        this.ik = ik;
        this.ksi = ksi;
    }

    public CK getCK() {
        return this.ck;
    }

    public IK getIK() {
        return this.ik;
    }

    public KSI getKSI() {
        return this.ksi;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UMTSSecurityContextData [");

        if (this.ck != null) {
            sb.append("ck=");
            sb.append(this.ck.toString());
            sb.append(", ");
        }

        if (this.ik != null) {
            sb.append("ik=");
            sb.append(this.ik.toString());
            sb.append(", ");
        }

        if (this.ksi != null) {
            sb.append("ksi=");
            sb.append(this.ksi.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();

    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(ck==null)
			throw new ASNParsingComponentException("ck should be set for umts security context", ASNParsingComponentExceptionReason.MistypedParameter);

		if(ik==null)
			throw new ASNParsingComponentException("ik should be set for umts security context", ASNParsingComponentExceptionReason.MistypedParameter);
		
		if(ksi==null)
			throw new ASNParsingComponentException("ksi should be set for umts security context", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
