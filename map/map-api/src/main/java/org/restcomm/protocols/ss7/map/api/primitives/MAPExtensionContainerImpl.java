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

package org.restcomm.protocols.ss7.map.api.primitives;

import java.util.ArrayList;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPExtensionContainerImpl {
	
	private ArrayList<MAPPrivateExtensionImpl> privateExtensionList;
    private ASNPCSExtentionImpl pcsExtensions;

    public MAPExtensionContainerImpl() {
    }

    public MAPExtensionContainerImpl(ArrayList<MAPPrivateExtensionImpl> privateExtensionList, ASNPCSExtentionImpl pcsExtensions) {
        this.privateExtensionList = privateExtensionList;
        this.pcsExtensions = pcsExtensions;
    }

    public ArrayList<MAPPrivateExtensionImpl> getPrivateExtensionList() {
        return this.privateExtensionList;
    }

    public void setPrivateExtensionList(ArrayList<MAPPrivateExtensionImpl> privateExtensionList) {
        this.privateExtensionList = privateExtensionList;
    }

    public ASNPCSExtentionImpl getPcsExtensions() {
        return this.pcsExtensions;
    }

    public void setPcsExtensions(ASNPCSExtentionImpl pcsExtensions) {
        this.pcsExtensions = pcsExtensions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Extension Container [");
        sb.append(" [");

        if (this.privateExtensionList != null && this.privateExtensionList.size() > 0) {
            for (MAPPrivateExtensionImpl pe : this.privateExtensionList) {
                sb.append("\n");
                sb.append(pe.toString());
            }
        }

        if (this.pcsExtensions != null) {
            sb.append("\nPcsExtensions=");
            sb.append(this.pcsExtensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pcsExtensions==null) ? 0 : pcsExtensions.hashCode());
        result = prime * result + ((privateExtensionList == null) ? 0 : privateExtensionList.hashCode());
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
        MAPExtensionContainerImpl other = (MAPExtensionContainerImpl) obj;
        if (pcsExtensions == null) {
        	if (other.pcsExtensions != null)
        		return false;
        } else if (!pcsExtensions.equals(other.pcsExtensions))
        	return false;
            
        if (privateExtensionList == null) {
            if (other.privateExtensionList != null)
                return false;
        } else if (!privateExtensionList.equals(other.privateExtensionList))
            return false;
        return true;
    }
}
