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

package org.restcomm.protocols.ss7.sccp.impl.message;

import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpConnRlcMessage;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class SccpConnRlcMessageImpl extends SccpConnReferencedMessageImpl implements SccpConnRlcMessage {

    public SccpConnRlcMessageImpl(int sls, int localSsn) {
        super(0, MESSAGE_TYPE_RLC, sls, localSsn);
    }

    protected SccpConnRlcMessageImpl(int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(0, MESSAGE_TYPE_RLC, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    @Override
    public void decode(ByteBuf buffer, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	LocalReferenceImpl ref = new LocalReferenceImpl();
        ref.decode(buffer, factory, sccpProtocolVersion);
        destinationLocalReferenceNumber = ref;

        ref = new LocalReferenceImpl();
        ref.decode(buffer, factory, sccpProtocolVersion);
        sourceLocalReferenceNumber = ref;
    }

    @Override
    public EncodingResultData encode(SccpStackImpl sccpStackImpl, LongMessageRuleType longMessageRuleType, int maxMtp3UserDataLength, Logger logger, boolean removeSPC, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	if (type == 0) {
            return new EncodingResultData(EncodingResult.MessageTypeMissing, null, null, null);
        }
        if (destinationLocalReferenceNumber == null) {
            return new EncodingResultData(EncodingResult.DestinationLocalReferenceNumberMissing, null, null, null);
        }
        if (sourceLocalReferenceNumber == null) {
            return new EncodingResultData(EncodingResult.SourceLocalReferenceNumberMissing, null, null, null);
        }

        // 7 is sum of 3 fixed-length field lengths
        ByteBuf out = Unpooled.buffer(7);

        out.writeByte(type);
        ((LocalReferenceImpl) destinationLocalReferenceNumber).encode(out,sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        ((LocalReferenceImpl) sourceLocalReferenceNumber).encode(out,sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
        return new EncodingResultData(EncodingResult.Success, out, null, null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Sccp Msg [Type=Rlc");
        sb.append(" networkId=");
        sb.append(this.networkId);
        sb.append(" sls=");
        sb.append(this.sls);
        sb.append(" incomingOpc=");
        sb.append(this.incomingOpc);
        sb.append(" incomingDpc=");
        sb.append(this.incomingDpc);
        sb.append(" outgoingDpc=");
        sb.append(this.outgoingDpc);
        sb.append(" DataLen=");

        sb.append(" sourceLR=");
        if (this.sourceLocalReferenceNumber != null)
            sb.append(this.sourceLocalReferenceNumber.getValue());
        sb.append(" destLR=");
        if (this.destinationLocalReferenceNumber != null)
            sb.append(this.destinationLocalReferenceNumber.getValue());

        sb.append(" isMtpOriginated=");
        sb.append(this.isMtpOriginated);

        sb.append("]");

        return sb.toString();
    }
}
