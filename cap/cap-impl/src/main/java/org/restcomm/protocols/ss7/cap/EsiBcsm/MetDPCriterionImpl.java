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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterion;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterionAlt;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC, tag=16,constructed = false,lengthIndefinite = false)
public class MetDPCriterionImpl implements MetDPCriterion {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = CellGlobalIdOrServiceAreaIdFixedLengthImpl.class)
    private CellGlobalIdOrServiceAreaIdFixedLength enteringCellGlobalId;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = CellGlobalIdOrServiceAreaIdFixedLengthImpl.class)
    private CellGlobalIdOrServiceAreaIdFixedLength leavingCellGlobalId;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = CellGlobalIdOrServiceAreaIdFixedLengthImpl.class)
    private CellGlobalIdOrServiceAreaIdFixedLength enteringServiceAreaId;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1,defaultImplementation = CellGlobalIdOrServiceAreaIdFixedLengthImpl.class)
    private CellGlobalIdOrServiceAreaIdFixedLength leavingServiceAreaId;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1, defaultImplementation = LAIFixedLengthImpl.class)
    private LAIFixedLength enteringLocationAreaId;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1, defaultImplementation = LAIFixedLengthImpl.class)
    private LAIFixedLength leavingLocationAreaId;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1)
    private ASNNull interSystemHandOverToUMTS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1)
    private ASNNull interSystemHandOverToGSM;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1)
    private ASNNull interPLMNHandOver;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1)
    private ASNNull interMSCHandOver;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1, defaultImplementation = MetDPCriterionAltImpl.class)
    private MetDPCriterionAlt metDPCriterionAlt;

    public MetDPCriterionImpl() {
    }

    public MetDPCriterionImpl(CellGlobalIdOrServiceAreaIdFixedLength value, CellGlobalIdOrServiceAreaIdFixedLength_Option option) {
        switch (option) {
        case enteringCellGlobalId:
            this.enteringCellGlobalId = value;
            break;
        case leavingCellGlobalId:
            this.leavingCellGlobalId = value;
            break;
        case enteringServiceAreaId:
            this.enteringServiceAreaId = value;
            break;
        case leavingServiceAreaId:
            this.leavingServiceAreaId = value;
            break;
        }
    }

    public MetDPCriterionImpl(LAIFixedLength value, LAIFixedLength_Option option) {
        switch (option) {
        case enteringLocationAreaId:
            this.enteringLocationAreaId = value;
            break;
        case leavingLocationAreaId:
            this.leavingLocationAreaId = value;
            break;
        }
    }

    public MetDPCriterionImpl(Boolean_Option option) {
        switch (option) {
        case interSystemHandOverToUMTS:
            this.interSystemHandOverToUMTS = new ASNNull();
            break;
        case interSystemHandOverToGSM:
            this.interSystemHandOverToGSM = new ASNNull();
            break;
        case interPLMNHandOver:
            this.interPLMNHandOver = new ASNNull();
            break;
        case interMSCHandOver:
            this.interMSCHandOver = new ASNNull();
            break;
        }
    }

    public MetDPCriterionImpl(MetDPCriterionAlt metDPCriterionAlt) {
        this.metDPCriterionAlt = metDPCriterionAlt;
    }

    public CellGlobalIdOrServiceAreaIdFixedLength getEnteringCellGlobalId() {
        return enteringCellGlobalId;
    }

    public CellGlobalIdOrServiceAreaIdFixedLength getLeavingCellGlobalId() {
        return leavingCellGlobalId;
    }

    public CellGlobalIdOrServiceAreaIdFixedLength getEnteringServiceAreaId() {
        return enteringServiceAreaId;
    }

    public CellGlobalIdOrServiceAreaIdFixedLength getLeavingServiceAreaId() {
        return leavingServiceAreaId;
    }

    public LAIFixedLength getEnteringLocationAreaId() {
        return enteringLocationAreaId;
    }

    public LAIFixedLength getLeavingLocationAreaId() {
        return leavingLocationAreaId;
    }

    public boolean getInterSystemHandOverToUMTS() {
        return interSystemHandOverToUMTS!=null;
    }

    public boolean getInterSystemHandOverToGSM() {
        return interSystemHandOverToGSM!=null;
    }

    public boolean getInterPLMNHandOver() {
        return interPLMNHandOver!=null;
    }

    public boolean getInterMSCHandOver() {
        return interMSCHandOver!=null;
    }

    public MetDPCriterionAlt getMetDPCriterionAlt() {
        return metDPCriterionAlt;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("MetDPCriterion [");

        if (enteringCellGlobalId != null) {
            sb.append("enteringCellGlobalId=");
            sb.append(enteringCellGlobalId.toString());
        } else if (leavingCellGlobalId != null) {
            sb.append("leavingCellGlobalId=");
            sb.append(leavingCellGlobalId.toString());
        } else if (enteringServiceAreaId != null) {
            sb.append("enteringServiceAreaId=");
            sb.append(enteringServiceAreaId.toString());
        } else if (leavingServiceAreaId != null) {
            sb.append("leavingServiceAreaId=");
            sb.append(leavingServiceAreaId.toString());
        } else if (enteringLocationAreaId != null) {
            sb.append("enteringLocationAreaId=");
            sb.append(enteringLocationAreaId.toString());
        } else if (leavingLocationAreaId != null) {
            sb.append("leavingLocationAreaId=");
            sb.append(leavingLocationAreaId.toString());
        } else if (interSystemHandOverToUMTS!=null) {
            sb.append("interSystemHandOverToUMTS");
        } else if (interSystemHandOverToGSM!=null) {
            sb.append("interSystemHandOverToGSM");
        } else if (interPLMNHandOver!=null) {
            sb.append("interPLMNHandOver");
        } else if (interMSCHandOver!=null) {
            sb.append("interMSCHandOver");
        } else if (metDPCriterionAlt != null) {
            sb.append("metDPCriterionAlt=");
            sb.append(metDPCriterionAlt.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(enteringCellGlobalId==null && leavingCellGlobalId==null && enteringServiceAreaId==null && leavingServiceAreaId==null && enteringLocationAreaId==null && leavingLocationAreaId==null && interSystemHandOverToUMTS==null && interSystemHandOverToGSM==null && interPLMNHandOver==null && interMSCHandOver==null && metDPCriterionAlt==null)
			throw new ASNParsingComponentException("one of child items should be set for met dp criterion", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
