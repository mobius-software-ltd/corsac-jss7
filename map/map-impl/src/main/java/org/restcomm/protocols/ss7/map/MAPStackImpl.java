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

package org.restcomm.protocols.ss7.map;

import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.MAPStack;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class MAPStackImpl implements MAPStack {

    protected TCAPStack tcapStack = null;

    protected MAPProviderImpl mapProvider = null;

    private State state = State.IDLE;

    private final String name;
    private final MAPStackConfigurationManagement mapCfg;

    public MAPStackImpl(String name, SccpProvider sccpPprovider, int ssn) {
        this.name = name;
        this.tcapStack = new TCAPStackImpl(name, sccpPprovider, ssn, 4);
        TCAPProvider tcapProvider = tcapStack.getProvider();
        this.mapCfg = new MAPStackConfigurationManagement();
        mapProvider = new MAPProviderImpl(name, tcapProvider,this.mapCfg);
        this.state = State.CONFIGURED;        
    }

    public MAPStackImpl(String name, TCAPProvider tcapProvider) {
        this.name = name;
        this.mapCfg = new MAPStackConfigurationManagement();
        mapProvider = new MAPProviderImpl(name, tcapProvider,this.mapCfg);
        this.tcapStack = tcapProvider.getStack();
        this.state = State.CONFIGURED;        
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MAPProvider getMAPProvider() {
        return this.mapProvider;
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
        this.mapProvider.start();

        this.state = State.RUNNING;

    }

    @Override
    public void stop() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("Stack is not running!");
        }
        this.mapProvider.stop();
        if (tcapStack != null) {
            this.tcapStack.stop();
        }

        this.state = State.CONFIGURED;
    }

    // // ///////////////
    // // CONF METHOD //
    // // ///////////////
    // /**
    // * @throws ConfigurationException
    // *
    // */
    // public void configure(Properties props) throws ConfigurationException {
    // if (state != State.IDLE) {
    // throw new
    // IllegalStateException("Stack already been configured or is already running!");
    // }
    // tcapStack.configure(props);
    // TCAPProvider tcapProvider = tcapStack.getProvider();
    // mapProvider = new MAPProviderImpl(tcapProvider);
    // this.state = State.CONFIGURED;
    // }

    private enum State {
        IDLE, CONFIGURED, RUNNING;
    }

    @Override
    public TCAPStack getTCAPStack() {
        return this.tcapStack;
    }
}
