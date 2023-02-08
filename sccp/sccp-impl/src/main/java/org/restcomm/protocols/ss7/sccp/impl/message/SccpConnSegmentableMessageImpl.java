/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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
