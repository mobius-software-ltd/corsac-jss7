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
package org.restcomm.protocols.ss7.cap.EsiGprs;

import org.restcomm.protocols.ss7.cap.api.EsiGprs.DisconnectSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.InitiatingEntity;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ASNInitiatingEntityImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class DisconnectSpecificInformationImpl implements DisconnectSpecificInformation {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInitiatingEntityImpl initiatingEntity;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNNull routeingAreaUpdate;

    public DisconnectSpecificInformationImpl() {
    }

    public DisconnectSpecificInformationImpl(InitiatingEntity initiatingEntity, boolean routeingAreaUpdate) {
    	if(initiatingEntity!=null)
    		this.initiatingEntity = new ASNInitiatingEntityImpl(initiatingEntity);
    		
    	if(routeingAreaUpdate)
    		this.routeingAreaUpdate = new ASNNull();
    }

    public InitiatingEntity getInitiatingEntity() {
    	if(initiatingEntity==null)
    		return null;
    	
        return this.initiatingEntity.getType();
    }

    public boolean getRouteingAreaUpdate() {
        return this.routeingAreaUpdate!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DisconnectSpecificInformation [");

        if (this.initiatingEntity != null && this.initiatingEntity.getType()!=null) {
            sb.append("initiatingEntity=");
            sb.append(this.initiatingEntity.getType().toString());
            sb.append(", ");
        }

        if (this.routeingAreaUpdate!=null) {
            sb.append("routeingAreaUpdate ");
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
