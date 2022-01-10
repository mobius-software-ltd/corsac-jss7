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
package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCSubsequent;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AOCGPRS;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AOCSubsequentImpl;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAI_GSM0224;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CAI_GSM0224Impl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AOCGPRSImpl implements AOCGPRS {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = CAI_GSM0224Impl.class)
    private CAI_GSM0224 aocInitial;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = AOCSubsequentImpl.class)
    private AOCSubsequent aocSubsequent;

    public AOCGPRSImpl() {
    }

    public AOCGPRSImpl(CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent) {
        this.aocInitial = aocInitial;
        this.aocSubsequent = aocSubsequent;
    }

    public CAI_GSM0224 getAOCInitial() {
        return this.aocInitial;
    }

    public AOCSubsequent getAOCSubsequent() {
        return this.aocSubsequent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AOCGPRS [");

        if (this.aocInitial != null) {
            sb.append("aocInitial=");
            sb.append(this.aocInitial.toString());
            sb.append(", ");
        }

        if (this.aocSubsequent != null) {
            sb.append("aocSubsequent=");
            sb.append(this.aocSubsequent.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
