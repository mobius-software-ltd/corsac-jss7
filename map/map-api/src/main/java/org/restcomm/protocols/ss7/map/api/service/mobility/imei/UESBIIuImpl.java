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

package org.restcomm.protocols.ss7.map.api.service.mobility.imei;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author normandes
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class UESBIIuImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private UESBIIuAImpl uesbiIuA;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private UESBIIuBImpl uesbiIuB;

    public UESBIIuImpl() {        
    }

    public UESBIIuImpl(UESBIIuAImpl uesbiIuA, UESBIIuBImpl uesbiIuB) {
        this.uesbiIuA = uesbiIuA;
        this.uesbiIuB = uesbiIuB;
    }

    public UESBIIuAImpl getUESBI_IuA() {
        return this.uesbiIuA;
    }

    public UESBIIuBImpl getUESBI_IuB() {
        return this.uesbiIuB;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UESBIIu [");
        if (this.uesbiIuA != null) {
            sb.append("uesbiIuA=");
            sb.append(this.uesbiIuA);
            sb.append(", ");
        }

        if (this.uesbiIuB != null) {
            sb.append("uesbiIuB=");
            sb.append(this.uesbiIuB);
        }
        sb.append("]");

        return sb.toString();
    }

}
