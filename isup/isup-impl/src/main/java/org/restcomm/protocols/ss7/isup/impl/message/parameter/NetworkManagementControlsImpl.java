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
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.NetworkManagementControls;

/**
 * Start time:12:02:43 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class NetworkManagementControlsImpl extends AbstractISUPParameter implements NetworkManagementControls {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;
    // FIXME - should we switch to boolean[] - its a slight perf loss :P
    private List<Boolean> networkManagementControls = null;

    public NetworkManagementControlsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public NetworkManagementControlsImpl() {
        super();

    }

    public void decode(ByteBuf b) throws ParameterException {
    	List<Boolean> nmc=new ArrayList<Boolean>();
    	while(b.readableBytes()>0) {    		
    		if((b.readByte() & 0x01)!=0)
    			nmc.add(true);
    		else
    			nmc.add(false);
    	}
    	
    	setNetworkManagementControls(nmc);
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        for (int index = 0; index < this.networkManagementControls.size()-1; index++) {
        	if(this.networkManagementControls.get(index))
        		buffer.writeByte(0x01);
        	else
        		buffer.writeByte(0x00);
        }
         
        if(this.networkManagementControls.get(this.networkManagementControls.size() - 1))
    		buffer.writeByte(0x81);
    	else
    		buffer.writeByte(0x80);        
    }

    public boolean isTARControlEnabled(byte b) {
        return (b & 0x01) == _TURN_ON;
    }

    public byte createTAREnabledByte(boolean enabled) {
        return (byte) (enabled ? _TURN_ON : _TURN_OFF);
    }

    public List<Boolean> getNetworkManagementControls() {
        return networkManagementControls;
    }

    public void setNetworkManagementControls(List<Boolean> networkManagementControls) throws IllegalArgumentException {
        if (networkManagementControls == null || networkManagementControls.size() == 0) {
            throw new IllegalArgumentException("buffer must not be null and length must be greater than 0");
        }
        this.networkManagementControls = networkManagementControls;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
