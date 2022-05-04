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

package org.restcomm.protocols.ss7.inap.charging;

import org.restcomm.protocols.ss7.inap.api.charging.SubTariffControl;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 *
 *
 * @author yulian.oifa
 *
 */
public class SubTariffControlIndicatorsImpl extends ASNBitString implements SubTariffControl {
	private static final int _ID_oneTimeCharge = 0;
    
    public SubTariffControlIndicatorsImpl() {
    	super("SubTariffControlIndicators",0,7,false);
    	
    }
    
    public SubTariffControlIndicatorsImpl(boolean oneTimeCharge) {
    	super("SubTariffControlIndicators",0,7,false);
        if (oneTimeCharge)
            this.setBit(_ID_oneTimeCharge);
    }
    
	@Override
	public boolean getOneTimeCharge() 
	{
		return this.isBitSet(_ID_oneTimeCharge);
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SubTariffControl [");

        if (getOneTimeCharge())
            sb.append("oneTimeCharge, ");
        
        sb.append("]");

        return sb.toString();
    }
}
