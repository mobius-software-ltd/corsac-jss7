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
package org.restcomm.protocols.ss7.cap.api.gap;

import org.restcomm.protocols.ss7.cap.api.isup.CauseCapImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSendImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSendWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class GapTreatmentImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private InformationToSendWrapperImpl informationToSend;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private CauseCapImpl releaseCause;

    public GapTreatmentImpl() {
    }

    public GapTreatmentImpl(InformationToSendImpl informationToSend) {
    	if(informationToSend!=null)
    		this.informationToSend = new InformationToSendWrapperImpl(informationToSend);
    }

    public GapTreatmentImpl(CauseCapImpl releaseCause) {
        this.releaseCause = releaseCause;
    }

    public InformationToSendImpl getInformationToSend() {
    	if(informationToSend==null)
    		return null;
    	
        return informationToSend.getInformationToSend();
    }

    public CauseCapImpl getCause() {
        return releaseCause;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("GapTreatment [");

        if (informationToSend != null) {
            sb.append("informationToSend=");
            sb.append(informationToSend);
        } else if (releaseCause != null) {
            sb.append(", releaseCause=");
            sb.append(releaseCause);
        }

        sb.append("]");

        return sb.toString();
    }

}
