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

package org.restcomm.protocols.ss7.tcap.tc.dialog.events;

import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.EventType;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcap.asn.ASNAbortSource;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextNameImpl;
import org.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnosticImpl;
import org.restcomm.protocols.ss7.tcap.asn.UserInformationImpl;

public class TCUserAbortIndicationImpl extends DialogIndicationImpl implements TCUserAbortIndication {
	private UserInformationImpl userInformation;
    private ASNAbortSource abortSource;
    private ApplicationContextNameImpl acn;
    private ResultSourceDiagnosticImpl resultSourceDiagnostic;
    private Boolean aareApdu = false;
    private Boolean abrtApdu = false;

    private SccpAddress originatingAddress;

    TCUserAbortIndicationImpl() {
        super(EventType.UAbort);
        // TODO Auto-generated constructor stub
    }

    // public External getAbortReason() {
    //
    // return abortReason;
    // }

    public Boolean IsAareApdu() {
        return this.aareApdu;
    }

    public void SetAareApdu() {
        this.aareApdu = true;
    }

    public Boolean IsAbrtApdu() {
        return this.abrtApdu;

    }

    public void SetAbrtApdu() {
        this.abrtApdu = true;
    }

    public UserInformationImpl getUserInformation() {

        return userInformation;
    }

    /**
     * @param userInformation the userInformation to set
     */
    public void setUserInformation(UserInformationImpl userInformation) {
        this.userInformation = userInformation;
    }

    /**
     * @return the abortSource
     */
    public ASNAbortSource getAbortSource() {
        return abortSource;
    }

    public void setAbortSource(ASNAbortSource abortSource) {
        this.abortSource = abortSource;

    }

    public ApplicationContextNameImpl getApplicationContextName() {
        return this.acn;
    }

    public void setApplicationContextName(ApplicationContextNameImpl acn) {
        this.acn = acn;
    }

    public ResultSourceDiagnosticImpl getResultSourceDiagnostic() {
        return this.resultSourceDiagnostic;
    }

    public void setResultSourceDiagnostic(ResultSourceDiagnosticImpl resultSourceDiagnostic) {
        this.resultSourceDiagnostic = resultSourceDiagnostic;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest# getOriginatingAddress()
     */
    public SccpAddress getOriginatingAddress() {

        return this.originatingAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest# setOriginatingAddress
     * (org.restcomm.protocols.ss7.sccp.parameter.SccpAddress)
     */
    public void setOriginatingAddress(SccpAddress dest) {
        this.originatingAddress = dest;

    }

}
