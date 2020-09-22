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

package org.restcomm.protocols.ss7.map.api.service.supplementary;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SSInfoImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,constructed=true,tag=0,index=-1)
    private ForwardingInfoImpl forwardingInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,constructed=true,tag=1,index=-1)
    private CallBarringInfoImpl callBarringInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,constructed=true,tag=3,index=-1)
    private SSDataImpl ssData;

    public SSInfoImpl() {
    }

    public SSInfoImpl(ForwardingInfoImpl forwardingInfo) {
        this.forwardingInfo = forwardingInfo;
    }

    public SSInfoImpl(CallBarringInfoImpl callBarringInfo) {
        this.callBarringInfo = callBarringInfo;
    }

    public SSInfoImpl(SSDataImpl ssData) {
        this.ssData = ssData;
    }

    public ForwardingInfoImpl getForwardingInfo() {
        return forwardingInfo;
    }

    public CallBarringInfoImpl getCallBarringInfo() {
        return callBarringInfo;
    }

    public SSDataImpl getSsData() {
        return ssData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SSInfo [");

        if (this.forwardingInfo != null) {
            sb.append("forwardingInfo=");
            sb.append(this.forwardingInfo.toString());
            sb.append(", ");
        }
        if (this.callBarringInfo != null) {
            sb.append("callBarringInfo=");
            sb.append(this.callBarringInfo.toString());
            sb.append(", ");
        }
        if (this.ssData != null) {
            sb.append("ssData=");
            sb.append(this.ssData.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
