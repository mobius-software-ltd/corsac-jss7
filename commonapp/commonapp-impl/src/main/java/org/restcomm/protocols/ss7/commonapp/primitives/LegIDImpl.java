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

package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x04,constructed=true,lengthIndefinite=false)
public class LegIDImpl implements LegID {
	private ReceivingLegIDImpl receivingLegID;
    private SendingLegIDImpl sendingLegID;

    public LegIDImpl() {
    }

    public LegIDImpl(LegType receivingLegID, LegType sendingLegID) {
    	if(receivingLegID!=null)
    		this.receivingLegID = new ReceivingLegIDImpl(receivingLegID);
    	
    	if(sendingLegID!=null)
    		this.sendingLegID = new SendingLegIDImpl(sendingLegID);
    }
    
    public LegType getReceivingSideID() {
    	if(receivingLegID==null)
    		return null;
    	
		return receivingLegID.getReceivingSideID();
	}

	public LegType getSendingSideID() {
		if(sendingLegID==null)
			return null;
		
		return sendingLegID.getSendingSideID();
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(" [");
        if (this.receivingLegID != null) {
            sb.append("receivingLegID=");
            sb.append(receivingLegID);
        }
        if (this.sendingLegID != null) {
            sb.append(", sendingLegID=");
            sb.append(sendingLegID);
        }
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(receivingLegID==null && sendingLegID==null)
			throw new ASNParsingComponentException("either receiving leg ID or sending leg ID should be set for leg ID", ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}
