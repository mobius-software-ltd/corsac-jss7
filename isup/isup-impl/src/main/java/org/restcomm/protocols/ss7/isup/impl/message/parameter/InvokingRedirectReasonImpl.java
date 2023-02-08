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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.InformationType;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingRedirectReason;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectReason;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public class InvokingRedirectReasonImpl extends AbstractInformationImpl implements InvokingRedirectReason {
	private List<RedirectReason> reasons = new ArrayList<RedirectReason>();


    public InvokingRedirectReasonImpl() {
        super(InformationType.InvokingRedirectReason);
    }
    @Override
    public void setReason(RedirectReason... reasons) {
        this.reasons.clear();
        if(reasons == null){
            return;
        }
        for(RedirectReason pr: reasons){
            if(pr!=null){
                this.reasons.add(pr);
            }
        }
    }

    @Override
    public RedirectReason[] getReason() {
        return this.reasons.toArray(new RedirectReason[this.reasons.size()]);
    }
    @Override
    public void encode(ByteBuf buffer) throws ParameterException {
        for (int index = 0; index < this.reasons.size(); index++) {
            RedirectReasonImpl ai = (RedirectReasonImpl) this.reasons.get(index);
            ai.trim();

            byte b = (byte) (ai.getRedirectReason() & 0x7F);
            if (index + 1 == this.reasons.size()) {
                b |= 0x80;
            }
            buffer.writeByte(b);
        }
    }

    @Override
    public void decode(ByteBuf data) throws ParameterException {
        while(data.readableBytes()>0){
            byte b = data.readByte();
            RedirectReasonImpl pr = new RedirectReasonImpl();
            pr.setRedirectReason((byte) (b & 0x7F));
            if( (b & 0x80) == 0 && data.readableBytes()==0){
                throw new ParameterException("Extension bit incicates more bytes, but we ran out of them!");
            }
            this.reasons.add(pr);
        }
    }
	@Override
	public int getLength() {
		return this.reasons.size();
	}

}
