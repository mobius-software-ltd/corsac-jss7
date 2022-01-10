/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.inap.EsiBcsm;

import org.restcomm.protocols.ss7.commonapp.api.isup.BackwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.commonapp.isup.BackwardCallIndicatorsIsupImpl;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AlertingSpecificInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AlertingSpecificInfoImpl implements AlertingSpecificInfo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = BackwardCallIndicatorsIsupImpl.class)
    private BackwardCallIndicatorsIsup backwardCallIndicators;
    
	public AlertingSpecificInfoImpl() {
    }

    public AlertingSpecificInfoImpl(BackwardCallIndicatorsIsup backwardCallIndicators) {
    	this.backwardCallIndicators=backwardCallIndicators;               
    }

    public BackwardCallIndicatorsIsup getBackwardCallIndicators() {
        return backwardCallIndicators;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AlertingSpecificInfo [");

        if (this.backwardCallIndicators != null) {
            sb.append(", backwardCallIndicators= [");
            sb.append(backwardCallIndicators.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
