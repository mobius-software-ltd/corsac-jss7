/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

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
 * @author kiss.balazs@alerant.hu
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 5,constructed = false,lengthIndefinite = false)
public class SpecializedResourceReportArgImpl {
	private ASNNull defaultParam;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1)
    private ASNNull isAllAnnouncementsComplete;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = false,index = -1)
    private ASNNull isFirstAnnouncementStarted;

    public SpecializedResourceReportArgImpl() {
    }

    public SpecializedResourceReportArgImpl(boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted) {
    	
    	if(isAllAnnouncementsComplete)
    		this.isAllAnnouncementsComplete = new ASNNull();
    	else if(isFirstAnnouncementStarted)
    		this.isFirstAnnouncementStarted = new ASNNull();
    	else
    		this.defaultParam = new ASNNull();
    }
    
    public boolean getAllAnnouncementsComplete() {
        return isAllAnnouncementsComplete!=null;
    }

    public boolean getFirstAnnouncementStarted() {
        return isFirstAnnouncementStarted!=null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SpecializedResourceReportRequestIndication [");
        
        if(this.defaultParam==null) {
	        if (this.isAllAnnouncementsComplete!=null) {
	            sb.append(", isAllAnnouncementsComplete");
	        }
	        if (this.isFirstAnnouncementStarted!=null) {
	            sb.append(", isFirstAnnouncementStarted");
	        }
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(isAllAnnouncementsComplete==null && isFirstAnnouncementStarted==null)
			throw new ASNParsingComponentException("either is all announcements complete or is first announcement started should be set for specialized resource report", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
