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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCBeforeAnswer;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCSubsequent;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAI_GSM0224;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 *
 * @author sergey vetyutnev
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
}
