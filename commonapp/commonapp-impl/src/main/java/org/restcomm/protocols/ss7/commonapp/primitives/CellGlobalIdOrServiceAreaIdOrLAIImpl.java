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

package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;


/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=false,lengthIndefinite=false)
public class CellGlobalIdOrServiceAreaIdOrLAIImpl implements CellGlobalIdOrServiceAreaIdOrLAI
{
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = CellGlobalIdOrServiceAreaIdFixedLengthImpl.class)
	private CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = null;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = LAIFixedLengthImpl.class)
	private LAIFixedLength laiFixedLength = null;

    /**
     *
     */
    public CellGlobalIdOrServiceAreaIdOrLAIImpl() {
        super();
    }

    public CellGlobalIdOrServiceAreaIdOrLAIImpl(CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength) {
        this.cellGlobalIdOrServiceAreaIdFixedLength = cellGlobalIdOrServiceAreaIdFixedLength;
    }

    public CellGlobalIdOrServiceAreaIdOrLAIImpl(LAIFixedLength laiFixedLength) {

        this.laiFixedLength = laiFixedLength;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. CellGlobalIdOrServiceAreaIdOrLAI
     * #getCellGlobalIdOrServiceAreaIdFixedLength()
     */
    public CellGlobalIdOrServiceAreaIdFixedLength getCellGlobalIdOrServiceAreaIdFixedLength() {
        return this.cellGlobalIdOrServiceAreaIdFixedLength;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. CellGlobalIdOrServiceAreaIdOrLAI#getLAIFixedLength()
     */
    public LAIFixedLength getLAIFixedLength() {
        return this.laiFixedLength;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("CellGlobalIdOrServiceAreaIdOrLAI [");
        if (this.cellGlobalIdOrServiceAreaIdFixedLength != null)
            sb.append(this.cellGlobalIdOrServiceAreaIdFixedLength.toString());
        if (this.laiFixedLength != null)
            sb.append(this.laiFixedLength.toString());
        sb.append("]");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((cellGlobalIdOrServiceAreaIdFixedLength == null) ? 0 : cellGlobalIdOrServiceAreaIdFixedLength.hashCode());
        result = prime * result + ((laiFixedLength == null) ? 0 : laiFixedLength.hashCode());
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
        CellGlobalIdOrServiceAreaIdOrLAIImpl other = (CellGlobalIdOrServiceAreaIdOrLAIImpl) obj;
        if (cellGlobalIdOrServiceAreaIdFixedLength == null) {
            if (other.cellGlobalIdOrServiceAreaIdFixedLength != null)
                return false;
        } else if (!cellGlobalIdOrServiceAreaIdFixedLength.equals(other.cellGlobalIdOrServiceAreaIdFixedLength))
            return false;
        if (laiFixedLength == null) {
            if (other.laiFixedLength != null)
                return false;
        } else if (!laiFixedLength.equals(other.laiFixedLength))
            return false;
        return true;
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(cellGlobalIdOrServiceAreaIdFixedLength==null && laiFixedLength==null)
			throw new ASNParsingComponentException("either cell global Id or service area id fixed length or lai fixed length should be set for cell global id or service area id or LAI", ASNParsingComponentExceptionReason.MistypedParameter);						
	}
}