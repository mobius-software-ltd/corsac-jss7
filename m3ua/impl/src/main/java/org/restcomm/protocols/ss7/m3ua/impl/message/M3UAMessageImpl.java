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

package org.restcomm.protocols.ss7.m3ua.impl.message;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.message.M3UAMessage;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 * @author amit bhayani
 * @author kulikov
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public abstract class M3UAMessageImpl implements M3UAMessage {
    // header part
    private int messageClass;
    private int messageType;

    private String message;
    protected ConcurrentHashMap<Short, Parameter> parameters = new ConcurrentHashMap<Short, Parameter>();

    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

    int initialPosition = 0;

    public M3UAMessageImpl(String message) {
        this.message = message;
    }

    protected M3UAMessageImpl(int messageClass, int messageType, String message) {
        this(message);
        this.messageClass = messageClass;
        this.messageType = messageType;
    }

    protected abstract void encodeParams(ByteBuf buffer);

    public void encode(ByteBuf byteBuf) {
        byteBuf.writeByte(1);
        byteBuf.writeByte(0);
        byteBuf.writeByte(messageClass);
        byteBuf.writeByte(messageType);

        byteBuf.markWriterIndex();
        byteBuf.writeInt(8);
        int currIndex=byteBuf.writerIndex();

        encodeParams(byteBuf);

        int newIndex=byteBuf.writerIndex();
        byteBuf.resetWriterIndex();
        byteBuf.writeInt(newIndex-currIndex + 8);
        byteBuf.writerIndex(newIndex);
    }

    protected void decode(ByteBuf data) {
        while (data.readableBytes() >= 4) {
            short tag = (short) ((data.readUnsignedByte() << 8) | (data.readUnsignedByte()));
            short len = (short) ((data.readUnsignedByte() << 8) | (data.readUnsignedByte()));

            if (data.readableBytes() < len - 4) {
                return;
            }
            
            parameters.put(tag, factory.createParameter(tag, data.slice(data.readerIndex(), len-4)));
            data.skipBytes(len-4);

            // The Parameter Length does not include any padding octets. We have
            // to consider padding here
            int padding = 4 - (len % 4);
            if (padding < 4) {
                if (data.readableBytes() < padding)
                    return;
                else
                    data.skipBytes(padding);
            }
        }
    }

    public int getMessageClass() {
        return messageClass;
    }

    public int getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        StringBuilder tb = new StringBuilder();
        tb.append(this.message).append(" Params(");
        Iterator<Parameter> iterator=parameters.values().iterator();
        while(iterator.hasNext()) {
            Parameter value = iterator.next();
            tb.append(value.toString());
            tb.append(", ");
        }
        tb.append(")");
        return tb.toString();
    }
}
