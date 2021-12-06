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

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GPRSEventSpecificInformationWrapperImpl {
	@ASNChoise
	private GPRSEventSpecificInformationImpl gprsEventSpecificInformation;

    public GPRSEventSpecificInformationWrapperImpl() {
    }

    public GPRSEventSpecificInformationWrapperImpl(GPRSEventSpecificInformation gprsEventSpecificInformation) {
    	if(gprsEventSpecificInformation!=null) {
    		if(gprsEventSpecificInformation instanceof GPRSEventSpecificInformationImpl)
    			this.gprsEventSpecificInformation = (GPRSEventSpecificInformationImpl)gprsEventSpecificInformation;
    		else if(gprsEventSpecificInformation.getDetachSpecificInformation()!=null)
    			this.gprsEventSpecificInformation=new GPRSEventSpecificInformationImpl(gprsEventSpecificInformation.getDetachSpecificInformation());
    		else if(gprsEventSpecificInformation.getDisconnectSpecificInformation()!=null)
    			this.gprsEventSpecificInformation=new GPRSEventSpecificInformationImpl(gprsEventSpecificInformation.getDisconnectSpecificInformation());
    		else if(gprsEventSpecificInformation.getLocationInformationGPRS()!=null)
    			this.gprsEventSpecificInformation=new GPRSEventSpecificInformationImpl(gprsEventSpecificInformation.getLocationInformationGPRS());
    		else if(gprsEventSpecificInformation.getPdpContextChangeOfPositionSpecificInformation()!=null)
    			this.gprsEventSpecificInformation=new GPRSEventSpecificInformationImpl(gprsEventSpecificInformation.getPdpContextChangeOfPositionSpecificInformation());
    		else if(gprsEventSpecificInformation.getPDPContextEstablishmentAcknowledgementSpecificInformation()!=null)
    			this.gprsEventSpecificInformation=new GPRSEventSpecificInformationImpl(gprsEventSpecificInformation.getPDPContextEstablishmentAcknowledgementSpecificInformation());
    		else if(gprsEventSpecificInformation.getPDPContextEstablishmentSpecificInformation()!=null)
    			this.gprsEventSpecificInformation=new GPRSEventSpecificInformationImpl(gprsEventSpecificInformation.getPDPContextEstablishmentSpecificInformation());    		
    	}
    }

    public GPRSEventSpecificInformation getGPRSEventSpecificInformation() {
    	return gprsEventSpecificInformation;
    }
}
