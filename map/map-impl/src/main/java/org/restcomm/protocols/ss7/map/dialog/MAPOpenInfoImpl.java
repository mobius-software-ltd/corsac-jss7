/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
<code>
MAP-OpenInfo ::= SEQUENCE {
  destinationReference [0] AddressString OPTIONAL,
  originationReference [1] AddressString OPTIONAL,
-- WS modification: Ericsson proprietary fields
  callingMsisdn        [2] AddressString OPTIONAL,
  callingVlrAddress    [3] AddressString OPTIONAL,
-- WS modification: Ericsson proprietary fields
  ...,
  extensionContainer ExtensionContainer OPTIONAL
  -- extensionContainer must not be used in version 2
}

Another (old) version of Ericsson :
MAP OpenInfo ::= SEQUENCE ( imsi (0) IMSI OPTIONAL, originationReference (1) AddressString OPTIONAL,
  msisdn (2) AddressString, ... vlrNo (3) AddressString OPTIONAL )
<code>

 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=true,lengthIndefinite=false)
public class MAPOpenInfoImpl {
	protected static final int ERI_MSISDN_TAG = 0x02;
    protected static final int ERI_NLR_NO_TAG = 0x03;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=false,index=-1)
    private AddressStringImpl destReference;
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x01,constructed=false,index=-1)
    private AddressStringImpl origReference;
    private MAPExtensionContainerImpl extensionContainer;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x02,constructed=false,index=-1)
    private AddressStringImpl eriMsisdn;
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x03,constructed=false,index=-1)
    private AddressStringImpl eriVlrNo;

    public AddressStringImpl getDestReference() {
        return this.destReference;
    }

    public AddressStringImpl getOrigReference() {
        return this.origReference;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    public boolean getEriStyle() {
        return this.eriMsisdn!=null;
    }

    public AddressStringImpl getEriMsisdn() {
        return eriMsisdn;
    }

    public AddressStringImpl getEriVlrNo() {
        return eriVlrNo;
    }

    public void setDestReference(AddressStringImpl destReference) {
        this.destReference = destReference;
    }

    public void setOrigReference(AddressStringImpl origReference) {
        this.origReference = origReference;
    }

    public void setExtensionContainer(MAPExtensionContainerImpl extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setEriMsisdn(AddressStringImpl eriMsisdn) {
        this.eriMsisdn = eriMsisdn;
    }

    public void setEriVlrNo(AddressStringImpl eriVlrNo) {
        this.eriVlrNo = eriVlrNo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPOpenInfo[");
        if (destReference != null) {
            sb.append("destReference=");
            sb.append(destReference);
        }
        if (origReference != null) {
            sb.append(", origReference=");
            sb.append(origReference);
        }
        if (extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer);
        }
        if (getEriStyle()) {
            sb.append(", eriStyle");
        }
        if (eriMsisdn != null) {
            sb.append(", eriMsisdn=");
            sb.append(eriMsisdn);
        }
        if (eriVlrNo != null) {
            sb.append(", eriVlrNo=");
            sb.append(eriVlrNo);
        }
        sb.append("]");

        return sb.toString();
    }

}
