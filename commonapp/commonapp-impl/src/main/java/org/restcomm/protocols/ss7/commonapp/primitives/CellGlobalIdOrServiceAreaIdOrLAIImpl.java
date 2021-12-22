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

package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;


/**
 * @author amit bhayani
 * @author sergey vetyutnev
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
}
