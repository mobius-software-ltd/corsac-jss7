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
    	else if(this.abortSource==null) {
    		this.abortSource=new ASNAbortSource();
    		this.abortSource.setAbortSourceType(abortSource);
    	}
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