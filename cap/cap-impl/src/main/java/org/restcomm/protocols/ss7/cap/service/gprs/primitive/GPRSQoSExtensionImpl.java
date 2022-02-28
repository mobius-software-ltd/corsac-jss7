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

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSExtension;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.Ext2QoSSubscribedImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class GPRSQoSExtensionImpl implements GPRSQoSExtension {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1,defaultImplementation = Ext2QoSSubscribedImpl.class)
    private Ext2QoSSubscribed supplementToLongQoSFormat;

    public GPRSQoSExtensionImpl() {
    }

    public GPRSQoSExtensionImpl(Ext2QoSSubscribed supplementToLongQoSFormat) {
        this.supplementToLongQoSFormat = supplementToLongQoSFormat;
    }

    public void setSupplementToLongQoSFormat(Ext2QoSSubscribed supplementToLongQoSFormat) {
        this.supplementToLongQoSFormat = supplementToLongQoSFormat;
    }

    public Ext2QoSSubscribed getSupplementToLongQoSFormat() {
        return this.supplementToLongQoSFormat;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GPRSQoSExtension [");

        if (this.supplementToLongQoSFormat != null) {
            sb.append("supplementToLongQoSFormat=");
            sb.append(this.supplementToLongQoSFormat.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(supplementToLongQoSFormat==null)
			throw new ASNParsingComponentException("supplement to long qos format should be set for gprs qos extension", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
