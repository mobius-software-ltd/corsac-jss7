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

package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class EPSInfoImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private PDNGWUpdateImpl pndGwUpdate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ISRInformationImpl isrInformation;

    public EPSInfoImpl() {
        super();
    }

    public EPSInfoImpl(PDNGWUpdateImpl pndGwUpdate) {
        this.pndGwUpdate = pndGwUpdate;
        this.isrInformation = null;
    }

    public EPSInfoImpl(ISRInformationImpl isrInformation) {
        this.pndGwUpdate = null;
        this.isrInformation = isrInformation;
    }

    public PDNGWUpdateImpl getPndGwUpdate() {
        return this.pndGwUpdate;
    }

    public ISRInformationImpl getIsrInformation() {
        return this.isrInformation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EPSInfo [");

        if (this.pndGwUpdate != null) {
            sb.append("pndGwUpdate=");
            sb.append(this.pndGwUpdate.toString());
        }

        if (this.isrInformation != null) {
            sb.append("isrInformation=");
            sb.append(this.isrInformation.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
