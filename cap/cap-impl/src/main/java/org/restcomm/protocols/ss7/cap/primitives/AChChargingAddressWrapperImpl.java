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

package org.restcomm.protocols.ss7.cap.primitives;

import org.restcomm.protocols.ss7.cap.api.primitives.AChChargingAddress;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AChChargingAddressWrapperImpl {
	@ASNChoise
	private AChChargingAddressImpl aChChargingAddress;

    public AChChargingAddressWrapperImpl() {
    }

    public AChChargingAddressWrapperImpl(AChChargingAddress aChChargingAddress) {
    	if(aChChargingAddress!=null) {
    		if(aChChargingAddress instanceof AChChargingAddressImpl)
    			this.aChChargingAddress = (AChChargingAddressImpl)aChChargingAddress;
    		else if(aChChargingAddress.getLegID()!=null)
    			this.aChChargingAddress = new AChChargingAddressImpl(aChChargingAddress.getLegID());
    		else if(aChChargingAddress.getSrfConnection()!=0)
    			this.aChChargingAddress = new AChChargingAddressImpl(aChChargingAddress.getSrfConnection());
    	}
    }

    public AChChargingAddress getAChChargingAddress() {
    	return aChChargingAddress;
    }
}
