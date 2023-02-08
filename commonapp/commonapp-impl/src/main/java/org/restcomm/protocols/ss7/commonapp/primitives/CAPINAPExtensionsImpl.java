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

package org.restcomm.protocols.ss7.commonapp.primitives;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ExtensionField;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CAPINAPExtensionsImpl implements CAPINAPExtensions {
	
	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,index = -1,defaultImplementation = ExtensionFieldImpl.class)
	private List<ExtensionField> extensionFields;

    public CAPINAPExtensionsImpl() {
    }

    public CAPINAPExtensionsImpl(List<ExtensionField> fieldsList) {
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(extensionFields==null || extensionFields.size()==0)
			throw new ASNParsingComponentException("extension fields should be set for extensions", ASNParsingComponentExceptionReason.MistypedParameter);				
		
		if(extensionFields.size()>10)
			throw new ASNParsingComponentException("extension fields count should be between 1 and 10 for extensions", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
