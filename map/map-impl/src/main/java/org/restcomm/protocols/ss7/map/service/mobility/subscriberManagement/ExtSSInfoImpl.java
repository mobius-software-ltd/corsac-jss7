/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EMLPPInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtSSInfoImpl implements ExtSSInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1,defaultImplementation = ExtForwInfoImpl.class)
    private ExtForwInfo forwardingInfo = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1,defaultImplementation = ExtCallBarInfoImpl.class)
    private ExtCallBarInfo callBarringInfo = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = CUGInfoImpl.class)
    private CUGInfo cugInfo = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1,defaultImplementation = ExtSSDataImpl.class)
    private ExtSSData ssData = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1,defaultImplementation = EMLPPInfoImpl.class)
    private EMLPPInfo emlppInfo = null;

    public ExtSSInfoImpl() {

    }

    public ExtSSInfoImpl(ExtForwInfo forwardingInfo) {

        this.forwardingInfo = forwardingInfo;
    }

    public ExtSSInfoImpl(ExtCallBarInfo callBarringInfo) {

        this.callBarringInfo = callBarringInfo;
    }

    public ExtSSInfoImpl(CUGInfo cugInfo) {

        this.cugInfo = cugInfo;
    }

    public ExtSSInfoImpl(ExtSSData ssData) {

        this.ssData = ssData;
    }

    public ExtSSInfoImpl(EMLPPInfo emlppInfo) {

        this.emlppInfo = emlppInfo;
    }

    public ExtForwInfo getForwardingInfo() {
        return this.forwardingInfo;
    }

    public ExtCallBarInfo getCallBarringInfo() {
        return this.callBarringInfo;
    }

    public CUGInfo getCugInfo() {
        return this.cugInfo;
    }

    public ExtSSData getSsData() {
        return this.ssData;
    }

    public EMLPPInfo getEmlppInfo() {
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(forwardingInfo==null && callBarringInfo==null && cugInfo==null && ssData==null && emlppInfo==null)
			throw new ASNParsingComponentException("one of child items should be set for ext ss info", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
