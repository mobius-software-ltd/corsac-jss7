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

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCBeforeAnswer;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCSubsequent;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAI_GSM0224;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CAI_GSM0224Impl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AOCBeforeAnswerImpl implements AOCBeforeAnswer {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1,defaultImplementation = CAI_GSM0224Impl.class)
    private CAI_GSM0224 aocInitial;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1,defaultImplementation = AOCSubsequentImpl.class)
    private AOCSubsequent aocSubsequent;

    public AOCBeforeAnswerImpl() {
    }

    public AOCBeforeAnswerImpl(CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent) {
        this.aocInitial = aocInitial;
        this.aocSubsequent = aocSubsequent;
    }

    public CAI_GSM0224 getAOCInitial() {
        return aocInitial;
    }

    public AOCSubsequent getAOCSubsequent() {
        return aocSubsequent;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AOCBeforeAnswer [");

        if (this.aocInitial != null) {
            sb.append("aocInitial=");
            sb.append(aocInitial.toString());
        }
        if (this.aocSubsequent != null) {
            sb.append(", aocSubsequent=");
            sb.append(aocSubsequent.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(aocInitial==null)
			throw new ASNParsingComponentException("aoc initial should be set for aoc before answer", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
