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
package org.restcomm.protocols.ss7.cap.service.gprs;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ChargingResultWrapperImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ChargingRollOverWrapperImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ApplyChargingReportGPRSRequestImpl extends GprsMessageImpl implements ApplyChargingReportGPRSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 0,constructed = true,index = -1)
    private ChargingResultWrapperImpl chargingResult;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 1,constructed = true,index = -1,defaultImplementation = QualityOfServiceImpl.class)
    private QualityOfService qualityOfService;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 2,constructed = false,index = -1)
    private ASNBoolean active;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 3,constructed = false,index = -1,defaultImplementation = PDPIDImpl.class)
    private PDPID pdpID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 4,constructed = true,index = -1)
    private ChargingRollOverWrapperImpl chargingRollOver;

    public ApplyChargingReportGPRSRequestImpl() {
    }

    public ApplyChargingReportGPRSRequestImpl(ChargingResult chargingResult, QualityOfService qualityOfService, boolean active,
            PDPID pdpID, ChargingRollOver chargingRollOver) {
        super();
        
        if(chargingResult!=null)
        	this.chargingResult = new ChargingResultWrapperImpl(chargingResult);
        
        this.qualityOfService = qualityOfService;
        this.active = new ASNBoolean(active);
        this.pdpID = pdpID;
        
        if(chargingRollOver!=null)
        	this.chargingRollOver = new ChargingRollOverWrapperImpl(chargingRollOver);
    }

    @Override
    public ChargingResult getChargingResult() {
    	if(this.chargingResult==null)
    		return null;
    	
        return this.chargingResult.getChargingResult();
    }

    @Override
    public QualityOfService getQualityOfService() {
        return this.qualityOfService;
    }

    @Override
    public boolean getActive() {
    	if(this.active==null || this.active.getValue()==null)
    		return true;
    	
        return this.active.getValue();
    }

    @Override
    public PDPID getPDPID() {
        return this.pdpID;
    }

    @Override
    public ChargingRollOver getChargingRollOver() {
    	if(this.chargingRollOver==null)
    		return null;
    	
        return this.chargingRollOver.getChargingRollOver();
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.applyChargingReportGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.applyChargingReportGPRS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ApplyChargingReportGPRSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.chargingResult != null && this.chargingResult.getChargingResult()!=null) {
            sb.append(", chargingResult=");
            sb.append(this.chargingResult.getChargingResult());
            sb.append(", ");
        }

        if (this.qualityOfService != null) {
            sb.append(", qualityOfService=");
            sb.append(this.qualityOfService.toString());
            sb.append(", ");
        }

        if (this.active==null || this.active.getValue()==null || this.active.getValue()) {
            sb.append(", active ");
            sb.append(", ");
        }

        if (this.pdpID != null) {
            sb.append(", pdpID=");
            sb.append(this.pdpID.toString());
            sb.append(", ");
        }

        if (this.chargingRollOver != null && this.chargingRollOver.getChargingRollOver()!=null) {
            sb.append(", chargingRollOver=");
            sb.append(this.chargingRollOver.getChargingRollOver());
        }

        sb.append("]");

        return sb.toString();
    }
}
