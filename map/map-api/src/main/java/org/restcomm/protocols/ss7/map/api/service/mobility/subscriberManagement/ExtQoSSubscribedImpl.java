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
public class ExtQoSSubscribedImpl extends ASNOctetString {
	public ExtQoSSubscribedImpl() {
    }

    public ExtQoSSubscribedImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public ExtQoSSubscribedImpl(int allocationRetentionPriority, ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus,
            ExtQoSSubscribed_DeliveryOrder deliveryOrder, ExtQoSSubscribed_TrafficClass trafficClass, ExtQoSSubscribed_MaximumSduSizeImpl maximumSduSize,
            ExtQoSSubscribed_BitRateImpl maximumBitRateForUplink, ExtQoSSubscribed_BitRateImpl maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER residualBER,
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio, ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority,
            ExtQoSSubscribed_TransferDelayImpl transferDelay, ExtQoSSubscribed_BitRateImpl guaranteedBitRateForUplink,
            ExtQoSSubscribed_BitRateImpl guaranteedBitRateForDownlink) {
        this.setData(allocationRetentionPriority, deliveryOfErroneousSdus, deliveryOrder, trafficClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, residualBER, sduErrorRatio, trafficHandlingPriority, transferDelay, guaranteedBitRateForUplink,
                guaranteedBitRateForDownlink);
    }

