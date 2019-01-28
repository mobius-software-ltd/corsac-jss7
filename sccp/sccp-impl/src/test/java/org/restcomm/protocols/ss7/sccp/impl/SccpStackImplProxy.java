/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

import java.util.Iterator;
import java.util.concurrent.Executors;

import org.restcomm.protocols.ss7.mtp.Mtp3UserPart;
import org.restcomm.protocols.ss7.sccp.impl.SccpProviderImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpResourceImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpRoutingControl;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.router.RouterImpl;

/**
 * @author baranowb
 *
 */
public class SccpStackImplProxy extends SccpStackImpl {

    /**
	 *
	 */
    public SccpStackImplProxy(String name) {
        super(name);
    }

    public SccpManagementProxy getManagementProxy() {
        return (SccpManagementProxy) super.sccpManagement;
    }

    @Override
    public void start() {
        this.messageFactory = new MessageFactoryImpl(this);

        this.sccpProvider = new SccpProviderImpl(this);

        super.sccpManagement = new SccpManagementProxy(this.getName(), sccpProvider, this);
        super.sccpRoutingControl = new SccpRoutingControl(sccpProvider, this);
//        super.sccpCongestionControl = new SccpCongestionControl(sccpManagement, this);

        super.sccpManagement.setSccpRoutingControl(sccpRoutingControl);
        super.sccpRoutingControl.setSccpManagement(sccpManagement);
//        this.sccpManagement.setSccpCongestionControl(sccpCongestionControl);

        this.router = new RouterImpl(this.getName(), this);
        this.router.start();

        this.sccpResource = new SccpResourceImpl(this.getName());
        this.sccpResource.start();

        this.sccpRoutingControl.start();
        this.sccpManagement.start();
        // layer3exec.execute(new MtpStreamHandler());

        this.msgDeliveryExecutors = Executors.newScheduledThreadPool(1);

        Iterator<Mtp3UserPart> iterator=this.mtp3UserParts.values().iterator();
        while(iterator.hasNext()) {
            Mtp3UserPart mup = iterator.next();
            mup.addMtp3UserPartListener(this);
        }
        // this.mtp3UserPart.addMtp3UserPartListener(this);
        // initiating of SCCP delivery executors

        int maxSls = 16;
        slsFilter = 0x0f;
        this.slsTable = new int[maxSls];
        this.createSLSTable(maxSls, this.deliveryTransferMessageThreadCount);
        
        this.state = State.RUNNING;
    }

    public int getReassemplyCacheSize() {
        return reassemplyCache.size();
    }

    @Override
    public void setReassemblyTimerDelay(int reassemblyTimerDelay) {
        this.reassemblyTimerDelay = reassemblyTimerDelay;
    }

}
