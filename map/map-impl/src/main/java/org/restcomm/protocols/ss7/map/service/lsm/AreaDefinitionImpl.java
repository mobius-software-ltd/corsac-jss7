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

import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.lsm.Area;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaDefinition;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AreaDefinitionImpl implements AreaDefinition {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private AreaListWrapperImpl areaList = null;

    /**
     *
     */
    public AreaDefinitionImpl() {
    }

    /**
     * @param areaList
     */
    public AreaDefinitionImpl(List<Area> areaList) {
        if(areaList!=null)
        	this.areaList = new AreaListWrapperImpl(areaList);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.AreaDefinition#getAreaList ()
     */
    public List<Area> getAreaList() {
    	if(this.areaList==null)
    		return null;
    	
        return this.areaList.getArea();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (this.areaList != null && this.areaList.getArea()!=null) {
            for (int i = 0; i < areaList.getArea().size(); i++) {
                Area a1 = areaList.getArea().get(i);
                result = prime * result + ((a1 == null) ? 0 : a1.hashCode());
            }
        }
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
        AreaDefinitionImpl other = (AreaDefinitionImpl) obj;
        if (areaList == null || areaList.getArea() == null) {
            if (other.areaList != null && other.areaList.getArea()!=null)
                return false;
        } else {
            if (areaList.getArea().size() != other.areaList.getArea().size())
                return false;
            for (int i = 0; i < areaList.getArea().size(); i++) {
                Area a1 = areaList.getArea().get(i);
                Area a2 = other.areaList.getArea().get(i);
                if (a1 == null) {
                    if (a2 != null)
                        return false;
                } else {
                    if (!a1.equals(a2))
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AreaDefinition [");

        if (this.areaList != null && this.areaList.getArea()!=null) {
            sb.append("areaList [");
            for (Area a1:this.areaList.getArea()) {
                sb.append("area=");
                sb.append(a1);
                sb.append(", ");
            }
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }
}
