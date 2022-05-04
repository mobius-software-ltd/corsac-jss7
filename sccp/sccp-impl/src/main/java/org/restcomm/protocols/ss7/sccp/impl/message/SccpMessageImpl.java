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

import io.netty.buffer.ByteBuf;

import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpMessage;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 *
 * @author kulikov
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public abstract class SccpMessageImpl implements SccpMessage {

    protected boolean isMtpOriginated;
    protected boolean isIncoming;
    protected int type;
    protected int localOriginSsn = -1;
    protected final int maxDataLen;

    // These are MTP3 signaling information set when message is received from MTP3
    protected int incomingOpc;
    protected int incomingDpc;
    protected int sls;
    // These are MTP3 signaling information that will be set into a MTP3 message when sending to MTP3
    protected int outgoingDpc = -1;
    protected int networkId;

    protected SccpMessageImpl(int maxDataLen, int type, int sls, int localSsn) {
        this.isMtpOriginated = false;
        this.type = type;
        this.localOriginSsn = localSsn;
        this.incomingOpc = -1;
        this.incomingDpc = -1;
        this.sls = sls;
        this.maxDataLen = maxDataLen;
    }

    protected SccpMessageImpl(int maxDataLen, int type, int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        this.isMtpOriginated = true;
        this.isIncoming = true;
        this.type = type;
        this.incomingOpc = incomingOpc;
        this.incomingDpc = incomingDpc;
        this.sls = incomingSls;
        this.maxDataLen = maxDataLen;
        this.networkId = networkId;
    }

    public int getSls() {
        return sls;
    }

    public void setSls(int sls) {
        this.sls = sls;
    }

    public int getIncomingOpc() {
        return incomingOpc;
    }

    public void setIncomingOpc(int opc) {
        this.incomingOpc = opc;
    }

    public int getIncomingDpc() {
        return incomingDpc;
    }

    public int getOutgoingDpc() {
        return outgoingDpc;
    }

    public void setIncomingDpc(int dpc) {
        this.incomingDpc = dpc;
    }

    public void setOutgoingDpc(int dpc) {
        this.outgoingDpc = dpc;
    }

    public int getType() {
        return type;
    }

    public boolean getIsMtpOriginated() {
        return isMtpOriginated;
    }

    public boolean getIsIncoming() {
        return isIncoming;
    }

    public void setIsIncoming(boolean incoming) {
        isIncoming = incoming;
    }

    public int getOriginLocalSsn() {
        return localOriginSsn;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    public abstract void decode(ByteBuf in, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException;

    public abstract EncodingResultData encode(SccpStackImpl sccpStackImpl, LongMessageRuleType longMessageRuleType, int maxMtp3UserDataLength, Logger logger,
            boolean removeSPC, SccpProtocolVersion sccpProtocolVersion) throws ParseException;

}
