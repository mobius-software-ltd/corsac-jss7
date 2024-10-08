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
import org.restcomm.protocols.ss7.isup.message.parameter.PivotStatus;
import org.restcomm.protocols.ss7.isup.message.parameter.Status;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public class PivotStatusImpl extends AbstractISUPParameter implements PivotStatus {
	private List<Status> statusList = new ArrayList<Status>();

    public PivotStatusImpl() {
        // TODO Auto-generated constructor stub
    }

    public PivotStatusImpl(ByteBuf data) throws ParameterException {
        decode(data);
    }

    @Override
    public int getCode() {
        return _PARAMETER_CODE;
    }

    @Override
    public void decode(ByteBuf b) throws ParameterException {
        //FIXME: ? this does not take into account case when extension bit is used.
        while(b.readableBytes()>0) {
            Status s = new StatusImpl();
            s.setStatus(b.readByte());
            this.statusList.add(s);
        }
    }

    @Override
    public void encode(ByteBuf buffer) throws ParameterException {
        for (int index = 0; index < this.statusList.size(); index++) {
        	buffer.writeByte(this.statusList.get(index).getStatus());
        }
    }

    @Override
    public void setStatus(Status... status) {
        this.statusList.clear();
        for (Status s : status) {
            if (s != null) {
                this.statusList.add(s);
            }
        }
    }

    @Override
    public Status[] getStatus() {
        return this.statusList.toArray(new Status[] {});
    }

}
