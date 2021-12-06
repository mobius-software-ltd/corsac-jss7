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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.QuintupletList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.TripletList;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AuthenticationSetListImpl implements AuthenticationSetList {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1, defaultImplementation = TripletListImpl.class)
    private TripletList tripletList;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = TripletListImpl.class)
	private TripletList tripletList2;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = QuintupletListImpl.class)
    private QuintupletList quintupletList;
    
    public AuthenticationSetListImpl() {
    }

    public AuthenticationSetListImpl(TripletList tripletList,long mapVersion) {
    	if(mapVersion>=3)
    		this.tripletList = tripletList;
    	else
    		this.tripletList2 = tripletList;
    }

    public AuthenticationSetListImpl(QuintupletList quintupletList) {
        this.quintupletList = quintupletList;
    }

    public TripletList getTripletList() {
    	if(tripletList==null)
    		return tripletList2;
    	
        return tripletList;
    }

    public QuintupletList getQuintupletList() {
        return quintupletList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthenticationSetList [");

        if (this.tripletList != null) {
            sb.append(this.tripletList.toString());
            sb.append(", ");
        } else if (this.tripletList2 != null) {
            sb.append(this.tripletList2.toString());
            sb.append(", ");
        }

        if (this.quintupletList != null) {
            sb.append(this.quintupletList.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}
