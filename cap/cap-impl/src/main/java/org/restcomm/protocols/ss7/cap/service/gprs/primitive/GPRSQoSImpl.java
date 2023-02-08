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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.QoSSubscribedImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class GPRSQoSImpl implements GPRSQoS {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = QoSSubscribedImpl.class)
    private QoSSubscribed shortQoSFormat;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = ExtQoSSubscribedImpl.class)
    private ExtQoSSubscribed longQoSFormat;

    public GPRSQoSImpl() {

    }

    public GPRSQoSImpl(QoSSubscribed shortQoSFormat) {
        this.shortQoSFormat = shortQoSFormat;
    }

    public GPRSQoSImpl(ExtQoSSubscribed longQoSFormat) {
        this.longQoSFormat = longQoSFormat;
    }

    public QoSSubscribed getShortQoSFormat() {
        return this.shortQoSFormat;
    }

    public ExtQoSSubscribed getLongQoSFormat() {
        return this.longQoSFormat;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GPRSQoS [");

        if (this.shortQoSFormat != null) {
            sb.append("shortQoSFormat=");
            sb.append(this.shortQoSFormat.toString());
        }

        if (this.longQoSFormat != null) {
            sb.append("longQoSFormat=");
            sb.append(this.longQoSFormat.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(shortQoSFormat==null && longQoSFormat==null)
			throw new ASNParsingComponentException("either short qos format or long qos format should be set for gprs qos", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
