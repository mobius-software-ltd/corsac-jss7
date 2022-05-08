/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.service.supplementary;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CCBSFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GenericServiceInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.primitives.ASNEMLPPPriorityImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GenericServiceInfoImpl implements GenericServiceInfo {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = SSStatusImpl.class)
	private SSStatus ssStatus;
    
	private ASNCliRestrictionOptionImpl cliRestrictionOption;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNEMLPPPriorityImpl maximumEntitledPriority;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNEMLPPPriorityImpl defaultPriority;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private CCBSFeatureListWrapperImpl ccbsFeatureList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNInteger nbrSB;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNInteger nbrUser;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNInteger nbrSN;

    public GenericServiceInfoImpl() {
    }

    public GenericServiceInfoImpl(SSStatus ssStatus, CliRestrictionOption cliRestrictionOption, EMLPPPriority maximumEntitledPriority,
            EMLPPPriority defaultPriority, List<CCBSFeature> ccbsFeatureList, Integer nbrSB, Integer nbrUser, Integer nbrSN) {
        this.ssStatus = ssStatus;
        
        if(cliRestrictionOption!=null)
        	this.cliRestrictionOption = new ASNCliRestrictionOptionImpl(cliRestrictionOption);
        	
        if(maximumEntitledPriority!=null)
        	this.maximumEntitledPriority = new ASNEMLPPPriorityImpl(maximumEntitledPriority);
        	
        if(defaultPriority!=null)
        	this.defaultPriority = new ASNEMLPPPriorityImpl(defaultPriority);
        	
        if(ccbsFeatureList!=null)
        	this.ccbsFeatureList = new CCBSFeatureListWrapperImpl(ccbsFeatureList);
        
        if(nbrSB!=null)
        	this.nbrSB = new ASNInteger(nbrSB,"NBRSB",2,7,false);
        	
        if(nbrUser!=null)
        	this.nbrUser = new ASNInteger(nbrUser,"NBRUser",1,7,false);
        	
        if(nbrSN!=null)
        	this.nbrSN = new ASNInteger(nbrSN,"NBRSN",1,7,false);        	
    }

    public SSStatus getSsStatus() {
        return ssStatus;
    }

    public CliRestrictionOption getCliRestrictionOption() {
    	if(cliRestrictionOption==null)
    		return null;
    	
        return cliRestrictionOption.getType();
    }

    public EMLPPPriority getMaximumEntitledPriority() {
    	if(maximumEntitledPriority==null)
    		return null;
    	
        return maximumEntitledPriority.getType();
    }

    public EMLPPPriority getDefaultPriority() {
    	if(defaultPriority==null)
    		return null;
    	
        return defaultPriority.getType();
    }

    public List<CCBSFeature> getCcbsFeatureList() {
    	if(ccbsFeatureList==null)
    		return null;
    	
        return ccbsFeatureList.getCCBSFeatures();
    }

    public Integer getNbrSB() {
    	if(nbrSB==null)
    		return null;
    	
        return nbrSB.getIntValue();
    }

    public Integer getNbrUser() {
    	if(nbrUser==null)
    		return null;
    	
        return nbrUser.getIntValue();
    }

    public Integer getNbrSN() {
    	if(nbrSN==null)
    		return null;
    	
        return nbrSN.getIntValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GenericServiceInfo [");

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(ssStatus);
            sb.append(", ");
        }
        if (this.cliRestrictionOption != null) {
            sb.append("cliRestrictionOption=");
            sb.append(cliRestrictionOption.getType());
            sb.append(", ");
        }
        if (this.maximumEntitledPriority != null) {
            sb.append("maximumEntitledPriority=");
            sb.append(maximumEntitledPriority.getType());
            sb.append(", ");
        }
        if (this.defaultPriority != null) {
            sb.append("defaultPriority=");
            sb.append(defaultPriority.getType());
            sb.append(", ");
        }
        if (this.ccbsFeatureList != null && this.ccbsFeatureList.getCCBSFeatures()!=null) {
            sb.append("ccbsFeatureList=[");
            boolean firstItem = true;
            for (CCBSFeature be : this.ccbsFeatureList.getCCBSFeatures()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }
        if (this.nbrSB != null) {
            sb.append("nbrSB=");
            sb.append(nbrSB.getValue());
            sb.append(", ");
        }
        if (this.nbrUser != null) {
            sb.append("nbrUser=");
            sb.append(nbrUser.getValue());
            sb.append(", ");
        }
        if (this.nbrSN != null) {
            sb.append("nbrSN=");
            sb.append(nbrSN.getValue());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(ssStatus==null)
			throw new ASNParsingComponentException("SS status should be set for generic service info", ASNParsingComponentExceptionReason.MistypedParameter);
		
		if(ccbsFeatureList!=null && ccbsFeatureList.getCCBSFeatures()!=null && ccbsFeatureList.getCCBSFeatures().size()>5)
			throw new ASNParsingComponentException("CCBS feature list should be between 1 and 5 for generic service info", ASNParsingComponentExceptionReason.MistypedParameter);
		
	}

}