    protected void setData(int allocationRetentionPriority, ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus,
            ExtQoSSubscribed_DeliveryOrder deliveryOrder, ExtQoSSubscribed_TrafficClass trafficClass, ExtQoSSubscribed_MaximumSduSizeImpl maximumSduSize,
            ExtQoSSubscribed_BitRateImpl maximumBitRateForUplink, ExtQoSSubscribed_BitRateImpl maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER residualBER,
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio, ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority,
            ExtQoSSubscribed_TransferDelayImpl transferDelay, ExtQoSSubscribed_BitRateImpl guaranteedBitRateForUplink,
            ExtQoSSubscribed_BitRateImpl guaranteedBitRateForDownlink) {
        byte[] data = new byte[9];

        data[0] = (byte) allocationRetentionPriority;
        data[1] = (byte) ((deliveryOfErroneousSdus != null ? deliveryOfErroneousSdus.getCode() : 0)
                | ((deliveryOrder != null ? deliveryOrder.getCode() : 0) << 3) | ((trafficClass != null ? trafficClass.getCode() : 0) << 5));
        data[2] = (byte) (maximumSduSize != null ? maximumSduSize.getSourceData() : 0);
        data[3] = (byte) (maximumBitRateForUplink != null ? maximumBitRateForUplink.getSourceData() : 0);
        data[4] = (byte) (maximumBitRateForDownlink != null ? maximumBitRateForDownlink.getSourceData() : 0);
        data[5] = (byte) ((sduErrorRatio != null ? sduErrorRatio.getCode() : 0) | ((residualBER != null ? residualBER.getCode() : 0) << 4));
        data[6] = (byte) ((trafficHandlingPriority != null ? trafficHandlingPriority.getCode() : 0) | ((transferDelay != null ? transferDelay
                .getSourceData() : 0) << 2));
        data[7] = (byte) (guaranteedBitRateForUplink != null ? guaranteedBitRateForUplink.getSourceData() : 0);
        data[8] = (byte) (guaranteedBitRateForDownlink != null ? guaranteedBitRateForDownlink.getSourceData() : 0);
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

    public int getAllocationRetentionPriority() {
    	byte[] data=getData();
        if (data == null || data.length < 1)
            return 0;

        return data[0] & 0xFF;
    }

    public ExtQoSSubscribed_DeliveryOfErroneousSdus getDeliveryOfErroneousSdus() {
    	byte[] data=getData();
    	if (data == null || data.length < 2)
            return null;

        return ExtQoSSubscribed_DeliveryOfErroneousSdus.getInstance(data[1] & 0x07);
    }

    public ExtQoSSubscribed_DeliveryOrder getDeliveryOrder() {
    	byte[] data=getData();
    	if (data == null || data.length < 2)
            return null;

        return ExtQoSSubscribed_DeliveryOrder.getInstance((data[1] & 0x18) >> 3);
    }

    public ExtQoSSubscribed_TrafficClass getTrafficClass() {
    	byte[] data=getData();
    	if (data == null || data.length < 2)
            return null;

        return ExtQoSSubscribed_TrafficClass.getInstance((data[1] & 0xE0) >> 5);
    }

    public ExtQoSSubscribed_MaximumSduSizeImpl getMaximumSduSize() {
    	byte[] data=getData();
    	if (data == null || data.length < 3)
            return null;

        return new ExtQoSSubscribed_MaximumSduSizeImpl(data[2] & 0xFF, true);
    }

    public ExtQoSSubscribed_BitRateImpl getMaximumBitRateForUplink() {
    	byte[] data=getData();
    	if (data == null || data.length < 4)
            return null;

        return new ExtQoSSubscribed_BitRateImpl(data[3] & 0xFF, true);
    }

    public ExtQoSSubscribed_BitRateImpl getMaximumBitRateForDownlink() {
    	byte[] data=getData();
    	if (data == null || data.length < 5)
            return null;

        return new ExtQoSSubscribed_BitRateImpl(data[4] & 0xFF, true);
    }

    public ExtQoSSubscribed_ResidualBER getResidualBER() {
    	byte[] data=getData();
    	if (data == null || data.length < 6)
            return null;

        return ExtQoSSubscribed_ResidualBER.getInstance((data[5] & 0xF0) >> 4);
    }

    public ExtQoSSubscribed_SduErrorRatio getSduErrorRatio() {
    	byte[] data=getData();
    	if (data == null || data.length < 6)
            return null;

        return ExtQoSSubscribed_SduErrorRatio.getInstance(data[5] & 0x0F);
    }

    public ExtQoSSubscribed_TrafficHandlingPriority getTrafficHandlingPriority() {
    	byte[] data=getData();
    	if (data == null || data.length < 7)
            return null;

        return ExtQoSSubscribed_TrafficHandlingPriority.getInstance(data[6] & 0x03);
    }

    public ExtQoSSubscribed_TransferDelayImpl getTransferDelay() {
    	byte[] data=getData();
    	if (data == null || data.length < 7)
            return null;

        return new ExtQoSSubscribed_TransferDelayImpl((data[6] & 0xFC) >> 2, true);
    }

    public ExtQoSSubscribed_BitRateImpl getGuaranteedBitRateForUplink() {
    	byte[] data=getData();
    	if (data == null || data.length < 8)
            return null;

        return new ExtQoSSubscribed_BitRateImpl(data[7] & 0xFF, true);
    }

    public ExtQoSSubscribed_BitRateImpl getGuaranteedBitRateForDownlink() {
    	byte[] data=getData();
    	if (data == null || data.length < 9)
            return null;

        return new ExtQoSSubscribed_BitRateImpl(data[8] & 0xFF, true);
    }

    public String toString() {
    	byte[] data=getData();
    	if (data != null && data.length >= 1) {
            int allocationRetentionPriority = getAllocationRetentionPriority();
            ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus = getDeliveryOfErroneousSdus();
            ExtQoSSubscribed_DeliveryOrder deliveryOrder = getDeliveryOrder();
            ExtQoSSubscribed_TrafficClass trafficClass = getTrafficClass();
            ExtQoSSubscribed_MaximumSduSizeImpl maximumSduSize = getMaximumSduSize();
            ExtQoSSubscribed_BitRateImpl maximumBitRateForUplink = getMaximumBitRateForUplink();
            ExtQoSSubscribed_BitRateImpl maximumBitRateForDownlink = getMaximumBitRateForDownlink();
            ExtQoSSubscribed_ResidualBER residualBER = getResidualBER();
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio = getSduErrorRatio();
            ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority = getTrafficHandlingPriority();
            ExtQoSSubscribed_TransferDelayImpl transferDelay = getTransferDelay();
            ExtQoSSubscribed_BitRateImpl guaranteedBitRateForUplink = getGuaranteedBitRateForUplink();
            ExtQoSSubscribed_BitRateImpl guaranteedBitRateForDownlink = getGuaranteedBitRateForDownlink();

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
