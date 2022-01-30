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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class UpdateGprsLocationRequestImpl extends MobilityMessageImpl implements UpdateGprsLocationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = IMSIImpl.class)
	private IMSI imsi;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1, defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString sgsnNumber;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=2, defaultImplementation = GSNAddressImpl.class)
	private GSNAddress sgsnAddress;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1, defaultImplementation = SGSNCapabilityImpl.class)
    private SGSNCapability sgsnCapability;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull informPreviousNetworkEntity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull psLCSNotSupportedByUE;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress vGmlcAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1, defaultImplementation = ADDInfoImpl.class)
    private ADDInfo addInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1)
    private EPSInfoWrapperImpl epsInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNNull servingNodeTypeIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNNull skipSubscriberDataUpdate;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNUsedRatTypeImpl usedRATType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private ASNNull gprsSubscriptionDataNotNeeded;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private ASNNull nodeTypeIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1)
    private ASNNull areaRestricted;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1)
    private ASNNull ueReachableIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=false,index=-1)
    private ASNNull epsSubscriptionDataNotNeeded;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1)
    private ASNUESRVCCCapabilityImpl uesrvccCapability;

    public UpdateGprsLocationRequestImpl() {
        super();
    }

    public UpdateGprsLocationRequestImpl(IMSI imsi, ISDNAddressString sgsnNumber, GSNAddress sgsnAddress,
    		MAPExtensionContainer extensionContainer, SGSNCapability sgsnCapability, boolean informPreviousNetworkEntity,
            boolean psLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo, EPSInfo epsInfo,
            boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability) {
        super();
        this.imsi = imsi;
        this.sgsnNumber = sgsnNumber;
        this.sgsnAddress = sgsnAddress;
        this.extensionContainer = extensionContainer;
        this.sgsnCapability = sgsnCapability;
        
        if(informPreviousNetworkEntity)
        	this.informPreviousNetworkEntity = new ASNNull();
        
        if(psLCSNotSupportedByUE)
        	this.psLCSNotSupportedByUE = new ASNNull();
        
        this.vGmlcAddress = vGmlcAddress;
        this.addInfo = addInfo;
        
        if(epsInfo!=null)
        	this.epsInfo = new EPSInfoWrapperImpl(epsInfo);
        
        if(servingNodeTypeIndicator)
        	this.servingNodeTypeIndicator = new ASNNull();
        
        if(skipSubscriberDataUpdate)
        	this.skipSubscriberDataUpdate = new ASNNull();
        
        if(usedRATType!=null)
        	this.usedRATType = new ASNUsedRatTypeImpl(usedRATType);
        	
        if(gprsSubscriptionDataNotNeeded)
        	this.gprsSubscriptionDataNotNeeded = new ASNNull();
        
        if(nodeTypeIndicator)
        	this.nodeTypeIndicator = new ASNNull();
        
        if(areaRestricted)
        	this.areaRestricted = new ASNNull();
                
        if(ueReachableIndicator)
        	this.ueReachableIndicator = new ASNNull();
        
        if(epsSubscriptionDataNotNeeded)
        	this.epsSubscriptionDataNotNeeded = new ASNNull();
        
        if(uesrvccCapability!=null)
        	this.uesrvccCapability = new ASNUESRVCCCapabilityImpl(uesrvccCapability);        	
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.updateGprsLocation_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.updateGprsLocation;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public ISDNAddressString getSgsnNumber() {
        return this.sgsnNumber;
    }

    @Override
    public GSNAddress getSgsnAddress() {
        return this.sgsnAddress;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public SGSNCapability getSGSNCapability() {
        return this.sgsnCapability;
    }

    @Override
    public boolean getInformPreviousNetworkEntity() {
        return this.informPreviousNetworkEntity!=null;
    }

    @Override
    public boolean getPsLCSNotSupportedByUE() {
        return this.psLCSNotSupportedByUE!=null;
    }

    @Override
    public GSNAddress getVGmlcAddress() {
        return this.vGmlcAddress;
    }

    @Override
    public ADDInfo getADDInfo() {
        return this.addInfo;
    }

    @Override
    public EPSInfo getEPSInfo() {
    	if(this.epsInfo==null)
    		return null;
    	
        return this.epsInfo.getEPSInfo();
    }

    @Override
    public boolean getServingNodeTypeIndicator() {
        return this.servingNodeTypeIndicator!=null;
    }

    @Override
    public boolean getSkipSubscriberDataUpdate() {
        return this.skipSubscriberDataUpdate!=null;
    }

    @Override
    public UsedRATType getUsedRATType() {
    	if(this.usedRATType==null)
    		return null;
    	
        return this.usedRATType.getType();
    }

    @Override
    public boolean getGprsSubscriptionDataNotNeeded() {
        return this.gprsSubscriptionDataNotNeeded!=null;
    }

    @Override
    public boolean getNodeTypeIndicator() {
        return this.nodeTypeIndicator!=null;
    }

    @Override
    public boolean getAreaRestricted() {
        return this.areaRestricted!=null;
    }

    @Override
    public boolean getUeReachableIndicator() {
        return this.ueReachableIndicator!=null;
    }

    @Override
    public boolean getEpsSubscriptionDataNotNeeded() {
        return this.epsSubscriptionDataNotNeeded!=null;
    }

    @Override
    public UESRVCCCapability getUESRVCCCapability() {
    	if(this.uesrvccCapability==null)
    		return null;
    	
        return this.uesrvccCapability.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateGprsLocationRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(this.imsi.toString());
            sb.append(", ");
        }

        if (this.sgsnNumber != null) {
            sb.append("sgsnNumber=");
            sb.append(this.sgsnNumber.toString());
            sb.append(", ");
        }

        if (this.sgsnAddress != null) {
            sb.append("sgsnAddress=");
            sb.append(this.sgsnAddress.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.sgsnCapability != null) {
            sb.append("sgsnCapability=");
            sb.append(this.sgsnCapability.toString());
            sb.append(", ");
        }

        if (this.informPreviousNetworkEntity!=null) {
            sb.append("informPreviousNetworkEntity, ");
        }

        if (this.psLCSNotSupportedByUE!=null) {
            sb.append("psLCSNotSupportedByUE, ");
        }

        if (this.vGmlcAddress != null) {
            sb.append("vGmlcAddress=");
            sb.append(this.vGmlcAddress.toString());
            sb.append(", ");
        }

        if (this.addInfo != null) {
            sb.append("addInfo=");
            sb.append(this.addInfo.toString());
            sb.append(", ");
        }

        if (this.epsInfo != null && this.epsInfo.getEPSInfo()!=null) {
            sb.append("epsInfo=");
            sb.append(this.epsInfo.getEPSInfo());
            sb.append(", ");
        }

        if (this.servingNodeTypeIndicator!=null) {
            sb.append("servingNodeTypeIndicator, ");
        }

        if (this.skipSubscriberDataUpdate!=null) {
            sb.append("skipSubscriberDataUpdate, ");
        }

        if (this.usedRATType != null) {
            sb.append("usedRATType=");
            sb.append(this.usedRATType.getType());
            sb.append(", ");
        }

        if (this.gprsSubscriptionDataNotNeeded!=null) {
            sb.append("gprsSubscriptionDataNotNeeded, ");
        }

        if (this.nodeTypeIndicator!=null) {
            sb.append("nodeTypeIndicator, ");
        }

        if (this.areaRestricted!=null) {
            sb.append("areaRestricted, ");
        }

        if (this.ueReachableIndicator!=null) {
            sb.append("ueReachableIndicator, ");
        }

        if (this.epsSubscriptionDataNotNeeded!=null) {
            sb.append("epsSubscriptionDataNotNeeded, ");
        }

        if (this.uesrvccCapability != null) {
            sb.append("uesrvccCapability=");
            sb.append(this.uesrvccCapability.getType());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
