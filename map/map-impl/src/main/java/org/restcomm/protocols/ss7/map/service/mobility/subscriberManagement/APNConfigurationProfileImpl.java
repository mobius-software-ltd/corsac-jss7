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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNConfiguration;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNConfigurationProfile;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class APNConfigurationProfileImpl implements APNConfigurationProfile {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=0)
    private ASNInteger defaultContext;
    private ASNNull completeDataListIncluded;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private APNConfigurationListWrapperImpl ePSDataList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public APNConfigurationProfileImpl() {
    }

    public APNConfigurationProfileImpl(int defaultContext, boolean completeDataListIncluded,
            List<APNConfiguration> ePSDataList, MAPExtensionContainer extensionContainer) {
        this.defaultContext = new ASNInteger(defaultContext,"DefaultContext",1,50,false);
        
        if(completeDataListIncluded)
        	this.completeDataListIncluded = new ASNNull();
        
        if(ePSDataList!=null)
        	this.ePSDataList = new APNConfigurationListWrapperImpl(ePSDataList);
        
        this.extensionContainer = extensionContainer;
    }

    public int getDefaultContext() {
    	if(this.defaultContext==null || this.defaultContext.getValue()==null)
    		return 0;
    	
        return this.defaultContext.getIntValue();
    }

    public boolean getCompleteDataListIncluded() {
        return this.completeDataListIncluded!=null;
    }

    public List<APNConfiguration> getEPSDataList() {
    	if(this.ePSDataList==null)
    		return null;
    	
        return this.ePSDataList.getAPNConfiguration();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("APNConfigurationProfile [");

        if(this.defaultContext!=null) {
	        sb.append("defaultContext=");
	        sb.append(this.defaultContext.getValue());
	        sb.append(", ");
        }
        
        if (this.completeDataListIncluded!=null) {
            sb.append("completeDataListIncluded, ");
        }

        if (this.ePSDataList != null && this.ePSDataList.getAPNConfiguration()!=null) {
            sb.append("ePSDataList=[");
            boolean firstItem = true;
            for (APNConfiguration be : this.ePSDataList.getAPNConfiguration()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(defaultContext==null)
			throw new ASNParsingComponentException("default context should be set for apn configuration", ASNParsingComponentExceptionReason.MistypedParameter);

		if(ePSDataList==null || ePSDataList.getAPNConfiguration()==null || ePSDataList.getAPNConfiguration().size()==0)
			throw new ASNParsingComponentException("eps data list should be set for apn configuration", ASNParsingComponentExceptionReason.MistypedParameter);

		if(ePSDataList.getAPNConfiguration().size()>50)
			throw new ASNParsingComponentException("eps data list size should be betwen 1 and 50 for apn configuration", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
