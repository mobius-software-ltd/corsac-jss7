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

package org.restcomm.protocols.ss7.cap;

import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPMessage;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public abstract class MessageImpl implements CAPMessage {
	private static final long serialVersionUID = 1L;

	private long invokeId;
    private CAPDialog capDialog;
    private ByteBuf originalBuffer;
    
    public long getInvokeId() {
        return this.invokeId;
    }

    public CAPDialog getCAPDialog() {
        return this.capDialog;
    }

    public void setInvokeId(long invokeId) {
        this.invokeId = invokeId;
    }

    public void setCAPDialog(CAPDialog capDialog) {
        this.capDialog = capDialog;
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
