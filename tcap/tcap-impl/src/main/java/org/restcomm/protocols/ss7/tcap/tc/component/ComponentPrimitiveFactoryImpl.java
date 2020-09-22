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

/**
 *
 */
package org.restcomm.protocols.ss7.tcap.tc.component;

import org.restcomm.protocols.ss7.tcap.TCAPProviderImpl;
import org.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemImpl;

/**
 * @author baranowb
 *
 */
public class ComponentPrimitiveFactoryImpl implements ComponentPrimitiveFactory {

    private TCAPProviderImpl provider;

    public ComponentPrimitiveFactoryImpl(TCAPProviderImpl tcaProviderImpl) {
        this.provider = tcaProviderImpl;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory#createTCInvokeRequest()
     */
    public ComponentImpl createTCInvokeRequest() {

    	ComponentImpl t = TcapFactory.createComponentInvoke();
        t.getInvoke().setProvider(provider);
        return t;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory# createTCInvokeRequest()
     */
    public ComponentImpl createTCInvokeRequest(InvokeClass invokeClass) {

    	ComponentImpl t = TcapFactory.createComponentInvoke(invokeClass);
        t.getInvoke().setProvider(provider);
        return t;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory# createTCRejectRequest()
     */
    public ComponentImpl createTCRejectRequest() {

        return TcapFactory.createComponentReject();
    }

    public ComponentImpl createTCReturnErrorRequest() {

        return TcapFactory.createComponentReturnError();
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory# createTCResultRequest(boolean)
     */
    public ComponentImpl createTCResultLastRequest() {

        return TcapFactory.createComponentReturnResultLast();

    }

    public ComponentImpl createTCResultRequest() {

        return TcapFactory.createComponentReturnResult();
    }

    public ProblemImpl createProblem() {
        return TcapFactory.createProblem();
    }
}
