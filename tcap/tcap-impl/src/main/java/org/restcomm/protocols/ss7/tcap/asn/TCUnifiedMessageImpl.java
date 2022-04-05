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

import org.restcomm.protocols.ss7.tcap.asn.comp.DestinationTransactionID;
import org.restcomm.protocols.ss7.tcap.asn.comp.OriginatingTransactionID;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUnifiedMessage;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogPortionImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;

import io.netty.buffer.ByteBuf;

/**
 * @author amit bhayani
 *
 */
public abstract class TCUnifiedMessageImpl implements TCUnifiedMessage {
	// mandatory
    @ASNProperty(asnClass=ASNClass.APPLICATION,tag=0x08,constructed=false,index=-1)
    private OriginatingTransactionID originatingTransactionId;
    
    @ASNProperty(asnClass=ASNClass.APPLICATION,tag=0x09,constructed=false,index=-1)
    private DestinationTransactionID destinationTransactionId;

    @ASNProperty(asnClass=ASNClass.APPLICATION,tag=0x0B,constructed=true,index=-1,defaultImplementation = DialogPortionImpl.class)
    private DialogPortion dialogPortion;
    /**
     *
     */
    public TCUnifiedMessageImpl() {
    }

    public ByteBuf getOriginatingTransactionId() {
    	if(originatingTransactionId==null)
    		return null;
    	
        return originatingTransactionId.getValue();
    }

    public ByteBuf getDestinationTransactionId() {
    	if(destinationTransactionId==null)
    		return null;
    	
        return destinationTransactionId.getValue();
    }

	@Override
	public void setOriginatingTransactionId(ByteBuf t) {
		if(t==null)
			return;
		
		this.originatingTransactionId=new OriginatingTransactionID(t);		
	}

	@Override
	public void setDestinationTransactionId(ByteBuf t) {
		if(t==null)
			return;
		
		this.destinationTransactionId=new DestinationTransactionID(t);		
	}

	@Override
	public DialogPortion getDialogPortion() {
		return dialogPortion;
	}

	@Override
	public void setDialogPortion(DialogPortion dp) {
		this.dialogPortion=dp;
	}
}