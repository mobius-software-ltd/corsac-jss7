/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageFacilityNotSup;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageFacilityNotSupImpl extends MAPErrorMessageImpl implements MAPErrorMessageFacilityNotSup {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull shapeOfLocationEstimateNotSupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull neededLcsCapabilityNotSupportedInServingNode;

    public MAPErrorMessageFacilityNotSupImpl(MAPExtensionContainer extensionContainer,
            Boolean shapeOfLocationEstimateNotSupported, Boolean neededLcsCapabilityNotSupportedInServingNode) {
        super(MAPErrorCode.facilityNotSupported);

        this.extensionContainer = extensionContainer;
        if(shapeOfLocationEstimateNotSupported==null || !shapeOfLocationEstimateNotSupported)
        	this.shapeOfLocationEstimateNotSupported=null;
        else 
        	this.shapeOfLocationEstimateNotSupported=new ASNNull();
        
        if(neededLcsCapabilityNotSupportedInServingNode==null || !neededLcsCapabilityNotSupportedInServingNode)
        	this.neededLcsCapabilityNotSupportedInServingNode=null;
        else
        	this.neededLcsCapabilityNotSupportedInServingNode=new ASNNull();
    }

    public MAPErrorMessageFacilityNotSupImpl() {
        super(MAPErrorCode.facilityNotSupported);
    }

    public boolean isEmFacilityNotSup() {
        return true;
    }

    public MAPErrorMessageFacilityNotSup getEmFacilityNotSup() {
        return this;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public Boolean getShapeOfLocationEstimateNotSupported() {
        return this.shapeOfLocationEstimateNotSupported!=null;
    }

    public Boolean getNeededLcsCapabilityNotSupportedInServingNode() {
        return this.neededLcsCapabilityNotSupportedInServingNode!=null;
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setShapeOfLocationEstimateNotSupported(Boolean shapeOfLocationEstimateNotSupported) {
    	if(shapeOfLocationEstimateNotSupported==null || !shapeOfLocationEstimateNotSupported)
    		this.shapeOfLocationEstimateNotSupported=null;
    	else
    		this.shapeOfLocationEstimateNotSupported=new ASNNull();
    }

    public void getNeededLcsCapabilityNotSupportedInServingNode(Boolean neededLcsCapabilityNotSupportedInServingNode) {
    	if(neededLcsCapabilityNotSupportedInServingNode==null || !neededLcsCapabilityNotSupportedInServingNode)
    		this.neededLcsCapabilityNotSupportedInServingNode=null;
    	else
    		this.neededLcsCapabilityNotSupportedInServingNode = new ASNNull();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageFacilityNotSup [");
        if (this.extensionContainer != null)
            sb.append("extensionContainer=" + this.extensionContainer.toString());
        if (this.shapeOfLocationEstimateNotSupported != null)
            sb.append(", shapeOfLocationEstimateNotSupported=true");
        if (this.neededLcsCapabilityNotSupportedInServingNode != null)
            sb.append(", neededLcsCapabilityNotSupportedInServingNode=true");
        sb.append("]");

        return sb.toString();
    }
}
