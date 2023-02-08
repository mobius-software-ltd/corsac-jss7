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

package org.restcomm.protocols.ss7.sccp.impl;

import java.io.IOException;

import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
/**
 * 
 * @author yulianoifa
 *
 */
public class AdvancedUser extends User {

    private static final long serialVersionUID = 1L;

    public AdvancedUser(SccpProvider provider, SccpAddress address, SccpAddress dest, int ssn) {
        super(provider, address, dest, ssn);
    }

    @Override
    public void onMessage(SccpDataMessage message) {
        this.messages.add(message);
        System.out.println(String.format("SccpDataMessage=%s seqControl=%d", message, message.getSls()));
        SccpAddress calledAddress = message.getCalledPartyAddress();
        SccpAddress callingAddress = message.getCallingPartyAddress();
        SccpDataMessage newMessage = provider.getMessageFactory().createDataMessageClass1(callingAddress, calledAddress, message.getData(),
                message.getSls(),message.getOriginLocalSsn(), true, message.getHopCounter(), message.getImportance());
        ((SccpMessageImpl) newMessage).setOutgoingDpc(message.getIncomingOpc());
        try {
            this.provider.send(newMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
