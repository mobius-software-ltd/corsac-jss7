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

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_DelayClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_MeanThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PeakThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PrecedenceClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_ReliabilityClass;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class QoSSubscribedImpl extends ASNOctetString2 implements QoSSubscribed {
	public QoSSubscribedImpl() {
    }

    public QoSSubscribedImpl(QoSSubscribed_ReliabilityClass reliabilityClass, QoSSubscribed_DelayClass delayClass,
            QoSSubscribed_PrecedenceClass precedenceClass, QoSSubscribed_PeakThroughput peakThroughput, QoSSubscribed_MeanThroughput meanThroughput) {
    	super(translate(reliabilityClass, delayClass, precedenceClass, peakThroughput, meanThroughput));
    }

    public static ByteBuf translate(QoSSubscribed_ReliabilityClass reliabilityClass, QoSSubscribed_DelayClass delayClass,
            QoSSubscribed_PrecedenceClass precedenceClass, QoSSubscribed_PeakThroughput peakThroughput, QoSSubscribed_MeanThroughput meanThroughput) {
        ByteBuf buffer=Unpooled.buffer(3);

        buffer.writeByte((((delayClass != null ? delayClass.getCode() : 0) << 3) + (reliabilityClass != null ? reliabilityClass.getCode() : 0)));
        buffer.writeByte((((peakThroughput != null ? peakThroughput.getCode() : 0) << 4) + (precedenceClass != null ? precedenceClass.getCode() : 0)));
        buffer.writeByte((meanThroughput != null ? meanThroughput.getCode() : 0));
        return buffer;
    }

    public QoSSubscribed_ReliabilityClass getReliabilityClass() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() != 3)
            return null;

        return QoSSubscribed_ReliabilityClass.getInstance(value.readByte() & 0x07);
    }

    public QoSSubscribed_DelayClass getDelayClass() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() != 3)
            return null;

        return QoSSubscribed_DelayClass.getInstance((value.readByte() & 0x38) >> 3);
    }

    public QoSSubscribed_PrecedenceClass getPrecedenceClass() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() != 3)
            return null;

    	value.skipBytes(1);
        return QoSSubscribed_PrecedenceClass.getInstance(value.readByte() & 0x07);
    }

    public QoSSubscribed_PeakThroughput getPeakThroughput() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() != 3)
            return null;

    	value.skipBytes(1);
        return QoSSubscribed_PeakThroughput.getInstance((value.readByte() & 0xF0) >> 4);
    }

    public QoSSubscribed_MeanThroughput getMeanThroughput() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() != 3)
            return null;

    	value.skipBytes(2);
        return QoSSubscribed_MeanThroughput.getInstance(value.readByte() & 0x1F);
    }

    @Override
    public String toString() {
        if (getValue()!=null) {
            QoSSubscribed_ReliabilityClass reliabilityClass = getReliabilityClass();
            QoSSubscribed_DelayClass delayClass = getDelayClass();
            QoSSubscribed_PrecedenceClass precedenceClass = getPrecedenceClass();
            QoSSubscribed_PeakThroughput peakThroughput = getPeakThroughput();
            QoSSubscribed_MeanThroughput meanThroughput = getMeanThroughput();

            StringBuilder sb = new StringBuilder();
            sb.append("QoSSubscribed [");
            if (reliabilityClass != null) {
                sb.append("reliabilityClass=");
                sb.append(reliabilityClass);
                sb.append(", ");
            }
            if (delayClass != null) {
                sb.append("delayClass=");
                sb.append(delayClass);
                sb.append(", ");
            }
            if (precedenceClass != null) {
                sb.append("precedenceClass=");
                sb.append(precedenceClass);
                sb.append(", ");
            }
            if (peakThroughput != null) {
                sb.append("peakThroughput=");
                sb.append(peakThroughput);
                sb.append(", ");
            }
            if (meanThroughput != null) {
                sb.append("meanThroughput=");
                sb.append(meanThroughput);
                sb.append(", ");
            }
            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }

}
