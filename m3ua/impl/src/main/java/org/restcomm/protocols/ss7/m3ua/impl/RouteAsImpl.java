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
package org.restcomm.protocols.ss7.m3ua.impl;

import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.RouteAs;
import org.restcomm.protocols.ss7.m3ua.impl.fsm.FSM;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.TrafficModeTypeImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class RouteAsImpl implements RouteAs {

    private M3UAManagementImpl m3uaManagement;
    private As[] asArray = null;
    private TrafficModeType trafficModeType = new TrafficModeTypeImpl(TrafficModeType.Loadshare);

    // After reading comma separted value from xml file, its stored here. And them M3USManagement will do the necessary setup.
    private String asArraytemp = null;

    public RouteAsImpl() {
        // TODO Auto-generated constructor stub
    }

    public TrafficModeType getTrafficModeType() {
        return trafficModeType;
    }

    public void setTrafficModeType(TrafficModeType trafficModeType) {
        this.trafficModeType = trafficModeType;
    }

    protected void setM3uaManagement(M3UAManagementImpl m3uaManagement) {
        this.m3uaManagement = m3uaManagement;
    }

    protected String getAsArraytemp() {
        return asArraytemp;
    }

    protected void addRoute(int dpc, int opc, int si, AsImpl asImpl, int traffmode) throws Exception {
        if (this.trafficModeType.getMode() != traffmode) {
            throw new Exception(
                    String.format(
                            "Route already setup for dpc=%d opc=%d si=%d with trafficModeType=%d. Cannot assign new trafficModeType=%d",
                            dpc, opc, si, this.trafficModeType.getMode(), traffmode));
        }

        if (asArray != null) {
            // check is this As is already added
            for (int count = 0; count < asArray.length; count++) {
                AsImpl asTemp = (AsImpl) asArray[count];
                if (asTemp != null && asImpl.equals(asTemp)) {
                    throw new Exception(String.format("As=%s already added for dpc=%d opc=%d si=%d", asImpl.getName(), dpc,
                            opc, si));
                }
            }
        } else {
            asArray = new AsImpl[this.m3uaManagement.maxAsForRoute];
        }

        // Add to first empty slot
        for (int count = 0; count < asArray.length; count++) {
            if (asArray[count] == null) {
                asArray[count] = asImpl;
                return;
            }
        }

        throw new Exception(String.format("dpc=%d opc=%d si=%d combination already has maximum possible As", dpc, opc, si));
    }

    protected void removeRoute(int dpc, int opc, int si, AsImpl asImpl) throws Exception {
        for (int count = 0; count < asArray.length; count++) {
            AsImpl asTemp = (AsImpl) asArray[count];
            if (asTemp != null && asImpl.equals(asTemp)) {
                asArray[count] = null;
                return;
            }
        }

        throw new Exception(String.format("No AS=%s configured  for dpc=%d opc=%d si=%d", asImpl.getName(), dpc, opc, si));
    }

    protected AsImpl getAsForRoute(int count) {

        if (this.trafficModeType.getMode() == TrafficModeType.Override) {
            // For Override we always try with first available AS
            count = 0;
        }

        // First attempt
        AsImpl asImpl = (AsImpl) asArray[count];
        if (this.isAsActive(asImpl)) {
            return asImpl;
        }

        // Second recursive Attempt
        for (int i = 0; i < this.m3uaManagement.getMaxAsForRoute(); i++) {
            count = count + 1;
            if (count == this.m3uaManagement.getMaxAsForRoute()) {
                // If count reaches same value as total As available for route,
                // restart from 0
                count = 0;
            }
            asImpl = (AsImpl) asArray[count];
            if (this.isAsActive(asImpl)) {
                return asImpl;
            }

        }
        return null;
    }

    private boolean isAsActive(AsImpl asImpl) {
        FSM fsm = null;
        if (asImpl != null) {
            if (asImpl.getFunctionality() == Functionality.AS
                    || (asImpl.getFunctionality() == Functionality.SGW && asImpl.getExchangeType() == ExchangeType.DE)
                    || (asImpl.getFunctionality() == Functionality.IPSP && asImpl.getExchangeType() == ExchangeType.DE)
                    || (asImpl.getFunctionality() == Functionality.IPSP && asImpl.getExchangeType() == ExchangeType.SE && asImpl
                            .getIpspType() == IPSPType.CLIENT)) {
                fsm = asImpl.getPeerFSM();
            } else {
                fsm = asImpl.getLocalFSM();
            }

            AsState asState = AsState.getState(fsm.getState().getName());

            return (asState == AsState.ACTIVE);
        }// if (as != null)
        return false;
    }

    protected boolean hasAs(AsImpl asImpl) {
        for (int count = 0; count < asArray.length; count++) {
            AsImpl asTemp = (AsImpl) asArray[count];
            if (asTemp != null && asTemp.equals(asImpl)) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasAs() {
        for (int count = 0; count < asArray.length; count++) {
            AsImpl asTemp = (AsImpl) asArray[count];
            if (asTemp != null){
                return true;
            }
        }
        return false;
    }

    public As[] getAsArray() {
        return this.asArray;
    }

    protected void reset() {
        AsImpl[] asList = new AsImpl[this.m3uaManagement.getMaxAsForRoute()];

        if (asArraytemp != null && !asArraytemp.equals("")) {
            String[] asNames = asArraytemp.split(",");
            for (int count = 0; count < asList.length && count < asNames.length; count++) {
                String asName = asNames[count];
                As as = this.m3uaManagement.getAs(asName);
                if (as == null) {
                    // TODO add warning
                    continue;
                }
                asList[count] = (AsImpl) as;
            }
        }// if (value != null && !value.equals(""))

        this.asArray = asList;
    }

}
