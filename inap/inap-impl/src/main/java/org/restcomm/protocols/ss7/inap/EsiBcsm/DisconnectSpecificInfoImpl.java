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

package org.restcomm.protocols.ss7.inap.EsiBcsm;

import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.DisconnectSpecificInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class DisconnectSpecificInfoImpl implements DisconnectSpecificInfo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = CauseIsupImpl.class)
    private CauseIsup releaseCause;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger connectTime;

	public DisconnectSpecificInfoImpl() {
    }

    public DisconnectSpecificInfoImpl(CauseIsup releaseCause,Integer connectTime) {
        this.releaseCause = releaseCause;
        
        if(connectTime!=null)
        	this.connectTime = new ASNInteger(connectTime,"ConnectTime",0,255,false);        	
    }

    public Integer getConnectTime() {
    	if(connectTime==null)
    		return null;
    	
        return connectTime.getIntValue();
    }

    public CauseIsup getReleaseCause() {
        return releaseCause;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DisconnectSpecificInfo [");
        
        if (this.connectTime != null) {
            sb.append("connectTime=");
            sb.append(connectTime.getValue().toString());
            sb.append(", ");
        }
        
        if (this.releaseCause != null) {
            sb.append("releaseCause= [");
            sb.append(releaseCause);
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }
}
