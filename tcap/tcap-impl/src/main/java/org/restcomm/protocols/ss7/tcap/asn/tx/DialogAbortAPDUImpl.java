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

import org.restcomm.protocols.ss7.tcap.asn.ASNAbortSource;
import org.restcomm.protocols.ss7.tcap.asn.AbortSourceType;
import org.restcomm.protocols.ss7.tcap.asn.DialogAPDUType;
import org.restcomm.protocols.ss7.tcap.asn.DialogAbortAPDU;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;
import org.restcomm.protocols.ss7.tcap.asn.UserInformationImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x04,constructed=true,lengthIndefinite=false)
public class DialogAbortAPDUImpl implements DialogAbortAPDU {
	private ASNAbortSource abortSource;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x1E,constructed=true,defaultImplementation = UserInformationImpl.class, index = -1)
    private UserInformation userInformation;

    /**
     * @return the abortSource
     */
    public AbortSourceType getAbortSource() throws ParseException {    
    	if(abortSource==null)
    		return null;
    	
    	return abortSource.getAbortSourceType();
    }

    /**
     * @param abortSource the abortSource to set
     */
    public void setAbortSource(AbortSourceType abortSource) {
    	if(abortSource==null)
    		this.abortSource=null;
    	else if(this.abortSource==null)
    		this.abortSource=new ASNAbortSource(abortSource);    		
    }

    /**
     * @return the userInformation
     */
    public UserInformation getUserInformation() {
        return userInformation;
    }

    /**
     * @param userInformation the userInformation to set
     */
    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.DialogAPDU#getType()
     */
    public DialogAPDUType getType() {
        return DialogAPDUType.Abort;
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
        return "DialogAbortAPDU[abortSource=" + abortSource + ", userInformation=" + userInformation + "]";
    }
}