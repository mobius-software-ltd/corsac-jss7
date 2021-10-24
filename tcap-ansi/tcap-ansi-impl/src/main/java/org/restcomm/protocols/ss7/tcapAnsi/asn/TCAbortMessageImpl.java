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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ASNPAbortCauseType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=22,constructed=true,lengthIndefinite=false)
public class TCAbortMessageImpl extends TCUnifiedMessageImpl implements TCAbortMessage {
	private ASNPAbortCauseType pAbortCause;
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=24,constructed=true,index=-1,defaultImplementation = UserInformationImpl.class)
    private UserInformation userAbortInformation;

    @Override
    public PAbortCause getPAbortCause() throws ParseException {
    	if(this.pAbortCause==null)
    		return null;
    	
        return this.pAbortCause.getCause();
    }

    @Override
    public void setPAbortCause(PAbortCause t) {
        this.pAbortCause = new ASNPAbortCauseType();
        this.pAbortCause.setCause(t);
    }

    @Override
    public UserInformation getUserAbortInformation() {
    	return userAbortInformation;
    }

    @Override
    public void setUserAbortInformation(UserInformation uai) {
        userAbortInformation = uai;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TCAbortMessage [");

        if (this.getDestinationTransactionId() != null) {
            sb.append("destinationTransactionId=[");
            sb.append(Arrays.toString(this.getDestinationTransactionId()));
            sb.append("], ");
        }
        if (this.pAbortCause != null) {
            sb.append("PAbortCause=");
            sb.append(this.pAbortCause);
            sb.append(", ");
        }
        if (this.getDialogPortion() != null) {
            sb.append("DialogPortion=");
            sb.append(this.getDialogPortion());
            sb.append(", ");
        }
        if (this.userAbortInformation != null) {
            sb.append("userAbortInformation=");
            sb.append(this.userAbortInformation);
        }
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public byte[] getOriginatingTransactionId() {
        return super.getDestinationTransactionId();
    }

    @Override
    public byte[] getDestinationTransactionId() {
        return super.getOriginatingTransactionId();
    }

    @Override
	public void setOriginatingTransactionId(byte[] txID) {
		super.setDestinationTransactionId(txID);
	}

	@Override
	public void setDestinationTransactionId(byte[] txID) {
		super.setOriginatingTransactionId(txID);
	}
	
    @Override
	public boolean validate() {
		return getOriginatingTransactionId()==null;
	}
}