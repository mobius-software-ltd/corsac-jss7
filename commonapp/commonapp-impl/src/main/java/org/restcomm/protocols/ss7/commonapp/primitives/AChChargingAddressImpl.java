/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.primitives.AChChargingAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AChChargingAddressImpl implements AChChargingAddress {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 2,constructed = true, index = -1)
    private LegIDWrapperImpl legID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 50,constructed = false, index = -1)
    private ASNInteger srfConnection;

    public AChChargingAddressImpl() {
    }

    public AChChargingAddressImpl(LegID legID) {
    	if(legID!=null)
    		this.legID = new LegIDWrapperImpl(legID);
    }

    public AChChargingAddressImpl(int srfConnection) {
        this.srfConnection = new ASNInteger();
        this.srfConnection.setValue(Long.valueOf(srfConnection));
    }

    public LegID getLegID() {
    	if(legID==null)
    		return null;
    	
        return legID.getLegID();
    }

    public int getSrfConnection() {
    	if(srfConnection==null)
    		return 0;
    	
        return srfConnection.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AChChargingAddress [");

        if (legID != null && this.legID.getLegID()!=null) {
            sb.append("legID=[");
            sb.append(legID.getLegID().toString());
            sb.append("]");
        } else if (srfConnection != null && srfConnection.getValue()!=null && srfConnection.getValue()!=0) {
            sb.append("srfConnection=[");
            sb.append(srfConnection);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
