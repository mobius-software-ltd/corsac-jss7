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
package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapIndicators;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapTreatment;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ASNControlTypeImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapIndicatorsImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapTreatmentWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CallGapRequestImpl extends CircuitSwitchedCallMessageImpl implements CallGapRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private GapCriteriaWrapperImpl gapCriteria;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1,defaultImplementation = GapIndicatorsImpl.class)
    private GapIndicators gapIndicators;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNControlTypeImpl controlType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private GapTreatmentWrapperImpl gapTreatment;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions capExtensions;

    public CallGapRequestImpl() {
    }

    public CallGapRequestImpl(GapCriteria gapCriteria, GapIndicators gapIndicators, ControlType controlType, GapTreatment gapTreatment,
            CAPINAPExtensions capExtension) {
    	
    	if(gapCriteria!=null)
    		this.gapCriteria = new GapCriteriaWrapperImpl(gapCriteria);
    	
        this.gapIndicators = gapIndicators;
        
        if(controlType!=null)
        	this.controlType = new ASNControlTypeImpl(controlType);
        	
        if(gapTreatment!=null)
        	this.gapTreatment = new GapTreatmentWrapperImpl(gapTreatment);
        
        this.capExtensions = capExtension;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.callGap_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.callGap;
    }

    public GapCriteria getGapCriteria() {
    	if(gapCriteria==null)
    		return null;
    	
        return gapCriteria.getGapCriteria();
    }

    public GapIndicators getGapIndicators() {
        return gapIndicators;
    }

    public ControlType getControlType() {
    	if(controlType==null)
    		return null;
    	
        return controlType.getType();
    }

    public GapTreatment getGapTreatment() {
    	if(gapTreatment==null)
    		return null;
    	
        return gapTreatment.getGapTreatment();
    }

    public CAPINAPExtensions getExtensions() {
        return capExtensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CallGapRequestIndication [");
        this.addInvokeIdInfo(sb);

        if(gapCriteria!=null && gapCriteria.getGapCriteria()!=null) {
        	sb.append(", gapCriteria=");
        	sb.append(gapCriteria.getGapCriteria());
        }
        
        if(gapIndicators!=null) {
        	sb.append(", gapIndicators=");
        	sb.append(gapIndicators);
        }
        
        if (this.controlType != null && this.controlType.getType()!=null) {
            sb.append(", controlType");
            sb.append(controlType.getType());
        }
        if (this.gapTreatment != null && this.gapTreatment.getGapTreatment()!=null) {
            sb.append(", gapTreatment");
            sb.append(gapTreatment.getGapTreatment());
        }
        
        if (this.capExtensions != null) {
            sb.append(", capExtensions");
            sb.append(capExtensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
