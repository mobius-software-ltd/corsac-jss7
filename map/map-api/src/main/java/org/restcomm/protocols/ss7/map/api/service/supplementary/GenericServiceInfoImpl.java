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

package org.restcomm.protocols.ss7.map.api.service.supplementary;

import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.primitives.ASNEMLPPPriorityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GenericServiceInfoImpl {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private SSStatusImpl ssStatus;
    
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

    public GenericServiceInfoImpl(SSStatusImpl ssStatus, CliRestrictionOption cliRestrictionOption, EMLPPPriority maximumEntitledPriority,
            EMLPPPriority defaultPriority, ArrayList<CCBSFeatureImpl> ccbsFeatureList, Integer nbrSB, Integer nbrUser, Integer nbrSN) {
        this.ssStatus = ssStatus;
        
        if(cliRestrictionOption!=null) {
        	this.cliRestrictionOption = new ASNCliRestrictionOptionImpl();
        	this.cliRestrictionOption.setType(cliRestrictionOption);
        }
        
        if(this.maximumEntitledPriority!=null) {
        	this.maximumEntitledPriority = new ASNEMLPPPriorityImpl();
        	this.maximumEntitledPriority.setType(maximumEntitledPriority);
        }
        
        if(this.defaultPriority!=null) {
        	this.defaultPriority = new ASNEMLPPPriorityImpl();
        	this.defaultPriority.setType(defaultPriority);
        }
        
        if(ccbsFeatureList!=null)
        	this.ccbsFeatureList = new CCBSFeatureListWrapperImpl(ccbsFeatureList);
        
        if(nbrSB!=null) {
        	this.nbrSB = new ASNInteger();
        	this.nbrSB.setValue(nbrSB.longValue());
        }
        
        if(nbrUser!=null) {
        	this.nbrUser = new ASNInteger();
        	this.nbrUser.setValue(nbrUser.longValue());
        }
        
        if(nbrSN!=null) {
        	this.nbrSN = new ASNInteger();
        	this.nbrSN.setValue(nbrSN.longValue());
        }
    }

    public SSStatusImpl getSsStatus() {
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

    public ArrayList<CCBSFeatureImpl> getCcbsFeatureList() {
    	if(ccbsFeatureList==null)
    		return null;
    	
        return ccbsFeatureList.getCCBSFeatures();
    }

    public Integer getNbrSB() {
    	if(nbrSB==null)
    		return null;
    	
        return nbrSB.getValue().intValue();
    }

    public Integer getNbrUser() {
    	if(nbrUser==null)
    		return null;
    	
        return nbrUser.getValue().intValue();
    }

    public Integer getNbrSN() {
    	if(nbrSN==null)
    		return null;
    	
        return nbrSN.getValue().intValue();
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
            for (CCBSFeatureImpl be : this.ccbsFeatureList.getCCBSFeatures()) {
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

}
