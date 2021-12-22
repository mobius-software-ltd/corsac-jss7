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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRateExtended;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class Ext3QoSSubscribedImpl extends ASNOctetString implements Ext3QoSSubscribed {
	public Ext3QoSSubscribedImpl() {
    }

    public Ext3QoSSubscribedImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public Ext3QoSSubscribedImpl(ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended,
            ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended) {
        this.setData(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);
    }

    protected void setData(ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended, ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended) {
        byte[] data = new byte[2];
        data[0] = (byte) (maximumBitRateForUplinkExtended != null ? maximumBitRateForUplinkExtended.getSourceData() : 0);
        data[1] = (byte) (guaranteedBitRateForUplinkExtended != null ? guaranteedBitRateForUplinkExtended.getSourceData() : 0);
        setValue(Unpooled.wrappedBuffer(data));
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public ExtQoSSubscribed_BitRateExtended getMaximumBitRateForUplinkExtended() {
    	byte[] data=getData();
    	if (data == null || data.length < 1)
            return null;

        return new ExtQoSSubscribed_BitRateExtended(data[0] & 0xFF, true);
    }

    public ExtQoSSubscribed_BitRateExtended getGuaranteedBitRateForUplinkExtended() {
    	byte[] data=getData();
    	if (data == null || data.length < 2)
            return null;

        return new ExtQoSSubscribed_BitRateExtended(data[1] & 0xFF, true);
    }

    @Override
    public String toString() {
    	byte[] data=getData();
        if (data != null && data.length >= 1) {
            ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended = getMaximumBitRateForUplinkExtended();
            ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended = getGuaranteedBitRateForUplinkExtended();

            StringBuilder sb = new StringBuilder();
            sb.append("Ext3QoSSubscribed [");

            if (maximumBitRateForUplinkExtended != null) {
                sb.append("maximumBitRateForUplinkExtended=");
                sb.append(maximumBitRateForUplinkExtended);
                sb.append(", ");
            }
            if (guaranteedBitRateForUplinkExtended != null) {
                sb.append("guaranteedBitRateForUplinkExtended=");
                sb.append(guaranteedBitRateForUplinkExtended);
                sb.append(", ");
            }
            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }
}
