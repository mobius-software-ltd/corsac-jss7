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

package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendAuthenticationInfoRequestImplV3 extends MobilityMessageImpl implements SendAuthenticationInfoRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0, defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=1)
	private ASNInteger numberOfRequestedVectors;
    
	private ASNNull segmentationProhibited;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull immediateResponsePreferred;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = ReSynchronisationInfoImpl.class)
    private ReSynchronisationInfo reSynchronisationInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNRequestingNodeType requestingNodeType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1,defaultImplementation = PlmnIdImpl.class)
    private PlmnId requestingPlmnId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNInteger numberOfRequestedAdditionalVectors;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNNull additionalVectorsAreForEPS;
    
    public SendAuthenticationInfoRequestImplV3() {
    }
    
    public SendAuthenticationInfoRequestImplV3(IMSI imsi, int numberOfRequestedVectors,
            boolean segmentationProhibited, boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
            MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) {
        this.imsi = imsi;
                
        this.numberOfRequestedVectors = new ASNInteger(numberOfRequestedVectors,"NumberOfRequestedVectors",1,5,false);
        
        if(segmentationProhibited)
        	this.segmentationProhibited = new ASNNull();
        
        if(immediateResponsePreferred)
        	this.immediateResponsePreferred = new ASNNull();
        
        this.reSynchronisationInfo = reSynchronisationInfo;
        this.extensionContainer = extensionContainer;
        
        if(requestingNodeType!=null)
        	this.requestingNodeType = new ASNRequestingNodeType(requestingNodeType);
        	
        this.requestingPlmnId = requestingPlmnId;
        
        if(numberOfRequestedAdditionalVectors!=null)
        	this.numberOfRequestedAdditionalVectors = new ASNInteger(numberOfRequestedAdditionalVectors,"NumberOfRequestedAdditionalVectors",1,5,false);
        	
        if(additionalVectorsAreForEPS)
        	this.additionalVectorsAreForEPS = new ASNNull();
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.sendAuthenticationInfo_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.sendAuthenticationInfo;
    }

    public IMSI getImsi() {
        return imsi;
    }

    public int getNumberOfRequestedVectors() {
    	if(numberOfRequestedVectors==null || numberOfRequestedVectors.getValue()==null)
    		return 0;
    	
        return numberOfRequestedVectors.getIntValue();
    }

    public boolean getSegmentationProhibited() {
        return segmentationProhibited!=null;
    }

    public boolean getImmediateResponsePreferred() {
        return immediateResponsePreferred!=null;
    }

    public ReSynchronisationInfo getReSynchronisationInfo() {
        return reSynchronisationInfo;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public RequestingNodeType getRequestingNodeType() {
    	if(requestingNodeType==null)
    		return null;
    	
        return requestingNodeType.getType();
    }

    public PlmnId getRequestingPlmnId() {
        return requestingPlmnId;
    }

    public Integer getNumberOfRequestedAdditionalVectors() {
    	if(numberOfRequestedAdditionalVectors==null)
    		return null;
    	
        return numberOfRequestedAdditionalVectors.getIntValue();
    }

    public boolean getAdditionalVectorsAreForEPS() {
        return additionalVectorsAreForEPS!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendAuthenticationInfoRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi.toString());
            sb.append(", ");
        }
        
        if(this.numberOfRequestedVectors!=null) {
	        sb.append("numberOfRequestedVectors=");
	        sb.append(numberOfRequestedVectors.getValue());
	        sb.append(", ");
        }
        
        if (this.segmentationProhibited!=null) {
            sb.append("segmentationProhibited, ");
        }
        if (this.immediateResponsePreferred!=null) {
            sb.append("immediateResponsePreferred, ");
        }
        if (this.reSynchronisationInfo != null) {
            sb.append("reSynchronisationInfo=");
            sb.append(reSynchronisationInfo.toString());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }
        if (this.requestingNodeType != null) {
            sb.append("requestingNodeType=");
            sb.append(requestingNodeType.getValue());
            sb.append(", ");
        }
        if (this.requestingPlmnId != null) {
            sb.append("requestingPlmnId=");
            sb.append(requestingPlmnId.toString());
            sb.append(", ");
        }
        if (this.numberOfRequestedAdditionalVectors != null) {
            sb.append("numberOfRequestedAdditionalVectors=");
            sb.append(numberOfRequestedAdditionalVectors.toString());
            sb.append(", ");
        }
        if (this.additionalVectorsAreForEPS!=null) {
            sb.append("additionalVectorsAreForEPS, ");
        }
        
        sb.append("]");

        return sb.toString();
    }
}
