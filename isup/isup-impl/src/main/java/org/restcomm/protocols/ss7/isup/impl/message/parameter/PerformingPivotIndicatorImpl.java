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

/**
 * Start time:09:09:20 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.InformationType;
import org.restcomm.protocols.ss7.isup.message.parameter.PerformingPivotIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotReason;

/**
 * Start time:09:09:20 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class PerformingPivotIndicatorImpl extends AbstractInformationImpl implements PerformingPivotIndicator {
	private List<PivotReason> reasons = new ArrayList<PivotReason>();

    public PerformingPivotIndicatorImpl() {
        super(InformationType.PerformingPivotIndicator);
        super.tag = 0x03;
    }

    @Override
    public void setReason(List<PivotReason> reasons) {
        this.reasons.clear();
        if(reasons == null){
            return;
        }
        for(PivotReason pr: reasons){
            if(pr!=null){
                this.reasons.add(pr);
            }
        }
    }

    @Override
    public List<PivotReason> getReason() {
        return this.reasons;
    }

    @Override
    public void encode(ByteBuf buffer) throws ParameterException {
        for(PivotReason pr:this.reasons){
            PivotReasonImpl ai = (PivotReasonImpl) pr;
            if(ai.getPivotPossibleAtPerformingExchange()!=0x00) {
                buffer.writeByte(ai.getPivotReason() & 0x7F);
            	buffer.writeByte((ai.getPivotPossibleAtPerformingExchange() & 0x07) | 0x80);
            }
            else {
            	buffer.writeByte((ai.getPivotReason() & 0x7F) | 0x80);            	
            }
        }
    }

    @Override
    public void decode(ByteBuf data) throws ParameterException {
        while(data.readableBytes()>0){
            byte b = data.readByte();
            PivotReasonImpl pr = new PivotReasonImpl();
            pr.setPivotReason((byte) (b & 0x7F));
            if( (b & 0x80) == 0){
                if (data.readableBytes()==0){
                    throw new ParameterException("Extension bit incicates more bytes, but we ran out of them!");
                }
                b = data.readByte();
                pr.setPivotPossibleAtPerformingExchange((byte) (b & 0x07));
            }
            this.reasons.add(pr);
        }
    }

	@Override
	public int getLength() {
		int length=0;
		for(PivotReason curr:reasons)
			if(curr.getPivotPossibleAtPerformingExchange() == 0)
				length+=1;
			else
				length+=2;
		
		return length;
	}
}