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

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ASNRequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
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

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private IMSIImpl imsi;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=1)
	private ASNInteger numberOfRequestedVectors;
    
	private ASNNull segmentationProhibited;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull immediateResponsePreferred;
    
    private ReSynchronisationInfoImpl reSynchronisationInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNRequestingNodeType requestingNodeType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private PlmnIdImpl requestingPlmnId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=54,constructed=false,index=-1)
    private ASNInteger numberOfRequestedAdditionalVectors;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ASNNull additionalVectorsAreForEPS;
    
    private long mapProtocolVersion;

    public SendAuthenticationInfoRequestImplV3(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendAuthenticationInfoRequestImplV3(long mapProtocolVersion, IMSIImpl imsi, int numberOfRequestedVectors,
            boolean segmentationProhibited, boolean immediateResponsePreferred, ReSynchronisationInfoImpl reSynchronisationInfo,
            MAPExtensionContainerImpl extensionContainer, RequestingNodeType requestingNodeType, PlmnIdImpl requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.imsi = imsi;
                
        this.numberOfRequestedVectors = new ASNInteger();
        this.numberOfRequestedVectors.setValue((long)numberOfRequestedVectors & 0x0FFFFFFFFL);
        
        if(segmentationProhibited)
        	this.segmentationProhibited = new ASNNull();
        
        if(immediateResponsePreferred)
        	this.immediateResponsePreferred = new ASNNull();
        
        this.reSynchronisationInfo = reSynchronisationInfo;
        this.extensionContainer = extensionContainer;
        
        if(requestingNodeType!=null) {
        	this.requestingNodeType = new ASNRequestingNodeType();
        	this.requestingNodeType.setType(requestingNodeType);
        }
        
        this.requestingPlmnId = requestingPlmnId;
        
        if(this.numberOfRequestedAdditionalVectors!=null) {
        	this.numberOfRequestedAdditionalVectors = new ASNInteger();
        	this.numberOfRequestedAdditionalVectors.setValue(this.numberOfRequestedAdditionalVectors.getValue().longValue());
        }
        
        if(additionalVectorsAreForEPS)
        	this.additionalVectorsAreForEPS = new ASNNull();
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.sendAuthenticationInfo_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.sendAuthenticationInfo;
    }

    public IMSIImpl getImsi() {
        return imsi;
    }

    public int getNumberOfRequestedVectors() {
    	if(numberOfRequestedVectors==null)
    		return 0;
    	
        return numberOfRequestedVectors.getValue().intValue();
    }

    public boolean getSegmentationProhibited() {
        return segmentationProhibited!=null;
    }

    public boolean getImmediateResponsePreferred() {
        return immediateResponsePreferred!=null;
    }

    public ReSynchronisationInfoImpl getReSynchronisationInfo() {
        return reSynchronisationInfo;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    public RequestingNodeType getRequestingNodeType() {
    	if(requestingNodeType==null)
    		return null;
    	
        return requestingNodeType.getType();
    }

    public PlmnIdImpl getRequestingPlmnId() {
        return requestingPlmnId;
    }

    public Integer getNumberOfRequestedAdditionalVectors() {
    	if(numberOfRequestedAdditionalVectors==null)
    		return null;
    	
        return numberOfRequestedAdditionalVectors.getValue().intValue();
    }

    public boolean getAdditionalVectorsAreForEPS() {
        return additionalVectorsAreForEPS!=null;
    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
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
        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }
}
