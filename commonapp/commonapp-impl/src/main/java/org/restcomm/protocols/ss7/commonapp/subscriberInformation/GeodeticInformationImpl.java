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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TypeOfShape;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class GeodeticInformationImpl extends ASNOctetString implements GeodeticInformation {
	
	public GeodeticInformationImpl() {
		super("GeodeticInformation",10,10,false);
    }

    public GeodeticInformationImpl(int screeningAndPresentationIndicators, TypeOfShape typeOfShape, double latitude,
            double longitude, double uncertainty, int confidence) throws ASNParsingException {
        super(translate(screeningAndPresentationIndicators, typeOfShape, latitude, longitude, uncertainty, confidence),"GeodeticInformation",10,10,false);
    }

    public static ByteBuf translate(int screeningAndPresentationIndicators, TypeOfShape typeOfShape, double latitude, double longitude,
            double uncertainty, int confidence) throws ASNParsingException {

        if (typeOfShape != TypeOfShape.EllipsoidPointWithUncertaintyCircle) {
            throw new ASNParsingException(
                    "typeOfShape parameter for GeographicalInformation can be only \" ellipsoid point with uncertainty circle\"");
        }

        ByteBuf value=Unpooled.buffer(10);

        value.writeByte(screeningAndPresentationIndicators);
        value.writeByte(typeOfShape.getCode() << 4);

        GeographicalInformationImpl.encodeLatitude(value, latitude);
        GeographicalInformationImpl.encodeLongitude(value, longitude);
        value.writeByte(GeographicalInformationImpl.encodeUncertainty(uncertainty));
        value.writeByte(confidence);
        return value;
    }

    public int getScreeningAndPresentationIndicators() {
    	ByteBuf buffer=getValue();
        if (buffer == null || buffer.readableBytes() != 10)
            return 0;

        return buffer.readByte();
    }

    public TypeOfShape getTypeOfShape() {
    	ByteBuf buffer=getValue();
        if (buffer == null || buffer.readableBytes() != 10)
            return null;

        buffer.skipBytes(1);
        return TypeOfShape.getInstance(buffer.readByte() >> 4);
    }

    public double getLatitude() {
    	ByteBuf buffer=getValue();
        if (buffer == null || buffer.readableBytes() != 10)
            return 0;

        buffer.skipBytes(2);
        return GeographicalInformationImpl.decodeLatitude(buffer);
    }

    public double getLongitude() {
    	ByteBuf buffer=getValue();
        if (buffer == null || buffer.readableBytes() != 10)
            return 0;

        buffer.skipBytes(5);
        return GeographicalInformationImpl.decodeLongitude(buffer);
    }

    public double getUncertainty() {
    	ByteBuf buffer=getValue();
        if (buffer == null || buffer.readableBytes() != 10)
            return 0;

        buffer.skipBytes(8);
        return GeographicalInformationImpl.decodeUncertainty(buffer.readByte());
    }

    public int getConfidence() {
    	ByteBuf buffer=getValue();
        if (buffer == null || buffer.readableBytes() != 10)
            return 0;

        buffer.skipBytes(9);
        return buffer.readByte();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GeodeticInformation [");

        sb.append("ScreeningAndPresentationIndicators=");
        sb.append(this.getScreeningAndPresentationIndicators());

        sb.append(", TypeOfShape=");
        sb.append(this.getTypeOfShape());

        sb.append(", Latitude=");
        sb.append(this.getLatitude());

        sb.append(", Longitude=");
        sb.append(this.getLongitude());

        sb.append(", Uncertainty=");
        sb.append(this.getUncertainty());

        sb.append(", Confidence=");
        sb.append(this.getConfidence());

        sb.append("]");

        return sb.toString();
    }
}
