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

package org.restcomm.protocols.ss7.map.service.pdpContextActivation;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendRoutingInfoForGprsRequestImpl extends PdpContextActivationMessageImpl implements SendRoutingInfoForGprsRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private IMSIImpl imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private GSNAddressImpl ggsnAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ISDNAddressStringImpl ggsnNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;

    public SendRoutingInfoForGprsRequestImpl() {
    }

    public SendRoutingInfoForGprsRequestImpl(IMSIImpl imsi, GSNAddressImpl ggsnAddress, ISDNAddressStringImpl ggsnNumber, MAPExtensionContainerImpl extensionContainer) {
        this.imsi = imsi;
        this.ggsnAddress = ggsnAddress;
        this.ggsnNumber = ggsnNumber;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfoForGprs_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfoForGprs;
    }

    @Override
    public IMSIImpl getImsi() {
        return imsi;
    }

    @Override
    public GSNAddressImpl getGgsnAddress() {
        return ggsnAddress;
    }

    @Override
    public ISDNAddressStringImpl getGgsnNumber() {
        return ggsnNumber;
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendRoutingInfoForGprsRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi);
            sb.append(", ");
        }
        if (this.ggsnAddress != null) {
            sb.append("ggsnAddress=");
            sb.append(ggsnAddress);
            sb.append(", ");
        }
        if (this.ggsnNumber != null) {
            sb.append("ggsnNumber=");
            sb.append(ggsnNumber);
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
