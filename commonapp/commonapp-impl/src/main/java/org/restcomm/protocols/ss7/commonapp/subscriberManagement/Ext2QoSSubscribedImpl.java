/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;


import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed_SourceStatisticsDescriptor;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRateExtended;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class Ext2QoSSubscribedImpl extends ASNOctetString implements Ext2QoSSubscribed {
	public Ext2QoSSubscribedImpl() {
		super("Ext2QoSSubscribed",1,3,false);
    }

    public Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor, boolean optimisedForSignallingTraffic,
            ExtQoSSubscribed_BitRateExtended maximumBitRateForDownlinkExtended, ExtQoSSubscribed_BitRateExtended guaranteedBitRateForDownlinkExtended) {
        super(translate(sourceStatisticsDescriptor, optimisedForSignallingTraffic, maximumBitRateForDownlinkExtended, guaranteedBitRateForDownlinkExtended),"Ext2QoSSubscribed",1,3,false);
    }

    protected static ByteBuf translate(Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor, boolean optimisedForSignallingTraffic,
            ExtQoSSubscribed_BitRateExtended maximumBitRateForDownlinkExtended, ExtQoSSubscribed_BitRateExtended guaranteedBitRateForDownlinkExtended) {
        ByteBuf buffer=Unpooled.buffer(3);
        buffer.writeByte(((sourceStatisticsDescriptor != null ? sourceStatisticsDescriptor.getCode() : 0) | ((optimisedForSignallingTraffic ? 1 : 0) << 4)));
        buffer.writeByte((maximumBitRateForDownlinkExtended != null ? maximumBitRateForDownlinkExtended.getSourceData() : 0));
        buffer.writeByte((guaranteedBitRateForDownlinkExtended != null ? guaranteedBitRateForDownlinkExtended.getSourceData() : 0));
        return buffer;
    }

    public Ext2QoSSubscribed_SourceStatisticsDescriptor getSourceStatisticsDescriptor() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() < 1)
            return null;

        return Ext2QoSSubscribed_SourceStatisticsDescriptor.getInstance(value.readByte() & 0x07);
    }

    public boolean isOptimisedForSignallingTraffic() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() < 1)
                return false;

        if ((value.readByte() & 0x10) != 0)
            return true;
        else
            return false;
    }

    public ExtQoSSubscribed_BitRateExtended getMaximumBitRateForDownlinkExtended() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() < 2)
    		return null;

    	value.skipBytes(1);
        return new ExtQoSSubscribed_BitRateExtended(value.readByte() & 0xFF, true);
    }

    public ExtQoSSubscribed_BitRateExtended getGuaranteedBitRateForDownlinkExtended() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() < 3)
    		return null;

    	value.skipBytes(2);
        return new ExtQoSSubscribed_BitRateExtended(value.readByte() & 0xFF, true);
    }

    @Override
    public String toString() {
    	if (getValue() != null) {
            Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor = getSourceStatisticsDescriptor();
            boolean optimisedForSignallingTraffic = isOptimisedForSignallingTraffic();
            ExtQoSSubscribed_BitRateExtended maximumBitRateForDownlinkExtended = getMaximumBitRateForDownlinkExtended();
            ExtQoSSubscribed_BitRateExtended guaranteedBitRateForDownlinkExtended = getGuaranteedBitRateForDownlinkExtended();

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
