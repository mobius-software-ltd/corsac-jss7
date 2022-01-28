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

package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DpSpecificCriteria;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class BCSMEventImpl implements BCSMEvent {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNEventTypeBCSM eventTypeBCSM;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNMonitorMode monitorMode;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true, index=-1)
    private LegIDWrapperImpl legID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = true, index=-1)
    private DpSpecificCriteriaWrapperImpl dpSpecificCriteria;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false, index=-1)
    private ASNNull automaticRearm;

    public BCSMEventImpl() {
    }

    public BCSMEventImpl(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID,
            DpSpecificCriteria dpSpecificCriteria, boolean automaticRearm) {
    	if(eventTypeBCSM!=null)
    		this.eventTypeBCSM = new ASNEventTypeBCSM(eventTypeBCSM);
    		
    	if(monitorMode!=null)
    		this.monitorMode = new ASNMonitorMode(monitorMode);
    		
        if(legID!=null)
        	this.legID = new LegIDWrapperImpl(legID);
        
        if(dpSpecificCriteria!=null)
        	this.dpSpecificCriteria = new DpSpecificCriteriaWrapperImpl(dpSpecificCriteria);        
        
        if(automaticRearm)
        	this.automaticRearm = new ASNNull();
    }

    public EventTypeBCSM getEventTypeBCSM() {
    	if(eventTypeBCSM==null)
    		return null;
    	
        return eventTypeBCSM.getType();
    }

    public MonitorMode getMonitorMode() {
    	if(monitorMode==null)
    		return null;
    	
        return monitorMode.getType();
    }

    public LegID getLegID() {
    	if(legID==null)
    		return null;
    	
        return legID.getLegID();
    }

    public DpSpecificCriteria getDpSpecificCriteria() {
    	if(dpSpecificCriteria==null)
    		return null;
    	
        return dpSpecificCriteria.getDpSpecificCriteria();
    }

    public boolean getAutomaticRearm() {
        return automaticRearm!=null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("BCSMEvent [");

        if (this.eventTypeBCSM != null && this.eventTypeBCSM.getType()!=null) {
            sb.append("eventTypeBCSM=");
            sb.append(eventTypeBCSM.getType());
        }
        if (this.monitorMode != null && this.monitorMode.getType()!=null) {
            sb.append(", monitorMode=");
            sb.append(monitorMode.getType());
        }
        if (this.legID != null && this.legID.getLegID()!=null) {
            sb.append(", legID=");
            sb.append(legID.getLegID().toString());
        }
        if (this.dpSpecificCriteria != null && this.dpSpecificCriteria.getDpSpecificCriteria()!=null) {
            sb.append(", dpSpecificCriteria=");
            sb.append(dpSpecificCriteria.getDpSpecificCriteria().toString());
        }
        if (this.automaticRearm!=null) {
            sb.append(", automaticRearm");
        }

        sb.append("]");

        return sb.toString();
    }
}
