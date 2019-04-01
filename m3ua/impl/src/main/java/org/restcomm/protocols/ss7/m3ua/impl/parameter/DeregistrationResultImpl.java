/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.m3ua.impl.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.m3ua.parameter.DeregistrationResult;
import org.restcomm.protocols.ss7.m3ua.parameter.DeregistrationStatus;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 *
 * @author amit bhayani
 *
 */
public class DeregistrationResultImpl extends ParameterImpl implements DeregistrationResult {

    private RoutingContext rc;
    private DeregistrationStatus status;
    private ByteBuf buf = Unpooled.buffer(16);

    public DeregistrationResultImpl(RoutingContext rc, DeregistrationStatus status) {
        this.tag = Parameter.Deregistration_Result;
        this.rc = rc;
        this.status = status;

        this.encode();
    }

    public DeregistrationResultImpl(ByteBuf data) {
        this.tag = Parameter.Deregistration_Result;
        while (data.readableBytes()>0) {
            short tag = (short) ((data.readByte() & 0xff) << 8 | (data.readByte() & 0xff));
            short len = (short) ((data.readByte() & 0xff) << 8 | (data.readByte() & 0xff));

            ByteBuf value = data.slice(data.readerIndex(),len - 4);
            data.skipBytes(len-4);
            // parameters.put(tag, factory.createParameter(tag, value));
            switch (tag) {
                case ParameterImpl.Routing_Context:
                    this.rc = new RoutingContextImpl(value);
                    break;

                case ParameterImpl.Deregistration_Status:
                    this.status = new DeregistrationStatusImpl(value);
                    break;

            }

            // The Parameter Length does not include any padding octets. We have
            // to consider padding here
            if(len%4!=0)
            	data.skipBytes(len%4);
        }// end of while
    }

    private void encode() {
        ((RoutingContextImpl) this.rc).write(buf);
        ((DeregistrationStatusImpl) this.status).write(buf);
    }

    @Override
    protected ByteBuf getValue() {
        return Unpooled.wrappedBuffer(this.buf);
    }

    public DeregistrationStatus getDeregistrationStatus() {
        return this.status;
    }

    public RoutingContext getRoutingContext() {
        return this.rc;
    }

}
