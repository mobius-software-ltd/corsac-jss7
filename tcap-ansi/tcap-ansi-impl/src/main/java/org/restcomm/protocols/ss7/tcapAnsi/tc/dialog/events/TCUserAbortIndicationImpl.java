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

package org.restcomm.protocols.ss7.tcapAnsi.tc.dialog.events;

import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ConfidentialityImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationExternalImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.EventType;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;

public class TCUserAbortIndicationImpl extends DialogIndicationImpl implements TCUserAbortIndication {
	private static final long serialVersionUID = 1L;

	private UserInformationImpl userInformation;
    private ApplicationContext acn;
    private SecurityContext securityContext;
    private ConfidentialityImpl confidentiality;
    private UserInformationExternalImpl userAbortInformation;
    private SccpAddress originatingAddress;


    TCUserAbortIndicationImpl() {
        super(EventType.Abort);
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

    public ApplicationContext getApplicationContextName() {
        return this.acn;
    }

    public void setApplicationContextName(ApplicationContext acn) {
        this.acn = acn;
    }

    @Override
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public void setSecurityContext(SecurityContext val) {
        securityContext = val;
    }

    @Override
    public ConfidentialityImpl getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(ConfidentialityImpl val) {
        confidentiality = val;
    }

    @Override
    public UserInformationExternalImpl getUserAbortInformation() {
        return userAbortInformation;
    }

    public void setUserAbortInformation(UserInformationExternalImpl val) {
        userAbortInformation = val;
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
