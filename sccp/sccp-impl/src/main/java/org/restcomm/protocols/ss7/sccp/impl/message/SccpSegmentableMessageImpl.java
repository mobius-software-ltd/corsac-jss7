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
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.restcomm.protocols.ss7.sccp.parameter.HopCounter;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.sccp.parameter.Segmentation;

/**
 *
 * This interface represents a SCCP message for connectionless data transfer (UDT, XUDT and LUDT)
 *
 * @author sergey vetyutnev
 * @author yulian.oifa
 *
 */
public abstract class SccpSegmentableMessageImpl extends SccpAddressedMessageImpl {

    protected ByteBuf data;
    private List<ByteBuf> allBufs=new ArrayList<ByteBuf>();
    protected SegmentationImpl segmentation;

    protected boolean isFullyRecieved;
    protected int remainingSegments;
    
    protected SccpStackImpl.MessageReassemblyProcess mrp;

    protected SccpSegmentableMessageImpl(int maxDataLen, int type, int outgoingSls, int localSsn,
            SccpAddress calledParty, SccpAddress callingParty, ByteBuf data, HopCounter hopCounter) {
        super(maxDataLen,type, outgoingSls, localSsn, calledParty, callingParty, hopCounter);

        this.data = data;
        this.allBufs.add(data);
        this.isFullyRecieved = true;
    }

    protected SccpSegmentableMessageImpl(int maxDataLen, int type, int incomingOpc, int incomingDpc,
            int incomingSls, int networkId) {
        super(maxDataLen,type, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    public Segmentation getSegmentation() {
        return segmentation;
    }

    public boolean getIsFullyRecieved() {
        return this.isFullyRecieved;
    }

    public int getRemainingSegments() {
        return remainingSegments;
    }

    public ByteBuf getData() {
        return Unpooled.wrappedBuffer(this.data);
    }

    public void setData(ByteBuf data) {
    	this.data = data;
    }

    public void setReceivedSingleSegment() {
        this.isFullyRecieved = true;
        //release and retain would be done based on array even for single item
        this.allBufs.clear();
        this.allBufs.add(data);        
    }

    public void setReceivedFirstSegment() {
        if (this.segmentation == null)
            // this can not occur
            return;

        this.allBufs.clear();
        this.allBufs.add(data);
        this.remainingSegments = this.segmentation.getRemainingSegments();        
    }

    public void setReceivedNextSegment(SccpSegmentableMessageImpl nextSegement) {
    	this.allBufs.add(nextSegement.data);
    	
    	ByteBuf[] bufarray=new ByteBuf[allBufs.size()];
    	bufarray=allBufs.toArray(bufarray);
    	this.data=Unpooled.wrappedBuffer(bufarray);
        if (--this.remainingSegments == 0) {
            this.isFullyRecieved = true;
        }
    }

    public void releaseBuffers() {
    	for(ByteBuf curr:allBufs)
    		ReferenceCountUtil.release(curr);
    }

    public void retainBuffers() {
    	for(ByteBuf curr:allBufs)
    		ReferenceCountUtil.retain(curr);
    }
    
    public void cancelSegmentation() {
        this.remainingSegments = -1;
        this.isFullyRecieved = false;
    }

    public SccpStackImpl.MessageReassemblyProcess getMessageReassemblyProcess() {
        return mrp;
    }

    public void setMessageReassemblyProcess(SccpStackImpl.MessageReassemblyProcess mrp) {
        this.mrp = mrp;
    }
}
