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

package org.restcomm.protocols.ss7.inap.charging;

import org.restcomm.protocols.ss7.inap.api.charging.TariffCurrencyFormat;
import org.restcomm.protocols.ss7.inap.api.charging.TariffSwitchCurrency;
import org.restcomm.protocols.ss7.inap.api.charging.TariffSwitchoverTime;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TariffSwitchCurrencyImpl implements TariffSwitchCurrency {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = TariffCurrencyFormatImpl.class)
    private TariffCurrencyFormat nextTariffCurrency;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1, defaultImplementation = TariffSwitchoverTimeImpl.class)
    private TariffSwitchoverTime tariffSwitchoverTime;

    public TariffSwitchCurrencyImpl() {
    }

    public TariffSwitchCurrencyImpl(TariffCurrencyFormat nextTariffCurrency,TariffSwitchoverTime tariffSwitchoverTime) {
    	this.nextTariffCurrency=nextTariffCurrency; 
    	this.tariffSwitchoverTime=tariffSwitchoverTime;
    }

    public TariffCurrencyFormat getNextTariffCurrency() {
    	return nextTariffCurrency;
    }

    public TariffSwitchoverTime getTariffSwitchoverTime() {
    	return tariffSwitchoverTime;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TariffSwitchCurrency [");

        if (this.nextTariffCurrency != null) {
            sb.append(", nextTariffCurrency=");
            sb.append(nextTariffCurrency);
        }

        if (this.tariffSwitchoverTime != null) {
            sb.append(", tariffSwitchoverTime=");
            sb.append(tariffSwitchoverTime);
        }
        
        sb.append("]");

        return sb.toString();
    }
}