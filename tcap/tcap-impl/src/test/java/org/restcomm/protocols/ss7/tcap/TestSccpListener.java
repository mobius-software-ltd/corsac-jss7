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
package org.restcomm.protocols.ss7.tcap;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.sccp.RemoteSccpStatus;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SignallingPointStatus;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.ErrorCause;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
/**
 * 
 * @author yulianoifa
 *
 */
public class TestSccpListener implements SccpListener {

	private static final long serialVersionUID = 1L;

	private boolean congestedStatusReceived = false;
	
	@Override
	public void onMessage(SccpDataMessage message) {
		
	}

	@Override
	public void onNotice(SccpNoticeMessage message) {
		
	}

	@Override
	public void onCoordResponse(int ssn, int multiplicityIndicator) {
		
	}

	@Override
	public void onState(int dpc, int ssn, boolean inService,
			int multiplicityIndicator) {
		
	}

	@Override
	public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel, RemoteSccpStatus remoteSccpStatus) {
		if(status.equals(SignallingPointStatus.CONGESTED))
		    congestedStatusReceived = true;
	}

	public boolean isCongestedStatusReceived() {
		return congestedStatusReceived;
	}

    @Override
    public void onConnectIndication(SccpConnection conn, SccpAddress calledAddress, SccpAddress callingAddress,
            ProtocolClass clazz, Credit credit, ByteBuf data, Importance importance) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onConnectConfirm(SccpConnection conn, ByteBuf data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, ReleaseCause reason, ByteBuf data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, RefusalCause reason, ByteBuf data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, ErrorCause errorCause) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onResetIndication(SccpConnection conn, ResetCause reason) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onResetConfirm(SccpConnection conn) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onData(SccpConnection conn, ByteBuf data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDisconnectConfirm(SccpConnection conn) {
        // TODO Auto-generated method stub
        
    }

}
