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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TypeOfShape;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class GeographicalInformationImpl extends ASNOctetString implements GeographicalInformation {
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
    	super("GeographicalInformation",8,8,false);
    }

    public GeographicalInformationImpl(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty)
            throws ASNParsingException {
        super(translate(typeOfShape, latitude, longitude, uncertainty),"GeographicalInformation",8,8,false);
    }

    public static ByteBuf translate(TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty) throws ASNParsingException {
        if (typeOfShape != TypeOfShape.EllipsoidPointWithUncertaintyCircle) {
            throw new ASNParsingException(
                    "typeOfShape parameter for GeographicalInformation can be only \"ellipsoid point with uncertainty circle\"");
        }

        ByteBuf value=Unpooled.buffer(8);
        value.writeByte((typeOfShape.getCode() << 4));

        encodeLatitude(value, latitude);
        encodeLongitude(value, longitude);
        value.writeByte(encodeUncertainty(uncertainty));
        return value;
    }

    public static double decodeLatitude(ByteBuf buffer) {
        int i1 = ((buffer.readByte() & 0xFF) << 16) + ((buffer.readByte() & 0xFF) << 8) + (buffer.readByte() & 0xFF);

        int sign = 1;
        if ((i1 & 0x800000) != 0) {
            sign = -1;
            i1 = i1 & 0x7FFFFF;
        }

        return i1 / koef23 * sign;
    }

    public static double decodeLongitude(ByteBuf buffer) {
        int i1 = ((buffer.readByte() & 0xFF) << 16) | ((buffer.readByte() & 0xFF) << 8) | (buffer.readByte() & 0xFF);

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

    public static TypeOfShape decodeTypeOfShape(int data) {
    	return TypeOfShape.getInstance(data >> 4);
    }

    public static void encodeLatitude(ByteBuf buffer, double val) {
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

        buffer.writeByte(((res & 0xFF0000) >> 16));
        buffer.writeByte(((res & 0xFF00) >> 8));
        buffer.writeByte((res & 0xFF));
    }

    public static void encodeLongitude(ByteBuf buffer, double val) {
        int res = (int) (koef24 * val);

        if (res > 0x7FFFFF)
            res = 0x7FFFFF;

        if (val < 0)
            res |= 0x800000;

        buffer.writeByte(((res & 0xFF0000) >> 16));
        buffer.writeByte(((res & 0xFF00) >> 8));
        buffer.writeByte((res & 0xFF));
    }

    public static int encodeUncertainty(double val) {
        for (int i = 0; i < 127; i++) {
            if (val < uncertaintyTable[i + 1]) {
                return i;
            }
        }

        return 127;
    }

    public TypeOfShape getTypeOfShape() {
    	ByteBuf buffer=getValue();
        if (buffer == null || buffer.readableBytes() != 8)
            return null;

        return TypeOfShape.getInstance((buffer.readByte() & 0xFF) >> 4);
    }

    public double getLatitude() {
    	ByteBuf buffer=getValue();
        if (buffer == null || buffer.readableBytes() != 8)
            return 0;

        buffer.skipBytes(1);
        return decodeLatitude(buffer);
    }

    public double getLongitude() {
    	ByteBuf buffer=getValue();
        if (buffer == null || buffer.readableBytes() != 8)
            return 0;

        buffer.skipBytes(4);
        return decodeLongitude(buffer);
    }

    public double getUncertainty() {
    	ByteBuf buffer=getValue();
        if (buffer == null || buffer.readableBytes() != 8)
            return 0;

        buffer.skipBytes(7);
        return decodeUncertainty(buffer.readByte());
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
