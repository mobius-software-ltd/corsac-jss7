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

import org.restcomm.protocols.ss7.tcap.asn.comp.ASNPAbortCause;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x07,constructed=true,lengthIndefinite=false)
public class TCAbortMessageImpl extends TCUnifiedMessageImpl implements TCAbortMessage {
	
	private ASNPAbortCause type;
    
	/*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage#getPAbortCause()
     */
    public PAbortCauseType getPAbortCause() throws ParseException {
    	if(type==null)
    		return null;
    	
        return type.getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage#setPAbortCause
     * (org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType)
     */
    public void setPAbortCause(PAbortCauseType t) {
        this.type = new ASNPAbortCause(t);        
    }

	@Override
	public boolean validate() {
		return getOriginatingTransactionId()==null;
	}
}