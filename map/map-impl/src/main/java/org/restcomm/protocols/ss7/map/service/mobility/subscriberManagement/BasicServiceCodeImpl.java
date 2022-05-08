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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class BasicServiceCodeImpl implements BasicServiceCode {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = BearerServiceCodeImpl.class)
    private BearerServiceCode bearerService;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = TeleserviceCodeImpl.class)
    private TeleserviceCode teleservice;

    public BasicServiceCodeImpl() {
    }

    public BasicServiceCodeImpl(TeleserviceCode teleservice) {
        this.teleservice = teleservice;
    }

    public BasicServiceCodeImpl(BearerServiceCode bearerService) {
        this.bearerService = bearerService;
    }

    public BearerServiceCode getBearerService() {
        return bearerService;
    }

    public TeleserviceCode getTeleservice() {
        return teleservice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("BasicServiceCode [");

        if (this.bearerService != null) {
            sb.append("bearerService=" + this.bearerService.toString());
            sb.append(", ");
        }
        if (this.teleservice != null)
            sb.append("teleservice=" + this.teleservice.toString());
        sb.append("]");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bearerService == null) ? 0 : bearerService.hashCode());
        result = prime * result + ((teleservice == null) ? 0 : teleservice.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BasicServiceCodeImpl other = (BasicServiceCodeImpl) obj;
        if (bearerService == null) {
            if (other.bearerService != null)
                return false;
        } else if (!bearerService.equals(other.bearerService))
            return false;
        if (teleservice == null) {
            if (other.teleservice != null)
                return false;
        } else if (!teleservice.equals(other.teleservice))
            return false;
        return true;
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(bearerService==null && teleservice==null)
			throw new ASNParsingComponentException("either bearer service or teleservice should be set for basic service code", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}