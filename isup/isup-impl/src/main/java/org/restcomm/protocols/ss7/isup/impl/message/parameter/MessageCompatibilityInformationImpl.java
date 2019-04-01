/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

import java.util.LinkedList;
import java.util.List;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageCompatibilityInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageCompatibilityInstructionIndicator;

/**
 * @author baranowb
 *
 */
public class MessageCompatibilityInformationImpl extends AbstractISUPParameter implements MessageCompatibilityInformation {
	private List<MessageCompatibilityInstructionIndicator> indicators = new LinkedList<MessageCompatibilityInstructionIndicator>();

    public MessageCompatibilityInformationImpl() {
        // TODO Auto-generated constructor stub
    }

    public MessageCompatibilityInformationImpl(ByteBuf body) throws ParameterException {
        decode(body);
    }

    @Override
    public int getCode() {
        return _PARAMETER_CODE;
    }

    @Override
    public void setMessageCompatibilityInstructionIndicators(List<MessageCompatibilityInstructionIndicator> indicators) {
        this.indicators.clear();
        for(MessageCompatibilityInstructionIndicator i:indicators){
            if(i == null)
                continue;
            this.indicators.add(i);
        }
    }

    @Override
    public List<MessageCompatibilityInstructionIndicator> getMessageCompatibilityInstructionIndicators() {
        return indicators;
    }

    @Override
    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() == 0) {
            throw new ParameterException("byte[] must  not be null and length must  be greater than 0");
        }

        while(b.readableBytes()>0){
            int v = b.readByte();
            if(b.readableBytes()==0){
                if( (v & 0x7F) == 0){
                    throw new ParameterException("Extension bit indicates more content, but byte[] is done...");
                }
            } else {
                if( (v & 0x7F) == 1){
                    throw new ParameterException("Extension bit indicates end of content, but byte[] is not done...");
                }
            }
            MessageCompatibilityInstructionIndicatorImpl instructions = new MessageCompatibilityInstructionIndicatorImpl();
            instructions.decode((byte)v);
            this.indicators.add(instructions);
        }
    }

    @Override
    public void encode(ByteBuf buffer) throws ParameterException {
        final int limit = this.indicators.size() -1;
        for(int index = 0;index<this.indicators.size();index++){
            byte val=((MessageCompatibilityInstructionIndicatorImpl)this.indicators.get(index)).encode();
            if(index==limit){
                val = (byte)(val & 0x7F | 0x80);
            } else {
                val = (byte)(val & 0x7F);
            }
            
            buffer.writeByte(val);
        }        
    }

}
