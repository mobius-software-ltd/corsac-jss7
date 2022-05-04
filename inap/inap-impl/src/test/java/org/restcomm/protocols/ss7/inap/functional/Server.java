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

package org.restcomm.protocols.ss7.inap.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPParameterFactory;
import org.restcomm.protocols.ss7.inap.api.INAPProvider;
import org.restcomm.protocols.ss7.inap.api.INAPStack;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageFactory;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RequestReportBCSMEventRequestImpl;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author yulian.oifa
*/
public class Server extends EventTestHarness {

    private static Logger logger = LogManager.getLogger(Server.class);

    protected INAPStack inapStack;
    protected INAPProvider inapProvider;

    protected INAPParameterFactory inapParameterFactory;
    protected INAPErrorMessageFactory inapErrorMessageFactory;
    protected ISUPParameterFactory isupParameterFactory;

    protected INAPDialog serverCscDialog;

    // private boolean _S_recievedDialogRequest;
    // private boolean _S_recievedInitialDp;
    //
    // private int dialogStep;
    // private long savedInvokeId;
    // private String unexpected = "";
    //
    // private FunctionalTestScenario step;

    Server(INAPStack inapStack, INAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
        super(logger);
        this.inapStack = inapStack;
        this.inapProvider = this.inapStack.getINAPProvider();

        this.inapParameterFactory = this.inapProvider.getINAPParameterFactory();
        this.isupParameterFactory = this.inapProvider.getISUPParameterFactory();

        this.inapErrorMessageFactory = this.inapProvider.getINAPErrorMessageFactory();
        
        this.inapProvider.addINAPDialogListener(UUID.randomUUID(),this);
        this.inapProvider.getINAPServiceCircuitSwitchedCall().addINAPServiceListener(this);
        
        this.inapProvider.getINAPServiceCircuitSwitchedCall().acivate();
    }        

    public RequestReportBCSMEventRequest getRequestReportBCSMEventRequest() {

        List<BCSMEvent> bcsmEventList = new ArrayList<BCSMEvent>();
        BCSMEvent ev = this.inapParameterFactory.createBCSMEvent(EventTypeBCSM.routeSelectFailure,
                MonitorMode.notifyAndContinue, null, null, false);
        bcsmEventList.add(ev);
        ev = this.inapParameterFactory.createBCSMEvent(EventTypeBCSM.oCalledPartyBusy, MonitorMode.interrupted, null, null,
                false);
        bcsmEventList.add(ev);
        ev = this.inapParameterFactory.createBCSMEvent(EventTypeBCSM.oNoAnswer, MonitorMode.interrupted, null, null, false);
        bcsmEventList.add(ev);
        ev = this.inapParameterFactory.createBCSMEvent(EventTypeBCSM.oAnswer, MonitorMode.notifyAndContinue, null, null, false);
        bcsmEventList.add(ev);
        LegID legId = this.inapParameterFactory.createLegID(null,LegType.leg1);
        ev = this.inapParameterFactory.createBCSMEvent(EventTypeBCSM.oDisconnect, MonitorMode.notifyAndContinue, legId, null,
                false);
        bcsmEventList.add(ev);
        legId = this.inapParameterFactory.createLegID(null, LegType.leg2);
        ev = this.inapParameterFactory.createBCSMEvent(EventTypeBCSM.oDisconnect, MonitorMode.interrupted, legId, null, false);
        bcsmEventList.add(ev);
        ev = this.inapParameterFactory.createBCSMEvent(EventTypeBCSM.oAbandon, MonitorMode.notifyAndContinue, null, null, false);
        bcsmEventList.add(ev);

        RequestReportBCSMEventRequestImpl res = new RequestReportBCSMEventRequestImpl(bcsmEventList, null, null);

        return res;
    }

    public void onDialogRequest(INAPDialog inapDialog) {
        super.onDialogRequest(inapDialog);
        serverCscDialog = inapDialog;
    }

    public void sendAccept() {
        try {
            serverCscDialog.send();
        } catch (INAPException e) {
            this.error("Error while trying to send/close() Dialog", e);
        }
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void error(String message, Exception e) {
        logger.error(message, e);
    }
}
