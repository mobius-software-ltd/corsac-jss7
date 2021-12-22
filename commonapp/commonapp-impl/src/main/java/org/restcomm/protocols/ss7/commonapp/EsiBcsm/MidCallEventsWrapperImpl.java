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

package org.restcomm.protocols.ss7.commonapp.EsiBcsm;

import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.MidCallEvents;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MidCallEventsWrapperImpl {
	@ASNChoise
	private MidCallEventsImpl midCallEvents;

    public MidCallEventsWrapperImpl() {
    }

    public MidCallEventsWrapperImpl(MidCallEvents midCallEvents) {
    	if(midCallEvents!=null) {
    		if(midCallEvents instanceof MidCallEventsImpl)
    			this.midCallEvents = (MidCallEventsImpl)midCallEvents;
    		else if(midCallEvents.getDTMFDigitsCompleted()!=null)
    			this.midCallEvents=new MidCallEventsImpl(midCallEvents.getDTMFDigitsCompleted(), true);
    		else if(midCallEvents.getDTMFDigitsTimeOut()!=null)
    			this.midCallEvents=new MidCallEventsImpl(midCallEvents.getDTMFDigitsTimeOut(), false);
    	}
    }

    public MidCallEvents getMidCallEventsWrapper() {
    	return midCallEvents;
    }
}
