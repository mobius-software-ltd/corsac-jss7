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

package org.restcomm.protocols.ss7.tcap.asn.tx;

import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.DialogAPDUType;
import org.restcomm.protocols.ss7.tcap.asn.DialogResponseAPDU;
import org.restcomm.protocols.ss7.tcap.asn.ProtocolVersion;
import org.restcomm.protocols.ss7.tcap.asn.ProtocolVersionImpl;
import org.restcomm.protocols.ss7.tcap.asn.Result;
import org.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;
import org.restcomm.protocols.ss7.tcap.asn.UserInformationImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNPostprocess;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x01,constructed=true,lengthIndefinite=false)
@ASNPostprocess
public class DialogResponseAPDUImpl implements DialogResponseAPDU {
	// mandatory
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=false,index=-1,defaultImplementation = ProtocolVersionImpl.class)
	private ProtocolVersion protocolVersion = null;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x01,constructed=true,index=-1,defaultImplementation = ApplicationContextNameImpl.class)
	private ApplicationContextName acn;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x02,constructed=true,index=-1,defaultImplementation = ResultImpl.class)
	private Result result;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x03,constructed=true,index=-1,defaultImplementation = ResultSourceDiagnosticImpl.class)
	private ResultSourceDiagnostic diagnostic;
    
    public DialogResponseAPDUImpl() {
    }

    public void setDoNotSendProtocolVersion(boolean val) {
    	if(val)
    		protocolVersion=null;
    	else
    		protocolVersion=new ProtocolVersionImpl(true);
    }

    // optional
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x1E,constructed=true,defaultImplementation = UserInformationImpl.class, index = -1)
    private UserInformation ui;

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDU# getApplicationContextName()
     */
    public ApplicationContextName getApplicationContextName() {
        return acn;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDU#getProtocolVersion ()
     */
    public ProtocolVersion getProtocolVersion() {

        return protocolVersion;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDU#getUserInformation ()
     */
    public UserInformation getUserInformation() {
        return this.ui;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDU# setApplicationContextName
     * (org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName)
     */
    public void setApplicationContextName(ApplicationContextName acn) {
        this.acn = acn;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDU#setUserInformation
     * (org.restcomm.protocols.ss7.tcap.asn.UserInformation[])
     */
    public void setUserInformation(UserInformation ui) {
        this.ui = ui;

    }

    public Result getResult() {

        return this.result;
    }

    public ResultSourceDiagnostic getResultSourceDiagnostic() {
        return this.diagnostic;
    }

    public void setResult(Result acn) {
        this.result = acn;

    }

    public void setResultSourceDiagnostic(ResultSourceDiagnostic acn) {
        this.diagnostic = acn;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.DialogAPDU#getType()
     */
    public DialogAPDUType getType() {

        return DialogAPDUType.Response;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.DialogAPDU#isUniDirectional()
     */
    public boolean isUniDirectional() {

        return false;
    }

    public String toString() {
        return "DialogResponseAPDU[acn=" + acn + ", result=" + result + ", diagnostic=" + diagnostic + ", ui=" + ui + "]";
    }
}
