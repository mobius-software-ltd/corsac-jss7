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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRate;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_DeliveryOfErroneousSdus;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_DeliveryOrder;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_MaximumSduSize;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_ResidualBER;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_SduErrorRatio;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TrafficClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TrafficHandlingPriority;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TransferDelay;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExtQoSSubscribedImpl extends ASNOctetString implements ExtQoSSubscribed {
	public ExtQoSSubscribedImpl() {
    }

    public ExtQoSSubscribedImpl(int allocationRetentionPriority, ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus,
            ExtQoSSubscribed_DeliveryOrder deliveryOrder, ExtQoSSubscribed_TrafficClass trafficClass, ExtQoSSubscribed_MaximumSduSize maximumSduSize,
            ExtQoSSubscribed_BitRate maximumBitRateForUplink, ExtQoSSubscribed_BitRate maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER residualBER,
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio, ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority,
            ExtQoSSubscribed_TransferDelay transferDelay, ExtQoSSubscribed_BitRate guaranteedBitRateForUplink,
            ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink) {
        super(translate(allocationRetentionPriority, deliveryOfErroneousSdus, deliveryOrder, trafficClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, residualBER, sduErrorRatio, trafficHandlingPriority, transferDelay, guaranteedBitRateForUplink,
                guaranteedBitRateForDownlink));
    }

    protected static ByteBuf translate(int allocationRetentionPriority, ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus,
            ExtQoSSubscribed_DeliveryOrder deliveryOrder, ExtQoSSubscribed_TrafficClass trafficClass, ExtQoSSubscribed_MaximumSduSize maximumSduSize,
            ExtQoSSubscribed_BitRate maximumBitRateForUplink, ExtQoSSubscribed_BitRate maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER residualBER,
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio, ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority,
            ExtQoSSubscribed_TransferDelay transferDelay, ExtQoSSubscribed_BitRate guaranteedBitRateForUplink,
            ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink) {
        ByteBuf buffer=Unpooled.buffer(9);

        buffer.writeByte(allocationRetentionPriority);
        buffer.writeByte(((deliveryOfErroneousSdus != null ? deliveryOfErroneousSdus.getCode() : 0)
                | ((deliveryOrder != null ? deliveryOrder.getCode() : 0) << 3) | ((trafficClass != null ? trafficClass.getCode() : 0) << 5)));
        buffer.writeByte((maximumSduSize != null ? maximumSduSize.getSourceData() : 0));
        buffer.writeByte((maximumBitRateForUplink != null ? maximumBitRateForUplink.getSourceData() : 0));
        buffer.writeByte((maximumBitRateForDownlink != null ? maximumBitRateForDownlink.getSourceData() : 0));
        buffer.writeByte(((sduErrorRatio != null ? sduErrorRatio.getCode() : 0) | ((residualBER != null ? residualBER.getCode() : 0) << 4)));
        buffer.writeByte(((trafficHandlingPriority != null ? trafficHandlingPriority.getCode() : 0) | ((transferDelay != null ? transferDelay
                .getSourceData() : 0) << 2)));
        buffer.writeByte((guaranteedBitRateForUplink != null ? guaranteedBitRateForUplink.getSourceData() : 0));
        buffer.writeByte((guaranteedBitRateForDownlink != null ? guaranteedBitRateForDownlink.getSourceData() : 0));
        return buffer;
    }

    public int getAllocationRetentionPriority() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 1)
            return 0;

        return value.readByte() & 0xFF;
    }

    public ExtQoSSubscribed_DeliveryOfErroneousSdus getDeliveryOfErroneousSdus() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 2)
            return null;

        value.skipBytes(1);
        return ExtQoSSubscribed_DeliveryOfErroneousSdus.getInstance(value.readByte() & 0x07);
    }

    public ExtQoSSubscribed_DeliveryOrder getDeliveryOrder() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 2)
            return null;

        value.skipBytes(1);
        return ExtQoSSubscribed_DeliveryOrder.getInstance((value.readByte() & 0x18) >> 3);
    }

    public ExtQoSSubscribed_TrafficClass getTrafficClass() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 2)
            return null;

        value.skipBytes(1);
        return ExtQoSSubscribed_TrafficClass.getInstance((value.readByte() & 0xE0) >> 5);
    }

    public ExtQoSSubscribed_MaximumSduSize getMaximumSduSize() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 3)
            return null;

        value.skipBytes(2);
        return new ExtQoSSubscribed_MaximumSduSize(value.readByte() & 0xFF, true);
    }

    public ExtQoSSubscribed_BitRate getMaximumBitRateForUplink() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 4)
            return null;

        value.skipBytes(3);
        return new ExtQoSSubscribed_BitRate(value.readByte() & 0xFF, true);
    }

    public ExtQoSSubscribed_BitRate getMaximumBitRateForDownlink() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 5)
            return null;

        value.skipBytes(4);
        return new ExtQoSSubscribed_BitRate(value.readByte() & 0xFF, true);
    }

    public ExtQoSSubscribed_ResidualBER getResidualBER() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 6)
            return null;

        value.skipBytes(5);
        return ExtQoSSubscribed_ResidualBER.getInstance((value.readByte() & 0xF0) >> 4);
    }

    public ExtQoSSubscribed_SduErrorRatio getSduErrorRatio() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 6)
            return null;

        value.skipBytes(5);
        return ExtQoSSubscribed_SduErrorRatio.getInstance(value.readByte() & 0x0F);
    }

    public ExtQoSSubscribed_TrafficHandlingPriority getTrafficHandlingPriority() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 7)
            return null;

        value.skipBytes(6);
        return ExtQoSSubscribed_TrafficHandlingPriority.getInstance(value.readByte() & 0x03);
    }

    public ExtQoSSubscribed_TransferDelay getTransferDelay() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 7)
            return null;

        value.skipBytes(6);
        return new ExtQoSSubscribed_TransferDelay((value.readByte() & 0xFC) >> 2, true);
    }

    public ExtQoSSubscribed_BitRate getGuaranteedBitRateForUplink() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 8)
            return null;

        value.skipBytes(7);
        return new ExtQoSSubscribed_BitRate(value.readByte() & 0xFF, true);
    }

    public ExtQoSSubscribed_BitRate getGuaranteedBitRateForDownlink() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() < 9)
            return null;

        value.skipBytes(8);
        return new ExtQoSSubscribed_BitRate(value.readByte() & 0xFF, true);
    }

    public String toString() {
    	if (getValue() != null) {
            int allocationRetentionPriority = getAllocationRetentionPriority();
            ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus = getDeliveryOfErroneousSdus();
            ExtQoSSubscribed_DeliveryOrder deliveryOrder = getDeliveryOrder();
            ExtQoSSubscribed_TrafficClass trafficClass = getTrafficClass();
            ExtQoSSubscribed_MaximumSduSize maximumSduSize = getMaximumSduSize();
            ExtQoSSubscribed_BitRate maximumBitRateForUplink = getMaximumBitRateForUplink();
            ExtQoSSubscribed_BitRate maximumBitRateForDownlink = getMaximumBitRateForDownlink();
            ExtQoSSubscribed_ResidualBER residualBER = getResidualBER();
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio = getSduErrorRatio();
            ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority = getTrafficHandlingPriority();
            ExtQoSSubscribed_TransferDelay transferDelay = getTransferDelay();
            ExtQoSSubscribed_BitRate guaranteedBitRateForUplink = getGuaranteedBitRateForUplink();
            ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink = getGuaranteedBitRateForDownlink();

            StringBuilder sb = new StringBuilder();
            sb.append("ExtQoSSubscribed [");

            sb.append("allocationRetentionPriority=");
            sb.append(allocationRetentionPriority);
            sb.append(", ");
            if (deliveryOfErroneousSdus != null) {
                sb.append("deliveryOfErroneousSdus=");
                sb.append(deliveryOfErroneousSdus);
                sb.append(", ");
            }
            if (deliveryOrder != null) {
                sb.append("deliveryOrder=");
                sb.append(deliveryOrder);
                sb.append(", ");
            }
            if (trafficClass != null) {
                sb.append("trafficClass=");
                sb.append(trafficClass);
                sb.append(", ");
            }
            if (maximumSduSize != null) {
                sb.append("maximumSduSize=");
                sb.append(maximumSduSize);
                sb.append(", ");
            }
            if (maximumBitRateForUplink != null) {
                sb.append("maximumBitRateForUplink=");
                sb.append(maximumBitRateForUplink);
                sb.append(", ");
            }
            if (maximumBitRateForDownlink != null) {
                sb.append("maximumBitRateForDownlink=");
                sb.append(maximumBitRateForDownlink);
                sb.append(", ");
            }
            if (residualBER != null) {
                sb.append("residualBER=");
                sb.append(residualBER);
                sb.append(", ");
            }
            if (sduErrorRatio != null) {
                sb.append("sduErrorRatio=");
                sb.append(sduErrorRatio);
                sb.append(", ");
            }
            if (trafficHandlingPriority != null) {
                sb.append("trafficHandlingPriority=");
                sb.append(trafficHandlingPriority);
                sb.append(", ");
            }
            if (transferDelay != null) {
                sb.append("transferDelay=");
                sb.append(transferDelay);
                sb.append(", ");
            }
            if (guaranteedBitRateForUplink != null) {
                sb.append("guaranteedBitRateForUplink=");
                sb.append(guaranteedBitRateForUplink);
                sb.append(", ");
            }
            if (guaranteedBitRateForDownlink != null) {
                sb.append("guaranteedBitRateForDownlink=");
                sb.append(guaranteedBitRateForDownlink);
                sb.append(", ");
            }
            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }
}