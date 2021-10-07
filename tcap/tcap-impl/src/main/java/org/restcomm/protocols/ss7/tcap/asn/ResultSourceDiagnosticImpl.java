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
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x03,constructed=true,lengthIndefinite=false)
public class ResultSourceDiagnosticImpl implements ResultSourceDiagnostic {
	private ASNDialogServiceProviderType providerType;
    private ASNDialogServiceUserType userType;

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnostic# getDialogServiceProviderType()
     */
    public DialogServiceProviderType getDialogServiceProviderType() throws ParseException {
    	if(providerType==null)
    		return null;
    	
        return providerType.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnostic# getDialogServiceUserType()
     */
    public DialogServiceUserType getDialogServiceUserType() throws ParseException {
    	if(userType==null)
    		return null;
    	
        return userType.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnostic# setDialogServiceProviderType
     * (org.restcomm.protocols.ss7.tcap.asn.DialogServiceProviderType)
     */
    public void setDialogServiceProviderType(DialogServiceProviderType t) {
        this.providerType = new ASNDialogServiceProviderType();
        this.providerType.setType(t);
        this.userType = null;

    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnostic# setDialogServiceUserType
     * (org.restcomm.protocols.ss7.tcap.asn.DialogServiceUserType)
     */
    public void setDialogServiceUserType(DialogServiceUserType t) {
        this.userType = new ASNDialogServiceUserType();
        this.userType.setType(t);
        this.providerType = null;

    }

    public String toString() {
    	DialogServiceProviderType pType=null;
    	try {
    		pType=getDialogServiceProviderType();
    	}
    	catch(ParseException ex) {
    		
    	}
    	
    	DialogServiceUserType uType=null;
    	try {
    		uType=getDialogServiceUserType();
    	}
    	catch(ParseException ex) {
    		
    	}
    	
        return "ResultSourceDiagnostic[providerType=" + pType + ", userType=" + uType + "]";
    }
}