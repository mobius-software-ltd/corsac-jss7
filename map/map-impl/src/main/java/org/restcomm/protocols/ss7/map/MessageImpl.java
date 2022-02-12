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

package org.restcomm.protocols.ss7.map;

import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPMessage;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public abstract class MessageImpl implements MAPMessage {
	private static final long serialVersionUID = 1L;

    private int invokeId;
    private MAPDialog mapDialog;
    private boolean returnResultNotLast = false;
    private ByteBuf originalBuffer;
    
    public int getInvokeId() {
        return this.invokeId;
    }

    public MAPDialog getMAPDialog() {
        return this.mapDialog;
    }

    public void setInvokeId(int invokeId) {
        this.invokeId = invokeId;
    }

    public void setMAPDialog(MAPDialog mapDialog) {
        this.mapDialog = mapDialog;
    }

    public boolean isReturnResultNotLast() {
        return returnResultNotLast;
    }

    public void setReturnResultNotLast(boolean returnResultNotLast) {
        this.returnResultNotLast = returnResultNotLast;
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
