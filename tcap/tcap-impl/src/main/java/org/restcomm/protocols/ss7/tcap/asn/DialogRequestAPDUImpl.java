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

package org.restcomm.protocols.ss7.tcap.asn;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author alerant appngin
 *
 */
@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x00,constructed=true,lengthIndefinite=false)
public class DialogRequestAPDUImpl implements DialogRequestAPDU {
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x00,constructed=false,index=-1,defaultImplementation = ProtocolVersionImpl.class)
	private ProtocolVersion protocolVersion;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x01,constructed=true,index=-1,defaultImplementation = ApplicationContextNameImpl.class)
	private ApplicationContextName acn;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x1E,constructed=true,index=-1,defaultImplementation = UserInformationImpl.class)
	private UserInformation ui;
    
	private boolean malformedUserInformation = false;

    public DialogRequestAPDUImpl() {
    }

    public void setDoNotSendProtocolVersion(boolean val) {
    	if(val)
    		protocolVersion=null;
    	else {
    		protocolVersion=new ProtocolVersionImpl();
    	}
    }

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

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.DialogAPDU#getType()
     */
    public DialogAPDUType getType() {
        return DialogAPDUType.Request;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.DialogAPDU#isUniDirectional()
     */
    public boolean isUniDirectional() {

        return false;
    }

    /**
     * Return true if the decoded request contained malformed User Information element
     *
     * @return true if the decoded request contained malformed User Information element
     */
    public boolean isMalformedUserInformation() {
        return malformedUserInformation;
    }

    public String toString() {
        return "DialogRequestAPDU[acn=" + acn + ", ui=" + (malformedUserInformation ? "<MALFORMED>" : ui) + "]";
    }
}