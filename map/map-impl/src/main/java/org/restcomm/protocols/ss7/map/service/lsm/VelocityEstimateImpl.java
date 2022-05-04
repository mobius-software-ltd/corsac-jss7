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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class VelocityEstimateImpl extends ASNOctetString implements VelocityEstimate {
	public VelocityEstimateImpl() {
		super("VelocityEstimate",4,7,false);
    }

	public VelocityEstimateImpl(VelocityType velocityType, int horizontalSpeed, int bearing, int verticalSpeed,
            int uncertaintyHorizontalSpeed, int uncertaintyVerticalSpeed) throws MAPException {
		super(translate(velocityType, horizontalSpeed, bearing, verticalSpeed, uncertaintyHorizontalSpeed, uncertaintyVerticalSpeed),"VelocityEstimate",4,7,false);
	}
	
    public static ByteBuf translate(VelocityType velocityType, int horizontalSpeed, int bearing, int verticalSpeed,
            int uncertaintyHorizontalSpeed, int uncertaintyVerticalSpeed) throws MAPException {
        if (velocityType == null) {
            throw new MAPException("velocityType parameter is null");
        }
        
        ByteBuf value=null;
        switch (velocityType) {
            case HorizontalVelocity:
            	value=initData(4, velocityType, horizontalSpeed, bearing, 0);
                break;

            case HorizontalWithVerticalVelocity:
            	value=initData(5, velocityType, horizontalSpeed, bearing, verticalSpeed);
                if (verticalSpeed < 0)
                    verticalSpeed = -verticalSpeed;
                value.writeByte((byte) (verticalSpeed & 0xFF));
                break;

            case HorizontalVelocityWithUncertainty:
            	value=initData(5, velocityType, horizontalSpeed, bearing, 0);
            	value.writeByte((byte) (uncertaintyHorizontalSpeed & 0xFF));
                break;

            case HorizontalWithVerticalVelocityAndUncertainty:
            	value=initData(7, velocityType, horizontalSpeed, bearing, verticalSpeed);
            	value.writeByte((byte) (verticalSpeed & 0xFF));
            	value.writeByte((byte) (uncertaintyHorizontalSpeed & 0xFF));
            	value.writeByte((byte) (uncertaintyVerticalSpeed & 0xFF));
                break;
        }
        
        return value;
    }

    private static ByteBuf initData(int len, VelocityType velocityType, int horizontalSpeed, int bearing, int verticalSpeed) {
    	ByteBuf buf = Unpooled.buffer(len);

    	buf.writeByte((byte) ((velocityType.getCode() << 4) | (verticalSpeed < 0 ? 0x02 : 0) | (bearing & 0x0100) >> 8));
    	buf.writeByte((byte) (bearing & 0xFF));
    	buf.writeByte((byte) ((horizontalSpeed & 0xFF00) >> 8));
    	buf.writeByte((byte) (horizontalSpeed & 0xFF));
        return buf;
    }

    public VelocityType getVelocityType() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() < 1)
            return null;

        return VelocityType.getInstance((value.readByte() & 0xF0) >> 4);
    }

    public int getHorizontalSpeed() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() < 4)
            return 0;

    	value.skipBytes(2);
        int res = ((value.readByte() & 0xFF) << 8) + (value.readByte() & 0xFF);
        return res;
    }

    public int getBearing() {
    	ByteBuf value=getValue();
    	if (value == null || value.readableBytes() < 4)
            return 0;

        int res = ((value.readByte() & 0x01) << 8) + (value.readByte() & 0xFF);
        return res;
    }

    public int getVerticalSpeed() {
        VelocityType velocityType = this.getVelocityType();
        if (velocityType == null)
            return 0;

        ByteBuf value=getValue();
    	switch (velocityType) {
            case HorizontalWithVerticalVelocity:
            case HorizontalWithVerticalVelocityAndUncertainty:
            	value.skipBytes(4);
                int res = (value.readByte() & 0xFF);
                return res;
			default:
				break;
        }

        return 0;
    }

    public int getUncertaintyHorizontalSpeed() {
        VelocityType velocityType = this.getVelocityType();
        if (velocityType == null)
            return 0;

        ByteBuf value=getValue();
    	switch (velocityType) {
            case HorizontalVelocityWithUncertainty:
            	value.skipBytes(4);
                int res = (value.readByte() & 0xFF);
                return res;
            case HorizontalWithVerticalVelocityAndUncertainty:
            	value.skipBytes(5);
                res = (value.readByte() & 0xFF);
                return res;
			default:
				break;
        }

        return 0;
    }

    public int getUncertaintyVerticalSpeed() {
    	VelocityType velocityType = this.getVelocityType();
        if (velocityType == null)
            return 0;

        ByteBuf value=getValue();
    	switch (velocityType) {
            case HorizontalWithVerticalVelocityAndUncertainty:
            	value.skipBytes(6);
                int res = (value.readByte() & 0xFF);
                return res;
			default:
				break;
        }

        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VelocityEstimate [");

        sb.append("VelocityType=");
        sb.append(this.getVelocityType());

        sb.append(", HorizontalSpeed=");
        sb.append(this.getHorizontalSpeed());

        sb.append(", Bearing=");
        sb.append(this.getBearing());

        VelocityType velocityType = this.getVelocityType();
        if (velocityType == VelocityType.HorizontalWithVerticalVelocity
                || velocityType == VelocityType.HorizontalWithVerticalVelocityAndUncertainty) {
            sb.append(", VerticalSpeed=");
            sb.append(this.getVerticalSpeed());
        }

        if (velocityType == VelocityType.HorizontalVelocityWithUncertainty
                || velocityType == VelocityType.HorizontalWithVerticalVelocityAndUncertainty) {
            sb.append(", UncertaintyHorizontalSpeed=");
            sb.append(this.getUncertaintyHorizontalSpeed());
        }

        if (velocityType == VelocityType.HorizontalWithVerticalVelocityAndUncertainty) {
            sb.append(", UncertaintyVerticalSpeed=");
            sb.append(this.getUncertaintyVerticalSpeed());
        }

        sb.append("]");

        return sb.toString();
    }
}
