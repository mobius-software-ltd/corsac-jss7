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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class Ext2QoSSubscribedImpl extends ASNOctetString {
	public Ext2QoSSubscribedImpl() {
    }

    public Ext2QoSSubscribedImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor, boolean optimisedForSignallingTraffic,
            ExtQoSSubscribed_BitRateExtendedImpl maximumBitRateForDownlinkExtended, ExtQoSSubscribed_BitRateExtendedImpl guaranteedBitRateForDownlinkExtended) {
        this.setData(sourceStatisticsDescriptor, optimisedForSignallingTraffic, maximumBitRateForDownlinkExtended, guaranteedBitRateForDownlinkExtended);
    }

    protected void setData(Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor, boolean optimisedForSignallingTraffic,
            ExtQoSSubscribed_BitRateExtendedImpl maximumBitRateForDownlinkExtended, ExtQoSSubscribed_BitRateExtendedImpl guaranteedBitRateForDownlinkExtended) {
        byte[] data = new byte[3];
        data[0] = (byte) ((sourceStatisticsDescriptor != null ? sourceStatisticsDescriptor.getCode() : 0) | ((optimisedForSignallingTraffic ? 1 : 0) << 4));
        data[1] = (byte) (maximumBitRateForDownlinkExtended != null ? maximumBitRateForDownlinkExtended.getSourceData() : 0);
        data[2] = (byte) (guaranteedBitRateForDownlinkExtended != null ? guaranteedBitRateForDownlinkExtended.getSourceData() : 0);
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

    public Ext2QoSSubscribed_SourceStatisticsDescriptor getSourceStatisticsDescriptor() {
    	byte[] data=getData();
    	if (data == null || data.length < 1)
            return null;

        return Ext2QoSSubscribed_SourceStatisticsDescriptor.getInstance(data[0] & 0x07);
    }

    public boolean isOptimisedForSignallingTraffic() {
    	byte[] data=getData();
    	if (data == null || data.length < 1)
            return false;

        if ((data[0] & 0x10) != 0)
            return true;
        else
            return false;
    }

    public ExtQoSSubscribed_BitRateExtendedImpl getMaximumBitRateForDownlinkExtended() {
    	byte[] data=getData();
    	if (data == null || data.length < 2)
            return null;

        return new ExtQoSSubscribed_BitRateExtendedImpl(data[1] & 0xFF, true);
    }

    public ExtQoSSubscribed_BitRateExtendedImpl getGuaranteedBitRateForDownlinkExtended() {
    	byte[] data=getData();
    	if (data == null || data.length < 3)
            return null;

        return new ExtQoSSubscribed_BitRateExtendedImpl(data[2] & 0xFF, true);
    }

    @Override
    public String toString() {
    	byte[] data=getData();
        if (data != null && data.length >= 1) {
            Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor = getSourceStatisticsDescriptor();
            boolean optimisedForSignallingTraffic = isOptimisedForSignallingTraffic();
            ExtQoSSubscribed_BitRateExtendedImpl maximumBitRateForDownlinkExtended = getMaximumBitRateForDownlinkExtended();
            ExtQoSSubscribed_BitRateExtendedImpl guaranteedBitRateForDownlinkExtended = getGuaranteedBitRateForDownlinkExtended();

            StringBuilder sb = new StringBuilder();
            sb.append("Ext2QoSSubscribed [");

            if (sourceStatisticsDescriptor != null) {
                sb.append("sourceStatisticsDescriptor=");
                sb.append(sourceStatisticsDescriptor);
                sb.append(", ");
            }
            sb.append("optimisedForSignallingTraffic=");
            sb.append(optimisedForSignallingTraffic);
            sb.append(", ");
            if (maximumBitRateForDownlinkExtended != null) {
                sb.append("maximumBitRateForDownlinkExtended=");
                sb.append(maximumBitRateForDownlinkExtended);
                sb.append(", ");
            }
            if (guaranteedBitRateForDownlinkExtended != null) {
                sb.append("guaranteedBitRateForDownlinkExtended=");
                sb.append(guaranteedBitRateForDownlinkExtended);
                sb.append(", ");
            }
            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }
}
