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

package org.restcomm.protocols.ss7.tcap.asn.tx;

import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.TCUnifiedMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ASNPAbortCause;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x07,constructed=true,lengthIndefinite=false)
public class TCAbortMessageImpl extends TCUnifiedMessageImpl implements TCAbortMessage {
	
	private ASNPAbortCause type;
    
	/*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage#getPAbortCause()
     */
    public PAbortCauseType getPAbortCause() throws ParseException {
    	if(type==null)
    		return null;
    	
        return type.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage#setPAbortCause
     * (org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType)
     */
    public void setPAbortCause(PAbortCauseType t) {
        this.type = new ASNPAbortCause(t);        
    }

    @ASNValidate
	public void validateElement() throws ASNParsingComponentException {
    	if(getOriginatingTransactionId()!=null)
    		throw new ASNParsingComponentException("Originating transaction ID should be null", ASNParsingComponentExceptionReason.MistypedParameter); 
	}
}