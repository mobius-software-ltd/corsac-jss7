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

import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCUnifiedMessage;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;

import io.netty.buffer.ByteBuf;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public abstract class TCUnifiedMessageImpl implements TCUnifiedMessage {
	private TransactionID transactionId=new TransactionID();
	
	public static List<String> getAllNames() {
    	return Arrays.asList(new String[] { TCAbortMessageImpl.NAME, TCConversationMessageImpl.NAME, TCQueryMessageImpl.NAME, TCResponseMessageImpl.NAME, TCUniMessageImpl.NAME, TCUnknownMessageImpl.NAME });
    }
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=25,constructed=true,index=-1,defaultImplementation = DialogPortionImpl.class)
	private DialogPortion dp;

    /**
     *
     */
    public TCUnifiedMessageImpl() {
    }

    public boolean isTransactionExists() {
        return this.transactionId!=null;
    }

    public ByteBuf getOriginatingTransactionId() {
        return transactionId.getFirstElem();
    }

    public ByteBuf getDestinationTransactionId() {
        return transactionId.getSecondElem();
    }

    @Override
	public void setOriginatingTransactionId(ByteBuf txID) {
		this.transactionId.setFirstElem(txID);
	}

	@Override
	public void setDestinationTransactionId(ByteBuf txID) {
		this.transactionId.setSecondElem(txID);
	}

	/*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage#getDialogPortion ()
     */
    public DialogPortion getDialogPortion() {
        return this.dp;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage#setDialogPortion
     * (org.restcomm.protocols.ss7.tcap.asn.DialogPortion)
     */
    public void setDialogPortion(DialogPortion dp) {
        this.dp = dp;
    }
    
    public boolean validateTransaction() {
    	return true;
    }
}