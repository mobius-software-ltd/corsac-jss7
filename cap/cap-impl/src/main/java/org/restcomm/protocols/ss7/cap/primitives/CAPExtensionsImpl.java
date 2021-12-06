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

package org.restcomm.protocols.ss7.cap.primitives;

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.restcomm.protocols.ss7.cap.api.primitives.ExtensionField;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CAPExtensionsImpl implements CAPExtensions {
	
	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,index = -1,defaultImplementation = ExtensionFieldImpl.class)
	private List<ExtensionField> extensionFields;

    public CAPExtensionsImpl() {
    }

    public CAPExtensionsImpl(List<ExtensionField> fieldsList) {
        this.extensionFields = fieldsList;
    }

    public List<ExtensionField> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<ExtensionField> fieldsList) {
        this.extensionFields = fieldsList;
    }
    
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CAPExtensions [");

        if (this.extensionFields != null) {
            boolean isFirst = true;
            for (ExtensionField fld : this.extensionFields) {
                if (isFirst)
                    isFirst = false;
                else
                    sb.append("\n");
                sb.append(fld.toString());
            }
        }
        sb.append("]");

        return sb.toString();
    }
}
