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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtSSInfoImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private ExtForwInfoImpl forwardingInfo = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private ExtCallBarInfoImpl callBarringInfo = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private CUGInfoImpl cugInfo = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private ExtSSDataImpl ssData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private EMLPPInfoImpl emlppInfo = null;

    public ExtSSInfoImpl() {

    }

    public ExtSSInfoImpl(ExtForwInfoImpl forwardingInfo) {

        this.forwardingInfo = forwardingInfo;
    }

    public ExtSSInfoImpl(ExtCallBarInfoImpl callBarringInfo) {

        this.callBarringInfo = callBarringInfo;
    }

    public ExtSSInfoImpl(CUGInfoImpl cugInfo) {

        this.cugInfo = cugInfo;
    }

    public ExtSSInfoImpl(ExtSSDataImpl ssData) {

        this.ssData = ssData;
    }

    public ExtSSInfoImpl(EMLPPInfoImpl emlppInfo) {

        this.emlppInfo = emlppInfo;
    }

    public ExtForwInfoImpl getForwardingInfo() {
        return this.forwardingInfo;
    }

    public ExtCallBarInfoImpl getCallBarringInfo() {
        return this.callBarringInfo;
    }

    public CUGInfoImpl getCugInfo() {
        return this.cugInfo;
    }

    public ExtSSDataImpl getSsData() {
        return this.ssData;
    }

    public EMLPPInfoImpl getEmlppInfo() {
        return this.emlppInfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtSSInfo [");

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

        if (this.cugInfo != null) {
            sb.append("cugInfo=");
            sb.append(this.cugInfo.toString());
            sb.append(", ");
        }

        if (this.ssData != null) {
            sb.append("ssData=");
            sb.append(this.ssData.toString());
            sb.append(", ");
        }

        if (this.emlppInfo != null) {
            sb.append("emlppInfo=");
            sb.append(this.emlppInfo.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
