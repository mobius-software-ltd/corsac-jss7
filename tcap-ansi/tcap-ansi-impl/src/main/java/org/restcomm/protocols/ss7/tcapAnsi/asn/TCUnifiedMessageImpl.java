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

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCUnifiedMessage;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;

/**
 * @author amit bhayani
 *
 */
public class TCUnifiedMessageImpl implements TCUnifiedMessage {
	private TransactionID transactionId=new TransactionID();
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=25,constructed=true,index=-1,defaultImplementation = DialogPortionImpl.class)
	private DialogPortion dp;

    /**
     *
     */
    public TCUnifiedMessageImpl() {
    }

    public boolean isDialogPortionExists() {
        return this.dp!=null;
    }

    public byte[] getOriginatingTransactionId() {
        return transactionId.getFirstElem();
    }

    public byte[] getDestinationTransactionId() {
        return transactionId.getSecondElem();
    }

    @Override
	public void setOriginatingTransactionId(byte[] txID) {
		this.transactionId.setFirstElem(txID);
	}

	@Override
	public void setDestinationTransactionId(byte[] txID) {
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

	@Override
	public boolean validate() {
		return true;
	}
}