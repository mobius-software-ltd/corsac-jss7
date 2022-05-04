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
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author yulianoifa
 *
 */
public abstract class SccpConnSegmentableMessageImpl extends SccpConnReferencedMessageImpl {
    protected ByteBuf userData;
    private List<ByteBuf> buffer = new ArrayList<>();
    private boolean isFullyReceived;

    protected SccpConnSegmentableMessageImpl(int maxDataLen, int type, int sls, int localSsn) {
        super(maxDataLen, type, sls, localSsn);
    }

    protected SccpConnSegmentableMessageImpl(int maxDataLen, int type, int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(maxDataLen, type, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    public ByteBuf getUserData() {
        return Unpooled.wrappedBuffer(userData);
    }

    public void setUserData(ByteBuf userData) {
    	this.userData = userData;
    }

    public boolean isFullyRecieved() {
        return this.isFullyReceived;
    }

    public abstract boolean isMoreData();

    public abstract void setMoreData(boolean moreData);

    public void setReceivedNextSegment(SccpConnSegmentableMessageImpl nextSegment) {
        if (this.buffer.isEmpty()) {
            this.buffer.add(userData);
        }
        this.buffer.add(nextSegment.userData);

        if (!nextSegment.isMoreData()) {
            userData=Unpooled.wrappedBuffer(buffer.toArray(new ByteBuf[0]));
            this.buffer.clear();
            this.isFullyReceived = true;
        }
    }

    public void cancelSegmentation() {
        this.isFullyReceived = false;
    }
}
