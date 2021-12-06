/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.Area;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaIdentification;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AreaImpl implements Area {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ASNAreaType areaType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=1,defaultImplementation = AreaIdentificationImpl.class)
    private AreaIdentification areaIdentification;

    /**
     *
     */
    public AreaImpl() {
    }

    /**
     * @param areaType
     * @param areaIdentification
     */
    public AreaImpl(AreaType areaType, AreaIdentification areaIdentification) {
        if(areaType!=null) {
        	this.areaType = new ASNAreaType();
        	this.areaType.setType(areaType);
        }
        
        this.areaIdentification = areaIdentification;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.Area#getAreaType()
     */
    public AreaType getAreaType() {
    	if(this.areaType==null)
    		return null;
    	
        return this.areaType.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.Area#getAreaIdentification ()
     */
    public AreaIdentification getAreaIdentification() {
        return this.areaIdentification;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((areaIdentification == null) ? 0 : areaIdentification.hashCode());
        result = prime * result + ((areaType == null) ? 0 : areaType.hashCode());
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
        AreaImpl other = (AreaImpl) obj;
        if (areaIdentification == null) {
            if (other.areaIdentification != null)
                return false;
        } else if (!areaIdentification.equals(other.areaIdentification))
            return false;

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Area [");

        if (this.areaType != null) {
            sb.append("areaType=");
            sb.append(this.areaType);
        }
        if (this.areaIdentification != null) {
            sb.append(", areaIdentification=");
            sb.append(this.areaIdentification.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
