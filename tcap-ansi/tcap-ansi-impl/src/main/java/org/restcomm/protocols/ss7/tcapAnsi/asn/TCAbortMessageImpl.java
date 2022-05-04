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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ASNPAbortCauseType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
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
        this.pAbortCause = new ASNPAbortCauseType(t);        
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
            sb.append(ASNOctetString.printDataArr(this.getDestinationTransactionId()));
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
    public ByteBuf getOriginatingTransactionId() {
        return super.getDestinationTransactionId();
    }

    @Override
    public ByteBuf getDestinationTransactionId() {
        return super.getOriginatingTransactionId();
    }

    @Override
	public void setOriginatingTransactionId(ByteBuf txID) {
		super.setDestinationTransactionId(txID);
	}

	@Override
	public void setDestinationTransactionId(ByteBuf txID) {
		super.setOriginatingTransactionId(txID);
	}
	
    public boolean validateTransaction() { 
		if(getOriginatingTransactionId()!=null)
			return false;
		
		return true;
	}

    public boolean isDialogPortionExists() {
        return this.getDialogPortion()!=null || pAbortCause!=null;
    }
}