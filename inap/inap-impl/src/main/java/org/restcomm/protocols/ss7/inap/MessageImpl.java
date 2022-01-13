/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.inap;

import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPMessage;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

/**
 *
 * @author yulian.oifa
 *
 */
public abstract class MessageImpl implements INAPMessage {
	private static final long serialVersionUID = 1L;

	private long invokeId;
    private INAPDialog inapDialog;
    private ByteBuf originalBuffer;

    public long getInvokeId() {
        return this.invokeId;
    }

    public INAPDialog getINAPDialog() {
        return this.inapDialog;
    }

    public void setInvokeId(long invokeId) {
        this.invokeId = invokeId;
    }

    public void setINAPDialog(INAPDialog inapDialog) {
        this.inapDialog = inapDialog;
    }

    protected void addInvokeIdInfo(StringBuilder sb) {
        sb.append("InvokeId=");
        sb.append(this.invokeId);
    }

    public void setOriginalBuffer(ByteBuf buffer) {
    	this.originalBuffer=buffer;
    }
    
    public void retain() {
    	if(originalBuffer!=null)
    		ReferenceCountUtil.retain(originalBuffer);
    }
    
    public void release() {
    	if(originalBuffer!=null)
    		ReferenceCountUtil.release(originalBuffer);
    }
}