/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdraw;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GPRSSubscriptionDataWithdrawWrapperImpl {
	
	@ASNChoise(defaultImplementation = GPRSSubscriptionDataWithdrawImpl.class)
	private GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw;

    public GPRSSubscriptionDataWithdrawWrapperImpl() {
    }

    public GPRSSubscriptionDataWithdrawWrapperImpl(GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw) {
    	this.gprsSubscriptionDataWithdraw = gprsSubscriptionDataWithdraw;
    }

    public GPRSSubscriptionDataWithdraw getGPRSSubscriptionDataWithdraw() {
    	return gprsSubscriptionDataWithdraw;
    }
}
