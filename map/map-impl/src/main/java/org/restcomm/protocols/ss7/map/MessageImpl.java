/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map;

import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPMessage;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
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
    
    public ByteBuf getOriginalBuffer() {
    	return this.originalBuffer;
    }
    
    public void retain() {
    	if(originalBuffer!=null)
    		ReferenceCountUtil.retain(originalBuffer);
    }
    
    public void release() {
    	if(originalBuffer!=null && originalBuffer.refCnt()>0)
    		ReferenceCountUtil.release(originalBuffer, originalBuffer.refCnt());
    }
}
