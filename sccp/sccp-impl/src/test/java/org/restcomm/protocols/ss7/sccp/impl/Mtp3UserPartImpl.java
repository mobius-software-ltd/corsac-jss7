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

package org.restcomm.protocols.ss7.sccp.impl;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusCause;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartBaseImpl;

/**
 * @author abhayani
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public class Mtp3UserPartImpl extends Mtp3UserPartBaseImpl {

    // protected ConcurrentLinkedQueue<byte[]> readFrom;
    // protected ConcurrentLinkedQueue<byte[]> writeTo;

    private List<Mtp3UserPartImpl> otherParts = new ArrayList<Mtp3UserPartImpl>();
    private ArrayList<Mtp3TransferPrimitive> messages = new ArrayList<Mtp3TransferPrimitive>();
    private List<Integer> dpcs = new ArrayList<Integer>();

    SccpHarness sccpHarness;

    public Mtp3UserPartImpl(SccpHarness sccpHarness) {
        super(null);
        this.sccpHarness = sccpHarness;
        try {
            this.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setOtherPart(Mtp3UserPartImpl otherPart) {
        this.otherParts.add(otherPart);
    }

    public void sendMessage(Mtp3TransferPrimitive msg) throws IOException {
        if (!this.otherParts.isEmpty()) {
            if (otherParts.size() == 1) {
                this.otherParts.iterator().next().sendTransferMessageToLocalUser(msg, msg.getSls());
            } else {
                for (Mtp3UserPartImpl part: otherParts) {
                    if (part.dpcs.contains(msg.getDpc())) {
                        part.sendTransferMessageToLocalUser(msg, msg.getSls());
                    }
                }
            }
        } else
            this.messages.add(msg);
    }

    public void sendTransferMessageToLocalUser(int opc, int dpc, ByteBuf data) {
        int si = Mtp3UserPartBaseImpl._SI_SERVICE_SCCP;
        int ni = 2;
        int mp = 0;
        int sls = 0;
        Mtp3TransferPrimitiveFactory factory = this.getMtp3TransferPrimitiveFactory();
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(si, ni, mp, opc, dpc, sls, data);
        int seqControl = 0;
        this.sendTransferMessageToLocalUser(msg, seqControl);
    }

    public void sendPauseMessageToLocalUser(int affectedDpc) {
        Mtp3PausePrimitive msg = new Mtp3PausePrimitive(affectedDpc);
        this.sendPauseMessageToLocalUser(msg);
    }

    public void sendResumeMessageToLocalUser(int affectedDpc) {
        Mtp3ResumePrimitive msg = new Mtp3ResumePrimitive(affectedDpc);
        this.sendResumeMessageToLocalUser(msg);
    }

    public void sendStatusMessageToLocalUser(int affectedDpc, Mtp3StatusCause cause, int congestionLevel, int userPartIdentity) {
        Mtp3StatusPrimitive msg = new Mtp3StatusPrimitive(affectedDpc, cause, congestionLevel, userPartIdentity);
        this.sendStatusMessageToLocalUser(msg);
    }

    public void sendEndCongestionMessageToLocalUser(int affectedDpc) {
        Mtp3EndCongestionPrimitive msg = new Mtp3EndCongestionPrimitive(affectedDpc);
        this.sendEndCongestionMessageToLocalUser(msg);
    }

    public List<Mtp3TransferPrimitive> getMessages() {
        return messages;
    }

    @Override
    public int getMaxUserDataLength(int dpc) {
        return 1000;
    }

    public void addDpc(int dpc) {
        if (!dpcs.contains(dpc)) {
            dpcs.add(dpc);
        }
    }
}
