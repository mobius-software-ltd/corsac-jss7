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

package org.restcomm.protocols.ss7.m3ua.impl.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
import org.restcomm.protocols.ss7.m3ua.parameter.RegistrationResult;
import org.restcomm.protocols.ss7.m3ua.parameter.RegistrationStatus;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
/**
 * 
 * @author yulianoifa
 *
 */
public class RegistrationResultImpl extends ParameterImpl implements RegistrationResult {

    private LocalRKIdentifier localRKId;
    private RegistrationStatus status;
    private RoutingContext rc;

    private ByteBuf buf;
    
    public RegistrationResultImpl(ByteBuf data) {
        this.tag = Parameter.Registration_Result;
        while (data.readableBytes()>0) {
            short tag = (short) ((data.readByte() & 0xff) << 8 | (data.readByte() & 0xff));
            short len = (short) ((data.readByte() & 0xff) << 8 | (data.readByte() & 0xff));

            ByteBuf value = data.slice(data.readerIndex(),len - 4);
            data.skipBytes(len-4);
            // parameters.put(tag, factory.createParameter(tag, value));
            switch (tag) {
                case ParameterImpl.Local_Routing_Key_Identifier:
                    this.localRKId = new LocalRKIdentifierImpl(value);
                    break;

                case ParameterImpl.Routing_Context:
                    this.rc = new RoutingContextImpl(value);
                    break;

                case ParameterImpl.Registration_Status:
                    this.status = new RegistrationStatusImpl(value);
                    break;

            }

            // The Parameter Length does not include any padding octets. We have
            // to consider padding here
            if(len%4!=0)
            	data.skipBytes(len%4);
        }// end of while
    }

    public RegistrationResultImpl(LocalRKIdentifier localRKId, RegistrationStatus status, RoutingContext rc) {
        this.tag = Parameter.Registration_Result;
        this.localRKId = localRKId;
        this.status = status;
        this.rc = rc;
    }

    private void encode() {
    	this.buf=Unpooled.buffer(24);
    	
        ((LocalRKIdentifierImpl) this.localRKId).write(buf);

        ((RoutingContextImpl) rc).write(buf);

        ((RegistrationStatusImpl) this.status).write(buf);        
    }

    @Override
    protected ByteBuf getValue() {
    	if(buf==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(this.buf);
    }

    public LocalRKIdentifier getLocalRKIdentifier() {
        return this.localRKId;
    }

    public RegistrationStatus getRegistrationStatus() {
        return this.status;
    }

    public RoutingContext getRoutingContext() {
        return this.rc;
    }

    @Override
    public String toString() {
        StringBuilder tb = new StringBuilder();
        tb.append("RegistrationResult(");
        if (localRKId != null) {
            tb.append(localRKId.toString());
        }

        if (status != null) {
            tb.append(status.toString());
        }

        if (rc != null) {
            tb.append(rc.toString());
        }
        tb.append(")");
        return tb.toString();
    }
}
