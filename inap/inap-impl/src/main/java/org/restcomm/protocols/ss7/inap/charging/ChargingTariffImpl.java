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

import org.restcomm.protocols.ss7.inap.api.charging.ChargingTariff;
import org.restcomm.protocols.ss7.inap.api.charging.TariffCurrency;
import org.restcomm.protocols.ss7.inap.api.charging.TariffPulse;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ChargingTariffImpl implements ChargingTariff {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = TariffCurrencyImpl.class)
    private TariffCurrency tariffCurrency;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1, defaultImplementation = TariffPulseImpl.class)
    private TariffPulse tariffPulse;

    public ChargingTariffImpl() {
    }

    public ChargingTariffImpl(TariffCurrency tariffCurrency) {
    	this.tariffCurrency=tariffCurrency;        
    }
    
    public ChargingTariffImpl(TariffPulse tariffPulse) {
    	this.tariffPulse=tariffPulse;
    }

    public TariffCurrency getTariffCurrency() {
    	return tariffCurrency;
    }

    public TariffPulse getTariffPulse() {
    	return tariffPulse;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ChargingTariff [");

        if (this.tariffCurrency != null) {
            sb.append(", tariffCurrency=");
            sb.append(tariffCurrency);
        }

        if (this.tariffPulse != null) {
            sb.append(", tariffPulse=");
            sb.append(tariffPulse);
        }
        
        sb.append("]");

        return sb.toString();
    }
}
