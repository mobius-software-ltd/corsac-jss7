package org.restcomm.protocols.ss7.sccp.impl.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

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
