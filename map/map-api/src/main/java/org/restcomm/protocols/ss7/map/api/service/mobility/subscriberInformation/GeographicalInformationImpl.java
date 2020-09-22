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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.map.api.MAPException;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class GeographicalInformationImpl extends ASNOctetString {
	private static double koef23 = Math.pow(2.0, 23) / 90;
    private static double koef24 = Math.pow(2.0, 24) / 360;
    private static double[] uncertaintyTable = initUncertaintyTable();

    private static double[] initUncertaintyTable() {
        double[] res = new double[128];

        double c = 10;
        double x = 0.1;
        for (int i = 1; i < 128; i++) {
            res[i] = c * (Math.pow(1 + x, i) - 1);
        }

        return res;
    }

    public GeographicalInformationImpl() {       
    }

    public GeographicalInformationImpl(byte[] data) {
    	 setValue(Unpooled.wrappedBuffer(data));
    }

    public GeographicalInformationImpl(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty)
            throws MAPException {
        setData(typeOfShape, latitude, longitude, uncertainty);
    }

    public void setData(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty) throws MAPException {
        if (typeOfShape != TypeOfShape.EllipsoidPointWithUncertaintyCircle) {
            throw new MAPException(
                    "typeOfShape parameter for GeographicalInformation can be only \"ellipsoid point with uncertainty circle\"");
        }

        byte[] data = new byte[8];

        data[0] = (byte) (typeOfShape.getCode() << 4);

        encodeLatitude(data, 1, latitude);
        encodeLongitude(data, 4, longitude);
        data[7] = (byte) encodeUncertainty(uncertainty);
        setValue(Unpooled.wrappedBuffer(data));
    }

    public static double decodeLatitude(byte[] data, int begin) {
        int i1 = ((data[begin] & 0xFF) << 16) + ((data[begin + 1] & 0xFF) << 8) + (data[begin + 2] & 0xFF);

        int sign = 1;
        if ((i1 & 0x800000) != 0) {
            sign = -1;
            i1 = i1 & 0x7FFFFF;
        }

        return i1 / koef23 * sign;
    }

    public static double decodeLongitude(byte[] data, int begin) {
        int i1 = ((data[begin] & 0xFF) << 16) | ((data[begin + 1] & 0xFF) << 8) | (data[begin + 2] & 0xFF);

        if ((i1 & 0x800000) != 0) {
            i1 = i1 | ((int) 0xFF000000);
        }

        return i1 / koef24;
    }

    public static double decodeUncertainty(int data) {
        if (data < 0 || data > 127)
            data = 0;
        double d = uncertaintyTable[data];
        return d;
    }

    public static void encodeLatitude(byte[] data, int begin, double val) {
        boolean negativeSign = false;
        if (val < 0) {
            negativeSign = true;
            val = -val;
        }

        int res = (int) (koef23 * val);

        if (res > 0x7FFFFF)
            res = 0x7FFFFF;
        if (negativeSign)
            res |= 0x800000;

        data[begin] = (byte) ((res & 0xFF0000) >> 16);
        data[begin + 1] = (byte) ((res & 0xFF00) >> 8);
        data[begin + 2] = (byte) (res & 0xFF);
    }

    public static void encodeLongitude(byte[] data, int begin, double val) {
        int res = (int) (koef24 * val);

        if (res > 0x7FFFFF)
            res = 0x7FFFFF;

        if (val < 0)
            res |= 0x800000;

        data[begin] = (byte) ((res & 0xFF0000) >> 16);
        data[begin + 1] = (byte) ((res & 0xFF00) >> 8);
        data[begin + 2] = (byte) (res & 0xFF);
    }

    public static int encodeUncertainty(double val) {
        for (int i = 0; i < 127; i++) {
            if (val < uncertaintyTable[i + 1]) {
                return i;
            }
        }

        return 127;
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public TypeOfShape getTypeOfShape() {
    	byte[] data=getData();
        if (data == null || data.length != 8)
            return null;

        return TypeOfShape.getInstance((data[0] & 0xFF) >> 4);
    }

    public double getLatitude() {
    	byte[] data=getData();
        if (data == null || data.length != 8)
            return 0;

        return decodeLatitude(data, 1);
    }

    public double getLongitude() {
    	byte[] data=getData();
        if (data == null || data.length != 8)
            return 0;

        return decodeLongitude(data, 4);
    }

    public double getUncertainty() {
    	byte[] data=getData();
        if (data == null || data.length != 8)
            return 0;

        return decodeUncertainty(data[7]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GeographicalInformation [");

        sb.append("TypeOfShape=");
        sb.append(this.getTypeOfShape());

        sb.append(", Latitude=");
        sb.append(this.getLatitude());

        sb.append(", Longitude=");
        sb.append(this.getLongitude());

        sb.append(", Uncertainty=");
        sb.append(this.getUncertainty());

        sb.append("]");

        return sb.toString();
    }
}
