/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.inap;

import org.restcomm.protocols.ss7.inap.api.INAPProvider;
import org.restcomm.protocols.ss7.inap.api.INAPStack;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPStackImpl implements INAPStack {

    protected TCAPStack tcapStack = null;

    protected INAPProviderImpl inapProvider = null;

    private State state = State.IDLE;

    private final String name;

    public INAPStackImpl(String name, SccpProvider sccpPprovider, int ssn,int threads) {
        this.name = name;
        this.tcapStack = new TCAPStackImpl(name, sccpPprovider, ssn, threads);
        TCAPProvider tcapProvider = tcapStack.getProvider();
        inapProvider = new INAPProviderImpl(name, tcapProvider);

        this.state = State.CONFIGURED;       
    }

    public INAPStackImpl(String name, TCAPProvider tcapProvider) {
        this.name = name;
        inapProvider = new INAPProviderImpl(name, tcapProvider);
        this.tcapStack = tcapProvider.getStack();
        this.state = State.CONFIGURED;        
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public INAPProvider getINAPProvider() {
        return this.inapProvider;
    }

    @Override
    public void start() throws Exception {
        if (state != State.CONFIGURED) {
            throw new IllegalStateException("Stack has not been configured or is already running!");
        }
        if (tcapStack != null) {
            // this is null in junits!
            this.tcapStack.start();
        }
        this.inapProvider.start();

        this.state = State.RUNNING;

    }

    @Override
    public void stop() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("Stack is not running!");
        }
        this.inapProvider.stop();
        if (tcapStack != null) {
            this.tcapStack.stop();
        }

        this.state = State.CONFIGURED;
    }

    @Override
    public TCAPStack getTCAPStack() {
        return this.tcapStack;
    }

    private enum State {
        IDLE, CONFIGURED, RUNNING;
    }

}
