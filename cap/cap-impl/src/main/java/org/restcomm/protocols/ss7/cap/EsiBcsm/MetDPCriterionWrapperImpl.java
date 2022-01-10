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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterion;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterion.Boolean_Option;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterion.CellGlobalIdOrServiceAreaIdFixedLength_Option;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterion.LAIFixedLength_Option;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MetDPCriterionWrapperImpl {
	@ASNChoise
	private List<MetDPCriterionImpl> metDPCriterionImp;

    public MetDPCriterionWrapperImpl() {
    }

    public MetDPCriterionWrapperImpl(List<MetDPCriterion> metDPCriterionImp) {
    	if(metDPCriterionImp!=null) {
    		this.metDPCriterionImp = new ArrayList<MetDPCriterionImpl>();
    		for(MetDPCriterion curr:metDPCriterionImp) {
    			if(curr instanceof MetDPCriterionImpl)
    				this.metDPCriterionImp.add((MetDPCriterionImpl)curr);
    			else if(curr.getEnteringCellGlobalId()!=null)
    				this.metDPCriterionImp.add(new MetDPCriterionImpl(curr.getEnteringCellGlobalId(), CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringCellGlobalId));
    			else if(curr.getEnteringServiceAreaId()!=null)
    				this.metDPCriterionImp.add(new MetDPCriterionImpl(curr.getEnteringServiceAreaId(), CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringServiceAreaId));
    			else if(curr.getLeavingCellGlobalId()!=null)
    				this.metDPCriterionImp.add(new MetDPCriterionImpl(curr.getLeavingCellGlobalId(), CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingCellGlobalId));
    			else if(curr.getLeavingServiceAreaId()!=null)
    				this.metDPCriterionImp.add(new MetDPCriterionImpl(curr.getLeavingServiceAreaId(), CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingServiceAreaId));
    			else if(curr.getEnteringLocationAreaId()!=null)
    				this.metDPCriterionImp.add(new MetDPCriterionImpl(curr.getEnteringLocationAreaId(), LAIFixedLength_Option.enteringLocationAreaId));
    			else if(curr.getLeavingLocationAreaId()!=null)
    				this.metDPCriterionImp.add(new MetDPCriterionImpl(curr.getLeavingLocationAreaId(), LAIFixedLength_Option.leavingLocationAreaId));
    			else if(curr.getMetDPCriterionAlt()!=null)
    				this.metDPCriterionImp.add(new MetDPCriterionImpl(curr.getMetDPCriterionAlt()));
    			else if(curr.getInterMSCHandOver())
    				this.metDPCriterionImp.add(new MetDPCriterionImpl(Boolean_Option.interMSCHandOver));
    			else if(curr.getInterPLMNHandOver())
    				this.metDPCriterionImp.add(new MetDPCriterionImpl(Boolean_Option.interPLMNHandOver));
    			else if(curr.getInterSystemHandOverToGSM())
    				this.metDPCriterionImp.add(new MetDPCriterionImpl(Boolean_Option.interSystemHandOverToGSM));
    			else if(curr.getInterSystemHandOverToUMTS())
    				this.metDPCriterionImp.add(new MetDPCriterionImpl(Boolean_Option.interSystemHandOverToUMTS));
    		}
    	}
    }

    public List<MetDPCriterion> getMetDPCriterion() {
    	if(metDPCriterionImp==null)
    		return null;
    	
    	List<MetDPCriterion> result=new ArrayList<MetDPCriterion>();
    	for(MetDPCriterionImpl curr:metDPCriterionImp)
    		result.add(curr);
    	
    	return result;
    }
}
