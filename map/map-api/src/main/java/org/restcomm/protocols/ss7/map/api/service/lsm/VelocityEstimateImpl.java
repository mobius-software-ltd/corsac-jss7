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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.map.api.MAPException;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VelocityEstimateImpl extends ASNOctetString {
	public VelocityEstimateImpl() {
    }

    public VelocityEstimateImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public VelocityEstimateImpl(VelocityType velocityType, int horizontalSpeed, int bearing, int verticalSpeed,
            int uncertaintyHorizontalSpeed, int uncertaintyVerticalSpeed) throws MAPException {
        if (velocityType == null) {
            throw new MAPException("velocityType parameter is null");
        }
        
        byte[] data=null;
        switch (velocityType) {
            case HorizontalVelocity:
                data=initData(4, velocityType, horizontalSpeed, bearing, 0);
                break;

            case HorizontalWithVerticalVelocity:
            	data=initData(5, velocityType, horizontalSpeed, bearing, verticalSpeed);
                if (verticalSpeed < 0)
                    verticalSpeed = -verticalSpeed;
                data[4] = (byte) (verticalSpeed & 0xFF);
                break;

            case HorizontalVelocityWithUncertainty:
            	data=initData(5, velocityType, horizontalSpeed, bearing, 0);
                data[4] = (byte) (uncertaintyHorizontalSpeed & 0xFF);
                break;

            case HorizontalWithVerticalVelocityAndUncertainty:
            	data=initData(7, velocityType, horizontalSpeed, bearing, verticalSpeed);
                data[4] = (byte) (verticalSpeed & 0xFF);
                data[5] = (byte) (uncertaintyHorizontalSpeed & 0xFF);
                data[6] = (byte) (uncertaintyVerticalSpeed & 0xFF);
                break;
        }
        
        if(data!=null)
        	setValue(Unpooled.wrappedBuffer(data));
    }

    private byte[] initData(int len, VelocityType velocityType, int horizontalSpeed, int bearing, int verticalSpeed) {
        byte[] data = new byte[len];

        data[0] = (byte) ((velocityType.getCode() << 4) | (verticalSpeed < 0 ? 0x02 : 0) | (bearing & 0x0100) >> 8);
        data[1] = (byte) (bearing & 0xFF);
        data[2] = (byte) ((horizontalSpeed & 0xFF00) >> 8);
        data[3] = (byte) (horizontalSpeed & 0xFF);
        return data;
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public VelocityType getVelocityType() {
    	byte[] data=getData();
    	if (data == null || data.length < 1)
            return null;

        return VelocityType.getInstance((data[0] & 0xF0) >> 4);
    }

    public int getHorizontalSpeed() {
    	byte[] data=getData();
    	if (data == null || data.length < 4)
            return 0;

        int res = ((data[2] & 0xFF) << 8) + (data[3] & 0xFF);
        return res;
    }

    public int getBearing() {
    	byte[] data=getData();
    	if (data == null || data.length < 4)
            return 0;

        int res = ((data[0] & 0x01) << 8) + (data[1] & 0xFF);
        return res;
    }

    public int getVerticalSpeed() {
        VelocityType velocityType = this.getVelocityType();
        if (velocityType == null)
            return 0;

        byte[] data=getData();
        switch (velocityType) {
            case HorizontalWithVerticalVelocity:
            case HorizontalWithVerticalVelocityAndUncertainty:
                int res = (data[4] & 0xFF);
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

        byte[] data=getData();
        switch (velocityType) {
            case HorizontalVelocityWithUncertainty:
                int res = (data[4] & 0xFF);
                return res;
            case HorizontalWithVerticalVelocityAndUncertainty:
                res = (data[5] & 0xFF);
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

        byte[] data=getData();
        switch (velocityType) {
            case HorizontalWithVerticalVelocityAndUncertainty:
                int res = (data[6] & 0xFF);
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
