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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 *
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AdditionalNumberImpl implements AdditionalNumber {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString mSCNumber = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString sGSNNumber = null;

    /**
     *
     */
    public AdditionalNumberImpl() {
        super();
    }

    /**
     * @param mSCNumber
     * @param sGSNNumber
     */
    public AdditionalNumberImpl(ISDNAddressString mSCNumber, ISDNAddressString sGSNNumber) {
        super();
        this.mSCNumber = mSCNumber;
        this.sGSNNumber = sGSNNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber#getMSCNumber ()
     */
    public ISDNAddressString getMSCNumber() {
        return this.mSCNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber# getSGSNNumber()
     */
    public ISDNAddressString getSGSNNumber() {
        return this.sGSNNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdditionalNumber [");

        if (this.mSCNumber != null) {
            sb.append("msc-Number=");
            sb.append(this.mSCNumber.toString());
        }
        if (this.sGSNNumber != null) {
            sb.append("sgsn-Number=");
            sb.append(this.sGSNNumber.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mSCNumber == null) ? 0 : mSCNumber.hashCode());
        result = prime * result + ((sGSNNumber == null) ? 0 : sGSNNumber.hashCode());
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
        AdditionalNumberImpl other = (AdditionalNumberImpl) obj;
        if (mSCNumber == null) {
            if (other.mSCNumber != null)
                return false;
        } else if (!mSCNumber.equals(other.mSCNumber))
            return false;
        if (sGSNNumber == null) {
            if (other.sGSNNumber != null)
                return false;
        } else if (!sGSNNumber.equals(other.sGSNNumber))
            return false;
        return true;
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(mSCNumber==null && sGSNNumber==null)
			throw new ASNParsingComponentException("either msc number or sgsn number should be set for additional number", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
