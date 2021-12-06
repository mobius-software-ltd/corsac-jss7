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

import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpcAv;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class EpsAuthenticationSetListImpl implements EpsAuthenticationSetList {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = EpcAvImpl.class)
	private List<EpcAv> epcAvs;

    public EpsAuthenticationSetListImpl() {
    }

    public EpsAuthenticationSetListImpl(List<EpcAv> epcAv) {
        this.epcAvs = epcAv;
    }

    public List<EpcAv> getEpcAv() {
        return epcAvs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EpsAuthenticationSetList [");

        if (this.epcAvs != null) {
            for (EpcAv at : this.epcAvs) {
                if (at != null) {
                    sb.append(at.toString());
                    sb.append(", ");
                }
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
