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

package org.restcomm.protocols.ss7.map.service.lsm;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPPrivateExtension;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPPrivateExtensionsListWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgPCSExtensions;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SLRArgExtensionContainerImpl implements SLRArgExtensionContainer {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private MAPPrivateExtensionsListWrapperImpl privateExtensionList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = SLRArgPCSExtensionsImpl.class)
    private SLRArgPCSExtensions slrArgPcsExtensions;

    public SLRArgExtensionContainerImpl() {
    }

    public SLRArgExtensionContainerImpl(List<MAPPrivateExtension> privateExtensionList,
    		SLRArgPCSExtensions slrArgPcsExtensions) {
        
    	if(privateExtensionList!=null) {
    		this.privateExtensionList = new MAPPrivateExtensionsListWrapperImpl(privateExtensionList);
    	}
    	
        this.slrArgPcsExtensions = slrArgPcsExtensions;
    }

    public List<MAPPrivateExtension> getPrivateExtensionList() {
    	if(privateExtensionList==null)
    		return null;
    	
        return privateExtensionList.getMAPPrivateExtensions();
    }

    public SLRArgPCSExtensions getSlrArgPcsExtensions() {
        return slrArgPcsExtensions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SLRArgExtensionContainer [");

        if (this.privateExtensionList != null && this.privateExtensionList.getMAPPrivateExtensions()!=null) {
            sb.append("privateExtensionList [");
            for (MAPPrivateExtension pe : this.privateExtensionList.getMAPPrivateExtensions()) {
                sb.append(pe.toString());
                sb.append(", ");
            }
            sb.append("]");
        }

        if (this.slrArgPcsExtensions != null) {
            sb.append(", slrArgPcsExtensions=");
            sb.append(this.slrArgPcsExtensions);
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(privateExtensionList!=null && privateExtensionList.getMAPPrivateExtensions()!=null && privateExtensionList.getMAPPrivateExtensions().size()>10)
			throw new ASNParsingComponentException("private extensions list size should be between 1 and 10 for slr arg extension container", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}